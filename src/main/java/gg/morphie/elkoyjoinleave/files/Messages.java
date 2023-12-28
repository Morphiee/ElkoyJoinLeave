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
        cfg.addDefault("Startup.Line1", " &9___________      ____.____ ");
        cfg.addDefault("Startup.Line2", "&9\\_   _____/     |    |    |          &aPlugin Version&8: &b" + this.plugin.Version);
        cfg.addDefault("Startup.Line3", "  &9|    __)_     |    |    |          &aAuthor&8: &bMorphie");
        cfg.addDefault("Startup.Line4", "  &9|        \\/\\__|    |    |___");
        cfg.addDefault("Startup.Line5", " &9/_______  /\\________|_______ \\");
        cfg.addDefault("Startup.Line6", "        &9\\/                  \\/");
        cfg.addDefault("Startup.Line7", "______________________________________________________________________");
        cfg.addDefault("Startup.Config", "&aConfig&7: &2Loaded");
        cfg.addDefault("Startup.GenConfig", "&aConfig&7: &bGenerating");
        cfg.addDefault("Startup.CleanerCheck", "&aPlayerData Cleaner&8: &bChecking for old files.");
        cfg.addDefault("Startup.CleanerNoneCleared", "&aPlayerData Cleaner&8: &cNo files cleared.");
        cfg.addDefault("Startup.CleanerCleared", "&aPlayerData Cleaner&8: &2Successfully cleared %FILES_DELETED% files.");
        cfg.addDefault("ErrorPrefix", "&8&l[&c&l!&8&l] ");
        cfg.addDefault("ConsoleSenderError", "&cOnly a player can send this message!");
        cfg.addDefault("NoPermsMessage", "&cInvalid permissions!");

        // GUI Messages
        cfg.addDefault("Menu.Title", "&9&lElkoyJoinLeave Messages");
        cfg.addDefault("Menu.NextPageItem", "&aGo to Next Page &8&l>>");
        cfg.addDefault("Menu.PreviousPageItem", "&8&l<< &aGo to Previous Page");
        cfg.addDefault("Menu.FilterItem.Title", "&3&lMenu Filter &7(&a%CURRENT_FILTER%&7)");
        List<String> list2 = new ArrayList<String>();
        list2.add(" ");
        list2.add("&3&lCurrent Filter Tag:");
        list2.add("&b&l| &7%ALL%");
        list2.add("&b&l| &7%JOIN%");
        list2.add("&b&l| &7%LEAVE%");
        list2.add(" ");
        list2.add("&b➥ Click to change the current menu filter");
        cfg.addDefault("Menu.FilterItem.Lore", list2);
    }
}
