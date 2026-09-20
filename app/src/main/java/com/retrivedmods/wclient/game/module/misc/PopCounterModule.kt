package com.retrivedmods.wclient.game.module.misc

import com.retrivedmods.wclient.game.InterceptablePacket
import com.retrivedmods.wclient.game.Module
import com.retrivedmods.wclient.game.ModuleCategory
import com.retrivedmods.wclient.game.entity.Player
import com.retrivedmods.wclient.game.friend.FriendManager
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.data.LevelEvent
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket
import org.cloudburstmc.protocol.bedrock.packet.RemoveEntityPacket
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket
import org.cloudburstmc.protocol.bedrock.packet.TextPacket
import java.util.UUID
import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Ported from Gato Client Mobile 1.8's PopCounter module (same reference used
 * there: the GatoClient PC module of the same name) — same settings, same
 * default values, same chat message templates and placeholders.
 *
 * Two things were adapted rather than copied verbatim, because this
 * codebase's APIs differ from 1.8's:
 *  - Friends are matched with FriendManager.isFriend(uuid) instead of
 *    isInList(username), since FriendManager here stores UUIDs, not names.
 *  - There is no LocalPlayer.vec3PositionFeet here. The local player's
 *    tracked position is eye-level (from PlayerAuthInputPacket), so an
 *    approximate 1.62-block offset is applied inline only for the local
 *    player, to compare against the totem sound position on the same
 *    footing as other entities (whose network position is feet-level).
 *
 * Like the original, a pop is counted for a player even while the module is
 * disabled (the counter increments before the isEnabled check below) — only
 * the chat messages are gated by it.
 */
class PopCounterModule : Module("PopCounter", ModuleCategory.Misc) {

    private var sendPops by boolValue("Send Pop Message", true)
    private var sendPopsOnDeath by boolValue("Send Death Message", true)
    private var countFriends by boolValue("Track Friends", false)
    private var countSelf by boolValue("Track Self", false)
    private var actorEventOnly by boolValue("ActorEvent", false)

    // Game-chat templates: $ becomes §, placeholders resolved in sanitize().
    private var popMessage by stringValue("Game Pop Message", "@!player! just popped !pops! !totem!", null)
    private var deathMessage by stringValue("Game Death Message", "@!player! just died after popping !pops! !totem!", null)

    // runtimeEntityId -> pops
    private val totemMap = HashMap<Long, Int>()

    // uniqueEntityId -> cached identity. RemoveEntityPacket only carries the
    // unique id, and by the time modules see it the entity is already gone
    // from Level's map, so identities of players that popped are cached
    // while they're still alive.
    private val entityCache = HashMap<Long, PopCandidate>()

    private data class PopCandidate(
        val runtimeId: Long,
        val uniqueId: Long,
        val uuid: UUID,
        val username: String
    )

    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {
        when (val packet = interceptablePacket.packet) {
            is LevelEventPacket -> {
                if (!actorEventOnly &&
                    packet.type == LevelEvent.SOUND_TOTEM_USED &&
                    (packet.position.x != 0f || packet.position.y != 0f || packet.position.z != 0f)
                ) {
                    handleLevelEventPop(packet)
                }
            }

            is EntityEventPacket -> {
                if (actorEventOnly && packet.type == EntityEventType.CONSUME_TOTEM) {
                    handleActorEventPop(packet)
                }
            }

            is RemoveEntityPacket -> handleRemove(packet)

            is RespawnPacket -> {
                totemMap[session.localPlayer.runtimeEntityId] = 0
            }

            else -> {}
        }
    }

