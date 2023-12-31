package gg.morphie.elkoyjoinleave.events;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import gg.morphie.elkoyjoinleave.util.StringUtils;
import gg.morphie.elkoyjoinleave.util.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class LeaveEvent implements Listener {

    private ElkoyJoinLeave plugin;

    public LeaveEvent(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        List<String> LeaveMessages = this.plugin.getConfig().getStringList("LeaveMessages");
        boolean hasMessage = false;
        for (String baseMessage : LeaveMessages) {
            String[] parts = baseMessage.split(":");
            hasMessage = parts[1].equals(new PlayerDataManager(plugin).getString(uuid, "CurrentLeaveMessage"));
            break;
        }
        if (hasMessage) {
            Bukkit.broadcastMessage(new StringUtils().addColor(new PlayerDataManager(plugin).getString(uuid, "CurrentLeaveMessage").replace("%PLAYER%", e.getPlayer().getName())));
        } else {
            Bukkit.broadcastMessage(new StringUtils().addColor(plugin.getConfig().getString("DefaultLeaveMessage").replace("%PLAYER%", e.getPlayer().getName())));
        }
    }
}
