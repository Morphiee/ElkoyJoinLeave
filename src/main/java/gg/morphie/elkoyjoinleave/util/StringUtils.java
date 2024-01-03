package gg.morphie.elkoyjoinleave.util;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class StringUtils {

    public String addColor(String message) {
        if (message == null) {
            return null;
        }
        String hexMessage = this.translateHexColorCodes("#", "", message);
        return ChatColor.translateAlternateColorCodes('&', hexMessage);
    }

    public String translateHexColorCodes(String startTag, String endTag, String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public String listToString(ArrayList<String> list) {
        StringBuilder newLore = new StringBuilder();
        for (String s : list) {
            newLore.append("\n").append(s);
        }
        return newLore.toString();
    }
}
