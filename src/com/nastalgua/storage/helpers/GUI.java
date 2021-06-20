package com.nastalgua.storage.helpers;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.events.Placement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class GUI {

    public static Pagination<OfflinePlayer> addPlayersPagination = new Pagination<>(Main.testPlayers);
    public static Pagination<OfflinePlayer> removePlayersPagination = new Pagination<>(Main.testPlayers);
    public static Pagination<String> historyPagination = new Pagination<>(new ArrayList<>());

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

        for (OfflinePlayer p : Main.getOnlinePlayers()) {
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

        PersistentData data = new PersistentData(Placement.KEY_NAME, StorageCommand.currentBlock);

        String str = data.container.get(data.key, PersistentDataType.STRING);

        // get all players already added
        int oldIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ',') continue;
            if (oldIndex == i) continue;

            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(str.substring(oldIndex, i)));
            oldIndex = i + 1;

            if (p.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())) continue;

            players.add(p);

        }

        removePlayersPagination.updateList(players);
        removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);

        player.openInventory(gui);
    }

    public static void showHistory(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "History");

        PersistentData data = new PersistentData(PersistentData.HISTORY_NAME, StorageCommand.currentBlock);
        String str = data.container.get(data.key, PersistentDataType.STRING);

        List<String> playerNames = new ArrayList<>();
        List<String> changesStr = new ArrayList<>();

        int left = -1;
        int right = -1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') left = i;
            if (str.charAt(i) == '/') right = i;

            if (left != -1 && right != -1) {
                playerNames.add(str.substring(left + 1, right));

                left = -1;
                right = -1;
            }
        }

        left = -1;
        right = -1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '<') left = i;
            if (str.charAt(i) == '>') right = i;

            if (left != -1 && right != -1) {
                changesStr.add(str.substring(left + 1, right));

                left = -1;
                right = -1;
            }

        }

        left = -1;
        right = -1;
        List<List<String>> changes = new ArrayList<>();
        for (String s : changesStr) {
            List<String> change = new ArrayList<>();

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '{') left = i;
                if (s.charAt(i) == '}') right = i;

                if (left != -1 && right != -1) {
                    String changeStr = s.substring(left + 1, right);

                    if (changeStr.charAt(0) == '+') {
                        change.add(ChatColor.GREEN + changeStr);
                    } else if (changeStr.charAt(0) == '-') {
                        change.add(ChatColor.RED + changeStr);
                    }

                    left = -1;
                    right = -1;
                }

            }

            changes.add(change);

        }

        Collections.reverse(changesStr);
        Collections.reverse(playerNames);
        Collections.reverse(changes);

        historyPagination.updateList(changesStr);
        historyPagination.loadPage(gui, Material.PAPER, playerNames, changes, false);

        player.openInventory(gui);
    }

}
