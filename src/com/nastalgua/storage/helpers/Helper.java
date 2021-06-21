package com.nastalgua.storage.helpers;

import com.nastalgua.storage.commands.StorageCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.CheckForNull;
import java.util.*;

public class Helper {

    public static List<ItemStack> oldItemStacks = new ArrayList<>();
    public static List<ItemStack> newItemStacks = new ArrayList<>();

    @CheckForNull
    public static Block getDoubleChest(Block block) {
        Directional blockDir = (Directional) block.getBlockData();

        Block block1 = null, block2 = null;

        if (blockDir.getFacing() == BlockFace.NORTH || blockDir.getFacing() == BlockFace.SOUTH) {
            block1 = block.getLocation().add(-1, 0, 0).getBlock();
            block2 = block.getLocation().add(+1, 0, 0).getBlock();
        } else {
            block1 = block.getLocation().add(0, 0, -1).getBlock();
            block2 = block.getLocation().add(0, 0, +1).getBlock();
        }

        if (block1 == null && block2 == null) return null;

        // this needs to come first b/c minecraft law: chest between 2 chest, makes double chest with chest on left
        if (block1.getType() == Material.CHEST) {
            Directional chest1Dir = (Directional) block1.getBlockData();

            if (chest1Dir.getFacing() == blockDir.getFacing()) {
                return block1;
            }
        }

        if (block2.getType() == Material.CHEST) {
            Directional chest2Dir = (Directional) block2.getBlockData();

            if (chest2Dir.getFacing() == blockDir.getFacing()) {
                return block2;
            }
        }

        return null;
    }

    public static List<OfflinePlayer> getOnlinePlayers() {
        List<OfflinePlayer> players = new ArrayList<>();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            players.add(player);
        }

        return players;

    }

    public static boolean invalidClick(InventoryClickEvent event, Player player) {
        if (StorageCommand.isCheckingStorage(player.getName())) {
            return event.getSlot() == -999
                    || event.getCurrentItem() == null
                    || event.getCurrentItem().getType() == Material.AIR;
        }

        return false;

    }

    public static boolean isStorageBlock(Material block) {
        return  block == Material.CHEST ||
                (block.ordinal() >= Material.WHITE_SHULKER_BOX.ordinal() && block.ordinal() <= Material.BLACK_SHULKER_BOX.ordinal()) ||
                block == Material.BARREL ||
                block == Material.TRAPPED_CHEST;
    }

    @CheckForNull
    public static Block getMasterBlock(PersistentData data, Player player) {
        if (data.containerHas(PersistentData.MASTER_CHEST_KEY, PersistentDataType.INTEGER_ARRAY)) {
            int[] pos = data.containerGetIntArray(PersistentData.MASTER_CHEST_KEY);
            return player.getWorld().getBlockAt(pos[0], pos[1], pos[2]);

        }

        return null;
    }

    public static void setInventoryItems(Container container, List<ItemStack> list, boolean cloneItem) {
        Map<Material, Integer> table = new HashMap<>();

        for (int i = 0; i < container.getInventory().getSize(); i++) {
            ItemStack item = container.getInventory().getItem(i);

            if (item == null || item.getType() == Material.AIR) continue;

            if (!table.containsKey(item.getType())) {
                table.put(item.getType(), item.getAmount());
            } else {
                table.put(item.getType(), table.get(item.getType()) + item.getAmount());
            }

        }

        for (Map.Entry item : table.entrySet()) {
            Material itemType = (Material) item.getKey();
            int amount = (int) item.getValue();

            list.add(new ItemStack(itemType, amount));
        }

    }

    public static List<String> getChanges() {
        List<String> changes = new ArrayList<>();

        for (ItemStack oldItem : oldItemStacks) {
            boolean stop = false;

            for (int i = 0; i < newItemStacks.size(); i++) {
                ItemStack newItem = newItemStacks.get(i);

                if (oldItem.isSimilar(newItem)) {
                    if (newItem.getAmount() != oldItem.getAmount()) {
                        int amount = oldItem.getAmount() - newItem.getAmount();
                        if (amount > 0) {
                            changes.add("- " + "(x" + amount + ") " + oldItem.getType());
                        }

                        stop = true;
                        break;
                    }
                }
            }

            if (stop) continue;

            if (!newItemStacks.contains(oldItem)) {
                changes.add("- " + "(x" + oldItem.getAmount() + ") " + oldItem.getType());
            }
        }

        for (ItemStack newItem : newItemStacks) {
            boolean stop = false;

            for (int i = 0; i < oldItemStacks.size(); i++) {
                ItemStack oldItem = oldItemStacks.get(i);

                if (newItem.isSimilar(oldItem)) {
                    if (oldItem.getAmount() != newItem.getAmount()) {
                        int amount = oldItem.getAmount() - newItem.getAmount();

                        if (amount < 0) {
                            changes.add("+ " + "(x" + Math.abs(amount) + ") " + newItem.getType());
                        }

                        stop = true;
                        break;
                    }
                }

            }

            if (stop) continue;

            if (!oldItemStacks.contains(newItem)) {
                changes.add("+ " + "(x" + newItem.getAmount() + ") " + newItem.getType());
            }

        }

        return changes;

    }
}
