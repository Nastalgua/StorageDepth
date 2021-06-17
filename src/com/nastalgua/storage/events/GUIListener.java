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

public class GUIListener implements Listener {

    public GUIListener() {}

    @EventHandler
    public void onGUIActivation(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (StorageCommand.checkingStorage.contains(player.getName()) && !invalidClick(player, event))
            event.setCancelled(true);


        if (!invalidClick(player, event)) {

            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                StorageCommand.checkingStorage.add(player.getName());
                GUI.showAddPlayers(player);
            } else if (event.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                StorageCommand.checkingStorage.add(player.getName());
                GUI.showRemovePlayers(player);
            } else if (event.getCurrentItem().getType() == Material.COBWEB) {
                StorageCommand.checkingStorage.add(player.getName());
                GUI.showHistory(player);
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Next Page")) {
                GUI.addPlayersPagination.currentPage++;

                Inventory gui = Bukkit.createInventory(null, 27, "Add Player");
                GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, player);

                player.openInventory(gui);
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Previous Page")) {
                GUI.addPlayersPagination.currentPage--;

                Inventory gui = Bukkit.createInventory(null, 27, "Add Player");
                GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, player);

                player.openInventory(gui);
            }

        }

    }

    @EventHandler
    public void onGUIClosing(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        if (StorageCommand.checkingStorage.contains(player.getName())) {
            StorageCommand.checkingStorage.remove(player.getName());
        }

    }

    public boolean invalidClick(Player player, InventoryClickEvent event) {

        if (StorageCommand.checkingStorage.contains(player.getName())) {
            return (event.getSlot() == -999
             || event.getCurrentItem() == null
             || event.getCurrentItem().getType() == Material.AIR);
        }

        return false;

    }

}
