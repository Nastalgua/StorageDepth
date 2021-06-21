package com.nastalgua.storage.commands;

import com.nastalgua.storage.Main;
import com.nastalgua.storage.events.Open;
import com.nastalgua.storage.helpers.GUI;
import com.nastalgua.storage.helpers.Helper;
import com.nastalgua.storage.helpers.PersistentData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StorageCommand implements CommandExecutor {

    final String ALREADY_ADDED = "This player already has been added.";

    public static List<String> checkingStorage = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player = (Player) commandSender;

        Main.currentBlock = player.getTargetBlock((Set<Material>) null, 10);

        if (!Helper.isStorageBlock(Main.currentBlock.getType())) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please look at a storage block...");
            return false;
        }

        PersistentData currentBlockData = new PersistentData(Main.currentBlock);

        // double chest: slave chest shifts to master chest
        if (currentBlockData.containerHas(PersistentData.MASTER_CHEST_KEY, PersistentDataType.INTEGER_ARRAY)) {
            int[] pos = currentBlockData.containerGetIntArray(PersistentData.MASTER_CHEST_KEY);

            Block masterBlock = player.getWorld().getBlockAt(pos[0], pos[1], pos[2]);

            Main.currentBlock = masterBlock;
            currentBlockData = new PersistentData(masterBlock);
        }

        if (!currentBlockData.containerHas(PersistentData.USERS_KEY, PersistentDataType.STRING)) return false;
        if (!currentBlockData.containerGetString(PersistentData.USERS_KEY).contains(player.getUniqueId().toString())) {
            player.sendMessage(Open.BLOCK_MSG);
            return false;
        }

        String subCmd;

        if (args.length > 0) {
            subCmd = args[0].toLowerCase();
        } else {
            GUI.openChestGUI(player);
            return false;
        }

        if (args.length > 2) player.sendMessage("Provided too many arguments!");

        String playerUUID = Bukkit.getPlayer(args[1]).getUniqueId().toString();

        switch (subCmd) {
            case "add":
                if (!alreadyAdded(playerUUID)) {
                    addPlayer(player, args[1], playerUUID);
                } else {
                    player.sendMessage(ALREADY_ADDED);
                }

                break;
            case "remove":
                // DO NOT REMOVE THIS IF STATEMENT
                // Storage block will be stuck in the world.
                if (currentBlockData.containerGetString(PersistentData.USERS_KEY)
                        .contains(player.getUniqueId().toString())) {
                    player.sendMessage("Nice try.");
                    break;
                }

                if (currentBlockData.containerGetString(PersistentData.USERS_KEY).contains(playerUUID)) {
                    removePlayer(player, args[1], playerUUID);
                } else {
                    player.sendMessage("That player already has no access to this storage block.");
                }

                break;
        }

        return false;
    }

    public static boolean isCheckingStorage(String playerName) {
        return checkingStorage.contains(playerName);
    }

    public static boolean alreadyAdded(String playerUUID) {
        PersistentData data = new PersistentData(Main.currentBlock);

        if (!data.containerHas(PersistentData.USERS_KEY, PersistentDataType.STRING)) return false;

        return data.containerGetString(PersistentData.USERS_KEY).contains(playerUUID);

    }

    public static void addPlayer(Player fromPlayer, String toPlayerName, String toPlayerUUID) {
        PersistentData data = new PersistentData(Main.currentBlock);

        String str = data.containerGetString(PersistentData.USERS_KEY) + toPlayerUUID + ",";
        data.containerSetString(PersistentData.USERS_KEY, str);

        data.update();

        fromPlayer.sendMessage("! Added " + toPlayerName + " !");
        fromPlayer.closeInventory();

    }

    public static void removePlayer(Player fromPlayer, String toPlayerName, String toPlayerUUID) {
        PersistentData data = new PersistentData(Main.currentBlock);

        String str = data.containerGetString(PersistentData.USERS_KEY).replace(toPlayerUUID + ",", "");

        data.containerSetString(PersistentData.USERS_KEY, str);
        data.update();

        fromPlayer.sendMessage("! Removed " + toPlayerName + " !");
        fromPlayer.closeInventory();

    }

}
