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
package se.crafted.chrisb.ecoCreature.drops.parties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import se.crafted.chrisb.ecoCreature.commons.PluginUtils;
import se.crafted.chrisb.ecoCreature.commons.LoggerUtil;

public class MobArenaParty extends AbstractParty
{
    public MobArenaParty(boolean shared)
    {
        super(shared);
    }

    @Override
    public Set<UUID> getMembers(Player player)
    {
        Set<UUID> party = Collections.emptySet();

        if (PluginUtils.hasMobArena() && PluginUtils.getMobArenaHandler().isPlayerInArena(player)) {
            party = new HashSet<>();

            for (Player member : PluginUtils.getMobArenaHandler().getArenaWithPlayer(player).getAllPlayers()) {
                party.add(member.getUniqueId());
            }
        }
        LoggerUtil.getInstance().debug("Party size: " + party.size());

        return party;
    }

    public static Collection<Party> parseConfig(ConfigurationSection config)
    {
        Collection<Party> parties = Collections.emptyList();

        if (config != null) {
            MobArenaParty party = new MobArenaParty(config.getBoolean("InArena.Share"));
            parties = new ArrayList<>();
            parties.add(party);
        }

        return parties;
    }
}
