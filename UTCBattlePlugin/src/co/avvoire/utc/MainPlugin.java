package co.avvoire.utc;

import co.avvoire.utc.UTCBattle;
import java.io.PrintStream;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MainPlugin
extends JavaPlugin
implements Listener {
    public static Plugin plugin;

    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents((Listener)new UTCBattle(this), (Plugin)this);
        plugin = this;
        System.out.println("UTCBattle is now enabled!");
    }

    public void onDisable() {
        System.out.println("UTCBattle is disabled.");
    }
}

