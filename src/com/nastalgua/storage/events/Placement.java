package com.nastalgua.storage.events;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Placement implements Listener {

    public static final String KEY_NAME = "private-chest";

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!StorageCommand.isStorageBlock(event.getBlock().getType())) return;
        if (!(event.getBlock().getState() instanceof TileState)) return;

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), KEY_NAME);

        container.set(key, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString());

        state.update();

        event.getPlayer().sendMessage("Chest Locked!");

    }

}
