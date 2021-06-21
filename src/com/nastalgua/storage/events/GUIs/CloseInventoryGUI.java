package com.nastalgua.storage.events.GUIs;

import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.helpers.GUI;
import com.nastalgua.storage.helpers.Helper;
import com.nastalgua.storage.helpers.PersistentData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CloseInventoryGUI implements Listener {

    public CloseInventoryGUI() {}

    @EventHandler
    public void storageInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (StorageCommand.checkingStorage.contains(player.getName())) {
            StorageCommand.checkingStorage.remove(player.getName()); return;
        }

        Block block = player.getTargetBlock((Set<Material>) null, 200);

        if (!Helper.isStorageBlock(block.getType())) return;

        PersistentData blockData = new PersistentData(block);
        Block masterBlock = Helper.getMasterBlock(blockData, player);

        Container container = (Container) block.getState();

        if (masterBlock != null) container = (Chest) masterBlock.getState();

        Helper.setInventoryItems(container, Helper.newItemStacks, false);
        Collections.reverse(Helper.newItemStacks);

        List<String> changes = Helper.getChanges();

        if (changes.size() > 0) {
            PersistentData chestData = new PersistentData(container.getBlock());

            String str = chestData.containerGetString(PersistentData.HISTORY_KEY)
                    + "[\\" + event.getPlayer().getName() + "/<";

            for (String change : changes) str += "{" + change + "}";

            chestData.containerSetString(PersistentData.HISTORY_KEY, str + ">],");
            chestData.update();
        }

        Helper.oldItemStacks.clear();
        Helper.newItemStacks.clear();

        GUI.historyPlayerNames.clear();
        GUI.historyChanges.clear();

    }

}
