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
package se.crafted.chrisb.ecoCreature.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

import se.crafted.chrisb.ecoCreature.commons.EventUtils;

public final class EntityFarmedEvent extends Event
{
    private static final HandlerList HANDLERS = new HandlerList();

    private final EntityDeathEvent event;

    public static EntityFarmedEvent createEvent(EntityDeathEvent event)
    {
        return new EntityFarmedEvent(event);
    }

    private EntityFarmedEvent(EntityDeathEvent event)
    {
        this.event = event;
    }

    public LivingEntity getEntity()
    {
        return event.getEntity();
    }

    public void resetDrops()
    {
        event.getDrops().clear();
    }

    public void resetDroppedExp()
    {
        event.setDroppedExp(0);
    }

    public boolean isFarmed()
    {
        return EventUtils.isEntityFarmed(event);
    }

    public boolean isFireFarmed()
    {
        return EventUtils.isEntityFireFarmed(event);
    }

    @Override
    public HandlerList getHandlers()
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }
}
