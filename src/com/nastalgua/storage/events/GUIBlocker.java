package com.nastalgua.storage.events;

import com.nastalgua.storage.commands.StorageCommand;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIBlocker implements Listener {

    public GUIBlocker() {}

    @EventHandler
    public void onGUIActivation(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (StorageCommand.checkingStorage.contains(player.getName()) && !invalidClick(player, event))
            event.setCancelled(true);

    }

    @EventHandler
    public void onGUIClosing(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        if (StorageCommand.checkingStorage.contains(player.getName()))
            StorageCommand.checkingStorage.remove(player.getName());

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
