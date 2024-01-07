package gg.morphie.elkoyjoinleave.files;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Messages implements Listener {
    private ElkoyJoinLeave plugin;
    public FileConfiguration messagesCFG;
    public File messagesFile;

    public Messages(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }
        this.messagesFile = new File(this.plugin.getDataFolder(), "messages.yml");
        if (!this.messagesFile.exists()) {
            try {
                this.messagesFile.createNewFile();

                this.messagesCFG = YamlConfiguration.loadConfiguration(this.messagesFile);

                this.addDefaults(this.messagesCFG);

                this.messagesCFG.options().copyDefaults(true);
                saveMessages();
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create the messages.yml file");
            }
        }
        this.messagesCFG = YamlConfiguration.loadConfiguration(this.messagesFile);

        this.addDefaults(this.messagesCFG);
    }

    public void saveMessages() {
        try {
            this.messagesCFG.save(this.messagesFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the messages.yml file");
        }
    }

    public void reloadMessages() {
        this.messagesCFG = YamlConfiguration.loadConfiguration(this.messagesFile);
        this.addDefaults(this.messagesCFG);
    }

    private void addDefaults(FileConfiguration cfg) {
        // Start up Messages for console
        cfg.addDefault("Startup.Line1", " &3___________      ____.____ ");
        cfg.addDefault("Startup.Line2", "&3\\_   _____/     |    |    |          &aPlugin Version&8: &b" + this.plugin.Version);
        cfg.addDefault("Startup.Line3", "  &3|    __)_     |    |    |          &aAuthor&8: &bMorphie");
        cfg.addDefault("Startup.Line4", "  &3|        \\/\\__|    |    |___");
        cfg.addDefault("Startup.Line5", " &3/_______  /\\________|_______ \\");
        cfg.addDefault("Startup.Line6", "        &3\\/                  \\/");
        cfg.addDefault("Startup.Line7", "______________________________________________________________________");
        cfg.addDefault("Startup.Config", "&aConfig&7: &2Loaded");
        cfg.addDefault("Startup.GenConfig", "&aConfig&7: &bGenerating");
        cfg.addDefault("Startup.CleanerCheck", "&aPlayerData Cleaner&8: &bChecking for old files.");
        cfg.addDefault("Startup.CleanerNoneCleared", "&aPlayerData Cleaner&8: &cNo files cleared.");
        cfg.addDefault("Startup.CleanerCleared", "&aPlayerData Cleaner&8: &2Successfully cleared %FILES_DELETED% files.");
        cfg.addDefault("ErrorPrefix", "&8&l[&c&l!&8&l] ");
        cfg.addDefault("ConsoleSenderError", "&cOnly a player can send this message!");
        cfg.addDefault("NoPermsMessage", "&cInvalid permissions!");
        cfg.addDefault("ReloadMessage", "&aPlugin has been successfully reloaded!");

        // GUI Messages
        cfg.addDefault("Menu.Title", "&3&lElkoyJoinLeave Messages");
        cfg.addDefault("Menu.NextPageItem", "&aGo to Next Page &8&l>>");
        cfg.addDefault("Menu.PreviousPageItem", "&8&l<< &aGo to Previous Page");
        cfg.addDefault("Menu.FilterItem.Title", "&3&lMenu Filter &8(&a%CURRENT_FILTER%&8)");
        List<String> list = new ArrayList<String>();
        list.add(" ");
        list.add("&3&lCurrent Filter Tag:");
        list.add("&b&l| &7%ALL%");
        list.add("&b&l| &7%JOIN%");
        list.add("&b&l| &7%LEAVE%");
        list.add(" ");
        list.add("&b➥ Click to change the current menu filter");
        cfg.addDefault("Menu.FilterItem.Lore", list);
        cfg.addDefault("Menu.MessageItem.Title", "&3&l%TITLE% &8(%STATUS%&8)");
        List<String> list3 = new ArrayList<String>();
        list3.add(" ");
        list3.add("&b&l| &3&lType&8: %TYPE%");
        list3.add(" ");
        list3.add("&b&l| &3&lMessage Preview&8:");
        list3.add("%MESSAGE%");
        list3.add(" ");
        list3.add("&b➥ Click to set this as your %TYPE% &bmessage.");
        cfg.addDefault("Menu.MessageItem.Lore", list3);
    }
}
