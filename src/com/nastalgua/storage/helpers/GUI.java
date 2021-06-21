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

import java.util.*;

public class GUI {

    public static Pagination<OfflinePlayer> addPlayersPagination = new Pagination<>(new ArrayList<OfflinePlayer>());
    public static Pagination<OfflinePlayer> removePlayersPagination = new Pagination<>(new ArrayList<OfflinePlayer>());
    public static Pagination<String> historyPagination = new Pagination<>(new ArrayList<>());

    public static List<String> historyPlayerNames = new ArrayList<>();
    public static List<List<String>> historyChanges = new ArrayList<>();

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
        Inventory gui = Bukkit.createInventory(null, 27, "Add Player");

        List<OfflinePlayer> players = new ArrayList<>();

        for (OfflinePlayer p : Helper.getOnlinePlayers()) {
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

        PersistentData data = new PersistentData(Main.currentBlock);

        String str = data.containerGetString(PersistentData.USERS_KEY);

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

        PersistentData data = new PersistentData(Main.currentBlock);
        String str = data.containerGetString(PersistentData.HISTORY_KEY);

        List<String> changesStr = new ArrayList<>();

        int left = -1;
        int right = -1;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') left = i;
            if (str.charAt(i) == '/') right = i;

            if (left != -1 && right != -1) {
                historyPlayerNames.add(str.substring(left + 1, right));

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

            historyChanges.add(change);

        }

        Collections.reverse(changesStr);
        Collections.reverse(historyPlayerNames);
        Collections.reverse(historyChanges);

        historyPagination.updateList(changesStr);
        historyPagination.loadPage(gui, Material.PAPER, historyPlayerNames, historyChanges, false);

        player.openInventory(gui);
    }

}
