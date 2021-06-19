package com.nastalgua.storage.commands;

import com.nastalgua.storage.events.Open;
import com.nastalgua.storage.events.Placement;
import com.nastalgua.storage.helpers.GUI;
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
    public static Block currentBlock = null;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player = (Player) commandSender;

        // check if looking at storage block
        Block cBlock = player.getTargetBlock((Set<Material>) null, 10);
        currentBlock = cBlock;

        if (!isStorageBlock(cBlock.getType())) {
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Please look at a storage block...");
            return false;
        }

        PersistentData data = new PersistentData(Placement.KEY_NAME);

        if (!data.container.has(data.key, PersistentDataType.STRING)) return false;
        if (!data.container.get(data.key, PersistentDataType.STRING).contains(player.getUniqueId().toString())) {
            player.sendMessage(Open.blockMsg);
            return false;
        }

        String subCmd;

        if (args.length > 0) {
            subCmd = args[0].toLowerCase();
        } else {
            subCmd = "";
            GUI.openChestGUI(player);
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
                if (data.container.get(data.key, PersistentDataType.STRING).contains(player.getUniqueId().toString())) {
                    player.sendMessage("Nice try.");
                    break;
                }

                if (data.container.get(data.key, PersistentDataType.STRING).contains(playerUUID)) {
                    removePlayer(player, args[1], playerUUID);
                } else {
                    player.sendMessage("That player already has no access to this storage block.");
                }

                break;
        }

        return false;
    }

    public static boolean isStorageBlock(Material block) {
        return  block == Material.CHEST ||
                (block.ordinal() >= Material.WHITE_SHULKER_BOX.ordinal() && block.ordinal() <= Material.BLACK_SHULKER_BOX.ordinal()) ||
                block == Material.BARREL ||
                block == Material.TRAPPED_CHEST ||
                block == Material.CHEST_MINECART;
    }

    public static boolean alreadyAdded(String playerUUID) {
        PersistentData data = new PersistentData(Placement.KEY_NAME);

        if (!data.container.has(data.key, PersistentDataType.STRING)) return false;

        String str = data.container.get(data.key, PersistentDataType.STRING);

        return str.contains(playerUUID);

    }

    public static void addPlayer(Player fromPlayer, String toPlayerName, String toPlayerUUID) {
        PersistentData data = new PersistentData(Placement.KEY_NAME);

        String str = data.container.get(data.key, PersistentDataType.STRING) + toPlayerUUID + ",";

        data.container.set(data.key, PersistentDataType.STRING, str);
        data.state.update();

        fromPlayer.sendMessage("! Added " + toPlayerName + " !");
        fromPlayer.closeInventory();
    }

    public static void removePlayer(Player fromPlayer, String toPlayerName, String toPlayerUUID) {
        PersistentData data = new PersistentData(Placement.KEY_NAME);

        String str = data.container.get(data.key, PersistentDataType.STRING).replace(toPlayerUUID + ",", "");

        data.container.set(data.key, PersistentDataType.STRING, str);
        data.state.update();

        fromPlayer.sendMessage("! Removed " + toPlayerName + " !");
        fromPlayer.closeInventory();

    }

}