    private fun handleLevelEventPop(packet: LevelEventPacket) {
        val eventPos = packet.position
        val local = session.localPlayer

        val candidates = mutableListOf<PopCandidate>()
        if (local.runtimeEntityId != 0L) {
            candidates.add(PopCandidate(local.runtimeEntityId, local.uniqueEntityId, local.uuid, local.username))
        }
        session.level.entityMap.values.forEach { entity ->
            if (entity is Player) {
                candidates.add(PopCandidate(entity.runtimeEntityId, entity.uniqueEntityId, entity.uuid, entity.username))
            }
        }

        // Nearest candidate within 10 blocks of the totem sound wins.
        var best: PopCandidate? = null
        var bestDist = Float.MAX_VALUE
        for (candidate in candidates) {
            val pos = if (candidate.runtimeId == local.runtimeEntityId) {
                Vector3f.from(local.posX, local.posY - 1.62f, local.posZ)
            } else {
                session.level.entityMap[candidate.runtimeId]?.vec3Position ?: continue
            }
            val dx = floor(pos.x) - eventPos.x
            val dy = floor(pos.y) - 1f - eventPos.y
            val dz = floor(pos.z) - eventPos.z
            val dist = sqrt(dx * dx + dy * dy + dz * dz)
            if (dist > 10f) continue
            if (dist < bestDist) {
                bestDist = dist
                best = candidate
            }
        }

        best?.let {
            entityCache[it.uniqueId] = it
            onActorPop(it.runtimeId, it.uuid, it.username, it.uniqueId == local.uniqueEntityId)
        }
    }

    private fun handleActorEventPop(packet: EntityEventPacket) {
        val runtimeId = packet.runtimeEntityId
        val local = session.localPlayer

        if (runtimeId == local.runtimeEntityId) {
            entityCache[local.uniqueEntityId] =
                PopCandidate(runtimeId, local.uniqueEntityId, local.uuid, local.username)
            onActorPop(runtimeId, local.uuid, local.username, true)
            return
        }

        val entity = session.level.entityMap[runtimeId] as? Player ?: return
        entityCache[entity.uniqueEntityId] =
            PopCandidate(runtimeId, entity.uniqueEntityId, entity.uuid, entity.username)
        onActorPop(runtimeId, entity.uuid, entity.username, false)
    }

    private fun handleRemove(packet: RemoveEntityPacket) {
        val cached = entityCache[packet.uniqueEntityId] ?: return
        val pops = totemMap[cached.runtimeId] ?: return
        if (pops == 0) return

        val isSelf = packet.uniqueEntityId == session.localPlayer.uniqueEntityId
        if (isSelf) {
            totemMap[cached.runtimeId] = 0
            return
        }

        val isFriend = FriendManager.isFriend(cached.uuid)
        val totemWord = if (pops > 1) "totems" else "totem"
        val prefix = if (isFriend) "[-]" else "[+]"
        val prefixColor = if (isFriend) "§c" else "§a"
        val nameColor = if (isFriend) "§9" else "§c"

        if (isEnabled) {
            session.displayClientMessage(
                "$prefixColor$prefix $nameColor${cached.username} §fdied after popping §b$pops §f$totemWord!"
            )
        }

        if (sendPopsOnDeath && !isFriend) {
            sendGameChat(sanitize(deathMessage, cached.username, pops))
        }

        totemMap[cached.runtimeId] = 0
    }

    /** Increments before any isEnabled check; only the messages are gated. */
    private fun onActorPop(runtimeId: Long, uuid: UUID, username: String, isSelf: Boolean) {
        val pops = (totemMap[runtimeId] ?: 0) + 1
        totemMap[runtimeId] = pops
        val totemWord = if (pops > 1) "totems" else "totem"

        if (!isEnabled) return

        val isFriend = FriendManager.isFriend(uuid)
        when {
            isSelf -> {
                if (countSelf) {
                    session.displayClientMessage("§6[!] §9You §fpopped §b$pops §f$totemWord!")
                }
            }

            isFriend -> {
                if (countFriends) {
                    session.displayClientMessage("§6[!] §9$username §fpopped §b$pops §f$totemWord!")
                }
            }

            else -> {
                session.displayClientMessage("§6[!] §c$username §fpopped §b$pops §f$totemWord!")
                if (sendPops) {
                    sendGameChat(sanitize(popMessage, username, pops))
                }
            }
        }
    }

    /** $ becomes §, placeholders resolved. */
    private fun sanitize(message: String, name: String, pops: Int): String =
        message
            .replace("$", "§")
            .replace("!clientname!", "Gato Client")
            .replace("!player!", name)
            .replace("!pops!", pops.toString())
            .replace("!totem!", if (pops > 1) "totems" else "totem")

    private fun sendGameChat(message: String) {
        session.serverBound(TextPacket().apply {
            type = TextPacket.Type.CHAT
            setMessage(message)
        })
    }

    /** Public accessor in case something else wants to read a player's pop count. */
    fun getTotemPops(runtimeId: Long): Int = totemMap[runtimeId] ?: 0
}
