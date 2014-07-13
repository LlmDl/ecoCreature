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
package se.crafted.chrisb.ecoCreature.rewards.sources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import se.crafted.chrisb.ecoCreature.commons.DependencyUtils;
import se.crafted.chrisb.ecoCreature.messages.CoinMessageDecorator;
import se.crafted.chrisb.ecoCreature.messages.DefaultMessage;
import se.crafted.chrisb.ecoCreature.messages.Message;
import se.crafted.chrisb.ecoCreature.messages.NoCoinMessageDecorator;
import se.crafted.chrisb.ecoCreature.rewards.Reward;
import se.crafted.chrisb.ecoCreature.rewards.models.AbstractItemDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.BookDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.CoinDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.CoinReward;
import se.crafted.chrisb.ecoCreature.rewards.models.EntityDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.EntityReward;
import se.crafted.chrisb.ecoCreature.rewards.models.ItemDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.ItemReward;
import se.crafted.chrisb.ecoCreature.rewards.models.LoreDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.JockeyDrop;
import se.crafted.chrisb.ecoCreature.rewards.models.JockeyReward;
import se.crafted.chrisb.ecoCreature.rewards.rules.AbstractRule;
import se.crafted.chrisb.ecoCreature.rewards.rules.Rule;

public abstract class AbstractRewardSource implements CoinReward, ItemReward, EntityReward, JockeyReward
{
    private static final String NO_COIN_REWARD_MESSAGE = "&7You slayed a &5<crt>&7 using a &3<itm>&7.";
    private static final String COIN_REWARD_MESSAGE = "&7You are awarded &6<amt>&7 for slaying a &5<crt>&7.";
    private static final String COIN_PENALTY_MESSAGE = "&7You are penalized &6<amt>&7 for slaying a &5<crt>&7.";

    private String name;
    private CoinDrop coin;
    private Collection<AbstractItemDrop> itemDrops;
    private Collection<EntityDrop> entityDrops;
    private Collection<JockeyDrop> jockeyDrops;

    private Message noCoinRewardMessage;
    private Message coinRewardMessage;
    private Message coinPenaltyMessage;

    private boolean fixedDrops;
    private boolean integerCurrency;
    private boolean addItemsToInventory;

    private Map<Class<? extends AbstractRule>, Rule> huntingRules;

    public AbstractRewardSource()
    {
        huntingRules = Collections.emptyMap();
    }

