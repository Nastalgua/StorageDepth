package com.nastalgua.storage.events;

import com.nastalgua.storage.helpers.Helper;
import com.nastalgua.storage.helpers.PersistentData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Placement implements Listener {

    public Placement() {}

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block placedBlock = event.getBlockPlaced();
        Player player = event.getPlayer();

        if (!Helper.isStorageBlock(placedBlock.getType())) return;
        if (!(placedBlock.getState() instanceof TileState)) return;

        boolean unshiftPlacedChest = Helper.getDoubleChest(placedBlock) != null && !event.getPlayer().isSneaking();
        boolean shiftPlacedChest = event.getBlockAgainst().getType() == Material.CHEST && event.getPlayer().isSneaking() &&
                event.getBlock().getRelative(BlockFace.DOWN, 1).getType() != Material.CHEST;

        PersistentData placedBlockData = new PersistentData(placedBlock);

        if (unshiftPlacedChest || shiftPlacedChest) { // double chest
            Block sideChest = Helper.getDoubleChest(placedBlock);
            PersistentData sideChestData = new PersistentData(sideChest);

            int[] sideChestPos = {sideChest.getX(), sideChest.getY(), sideChest.getZ()};
            placedBlockData.containerSetIntArray(PersistentData.MASTER_CHEST_KEY, sideChestPos);

            int[] placedChestPos = {placedBlock.getX(), placedBlock.getY(), placedBlock.getZ()};
            sideChestData.containerSetIntArray(PersistentData.SLAVE_CHEST_KEY, placedChestPos);

            sideChestData.update();

        } else { // single storage block
            placedBlockData.containerSetString(PersistentData.USERS_KEY, player.getUniqueId() + ",");
            placedBlockData.containerSetString(PersistentData.HISTORY_KEY, "");

            player.sendMessage("Chest Locked");
        }

        placedBlockData.update();

    }

}
