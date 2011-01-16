package com.bukkit.mg.plantspreader;

import java.io.File;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * PlantSpreader for Bukkit
 *
 * @author Master-Guy
 */

public class PlantSpreader extends JavaPlugin {
    private final PlantSpreaderPlayerListener playerListener = new PlantSpreaderPlayerListener(this);
    private final PlantSpreaderBlockListener blockListener = new PlantSpreaderBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	public HashMap<String, Integer> stickMap = new HashMap<String, Integer>();

    public PlantSpreader(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        // TODO: Place any custom initialisation code here

        // NOTE: Event registration should be done in onEnable not here as all events are unregistered when a plugin is disabled
    }

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
                
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        //PluginDescriptionFile pdfFile = this.getDescription();
        //System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    public void onDisable() {
        // TODO: Place any custom disable code here

        // NOTE: All registered events are automatically unregistered when a plugin is disabled

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("Goodbye world!");
    }
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public void log(String logText) {
    	System.out.println(logText);
    }
}