    public AbstractRewardSource(String section, ConfigurationSection config)
    {
        this();

        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        ConfigurationSection rewardConfig = config.getConfigurationSection(section);
        name = rewardConfig.getName();

        itemDrops = new ArrayList<AbstractItemDrop>();
        itemDrops.addAll(ItemDrop.parseConfig(rewardConfig));
        itemDrops.addAll(BookDrop.parseConfig(rewardConfig));
        itemDrops.addAll(LoreDrop.parseConfig(rewardConfig));
        entityDrops = EntityDrop.parseConfig(rewardConfig);
        // TODO: hack - need to fix
        jockeyDrops = new ArrayList<JockeyDrop>();
        for (EntityDrop drop : JockeyDrop.parseConfig(rewardConfig)) {
            if (drop instanceof JockeyDrop) {
                jockeyDrops.add((JockeyDrop) drop);
            }
        }
        coin = CoinDrop.parseConfig(rewardConfig);

        coinRewardMessage = new CoinMessageDecorator(new DefaultMessage(rewardConfig.getString("Reward_Message", config.getString("System.Messages.Reward_Message", COIN_REWARD_MESSAGE))));
        coinPenaltyMessage = new CoinMessageDecorator(new DefaultMessage(rewardConfig.getString("Penalty_Message", config.getString("System.Messages.Penalty_Message", COIN_PENALTY_MESSAGE))));
        noCoinRewardMessage = new NoCoinMessageDecorator(new DefaultMessage(rewardConfig.getString("NoReward_Message", config.getString("System.Messages.NoReward_Message", NO_COIN_REWARD_MESSAGE))));

        addItemsToInventory = rewardConfig.getBoolean("AddItemsToInventory", false);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean hasPermission(Player player)
    {
        return DependencyUtils.hasPermission(player, "reward." + name);
    }

    @Override
    public boolean hasCoin()
    {
        return coin != null;
    }

    @Override
    public CoinDrop getCoin()
    {
        return coin;
    }

    @Override
    public void setCoin(CoinDrop coin)
    {
        this.coin = coin;
    }

    @Override
    public boolean hasItemDrops()
    {
        return itemDrops != null && !itemDrops.isEmpty();
    }

    @Override
    public Collection<AbstractItemDrop> getItemDrops()
    {
        return itemDrops;
    }

    @Override
    public void setItemDrops(Collection<AbstractItemDrop> drops)
    {
        this.itemDrops = drops;
    }

    @Override
    public boolean hasEntityDrops()
    {
        return entityDrops != null && !entityDrops.isEmpty();
    }

    @Override
    public Collection<EntityDrop> getEntityDrops()
    {
        return entityDrops;
    }

    @Override
    public void setEntityDrops(Collection<EntityDrop> entityDrops)
    {
        this.entityDrops = entityDrops;
    }

    @Override
    public boolean hasJockeyDrops()
    {
        return jockeyDrops != null && !jockeyDrops.isEmpty();
    }

    @Override
    public Collection<JockeyDrop> getJockeyDrops()
    {
        return jockeyDrops;
    }

    @Override
    public void setJockeyDrops(Collection<JockeyDrop> jockeyDrops)
    {
        this.jockeyDrops = jockeyDrops;
    }

    @Override
    public Message getNoCoinRewardMessage()
    {
        return noCoinRewardMessage;
    }

    @Override
    public void setNoCoinRewardMessage(Message noCoinRewardMessage)
    {
        this.noCoinRewardMessage = noCoinRewardMessage;
    }

    @Override
    public Message getCoinRewardMessage()
    {
        return coinRewardMessage;
    }

    @Override
    public void setCoinRewardMessage(Message coinRewardMessage)
    {
        this.coinRewardMessage = coinRewardMessage;
    }

    @Override
    public Message getCoinPenaltyMessage()
    {
        return coinPenaltyMessage;
    }

    @Override
    public void setCoinPenaltyMessage(Message coinPenaltyMessage)
    {
        this.coinPenaltyMessage = coinPenaltyMessage;
    }

    @Override
    public Boolean isFixedDrops()
    {
        return fixedDrops;
    }

    @Override
    public void setFixedDrops(Boolean fixedDrops)
    {
        this.fixedDrops = fixedDrops;
    }

    @Override
    public Boolean isIntegerCurrency()
    {
        return integerCurrency;
    }

    @Override
    public void setIntegerCurrency(Boolean integerCurrency)
    {
        this.integerCurrency = integerCurrency;
    }

    @Override
    public Boolean isAddItemsToInventory()
    {
        return addItemsToInventory;
    }

    @Override
    public void setAddItemsToInventory(Boolean addItemsToInventory)
    {
        this.addItemsToInventory = addItemsToInventory;
    }

    public Map<Class<? extends AbstractRule>, Rule> getHuntingRules()
    {
        return huntingRules;
    }

    public void setHuntingRules(Map<Class<? extends AbstractRule>, Rule> huntingRules)
    {
        this.huntingRules = huntingRules;
    }

    public Reward createReward(Event event)
    {
        Reward reward = new Reward(getLocation(event));

        reward.setName(name);
        reward.setItemDrops(getItemDropOutcomes());
        reward.setEntityDrops(getEntityDropOutcomes());
        reward.setJockeyDrops(getJockeyDropOutcomes());

        if (hasCoin()) {
            reward.setCoin(coin.getOutcome());

            if (reward.getCoin() > 0.0) {
                reward.setMessage(coinRewardMessage);
            }
            else if (reward.getCoin() < 0.0) {
                reward.setMessage(coinPenaltyMessage);
            }
            else {
                reward.setMessage(noCoinRewardMessage);
            }
        }

        reward.setIntegerCurrency(integerCurrency);
        reward.setAddItemsToInventory(addItemsToInventory);

        return reward;
    }

    protected abstract Location getLocation(Event event);

    private Collection<ItemStack> getItemDropOutcomes()
    {
        Collection<ItemStack> stacks = Collections.emptyList();

        if (itemDrops != null) {
            stacks = new ArrayList<ItemStack>();

            for (AbstractItemDrop drop : itemDrops) {
                ItemStack itemStack = drop.getOutcome(fixedDrops);
                if (itemStack != null) {
                    stacks.add(itemStack);
                }
            }
        }

        return stacks;
    }

    private Collection<EntityType> getEntityDropOutcomes()
    {
        Collection<EntityType> types = Collections.emptyList();

        if (entityDrops != null) {
            types = new ArrayList<EntityType>();

            for (EntityDrop drop : entityDrops) {
                types.addAll(drop.getOutcome());
            }
        }

        return types;
    }

    private Collection<EntityType> getJockeyDropOutcomes()
    {
        Collection<EntityType> types = Collections.emptyList();

        if (jockeyDrops != null) {
            types = new ArrayList<EntityType>();

            for (EntityDrop drop : jockeyDrops) {
                if (drop instanceof JockeyDrop) {
                    JockeyDrop jockeyDrop = (JockeyDrop) drop;
                    types.addAll(jockeyDrop.getOutcome());
                }
            }
        }

        return types;
    }
}
