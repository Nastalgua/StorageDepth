package com.nastalgua.storage.events;

import com.nastalgua.storage.helpers.Helper;
import com.nastalgua.storage.helpers.PersistentData;

import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class Open implements Listener {

    public static final String BLOCK_MSG = "You do not own this chest.";

    public Open() {}

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;

        Block openBlock = event.getClickedBlock();
        Player player = event.getPlayer();

        if (player.isOp()) return;
        if (!Helper.isStorageBlock(openBlock.getType())) return;
        if (!(openBlock.getState() instanceof TileState)) return;

        PersistentData openBlockData = new PersistentData(openBlock);

        String users = "";

        if (openBlockData.containerHas(PersistentData.USERS_KEY, PersistentDataType.STRING)) {
            users = openBlockData.containerGetString(PersistentData.USERS_KEY);
        }

        if (openBlockData.containerHas(PersistentData.MASTER_CHEST_KEY, PersistentDataType.INTEGER_ARRAY)) {
            int[] pos = openBlockData.containerGetIntArray(PersistentData.MASTER_CHEST_KEY);

            Block masterChest = player.getWorld().getBlockAt(pos[0], pos[1], pos[2]);

            if (!(masterChest.getState() instanceof TileState)) return;

            PersistentData masterChestData = new PersistentData(masterChest);

            users = masterChestData.containerGetString(PersistentData.USERS_KEY);

        }

        if (users.contains(player.getUniqueId().toString())) {
            return;
        } else {
            event.setCancelled(true);
            event.getPlayer().sendMessage(BLOCK_MSG);
        }

    }

}
