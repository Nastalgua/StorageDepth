package com.nastalgua.storage;

import com.nastalgua.storage.events.GUIs.ActivateGUI;
import com.nastalgua.storage.events.GUIs.CloseInventoryGUI;
import com.nastalgua.storage.events.GUIs.OpenInventoryGUI;
import com.nastalgua.storage.events.Open;
import com.nastalgua.storage.events.Placement;
import com.nastalgua.storage.helpers.GUI;
import com.nastalgua.storage.helpers.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.nastalgua.storage.commands.StorageCommand;

public class Main extends JavaPlugin {

    private static final String[] GUI_NAMES = { "Add Players", "History", "Remove Players" };

    public static Block currentBlock = null;

    @Override
    public void onEnable() {
        System.out.println("StorageDepth is enabled!");

        getCommand("storage").setExecutor(new StorageCommand());

        Bukkit.getServer().getPluginManager().registerEvents(new ActivateGUI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OpenInventoryGUI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CloseInventoryGUI(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new Open(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Placement(), this);
    }

    @Override
    public void onDisable() {
        System.out.println("StorageDepth is disabled...");
    }

    public static void changePage(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Pagination.RIGHT_NAME)) {
            Inventory gui = Bukkit.createInventory(null, 27, GUI_NAMES[Pagination.currentGUI.toInt()]);
            StorageCommand.checkingStorage.add(player.getName());

            switch (Pagination.currentGUI) {
                case ADD_PLAYERS:
                    GUI.addPlayersPagination.currentPage++;
                    GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                case HISTORY:
                    GUI.historyPagination.currentPage++;
                    GUI.historyPagination.loadPage(gui, Material.PAPER, GUI.historyPlayerNames, GUI.historyChanges, false);
                    break;
                case REMOVE_PLAYERS:
                    GUI.removePlayersPagination.currentPage++;
                    GUI.removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
            }

            player.openInventory(gui);

        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Pagination.LEFT_NAME)) {
            Inventory gui = Bukkit.createInventory(null, 27, GUI_NAMES[Pagination.currentGUI.toInt()]);
            StorageCommand.checkingStorage.add(player.getName());

            switch (Pagination.currentGUI) {
                case ADD_PLAYERS:
                    GUI.addPlayersPagination.currentPage--;
                    GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                case HISTORY:
                    GUI.historyPagination.currentPage--;
                    GUI.historyPagination.loadPage(gui, Material.PAPER, GUI.historyPlayerNames, GUI.historyChanges, false);
                    break;
                case REMOVE_PLAYERS:
                    GUI.removePlayersPagination.currentPage--;
                    GUI.removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
            }

            player.openInventory(gui);

        }
    }

}
