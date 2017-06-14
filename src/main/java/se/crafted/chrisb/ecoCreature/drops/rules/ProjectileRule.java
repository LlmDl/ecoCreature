/*
 * This file is part of ecoCreature.
 *
 * Copyright (c) 2011-2017, R. Ramos <http://github.com/mung3r/>
 * ecoCreature is licensed under the GNU Lesser General Public License.
 *
 * ecoCreature is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ecoCreature is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.crafted.chrisb.ecoCreature.drops.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import se.crafted.chrisb.ecoCreature.commons.LoggerUtil;
import se.crafted.chrisb.ecoCreature.events.EntityKilledEvent;
import se.crafted.chrisb.ecoCreature.messages.DefaultMessage;

public class ProjectileRule extends AbstractEntityRule
{
    private static final String NO_BOW_REWARD_MESSAGE = "&7You find no rewards on this creature.";

    private final boolean bowRewards;

    public ProjectileRule(boolean bowRewards)
    {
        this.bowRewards = bowRewards;
    }

    @Override
    protected boolean isBroken(EntityKilledEvent event)
    {
        boolean ruleBroken = !bowRewards && event.isProjectileKill();
        LoggerUtil.getInstance().debugTrue("No reward for " + event.getKiller().getName() + " using projectiles.", ruleBroken);

        return ruleBroken;
    }

    public static Map<Class<? extends AbstractRule>, Rule> parseConfig(ConfigurationSection system)
    {
        Map<Class<? extends AbstractRule>, Rule> rules = Collections.emptyMap();

        if (system != null && system.isConfigurationSection("Hunting")) {
            ProjectileRule rule = new ProjectileRule(system.getBoolean("Hunting.BowRewards", true));
            rule.setMessage(new DefaultMessage(system.getString("Messages.NoBowMessage", NO_BOW_REWARD_MESSAGE), system.getBoolean("Messages.Output")));
            rules = new HashMap<>();
            rules.put(ProjectileRule.class, rule);
        }

        return rules;
    }
}
