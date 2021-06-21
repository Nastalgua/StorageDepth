package com.nastalgua.storage.events.GUIs;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.helpers.GUI;
import com.nastalgua.storage.helpers.Helper;
import com.nastalgua.storage.helpers.Pagination;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

public class ActivateGUI implements Listener {

    public ActivateGUI() {}

    @EventHandler
    public void onGUIActivation(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (StorageCommand.checkingStorage.contains(player.getName()) && Helper.invalidClick(event, player))
            event.setCancelled(true);

        if (!Helper.invalidClick(event, player)) {
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
                    case REMOVE_PLAYERS:
                        StorageCommand.removePlayer(player, skull.getOwningPlayer().getName(), skull.getOwningPlayer().getUniqueId().toString());
                        break;
                }
            } else if (event.getCurrentItem().getType() == Material.PAPER) {
                player.sendMessage(event.getCurrentItem().getItemMeta().getDisplayName());

                for (String s : event.getCurrentItem().getItemMeta().getLore()) {
                    player.sendMessage(s);
                }

                player.closeInventory();
            }
        }

        // make sure player doesn't click anything other than gui
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        Main.changePage(event, player);

    }

}
