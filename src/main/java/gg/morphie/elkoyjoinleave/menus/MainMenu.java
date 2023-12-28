package gg.morphie.elkoyjoinleave.menus;

import de.themoep.inventorygui.*;
import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.util.ItemStackUtils;
import gg.morphie.elkoyjoinleave.util.MenuFilterManager;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import gg.morphie.elkoyjoinleave.util.playerdata.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

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
        ItemStack material;
        List<String> JoinMessages = plugin.getConfig().getStringList("JoinMessages");
        List<String> LeaveMessages = plugin.getConfig().getStringList("LeaveMessages");
        UUID uuid = p.getUniqueId();

        //Filler Item
        gui.setFiller(new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("FillerItem.ItemName"), 1, plugin.getConfig().getInt("FillerItem.CustomModelID"), null, null, false));

        // Previous page
        gui.addElement(new GuiPageElement('(', new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("PreviousPageItem.ItemName"), 1, plugin.getConfig().getInt("PreviousPageItem.CustomModelID"), null, null, false), GuiPageElement.PageAction.PREVIOUS, plugin.getMessage("Menu.PreviousPageItem")));

        // Next page
        gui.addElement(new GuiPageElement(')', new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("NextPageItem.ItemName"), 1, plugin.getConfig().getInt("NextPageItem.CustomModelID"), null, null, false), GuiPageElement.PageAction.NEXT, plugin.getMessage("Menu.NextPageItem")));

        ArrayList<String> filterLore = new ArrayList();
        for (String s : plugin.getMessageList("Menu.FilterItem.Lore")) {
            filterLore.add(new StringUtils().addColor(s
                    .replace("%ALL%", new MenuFilterManager(plugin).getTag("ALL", p.getUniqueId()))
                    .replace("%JOIN%", new MenuFilterManager(plugin).getTag("JOIN", p.getUniqueId()))
                    .replace("%LEAVE%", new MenuFilterManager(plugin).getTag("LEAVE", p.getUniqueId()))));
        }
        gui.addElement(new DynamicGuiElement('1', (viewer) -> new StaticGuiElement('d', new ItemStackUtils(plugin).createItem(plugin.getConfig().getString("FilterItem.ItemName"), 1, plugin.getConfig().getInt("FilterItem.CustomModelID"), null, filterLore, false),
                click -> {
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
                new StringUtils().addColor(plugin.getMessage("Menu.FilterItem.Title").replace("%CURRENT_FILTER%", new MenuFilterManager(plugin).getPlayerTag(p.getUniqueId()))))));

        gui.addElement(group);
        gui.show(p);
    }
}
