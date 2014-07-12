/*
 * This file is part of ecoCreature.
 *
 * Copyright (c) 2011-2014, R. Ramos <http://github.com/mung3r/>
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
package se.crafted.chrisb.ecoCreature.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import se.crafted.chrisb.ecoCreature.events.EntityKilledEvent;
import se.crafted.chrisb.ecoCreature.events.PlayerKilledEvent;
import se.crafted.chrisb.ecoCreature.rewards.sources.AbstractRewardSource;
import se.crafted.chrisb.ecoCreature.settings.types.CustomEntityRewardType;

public class CustomEntityRewardSettings extends AbstractRewardSettings<CustomEntityRewardType>
{
    public CustomEntityRewardSettings(Map<CustomEntityRewardType, List<AbstractRewardSource>> sources)
    {
        super(sources);
    }

    @Override
    public boolean hasRewardSource(Event event)
    {
        if (event instanceof PlayerKilledEvent) {
            return hasRewardSource((PlayerKilledEvent) event);
        }
        else if (event instanceof EntityKilledEvent) {
            return hasRewardSource((EntityKilledEvent) event);
        }

        return false;
    }

    private boolean hasRewardSource(final PlayerKilledEvent event)
    {
        return hasRewardSource(CustomEntityRewardType.PLAYER) 
                && Iterables.any(getRewardSource(CustomEntityRewardType.PLAYER), new Predicate<AbstractRewardSource>() {

                    @Override
                    public boolean apply(AbstractRewardSource source)
                    {
                        return source.hasPermission(event.getKiller()) && isNotRuleBroken(event, source.getHuntingRules().values());
                    }
                });
    }

    private boolean hasRewardSource(final EntityKilledEvent event)
    {
        CustomEntityRewardType type = CustomEntityRewardType.fromEntity(event.getEntity());

        return hasRewardSource(type)
                && Iterables.any(getRewardSource(type), new Predicate<AbstractRewardSource>() {

                    @Override
                    public boolean apply(AbstractRewardSource source)
                    {
                        return source.hasPermission(event.getKiller()) && isNotRuleBroken(event, source.getHuntingRules().values());
                    }
                });
    }

    @Override
    public List<AbstractRewardSource> getRewardSource(Event event)
    {
        if (event instanceof PlayerKilledEvent) {
            return getRewardSource(((PlayerKilledEvent) event).getEntity());
        }
        else if (event instanceof EntityKilledEvent) {
            return getRewardSource(((EntityKilledEvent) event).getEntity());
        }

        return null;
    }

    private List<AbstractRewardSource> getRewardSource(Entity entity)
    {
        return getRewardSource(CustomEntityRewardType.fromEntity(entity));
    }

    public static AbstractRewardSettings<CustomEntityRewardType> parseConfig(ConfigurationSection config)
    {
        Map<CustomEntityRewardType, List<AbstractRewardSource>> sources = new HashMap<CustomEntityRewardType, List<AbstractRewardSource>>();
        ConfigurationSection rewardTable = config.getConfigurationSection("RewardTable");

        if (rewardTable != null) {
            for (String typeName : rewardTable.getKeys(false)) {
                CustomEntityRewardType type = CustomEntityRewardType.fromName(typeName);

                if (type.isValid()) {
                    AbstractRewardSource source = configureRewardSource(RewardSourceFactory.createSource("RewardTable." + typeName, config), config);
                    source.setHuntingRules(loadHuntingRules(config.getConfigurationSection("System")));
                    source.getHuntingRules().putAll(loadHuntingRules(config.getConfigurationSection("RewardTable." + typeName)));
                    source.getHuntingRules().putAll(loadGainRules(config.getConfigurationSection("Gain")));

                    if (!sources.containsKey(type)) {
                        sources.put(type, new ArrayList<AbstractRewardSource>());
                    }

                    sources.get(type).add(source);
                    sources.get(type).addAll(getSets("RewardTable." + typeName, config));
                }
            }
        }

        return new CustomEntityRewardSettings(sources);
    }
}
