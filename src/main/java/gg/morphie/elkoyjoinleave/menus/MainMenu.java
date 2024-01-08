package gg.morphie.elkoyjoinleave.menus;

import de.themoep.inventorygui.*;
import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.util.ItemStackUtils;
import gg.morphie.elkoyjoinleave.util.MenuFilterManager;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import gg.morphie.elkoyjoinleave.util.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainMenu implements Listener {
    private ElkoyJoinLeave plugin;
    public MainMenu(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
        guiSetup = plugin.getConfig().getStringList("GuiSetup").toArray(new String[0]);
    }

    String[] guiSetup;
    public void openGUI(Player p) {
        InventoryGui gui = new InventoryGui(plugin, p, new StringUtils().addColor(plugin.getMessage("Menu.Title")), guiSetup);
        GuiElementGroup group = new GuiElementGroup('g');
        List<String> JoinMessages = plugin.getConfig().getStringList("JoinMessages");
        List<String> LeaveMessages = plugin.getConfig().getStringList("LeaveMessages");
        List<String> AllMessages = new ArrayList<String>();
        AllMessages.addAll(JoinMessages);
        AllMessages.addAll(LeaveMessages);
        UUID uuid = p.getUniqueId();

        //Filler Item
        gui.setFiller(new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("FillerItem.ItemName"), 1, plugin.getConfig().getInt("FillerItem.CustomModelID"), null, null, false));

        // Previous page
        gui.addElement(new GuiPageElement('(', new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("PreviousPageItem.ItemName"), 1, plugin.getConfig().getInt("PreviousPageItem.CustomModelID"), null, null, false), GuiPageElement.PageAction.PREVIOUS, plugin.getMessage("Menu.PreviousPageItem")));

        // Next page
        gui.addElement(new GuiPageElement(')', new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("NextPageItem.ItemName"), 1, plugin.getConfig().getInt("NextPageItem.CustomModelID"), null, null, false), GuiPageElement.PageAction.NEXT, plugin.getMessage("Menu.NextPageItem")));

        ArrayList<String> filterLore = new ArrayList();
        for (String s : plugin.getMessageList("Menu.FilterItem.Lore")) {
            filterLore.add(s
                    .replace("%ALL%", new MenuFilterManager(plugin).getTag("All", p.getUniqueId()))
                    .replace("%JOIN%", new MenuFilterManager(plugin).getTag("Join", p.getUniqueId()))
                    .replace("%LEAVE%", new MenuFilterManager(plugin).getTag("Leave", p.getUniqueId())));
        }
        gui.addElement(new DynamicGuiElement('1', (viewer) -> new StaticGuiElement('d', new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("FilterItem.ItemName"), 1, plugin.getConfig().getInt("FilterItem.CustomModelID"), null, null, false),
                (GuiElement.Action) click -> {
                    String playerCurrentFilter = new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
                    if (playerCurrentFilter.equalsIgnoreCase(new MenuFilterManager(plugin).getDefaultFilter())) {
                        new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new MenuFilterManager(plugin).getMenuFilters().get(0));
                        this.openGUI(p);
                        InventoryGui.clearHistory(p);
                        return true;
                    } else if (new MenuFilterManager(plugin).getMenuFilters().indexOf(playerCurrentFilter) == new MenuFilterManager(plugin).getMenuFilters().size()-1) {
                        new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new MenuFilterManager(plugin).getDefaultFilter());
                        this.openGUI(p);
                        InventoryGui.clearHistory(p);
                        return true;
                    } else {
                        int playerFilterIndex = new MenuFilterManager(plugin).getMenuFilters().indexOf(playerCurrentFilter);
                        new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new MenuFilterManager(plugin).getMenuFilters().get(playerFilterIndex+1));
                        this.openGUI(p);
                        InventoryGui.clearHistory(p);
                        return true;
                    }
                },
                new StringUtils().addColor(plugin.getMessage("Menu.FilterItem.Title").replace("%CURRENT_FILTER%", new MenuFilterManager(plugin).getPlayerTag(p.getUniqueId()))), new StringUtils().listToString(filterLore))));

        String playerCurrentFilter = new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
        if (playerCurrentFilter.equalsIgnoreCase(new MenuFilterManager(plugin).getDefaultFilter())) {
            for (int i = 0; i < AllMessages.size(); i++) {
                String baseMessage = AllMessages.get(i);
                String[] parts = baseMessage.split(":");
                String title = parts[0];
                String message = parts[1].replace("%PLAYER%", p.getName());
                String material;
                String permission = parts[4];
                int modelData;
                String type;
                String status;
                ArrayList<String> messageLore = new ArrayList();
                String titleMessage;
                boolean glow;

                if (JoinMessages.contains(AllMessages.get(i))) {
                    type = this.plugin.getConfig().getString("Settings.Colors.JoinColor") + "Join";
                } else if (LeaveMessages.contains(AllMessages.get(i))) {
                    type = this.plugin.getConfig().getString("Settings.Colors.LeaveColor") + "Leave";
                } else {
                    type = "&cBroken Config";
                }

                if (p.hasPermission("elcore.messages." + permission)) {
                    status = plugin.getConfig().getString("Settings.Colors.UnlockedStatusColor") + "Unlocked";
                } else {
                    status = plugin.getConfig().getString("Settings.Colors.LockedStatusColor") + "Locked";
                }

                if (this.plugin.getConfig().getBoolean("LockMessages.Enabled") && status.contains("Locked")) {
                    material = this.plugin.getConfig().getString("LockMessages.Material");
                    modelData = this.plugin.getConfig().getInt("LockMessages.CustomModelID");
                    for (String s : plugin.getMessageList("Menu.LockedItem.Lore")) {
                        messageLore.add(s.replace("%TYPE%", type).replace("%MESSAGE%", message));
                    }
                    titleMessage = plugin.getMessage("Menu.LockedItem.Title");
                    glow = plugin.getConfig().getBoolean("LockMessages.Glow");
                } else {
                    material = parts[2];
                    modelData = Integer.parseInt(parts[3]);
                    for (String s : plugin.getMessageList("Menu.MessageItem.Lore")) {
                        messageLore.add(s.replace("%TYPE%", type).replace("%MESSAGE%", message));
                    }
                    titleMessage = plugin.getMessage("Menu.MessageItem.Title");
                    glow = Boolean.parseBoolean(parts[5]);
                }

                group.addElement(new DynamicGuiElement('g', (viewer) -> // returning false will not cancel the initial click event to the gui
                        new StaticGuiElement('g', new ItemStackUtils(plugin).createItem(material, 1, modelData, message, null, glow),
                                click -> {
                                    if (click.getType().isLeftClick()) {
                                        if (status.contains("Unlocked")) {
                                            if (type.equalsIgnoreCase(this.plugin.getConfig().getString("Settings.Colors.JoinColor") + "Join")) {
                                                new PlayerDataManager(plugin).setString(uuid, "CurrentJoinMessage", message);
                                            } else {
                                                new PlayerDataManager(plugin).setString(uuid, "CurrentLeaveMessage", message);
                                            }
                                            p.sendMessage(new StringUtils().addColor(plugin.getMessage("Prefix") + plugin.getMessage("MessageSet").replace("%TYPE%", type)));
                                            return true;
                                        }
                                        return true;
                                    }
                                    return true; // returning false will not cancel the initial click event to the gui
                                },
                                new StringUtils().addColor(titleMessage).replace("%STATUS%", status).replace("%TITLE%", title), new StringUtils().listToString(messageLore))));
            }
        } else if (playerCurrentFilter.equalsIgnoreCase("Join")) {
            for (int i = 0; i < JoinMessages.size(); i++) {
                String baseMessage = JoinMessages.get(i);
                String[] parts = baseMessage.split(":");
                String title = parts[0];
                String message = parts[1].replace("%PLAYER%", p.getName());
                String material;
                String permission = parts[4];
                int modelData;
                String type = this.plugin.getConfig().getString("Settings.Colors.JoinColor") + "Join";
                String status;
                ArrayList<String> messageLore = new ArrayList();
                String titleMessage;
                boolean glow;

                if (p.hasPermission("elcore.messages." + permission)) {
                    status = plugin.getConfig().getString("Settings.Colors.UnlockedStatusColor") + "Unlocked";
                } else {
                    status = plugin.getConfig().getString("Settings.Colors.LockedStatusColor") + "Locked";
                }

                if (this.plugin.getConfig().getBoolean("LockMessages.Enabled") && status.contains("Locked")) {
                    material = this.plugin.getConfig().getString("LockMessages.Material");
                    modelData = this.plugin.getConfig().getInt("LockMessages.CustomModelID");
                    for (String s : plugin.getMessageList("Menu.LockedItem.Lore")) {
                        messageLore.add(s.replace("%TYPE%", type).replace("%MESSAGE%", message));
                    }
                    titleMessage = plugin.getMessage("Menu.LockedItem.Title");
                    glow = plugin.getConfig().getBoolean("LockMessages.Glow");
                } else {
                    material = parts[2];
                    modelData = Integer.parseInt(parts[3]);
                    for (String s : plugin.getMessageList("Menu.MessageItem.Lore")) {
                        messageLore.add(s.replace("%TYPE%", type).replace("%MESSAGE%", message));
                    }
                    titleMessage = plugin.getMessage("Menu.MessageItem.Title");
                    glow = Boolean.parseBoolean(parts[5]);
                }

                group.addElement(new DynamicGuiElement('g', (viewer) -> // returning false will not cancel the initial click event to the gui
                        new StaticGuiElement('g', new ItemStackUtils(plugin).createItem(material, 1, modelData, message, null, glow),
                                click -> {
                                    if (click.getType().isLeftClick()) {
                                        if (status.contains("Unlocked")) {
                                            new PlayerDataManager(plugin).setString(uuid, "CurrentJoinMessage", message);
                                            p.sendMessage(new StringUtils().addColor(plugin.getMessage("Prefix") + plugin.getMessage("MessageSet").replace("%TYPE%", type)));
                                            return true;
                                        }
                                        return true;
                                    }
                                    return true; // returning false will not cancel the initial click event to the gui
                                },
                                new StringUtils().addColor(titleMessage).replace("%STATUS%", status).replace("%TITLE%", title), new StringUtils().listToString(messageLore))));
            }
        } else if (playerCurrentFilter.equalsIgnoreCase("Leave")) {
            for (int i = 0; i < LeaveMessages.size(); i++) {
                String baseMessage = LeaveMessages.get(i);
                String[] parts = baseMessage.split(":");
                String title = parts[0];
                String message = parts[1].replace("%PLAYER%", p.getName());
                String material;
                String permission = parts[4];
                int modelData;
                String type = this.plugin.getConfig().getString("Settings.Colors.LeaveColor") + "Leave";
                String status;
                ArrayList<String> messageLore = new ArrayList();
                String titleMessage;
                boolean glow;

                if (p.hasPermission("elcore.messages." + permission)) {
                    status = plugin.getConfig().getString("Settings.Colors.UnlockedStatusColor") + "Unlocked";
                } else {
                    status = plugin.getConfig().getString("Settings.Colors.LockedStatusColor") + "Locked";
                }

                if (this.plugin.getConfig().getBoolean("LockMessages.Enabled") && status.contains("Locked")) {
                    material = this.plugin.getConfig().getString("LockMessages.Material");
                    modelData = this.plugin.getConfig().getInt("LockMessages.CustomModelID");
                    for (String s : plugin.getMessageList("Menu.LockedItem.Lore")) {
                        messageLore.add(s.replace("%TYPE%", type).replace("%MESSAGE%", message));
                    }
                    titleMessage = plugin.getMessage("Menu.LockedItem.Title");
                    glow = plugin.getConfig().getBoolean("LockMessages.Glow");
                } else {
                    material = parts[2];
                    modelData = Integer.parseInt(parts[3]);
                    for (String s : plugin.getMessageList("Menu.MessageItem.Lore")) {
                        messageLore.add(s.replace("%TYPE%", type).replace("%MESSAGE%", message));
                    }
                    titleMessage = plugin.getMessage("Menu.MessageItem.Title");
                    glow = Boolean.parseBoolean(parts[5]);
                }

                group.addElement(new DynamicGuiElement('g', (viewer) -> // returning false will not cancel the initial click event to the gui
                        new StaticGuiElement('g', new ItemStackUtils(plugin).createItem(material, 1, modelData, message, null, glow),
                                click -> {
                                    if (click.getType().isLeftClick()) {
                                        if (status.contains("Unlocked")) {
                                            new PlayerDataManager(plugin).setString(uuid, "CurrentLeaveMessage", message);
                                            p.sendMessage(new StringUtils().addColor(plugin.getMessage("Prefix") + plugin.getMessage("MessageSet").replace("%TYPE%", type)));
                                            return true;
                                        }
                                        return true;
                                    }
                                    return true; // returning false will not cancel the initial click event to the gui
                                },
                                new StringUtils().addColor(titleMessage).replace("%STATUS%", status).replace("%TITLE%", title), new StringUtils().listToString(messageLore))));
            }
        }
        gui.addElement(group);
        gui.show(p);
    }
}
