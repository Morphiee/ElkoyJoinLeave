package gg.morphie.elkoyjoinleave.events;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import gg.morphie.elkoyjoinleave.util.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class JoinEvent implements Listener {

    private ElkoyJoinLeave plugin;

    public JoinEvent(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        List<String> JoinMessages = this.plugin.getConfig().getStringList("JoinMessages");
        if (JoinMessages.contains(new PlayerDataManager(plugin).getString(uuid, "CurrentJoinMessage"))) {
            Bukkit.broadcastMessage(new StringUtils().addColor(new PlayerDataManager(plugin).getString(uuid, "CurrentJoinMessage").replace("%PLAYER%", e.getPlayer().getName())));
        } else {
            Bukkit.broadcastMessage(new StringUtils().addColor(plugin.getConfig().getString("DefaultJoinMessage").replace("%PLAYER%", e.getPlayer().getName())));
        }
    }
}
