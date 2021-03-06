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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class DefaultMessage implements Message
{
    private static final String EMPTY_MESSAGE_TEMPLATE = "";

    public static final DefaultMessage EMPTY_MESSAGE = new DefaultMessage(EMPTY_MESSAGE_TEMPLATE);

    private boolean enabled;

    private final String template;

    public DefaultMessage(String template, boolean enabled)
    {
        this.template = convertTemplate(template);
        this.enabled = enabled;
    }

    public DefaultMessage(String template)
    {
        this(convertTemplate(template), true);
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public String assembleMessage(Map<MessageToken, String> parameters)
    {
        String assembledMessage = template;

        if (StringUtils.isNotEmpty(assembledMessage)) {
            for (Entry<MessageToken, String> entry : parameters.entrySet()) {
                if (entry.getKey() == MessageToken.AMOUNT) {
                    assembledMessage = assembledMessage.replaceAll(entry.getKey().toString(), entry.getValue().replaceAll("\\$", "\\\\\\$"));
                }
                else if (entry.getKey() == MessageToken.ITEM) {
                    assembledMessage = assembledMessage.replaceAll(entry.getKey().toString(), toCamelCase(entry.getValue()));
                }
                else {
                    assembledMessage = assembledMessage.replaceAll(entry.getKey().toString(), entry.getValue());
                }
            }
        }

        return assembledMessage;
    }

    private static String toCamelCase(String rawItemName)
    {
        String[] rawItemNameParts = rawItemName.split("_");
        StringBuilder itemNameBuilder = new StringBuilder("");

        for (String itemNamePart : rawItemNameParts) {
            itemNameBuilder.append(" ").append(toProperCase(itemNamePart));
        }

        String itemName = itemNameBuilder.toString();
        if (itemName.trim().equals("Air")) {
            itemName = "Fists";
        }

        if (itemName.trim().equals("Bow")) {
            itemName = "Bow & Arrow";
        }

        return itemName.trim();
    }

    private static String toProperCase(String str)
    {
        if (str.length() < 1) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String convertTemplate(String template)
    {
        if (template != null) {
            return template.replaceAll("&&", "\b").replaceAll("&", "§").replaceAll("\b", "&");
        }

        return null;
    }

    public static List<String> convertTemplates(List<String> templates) {
        List<String> convertedTemplates = new ArrayList<>();

        for (String template : templates) {
            convertedTemplates.add(convertTemplate(template));
        }

        return convertedTemplates;
    }
}
