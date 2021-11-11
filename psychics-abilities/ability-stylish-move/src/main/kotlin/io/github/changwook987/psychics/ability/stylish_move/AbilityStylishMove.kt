package io.github.changwook987.psychics.ability.stylish_move

import io.github.monun.psychics.AbilityConcept
import io.github.monun.psychics.ActiveAbility
import io.github.monun.tap.config.Name
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

@Name("stylish-move")
class AbilityConceptSample : AbilityConcept() {
    companion object {
        private val feather = ItemStack(Material.FEATHER).apply {
            itemMeta = itemMeta.apply {
                displayName(text().content("Stylish!").color(NamedTextColor.AQUA).build())
                lore(
                    listOf(
                        text("first->jump"),
                        text("second->dive")
                    )
                )
            }
        }
    }

    init {
        description = listOf(
            text("멋지고 빠르게 이동")
        )

        wand = feather

        durationTime = 500L
        cooldownTime = 1500L
        cost = 10.0

        supplyItems = listOf(
            feather
        )

    }
}

class AbilityStylishMove : ActiveAbility<AbilityConceptSample>(), Listener {
    override fun onEnable() {
        psychic.registerEvents(this)
    }

    @EventHandler(ignoreCancelled = true)
    private fun disableFallDamage(event: EntityDamageEvent) {
        val player = esper.player
        if (event.entity != player) return
        if (durationTime <= 0) return

        if (event.cause != EntityDamageEvent.DamageCause.FALL) return

        esper.player.sendMessage("캔슬")

        event.isCancelled = true

    }

    override fun onCast(event: PlayerEvent, action: WandAction, target: Any?) {
        val player = esper.player

        if (durationTime <= 0) {
            player.apply {
                velocity = velocity.setY(1)
            }

            psychic.consumeMana(concept.cost)
            durationTime = concept.durationTime

        } else {
            val dir = player.location.apply {
                pitch = 45f
            }.direction

            player.velocity = dir.multiply(2)

            exhaust()
            durationTime = concept.durationTime.times(2)
        }
    }
}