package com.nastalgua.storage.helpers;

import com.nastalgua.storage.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;

public class PersistentData {

    public static final String HISTORY_KEY = "history";
    public static final String USERS_KEY = "users";
    public static final String MASTER_CHEST_KEY = "master-chest";
    public static final String SLAVE_CHEST_KEY = "slave-chest";

    private PersistentDataContainer container;
    private TileState state;

    public PersistentData(Block block) {
        this.state = (TileState) block.getState();
        this.container = this.state.getPersistentDataContainer();
    }

    public static NamespacedKey convertToKey(String keyName) {
        return new NamespacedKey(Main.getPlugin(Main.class), keyName);
    }

    public boolean containerHas(String keyName, PersistentDataType type) {
        return this.container.has(convertToKey(keyName), type);
    }

    public String containerGetString(String keyName) {
        return this.container.get(convertToKey(keyName), PersistentDataType.STRING);
    }

    public void containerSetString(String keyName, String value) {
        this.container.set(convertToKey(keyName), PersistentDataType.STRING, value);
    }

    public int[] containerGetIntArray(String keyName) {
        return this.container.get(convertToKey(keyName), PersistentDataType.INTEGER_ARRAY);
    }

    public void containerSetIntArray(String keyName, int[] value) {
        this.container.set(convertToKey(keyName), PersistentDataType.INTEGER_ARRAY, value);
    }

    public void update() {
        this.state.update();
    }

}
