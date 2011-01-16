package com.bukkit.mg.plantspreader;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

/**
 * MG block listener
 * @author Master-Guy
 */

public class PlantSpreaderBlockListener extends BlockListener {
    private final PlantSpreader plugin;
    private int I;

    public PlantSpreaderBlockListener(final PlantSpreader plugin) {
        this.plugin = plugin;
    }

    public void onBlockRightClick(BlockRightClickEvent event) {
    	log("t1");
    	log("X"+event.getBlockAgainst().getX()+"; Y"+event.getBlockAgainst().getY()+"; Z"+event.getBlockAgainst().getZ());
    }
    
    public void log(String logText) {
    	System.out.println(logText);
    }
    
    public void logToAll(String logText) {
    	System.out.println(logText);
    	I = 0;
    	while (I < this.plugin.getServer().getOnlinePlayers().length) {
    		this.plugin.getServer().getOnlinePlayers()[I].sendMessage(logText);
    		I = I + 1;
    	}
    }
}
