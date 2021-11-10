package io.github.changwook987.psychics.ability.knockdown

import io.github.monun.psychics.AbilityConcept
import io.github.monun.psychics.ActiveAbility
import io.github.monun.psychics.attribute.EsperAttribute
import io.github.monun.psychics.damage.Damage
import io.github.monun.psychics.damage.DamageType
import io.github.monun.tap.config.Name
import net.kyori.adventure.text.Component.text
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

@Name("knockdown")
class AbilityConceptSample : AbilityConcept() {
    init {
        castingTime = 1000L
        cooldownTime = 5000L
        durationTime = 1080

        knockback = 10.0
        range = 10.0

        description = listOf(
            text("주변에 장막을 펼쳐 몬스터를 날려보냅니다")
        )

        damage = Damage.of(DamageType.RANGED, EsperAttribute.ATTACK_DAMAGE to 0.1)

        wand = ItemStack(Material.BARRIER)

        supplyItems = listOf(
            ItemStack(Material.BARRIER, 1)
        )
    }
}

class AbilityKnockDown : ActiveAbility<AbilityConceptSample>(), Listener {
    override fun onEnable() {
        psychic.runTaskTimer(this::draw, 0L, 1L)
    }

    private var tick = 0

    private fun draw() {
        if (durationTime <= 0) return

        repeat(60) {
            tick++
            tick %= 1080

            val height = Math.toRadians(tick.toDouble().div(10))
            val angle = Math.toRadians(tick.mod(360).toDouble())

            val loc = Vector(cos(angle), height, sin(angle))

            esper.player.world.spawnParticle(
                Particle.REDSTONE,
                esper.player.location.add(loc),
                1,
                Particle.DustOptions(Color.fromRGB(tick.mod(256),0,0), 1f)
            )
        }
    }

    override fun onCast(event: PlayerEvent, action: WandAction, target: Any?) {
        val player = esper.player

        exhaust()
        durationTime = concept.durationTime

        val entities =
            player.getNearbyEntities(
                concept.range,
                concept.range,
                concept.range
            ).filterIsInstance<LivingEntity>()

        for (i in entities) {
            i.psychicDamage()
        }
    }
}