package com.nastalgua.storage.commands;

import com.nastalgua.storage.helpers.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            GUI.openChestGUI(player);
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

    // Todo: Check for interaction with item stack
    // Todo: Different GUIs

}
