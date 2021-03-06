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
package se.crafted.chrisb.ecoCreature.messages;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import se.crafted.chrisb.ecoCreature.commons.LoggerUtil;

public class MessageHandler
{
    private final Message message;
    private final Map<MessageToken, String> parameters;

    public MessageHandler(Message message, Map<MessageToken, String> parameters)
    {
        this.message = message;
        this.parameters = parameters;
    }

    public void send(Player player)
    {
        send(player.getUniqueId());
    }

    public void send(UUID playerId)
    {
        String assembledMessage = message.assembleMessage(parameters);

        if (assembledMessage != null && assembledMessage.length() > 0) {
            if (message.isEnabled()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerId);
                if (offlinePlayer.isOnline()) {
                    offlinePlayer.getPlayer().sendMessage(assembledMessage);
                }
            }

            if (message instanceof CoinMessageDecorator && ((CoinMessageDecorator) message).isLoggingEnabled()) {
                LoggerUtil.getInstance().info(removeColorCodes(String.format("%s: %s", getAwardedPlayerName(), assembledMessage)));
            }
        }
    }

    private String getAwardedPlayerName()
    {
        return parameters.get(MessageToken.PLAYER);
    }

    private static String removeColorCodes(String msg)
    {
        return msg.replaceAll("(?i)§[a-fklmnor0-9]", "");
    }
}
