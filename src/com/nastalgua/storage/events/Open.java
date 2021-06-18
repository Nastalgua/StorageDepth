package com.nastalgua.storage.events;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Open implements Listener {

    public static final String blockMsg = "You do not own this chest.";

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (!StorageCommand.isStorageBlock(event.getClickedBlock().getType())) return;
        if (!(event.getClickedBlock().getState() instanceof TileState)) return;

        TileState state = (TileState) event.getClickedBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), Placement.KEY_NAME);

        if (!container.has(key, PersistentDataType.STRING)) return;

        if (container.get(key, PersistentDataType.STRING).contains(event.getPlayer().getUniqueId().toString())) {
            return;
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(blockMsg);
        }

    }

}
