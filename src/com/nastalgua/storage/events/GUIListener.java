package com.nastalgua.storage.events;

import com.nastalgua.storage.commands.StorageCommand;

import com.nastalgua.storage.helpers.GUI;
import com.nastalgua.storage.helpers.Pagination;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.SkullMeta;

public class GUIListener implements Listener {

    private final String[] GUINames = { "Add Players", "History", "Remove Players" };

    public GUIListener() {}

    @EventHandler
    public void onGUIActivation(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (StorageCommand.checkingStorage.contains(player.getName()) && !invalidClick(event, player)) event.setCancelled(true);

        if (!invalidClick(event, player)) {

            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                StorageCommand.checkingStorage.add(player.getName());

                GUI.showAddPlayers(player);
                Pagination.currentGUI = Pagination.GUIStatus.ADD_PLAYERS;

            } else if (event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                StorageCommand.checkingStorage.add(player.getName());

                GUI.showRemovePlayers(player);
                Pagination.currentGUI = Pagination.GUIStatus.REMOVE_PLAYERS;

            } else if (event.getCurrentItem().getType() == Material.COBWEB) {
                StorageCommand.checkingStorage.add(player.getName());

                GUI.showHistory(player);
                Pagination.currentGUI = Pagination.GUIStatus.HISTORY;

            } else if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                SkullMeta skull = (SkullMeta) event.getCurrentItem().getItemMeta();
                switch (Pagination.currentGUI) {
                    case ADD_PLAYERS:
                        StorageCommand.addPlayer(player, skull.getOwningPlayer().getName(), skull.getOwningPlayer().getUniqueId().toString());
                        break;
                    case HISTORY:
                        break;
                    case REMOVE_PLAYERS:
                        StorageCommand.removePlayer(player, skull.getOwningPlayer().getName(), skull.getOwningPlayer().getUniqueId().toString());
                        break;
                }
            }

            // make sure player doesn't click anything other than gui
            if (event.getCurrentItem().getType() == Material.AIR) return;

            changePage(event, player);

        }

    }

    @EventHandler
    public void onGUIClosing(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        if (StorageCommand.checkingStorage.contains(player.getName())) {
            StorageCommand.checkingStorage.remove(player.getName());
        }

    }

    public boolean invalidClick(InventoryClickEvent event, Player player) {

        if (StorageCommand.checkingStorage.contains(player.getName())) {
            return (event.getSlot() == -999
             || event.getCurrentItem() == null
             || event.getCurrentItem().getType() == Material.AIR);
        }

        return false;

    }

    private void changePage(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Pagination.RIGHT_NAME)) {
            Inventory gui = Bukkit.createInventory(null, 27, GUINames[Pagination.currentGUI.toInt()]);
            StorageCommand.checkingStorage.add(player.getName());

            switch (Pagination.currentGUI) {
                case ADD_PLAYERS:
                    GUI.addPlayersPagination.currentPage++;
                    GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                case HISTORY:

                    break;
                case REMOVE_PLAYERS:
                    GUI.removePlayersPagination.currentPage++;
                    GUI.removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                default:

            }

            player.openInventory(gui);

        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Pagination.LEFT_NAME)) {
            Inventory gui = Bukkit.createInventory(null, 27, GUINames[Pagination.currentGUI.toInt()]);
            StorageCommand.checkingStorage.add(player.getName());

            switch (Pagination.currentGUI) {
                case ADD_PLAYERS:
                    GUI.addPlayersPagination.currentPage--;
                    GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                case HISTORY:

                    break;
                case REMOVE_PLAYERS:
                    GUI.removePlayersPagination.currentPage--;
                    GUI.removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                default:

            }

            player.openInventory(gui);

        }
    }


}
