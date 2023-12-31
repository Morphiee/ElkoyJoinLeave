package gg.morphie.elkoyjoinleave.util;

import gg.morphie.elkoyjoinleave.ElkoyJoinLeave;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemStackUtils {
    private ElkoyJoinLeave plugin;

    public ItemStackUtils(ElkoyJoinLeave plugin) {
        this.plugin = plugin;
    }

    ItemMeta lastMeta;

    public ItemStack createItem(String paramString1, int paramInt, int paramInt2, String paramString2, ArrayList<String> paramArrayList, boolean paramBoolean) {
        ItemStack localItemStack = new ItemStack(Material.matchMaterial(paramString1), paramInt);
        ItemMeta localItemMeta = localItemStack.getItemMeta();
        localItemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        localItemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
        localItemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
        if (paramBoolean) {
            localItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            localItemMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        }
        localItemMeta.setDisplayName(new StringUtils().addColor(paramString2));
        localItemMeta.setLore(paramArrayList);
        localItemMeta.setCustomModelData(paramInt2);
        localItemStack.setItemMeta(localItemMeta);

        lastMeta = localItemMeta;
        return localItemStack;
    }

    public ItemMeta getMeta() {
        return lastMeta;
    }
}
