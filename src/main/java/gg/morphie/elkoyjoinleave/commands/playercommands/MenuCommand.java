package gg.morphie.elkoyjoinleave.commands.playercommands;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.menus.MainMenu;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import org.bukkit.entity.Player;

public class MenuCommand {
    private ElkoyJoinLeave plugin;

    public MenuCommand(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        if (!player.isSleeping()) {
            if (player.hasPermission("elkoyjoinleave.menu")) {
                new MainMenu(plugin).openGUI(player);
            } else {
                player.sendMessage(new StringUtils().addColor(this.plugin.getMessage("ErrorPrefix") + this.plugin.getMessage("NoPermsMessage")));
            }
        }
    }
}
