package co.gameserv.utc;

import co.gameserv.utc.UTCHeal;
import java.io.PrintStream;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin
implements Listener {
    public static Plugin plugin;

    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents((Listener)new UTCHeal(this), (Plugin)this);
        plugin = this;
        System.out.println("UTCHeal is now enabled!");
    }

    public void onDisable() {
        System.out.println("UTCHeal is disabled.");
    }
}