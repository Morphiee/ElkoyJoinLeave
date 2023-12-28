package gg.morphie.elkoyjoinleave;

import gg.morphie.elkoyjoinleave.commands.CommandHandler;
import gg.morphie.elkoyjoinleave.events.PlayerDataEvents;
import gg.morphie.elkoyjoinleave.files.Messages;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import gg.morphie.elkoyjoinleave.util.playerdata.PlayerDataCleaner;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ElkoyJoinLeave extends JavaPlugin implements Listener {
    public static Logger log = Logger.getLogger("Minecraft");
    public java.lang.String Version = "1.0.0";
    public Messages messagescfg;
    private PlayerDataEvents pe;

    public void onEnable() {
        Objects.requireNonNull(getCommand("messages")).setExecutor(new CommandHandler(this));
        this.pe = new PlayerDataEvents(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(this.pe, this);
        Version = this.getDescription().getVersion();
        loadConfigManager();
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line1")));
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line2")));
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line3")));
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line4")));
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line5")));
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line6")));
        getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Line7")));
        createConfig();
        if (this.getConfig().getBoolean("Settings.AutoDeletePlayerFiles.Enabled")) {
            getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.CleanerCheck")));
            new PlayerDataCleaner(this).cleanPlayerData();
        }
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.GenConfig")));
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
            } else {
                getServer().getConsoleSender().sendMessage(new StringUtils().addColor(this.getMessage("Startup.Config")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfigManager() {
        this.messagescfg = new Messages(this);
        this.messagescfg.setup();
    }

    public java.lang.String getMessage(java.lang.String string) {
        java.lang.String gotString = this.messagescfg.messagesCFG.getString(string);
        if (gotString != null) return gotString;
        return "Null Message";
    }

    public List<String> getMessageList(java.lang.String string) {
        return this.messagescfg.messagesCFG.getStringList(string);
    }
}
