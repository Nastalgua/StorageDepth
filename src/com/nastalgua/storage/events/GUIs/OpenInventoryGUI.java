package com.nastalgua.storage.events.GUIs;

import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.helpers.Helper;
import com.nastalgua.storage.helpers.PersistentData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Collections;
import java.util.Set;

public class OpenInventoryGUI implements Listener {

    public OpenInventoryGUI() {}

    @EventHandler
    public void storageInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Block block = player.getTargetBlock((Set<Material>) null, 5);

        if (StorageCommand.isCheckingStorage(player.getName())) return;
        if (!Helper.isStorageBlock(block.getType())) return;

        PersistentData blockData = new PersistentData(block);

        Block masterBlock = Helper.getMasterBlock(blockData, player);

        Container container = (Container) block.getState();

        if (masterBlock != null) container = (Chest) masterBlock.getState();

        Helper.setInventoryItems(container, Helper.oldItemStacks, true);
        Collections.reverse(Helper.oldItemStacks);

    }

}
