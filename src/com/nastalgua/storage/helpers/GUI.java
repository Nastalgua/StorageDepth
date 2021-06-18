package com.nastalgua.storage.helpers;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.events.Placement;
import org.bukkit.*;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GUI {

    public static Pagination<OfflinePlayer> addPlayersPagination = new Pagination<>(Main.testPlayers);
    public static Pagination<OfflinePlayer> removePlayersPagination = new Pagination<>(Main.testPlayers);

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

    // TODO: Add persistent data and filter out player already added
    public static void showAddPlayers(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Add Player");

        List<OfflinePlayer> players = new ArrayList<>();

        for (OfflinePlayer p : Main.testPlayers) {
            if (p.getUniqueId() == player.getUniqueId()) continue;
            if (StorageCommand.alreadyAdded(p.getUniqueId().toString())) continue;

            players.add(p);

        }

        addPlayersPagination.updateList(players);
        addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);

        player.openInventory(gui);
    }

    public static void showRemovePlayers(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Remove Players");

        List<OfflinePlayer> players = new ArrayList<>();

        TileState state = (TileState) StorageCommand.currentBlock.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), Placement.KEY_NAME);

        String str = container.get(key, PersistentDataType.STRING);

        // get all players already added
        for (int i = 0; i < str.length(); i += 38) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(str.substring(i, i + 36)));
            players.add(p);
        }

        removePlayersPagination.updateList(players);
        removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);

        player.openInventory(gui);
    }

    public static void showHistory(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "History");
        player.openInventory(gui);
    }

}
