package com.nastalgua.storage.events;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import com.nastalgua.storage.helpers.PersistentData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Placement implements Listener {

    public static final String KEY_NAME = "private-chest";
    public static final String SECOND_BLOCK = "second-block";

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        // TODO: setup master block (block that is always referred to)
        if (event.getBlockAgainst().getType() == Material.CHEST) {
            Block blockAgainst = event.getBlockAgainst();
            Block thisBlock = event.getBlock();

            TileState againstState = (TileState) blockAgainst.getState();
            PersistentDataContainer againstContainer = againstState.getPersistentDataContainer();

            NamespacedKey againstKey = new NamespacedKey(Main.getPlugin(Main.class), SECOND_BLOCK);

            int[] againstPos = { thisBlock.getX(), thisBlock.getY(), thisBlock.getZ() };

            againstContainer.set(againstKey, PersistentDataType.INTEGER_ARRAY, againstPos);

            TileState state = (TileState) thisBlock.getState();
            PersistentDataContainer container = state.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), KEY_NAME);
            NamespacedKey historyKey = new NamespacedKey(Main.getPlugin(Main.class), PersistentData.HISTORY_NAME);

            int[] pos = { blockAgainst.getX(), blockAgainst.getY(), blockAgainst.getZ() };

            container.set(againstKey, PersistentDataType.INTEGER_ARRAY, pos);
            container.set(key, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString() + ",");
            container.set(historyKey, PersistentDataType.STRING, "");

            againstState.update();
            state.update();

            return;
        }

        if (!StorageCommand.isStorageBlock(event.getBlock().getType())) return;
        if (!(event.getBlock().getState() instanceof TileState)) return;

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), KEY_NAME);
        NamespacedKey historyKey = new NamespacedKey(Main.getPlugin(Main.class), PersistentData.HISTORY_NAME);

        container.set(key, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString() + ",");
        container.set(historyKey, PersistentDataType.STRING, "");

        state.update();

        event.getPlayer().sendMessage("Chest Locked!");

    }

}
