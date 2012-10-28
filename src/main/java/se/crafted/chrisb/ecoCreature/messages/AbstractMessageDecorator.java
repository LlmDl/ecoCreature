/*
 * This file is part of ecoCreature.
 *
 * Copyright (c) 2011-2012, R. Ramos <http://github.com/mung3r/>
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

abstract class AbstractMessageDecorator implements Message
{
    private Message decoratedMessage;

    public AbstractMessageDecorator(Message decoratedMessage)
    {
        this.decoratedMessage = decoratedMessage;
    }

    @Override
    public boolean isMessageOutputEnabled()
    {
        return decoratedMessage.isMessageOutputEnabled();
    }

    @Override
    public void setMessageOutputEnabled(boolean messageOutputEnabled)
    {
        decoratedMessage.setMessageOutputEnabled(messageOutputEnabled);
    }

    @Override
    public boolean isCoinLoggingEnabled()
    {
        return decoratedMessage.isCoinLoggingEnabled();
    }

    @Override
    public void setCoinLoggingEnabled(boolean coinLoggingEnabled)
    {
        decoratedMessage.setCoinLoggingEnabled(coinLoggingEnabled);
    }

    @Override
    public String getTemplate()
    {
        return decoratedMessage.getTemplate();
    }

    @Override
    public void setTemplate(String template)
    {
        decoratedMessage.setTemplate(template);
    }

    @Override
    public boolean isAmountInMessage()
    {
        return decoratedMessage.isAmountInMessage();
    }

    @Override
    public String getAssembledMessage(Map<MessageToken, String> parameters)
    {
        return decoratedMessage.getAssembledMessage(parameters);
    }
}
