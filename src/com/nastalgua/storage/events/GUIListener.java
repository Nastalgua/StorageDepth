package com.nastalgua.storage.events;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.commands.StorageCommand;

import com.nastalgua.storage.helpers.GUI;
import com.nastalgua.storage.helpers.Pagination;
import com.nastalgua.storage.helpers.PersistentData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class GUIListener implements Listener {

    private final String[] GUINames = { "Add Players", "History", "Remove Players" };

    private static ArrayList<Integer> oldItemIndices = new ArrayList<>();
    private static ArrayList<ItemStack> oldItemStacks = new ArrayList<>();

    private static ArrayList<Integer> newItemIndices = new ArrayList<>();
    private static ArrayList<ItemStack> newItemStacks = new ArrayList<>();

    public GUIListener() {}

    // TODO: Make sure to test randomly generated chests

    @EventHandler
    public void onGUIActivation(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (StorageCommand.checkingStorage.contains(player.getName()) && !invalidClick(event, player)) event.setCancelled(true);

        if (!invalidClick(event, player)) {

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
                    case HISTORY:
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

            // make sure player doesn't click anything other than gui
            if (event.getCurrentItem().getType() == Material.AIR) return;

            changePage(event, player);

        }

    }

    @EventHandler
    public void onPlayerInteractStorage(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!StorageCommand.isStorageBlock(event.getClickedBlock().getType())) return;

        StorageCommand.currentBlock = event.getClickedBlock();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (StorageCommand.checkingStorage.contains(event.getPlayer().getName())) return;

        Block block = getTargetBlock((Player) event.getPlayer(), 5);

        if (!StorageCommand.isStorageBlock(block.getType())) return;

        Chest chest = (Chest) block.getState();

        InventoryHolder holder = chest.getInventory().getHolder();

//        if (holder instanceof DoubleChest) {
//            PersistentData data = new PersistentData(Placement.SECOND_BLOCK, block);
//            int[] secondBlockPos = data.container.get(data.key, PersistentDataType.INTEGER_ARRAY);
//            Chest secondBlock = (Chest) event.getPlayer().getWorld().getBlockAt(secondBlockPos[0], secondBlockPos[1], secondBlockPos[2]).getState();
//
//            for (int i = 0; i < secondBlock.getBlockInventory().getSize(); i++) {
//                ItemStack item = secondBlock.getBlockInventory().getItem(i);
//                if (item == null || item.getType() == Material.AIR) continue;
//
//                oldItemIndices.add(i);
//                oldItemStacks.add(item);
//            }
//
//        } else {
            for (int i = 0; i < chest.getBlockInventory().getSize(); i++) {
                ItemStack item = chest.getBlockInventory().getItem(i);
                if (item == null || item.getType() == Material.AIR) continue;

                oldItemIndices.add(i);
                oldItemStacks.add(new ItemStack(item));

            }
//        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        StorageCommand.currentBlock = null;

        if (StorageCommand.checkingStorage.contains(event.getPlayer().getName())) {
            StorageCommand.checkingStorage.remove(event.getPlayer().getName());
            return;
        }

        Block block = getTargetBlock((Player) event.getPlayer(), 200);

        if (!StorageCommand.isStorageBlock(block.getType())) return;

        Chest chest = (Chest) block.getState();

        InventoryHolder holder = chest.getInventory().getHolder();

//        if (holder instanceof DoubleChest) {
//            PersistentData data = new PersistentData(Placement.SECOND_BLOCK, block);
//            int[] secondBlockPos = data.container.get(data.key, PersistentDataType.INTEGER_ARRAY);
//            Chest secondBlock = (Chest) event.getPlayer().getWorld().getBlockAt(secondBlockPos[0], secondBlockPos[1], secondBlockPos[2]).getState();
//
//            for (int i = 0; i < secondBlock.getBlockInventory().getSize(); i++) {
//                ItemStack item = secondBlock.getBlockInventory().getItem(i);
//                if (item == null || item.getType() == Material.AIR) continue;
//
//                newItemIndices.add(i);
//                newItemStacks.add(item);
//            }
//
//        } else {
            for (int i = 0; i < chest.getBlockInventory().getSize(); i++) {
                ItemStack item = chest.getBlockInventory().getItem(i);
                if (item == null || item.getType() == Material.AIR) continue;

                newItemIndices.add(i);
                newItemStacks.add(item);

            }
//        }

        // check for any changes
        List<String> changes = new ArrayList<>();

        boolean stop = false;
        for (ItemStack item : oldItemStacks) {
            stop = false;

            for (int i = 0; i < newItemStacks.size(); i++) {
                if (newItemStacks.get(i).isSimilar(item) && newItemStacks.get(i).getAmount() != item.getAmount()) {
                    changes.add("- " + "(x" + (item.getAmount() - newItemStacks.get(i).getAmount()) + ") " + item.getType());
                    stop = true;
                    break;
                }
            }

            if (stop) continue;

            if (!newItemStacks.contains(item)) {
                changes.add("- " + "(x" + item.getAmount() + ") " + item.getType());
            }
        }

        stop = false;
        for (ItemStack item : newItemStacks) {
            stop = false;

            for (int i = 0; i < oldItemStacks.size(); i++) {
                if (oldItemStacks.get(i).isSimilar(item) && oldItemStacks.get(i).getAmount() != item.getAmount()) {
                    stop = true;
                    break;
                }
            }

            if (stop) continue;

            if (!oldItemStacks.contains(item)) {
                changes.add("+ " + "(x" + item.getAmount() + ") " + item.getType());
            }
        }

        if (changes.size() > 0) {
            PersistentData data = new PersistentData(PersistentData.HISTORY_NAME, block);

            String str = data.container.get(data.key, PersistentDataType.STRING)
                    + "[\\" + event.getPlayer().getName() + "/<";

            for (String change : changes) {
                str += "{" + (change) + "}";
            }

            /*
            if (data.container.has(new NamespacedKey(Main.getPlugin(Main.class), Placement.SECOND_BLOCK), PersistentDataType.INTEGER_ARRAY)) {
                int[] secondBlockPos = data.container.get(new NamespacedKey(Main.getPlugin(Main.class), Placement.SECOND_BLOCK), PersistentDataType.INTEGER_ARRAY);
                System.out.println(secondBlockPos[0] + " " + secondBlockPos[1] + " " + secondBlockPos[2]);
                Block secondBlock = event.getPlayer().getWorld().getBlockAt(secondBlockPos[0], secondBlockPos[1], secondBlockPos[2]);
                PersistentData secondBlockData = new PersistentData(PersistentData.HISTORY_NAME, secondBlock);

                secondBlockData.container.set(secondBlockData.key, PersistentDataType.STRING, (str + ">],"));
                secondBlockData.state.update();
            }
             */

            data.container.set(data.key, PersistentDataType.STRING, (str + ">],"));

            data.state.update();
        }

        oldItemIndices.clear();
        oldItemStacks.clear();

        newItemIndices.clear();
        newItemStacks.clear();

    }

    public boolean invalidClick(InventoryClickEvent event, Player player) {

        if (StorageCommand.checkingStorage.contains(player.getName())) {
            return (event.getSlot() == -999
             || event.getCurrentItem() == null
             || event.getCurrentItem().getType() == Material.AIR);
        }

        return false;

    }

    private void changePage(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Pagination.RIGHT_NAME)) {
            Inventory gui = Bukkit.createInventory(null, 27, GUINames[Pagination.currentGUI.toInt()]);
            StorageCommand.checkingStorage.add(player.getName());

            switch (Pagination.currentGUI) {
                case ADD_PLAYERS:
                    GUI.addPlayersPagination.currentPage++;
                    GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                case HISTORY:

                    break;
                case REMOVE_PLAYERS:
                    GUI.removePlayersPagination.currentPage++;
                    GUI.removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                default:

            }

            player.openInventory(gui);

        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Pagination.LEFT_NAME)) {
            Inventory gui = Bukkit.createInventory(null, 27, GUINames[Pagination.currentGUI.toInt()]);
            StorageCommand.checkingStorage.add(player.getName());

            switch (Pagination.currentGUI) {
                case ADD_PLAYERS:
                    GUI.addPlayersPagination.currentPage--;
                    GUI.addPlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                case HISTORY:

                    break;
                case REMOVE_PLAYERS:
                    GUI.removePlayersPagination.currentPage--;
                    GUI.removePlayersPagination.loadPage(gui, Material.PLAYER_HEAD, null, null, true);
                    break;
                default:

            }

            player.openInventory(gui);

        }
    }

    public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();

        while (iter.hasNext()) {
            lastBlock = iter.next();

            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }

        return lastBlock;
    }
}
