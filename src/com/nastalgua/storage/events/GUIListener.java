package com.nastalgua.storage.events;

import com.nastalgua.storage.commands.StorageCommand;

import com.nastalgua.storage.helpers.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {

    public GUIListener() {}

    @EventHandler
    public void onGUIActivation(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        System.out.println("Contains player: " + StorageCommand.checkingStorage.contains(player.getName()));
        System.out.println("Valid Click: " + !invalidClick(player, event));

        if (StorageCommand.checkingStorage.contains(player.getName()) && !invalidClick(player, event)) {
            System.out.println("Cancel event");
            event.setCancelled(true);
        }

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
