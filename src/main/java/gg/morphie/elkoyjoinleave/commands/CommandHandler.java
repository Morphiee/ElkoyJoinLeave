package gg.morphie.elkoyjoinleave.commands;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.commands.playercommands.MenuCommand;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private ElkoyJoinLeave plugin;

    public CommandHandler(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("messages")) {
            if (args.length == 0) {
                //player only
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    new MenuCommand(plugin).openMenu(player);
                } else {
                    sender.sendMessage(new StringUtils().addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ConsoleSenderError")));
                }
                return true;
            }
        }
        return false;
    }
}
