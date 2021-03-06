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
package se.crafted.chrisb.ecoCreature.drops.categories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import se.crafted.chrisb.ecoCreature.events.EntityKilledEvent;
import se.crafted.chrisb.ecoCreature.events.PlayerKilledEvent;
import se.crafted.chrisb.ecoCreature.drops.rules.AbstractRule;
import se.crafted.chrisb.ecoCreature.drops.rules.Rule;
import se.crafted.chrisb.ecoCreature.drops.sources.AbstractDropSource;
import se.crafted.chrisb.ecoCreature.drops.sources.DropSourceFactory;
import se.crafted.chrisb.ecoCreature.drops.categories.types.CustomEntityType;

public class CustomEntityDropCategory extends AbstractDropCategory<CustomEntityType>
{
    public CustomEntityDropCategory(Map<CustomEntityType, Collection<AbstractDropSource>> dropSourceMap)
    {
        super(dropSourceMap);
    }

    @Override
    protected boolean isValidEvent(Event event)
    {
        return event instanceof PlayerKilledEvent || event instanceof EntityKilledEvent;
    }

    @Override
    protected CustomEntityType extractType(Event event)
    {
        CustomEntityType type = CustomEntityType.INVALID;

        if (event instanceof PlayerKilledEvent) {
            type = CustomEntityType.PLAYER;
        }
        else if (event instanceof EntityKilledEvent) {
            type = CustomEntityType.fromEntity(((EntityKilledEvent) event).getEntity());
        }

        return type;
    }

    @Override
    protected Player extractPlayer(Event event)
    {
        Player player = null;

        if (event instanceof PlayerKilledEvent) {
            player = ((PlayerKilledEvent) event).getKiller();
        }
        else if (event instanceof EntityKilledEvent) {
            player = ((EntityKilledEvent) event).getKiller();
        }

        return player;
    }

    public static AbstractDropCategory<CustomEntityType> parseConfig(ConfigurationSection config)
    {
        Map<CustomEntityType, Collection<AbstractDropSource>> dropSourceMap = new HashMap<>();
        ConfigurationSection rewardTable = config.getConfigurationSection("RewardTable");

        if (rewardTable != null) {
            for (String typeName : rewardTable.getKeys(false)) {
                CustomEntityType type = CustomEntityType.fromName(typeName);

                if (type.isValid()) {
                    Map<Class<? extends AbstractRule>, Rule> huntingRules = loadHuntingRules(config.getConfigurationSection("System"));
                    huntingRules.putAll(loadHuntingRules(config.getConfigurationSection("RewardTable." + typeName)));
                    huntingRules.putAll(loadGainRules(config.getConfigurationSection("Gain")));

                    for (AbstractDropSource dropSource : DropSourceFactory.createSources("RewardTable." + typeName, config)) {
                        dropSource.setHuntingRules(huntingRules);

                        if (!dropSourceMap.containsKey(type)) {
                            dropSourceMap.put(type, new ArrayList<AbstractDropSource>());
                        }

                        dropSourceMap.get(type).add(dropSource);
                        dropSourceMap.get(type).addAll(parseSets("RewardTable." + typeName, config));
                    }
                }
            }
        }

        return new CustomEntityDropCategory(dropSourceMap);
    }
}
