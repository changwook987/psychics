package io.github.changwook987.psychics.ability.runner

import io.github.monun.psychics.AbilityConcept
import io.github.monun.psychics.ActiveAbility
import io.github.monun.tap.config.Config
import io.github.monun.tap.config.Name
import io.github.monun.tap.config.RangeInt
import net.kyori.adventure.text.Component.text
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

// 달린다!!!!
@Name("sample")
class AbilityConceptSample : AbilityConcept() {
    @Config
    @RangeInt(min = 1)
    var speedAmount = 2

    init {
        cooldownTime = 5000L
        cost = 10.0
        durationTime = 1000

        description = listOf(
            text("달린다!")
        )
        //완드는 물병
        wand = ItemStack(Material.POTION).apply {
            itemMeta = (itemMeta as PotionMeta).apply {
                basePotionData = PotionData(PotionType.WATER)
            }
        }
    }
}

class AbilityRunner : ActiveAbility<AbilityConceptSample>(), Listener {

    override fun onEnable() {
        psychic.runTaskTimer(this::playParticle, 0L, 1L)
    }

    override fun onCast(event: PlayerEvent, action: WandAction, target: Any?) {
        if (action != WandAction.LEFT_CLICK) return

        exhaust()
        durationTime = concept.durationTime

        event.player.inventory.setItemInMainHand(ItemStack(Material.GLASS_BOTTLE))

        val player = event.player

        player.apply {
            removePotionEffect(PotionEffectType.SLOW)

            addPotionEffect(
                PotionEffect(
                    PotionEffectType.SPEED,
                    concept.durationTime.div(50).toInt(),
                    concept.speedAmount.minus(1)
                )
            )
        }
    }

    private fun playParticle() {
        if (durationTime <= 0) return

        drawCircle(Color.LIME, 0.5)
        drawCircle(Color.BLUE, 0.75)
    }

    private fun drawCircle(color: Color, radius: Double = 0.5) {
        repeat(360) {
            val angle = Math.toRadians(it.toDouble())

            val loc = Vector(cos(angle), 0.0, sin(angle)).multiply(radius.times(2))
            esper.player.world.apply {
                spawnParticle(
                    Particle.REDSTONE,
                    esper.player.location.add(loc),
                    1,
                    Particle.DustOptions(color, 1.0f)
                )
            }
        }
    }
}