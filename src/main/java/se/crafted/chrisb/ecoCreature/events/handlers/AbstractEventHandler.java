package se.crafted.chrisb.ecoCreature.events.handlers;

import java.util.Collections;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.event.Event;

import se.crafted.chrisb.ecoCreature.ecoCreature;
import se.crafted.chrisb.ecoCreature.events.RewardEvent;
import se.crafted.chrisb.ecoCreature.rewards.WorldSettings;

public abstract class AbstractEventHandler implements RewardEventHandler
{
    protected ecoCreature plugin;

    public AbstractEventHandler(ecoCreature plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public Set<RewardEvent> getRewardEvents(Event event)
    {
        return Collections.emptySet();
    }

    @Override
    public WorldSettings getSettings(World world)
    {
        return plugin.getWorldSettings(world);
    }
}