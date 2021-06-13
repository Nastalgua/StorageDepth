package com.nastalgua.storage.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StorageCommand implements CommandExecutor {

    public static List<String> checkingStorage = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player = (Player) commandSender;

        // check if looking at storage block
        Material cBlock = player.getTargetBlock((Set<Material>) null, 10).getType();

        if (!isStorageBlock(cBlock)) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please look at a storage block...");
            return false;
        }

        String subCmd;

        if (args.length > 0) {
            subCmd = args[0].toLowerCase();
        } else {
            subCmd = "";
            openChestGUI(player);
        }

        switch (subCmd) {
            case "add":
                break;
            case "remove":
                break;
        }

        return false;
    }

    public boolean isStorageBlock(Material block) {
        return  block == Material.CHEST ||
                (block.ordinal() >= Material.WHITE_SHULKER_BOX.ordinal() && block.ordinal() <= Material.BLACK_SHULKER_BOX.ordinal()) ||
                block == Material.BARREL ||
                block == Material.TRAPPED_CHEST ||
                block == Material.CHEST_MINECART;
    }

    public void openChestGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, player.getDisplayName() + "'s Chest");

        checkingStorage.add(player.getName());

        List<String> historyLore = new ArrayList<>();
        historyLore.add(ChatColor.GRAY + "Check chest history.");

        List<String> addLore = new ArrayList<>();
        addLore.add(ChatColor.GRAY + "Add players who can");
        addLore.add(ChatColor.GRAY + "access this storage block.");

        List<String> removeLore = new ArrayList<>();
        removeLore.add(ChatColor.GRAY + "Remove players who can");
        removeLore.add(ChatColor.GRAY + "access this storage block.");

        ItemStack add = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta addMeta = add.getItemMeta();
        addMeta.setDisplayName(ChatColor.GREEN + "Add Player");
        addMeta.setLore(addLore);
        add.setItemMeta(addMeta);

        ItemStack history = new ItemStack(Material.COBWEB);
        ItemMeta historyMeta = history.getItemMeta();
        historyMeta.setDisplayName(ChatColor.DARK_GRAY + "History");
        historyMeta.setLore(historyLore);
        history.setItemMeta(historyMeta);

        ItemStack remove = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta removeMeta = remove.getItemMeta();
        removeMeta.setDisplayName(ChatColor.RED + "Remove Player");
        removeMeta.setLore(removeLore);
        remove.setItemMeta(removeMeta);

        gui.setItem(11, add);
        gui.setItem(13, history);
        gui.setItem(15, remove);

        player.openInventory(gui);
    }

}
