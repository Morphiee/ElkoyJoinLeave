package gg.morphie.elkoyjoinleave.commands.admincommands;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ReloadCommand {
    private ElkoyJoinLeave plugin;

    public ReloadCommand(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    public void reloadPlugin(Player player) {
        if (player.hasPermission("elcore.messages.admin") || player.hasPermission("elcore.messages.reload")  ) {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ShopHistory");
            if (this.plugin != null) {
                this.plugin.reloadConfig();
                this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
                this.plugin.getServer().getPluginManager().enablePlugin(this.plugin);
                player.sendMessage(new StringUtils().addColor(this.plugin.getMessage("Prefix") + this.plugin.getMessage("ReloadMessage")));
            }
        } else {
            player.sendMessage(new StringUtils().addColor(this.plugin.getMessage("ErrorPrefix") + this.plugin.getMessage("NoPermsMessage")));
        }
    }
}
