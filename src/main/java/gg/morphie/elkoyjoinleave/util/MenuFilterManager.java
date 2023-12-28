package gg.morphie.elkoyjoinleave.util;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.util.playerdata.PlayerDataManager;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MenuFilterManager {
    private ElkoyJoinLeave plugin;

    public MenuFilterManager(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    String DefaultFilter = "All";
    List<String> MenuFilters = Arrays.asList("Join", "Leave");

    public String getPlayerTag(UUID uuid) {
        return new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
    }

    public String getDefaultFilter() {
        return DefaultFilter;
    }

    public List<String> getMenuFilters() {
        return MenuFilters;
    }

    public String getTag(String string, UUID uuid) {
        switch (string) {
            case "ALL":
                if (getPlayerTag(uuid).equals(string)) {
                    return "&a" + getPlayerTag(uuid);
                } else {
                    return "&7All";
                }
            case "JOIN":
                if (getPlayerTag(uuid).equals(string)) {
                    return "&a" + getPlayerTag(uuid);
                } else {
                    return "&7Join";
                }
            case "LEAVE":
                if (getPlayerTag(uuid).equals(string)) {
                    return "&a" + getPlayerTag(uuid);
                } else {
                    return "&7Leave";
                }
        }
        return null;
    }

    public String getPrevTag(UUID uuid) {
        int listSize = MenuFilters.size();
        if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(DefaultFilter)) {
            return MenuFilters.get(listSize - 1);
        } else if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(MenuFilters.get(0))) {
            return DefaultFilter;
        } else {
            int prevIndex = MenuFilters.indexOf(getPlayerTag(uuid));
            return MenuFilters.get(prevIndex-1);
        }
    }

    public String getNextTag(UUID uuid) {
        int listSize = MenuFilters.size();
        if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(DefaultFilter)) {
            List<String> customTags = MenuFilters;
            return customTags.get(0);
        } else if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(MenuFilters.get(listSize-1))) {
            return DefaultFilter;
        } else {
            int nextIndex = MenuFilters.indexOf(getPlayerTag(uuid));
            return MenuFilters.get(nextIndex+1);
        }
    }
}