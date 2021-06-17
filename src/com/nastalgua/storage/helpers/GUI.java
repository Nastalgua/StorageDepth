package com.nastalgua.storage.helpers;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUI {

    public static Pagination<OfflinePlayer> addPlayersPagination = new Pagination<>(Main.testPlayers);

    public static void openChestGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, player.getDisplayName() + "'s Chest");

        StorageCommand.checkingStorage.add(player.getName());

        List<String> historyLore = new ArrayList<>();
        historyLore.add(ChatColor.GRAY + "Check chest history.");

        List<String> addLore = new ArrayList<>();
        addLore.add(ChatColor.GRAY + "Add players who can");
        addLore.add(ChatColor.GRAY + "access this storage block.");

        List<String> removeLore = new ArrayList<>();
        removeLore.add(ChatColor.GRAY + "Remove players who can");
        removeLore.add(ChatColor.GRAY + "access this storage block.");

        ItemStack add = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta addMeta = add.getItemMeta();
        addMeta.setDisplayName(ChatColor.GREEN + "Add Player");
        addMeta.setLore(addLore);
        add.setItemMeta(addMeta);

        ItemStack history = new ItemStack(Material.COBWEB);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName(ChatColor.DARK_GRAY + "History");
        historyMeta.setLore(historyLore);
        history.setItemMeta(historyMeta);

        ItemStack remove = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta removeMeta = remove.getItemMeta();
        removeMeta.setDisplayName(ChatColor.RED + "Remove Player");
        removeMeta.setLore(removeLore);
        remove.setItemMeta(removeMeta);

        gui.setItem(11, add);
        gui.setItem(13, history);
        gui.setItem(15, remove);

        player.openInventory(gui);
    }

    public static void showAddPlayers(Player player) {
        // TODO: Pagination
        Inventory gui = Bukkit.createInventory(null, 27, "Add Player");
        // Bukkit.getServer().getOnlinePlayers().toArray()
//        addPlayersPagination = new Pagination<>(Main.testPlayers);
        addPlayersPagination.updateList(Main.testPlayers);

        addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, player);

        player.openInventory(gui);
    }

    public static void showRemovePlayers(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "Remove Players");
        player.openInventory(gui);
    }

    public static void showHistory(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "History");
        player.openInventory(gui);
    }

}
