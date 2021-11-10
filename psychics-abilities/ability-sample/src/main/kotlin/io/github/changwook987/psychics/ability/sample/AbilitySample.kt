package io.github.changwook987.psychics.ability.sample

import io.github.monun.psychics.Ability
import io.github.monun.psychics.AbilityConcept
import io.github.monun.tap.config.Name
import net.kyori.adventure.text.Component.text
import org.bukkit.event.Listener

@Name("sample")
class AbilityConceptSample : AbilityConcept() {
    init {
        description = listOf(
            text("")
        )
    }
}

class AbilitySample : Ability<AbilityConceptSample>(), Listener {

}