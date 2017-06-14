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
package se.crafted.chrisb.ecoCreature.drops.gain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import se.crafted.chrisb.ecoCreature.commons.LoggerUtil;
import se.crafted.chrisb.ecoCreature.commons.WeatherType;

public class WeatherGain extends AbstractPlayerGain<WeatherType>
{
    public WeatherGain(Map<WeatherType, Double> multipliers)
    {
        super(multipliers, "gain.weather");
    }

    @Override
    public double getGain(Player player)
    {
        double multiplier = getMultiplier(WeatherType.fromEntity(player));
        LoggerUtil.getInstance().debug("Gain: " + multiplier);
        return multiplier;
    }

    public static Collection<PlayerGain> parseConfig(ConfigurationSection config)
    {
        Collection<PlayerGain> gain = Collections.emptyList();

        if (config != null) {
            Map<WeatherType, Double> multipliers = new HashMap<>();
            for (String weather : config.getKeys(false)) {
                try {
                    multipliers.put(WeatherType.valueOf(weather.toUpperCase()),
                            config.getConfigurationSection(weather).getDouble(AMOUNT_KEY, 1.0D));
                }
                catch (IllegalArgumentException e) {
                    LoggerUtil.getInstance().warning("Skipping unknown weather name: " + weather);
                }
            }
            gain = new ArrayList<>();
            gain.add(new WeatherGain(multipliers));
        }

        return gain;
    }
}
