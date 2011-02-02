package com.bukkit.MasterGuy;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * Handle events for all Player related events
 * @author Master-Guy
 */

public class PlantSpreaderPlayerListener extends PlayerListener {
    Timer timer;
    private final PlantSpreader plugin;
    public boolean doSpreadNow, foundValidBlock;
    private final Settings Settings = new Settings();
	private Integer I, J, X, Y, Z, T, minY, minZ, maxX, maxY, maxZ, foundBlock, rn2, patchCount, pmxX, pmxY, pmxZ, pmnX, pmnY, pmnZ, possibleSpreads, maxPatchSize;
	public HashMap<String, Integer> speadOptions = new HashMap<String, Integer>();
	Integer timerDelay;

    public PlantSpreaderPlayerListener(PlantSpreader instance) {
        plugin = instance;
        doSpreadNow = false;
        timer = new Timer();
        timerDelay = Integer.parseInt(Settings.getSetting("settings/PlantSpreader.ini", "spreadDelay", "5")[0])*1000;
        timer.schedule(new RemindTask(), timerDelay, timerDelay);
        log("Plants are scheduled to spread every "+(timerDelay/1000)+" seconds.");
    }
    
    public class RemindTask extends TimerTask {
        public void run() {
            doSpread();
        }
    }
    
    public void onPlayerCommand(PlayerChatEvent event) {
    	String[] split = event.getMessage().split(" ");
    	String[] setting;
        final String command = split[0].toString();
        boolean allowPlayer;
        Integer I;
        
    	allowPlayer = false;
    	I = 0;
    	setting = Settings.getSetting("settings/PlantSpreader.ini", "allowedPlayers", "MasterGuy013,AdminAccount1,ModAccount2,PlayerAccount3", ",");
    	while (I < setting.length) {
    		if(event.getPlayer().getName().equalsIgnoreCase(setting[I])) {
    			allowPlayer = true;
    		}
    		I = I + 1;
    	}
    	if(setting[0].equalsIgnoreCase("*")) {
    		allowPlayer = true;
    	}
    	
    	if(command.equalsIgnoreCase(Settings.getSetting("settings/PlantSpreader.ini", "timerReloadCommand", "/reloadSpreaderTimer")[0])) {
    		if(allowPlayer) {
    			timer.cancel();
    	        timer = new Timer();
    	        timerDelay = Integer.parseInt(Settings.getSetting("settings/PlantSpreader.ini", "spreadDelay", "5")[0])*1000;
    	        timer.schedule(new RemindTask(), timerDelay, timerDelay);
    	        log("Plants are scheduled to spread every "+(timerDelay/1000)+" seconds.");
    	        event.getPlayer().sendMessage("Plants are scheduled to spread every "+(timerDelay/1000)+" seconds.");
            	event.setCancelled(true);
    		} else {
    			event.getPlayer().sendMessage("You do not have access to this command!");
    		}
    	}
        
    	if(command.equalsIgnoreCase(Settings.getSetting("settings/PlantSpreader.ini", "spreadCommand", "/plantspread")[0])) {
    		if(allowPlayer) {
    			doSpread();
    	        event.getPlayer().sendMessage("You triggered a spread loop.");
            	event.setCancelled(true);
    		} else {
    			event.getPlayer().sendMessage("You do not have access to this command!");
    		}
    	}
    }
    
    public void doSpread() {
		Random generator = new Random();
		String[] spreadBlocks;
		possibleSpreads = 0;
		I = 0;
		spreadBlocks = Settings.getSetting("settings/PlantSpreader.ini", "spreadBlocks", "37,38,39,40,86", ",");
		maxPatchSize = Integer.parseInt(Settings.getSetting("settings/PlantSpreader.ini", "maxPatchSize", "9")[0]);
		Integer spaXZ = Integer.parseInt(Settings.getSetting("settings/PlantSpreader.ini", "spreadPlayerAreaXZ", "10")[0]);
		Integer spaY = Integer.parseInt(Settings.getSetting("settings/PlantSpreader.ini", "spreadPlayerAreaY", "2")[0]);
    	while (I < this.plugin.getServer().getOnlinePlayers().length) {
    		X = this.plugin.getServer().getOnlinePlayers()[I].getLocation().getBlockX()-spaXZ;
    		Y = this.plugin.getServer().getOnlinePlayers()[I].getLocation().getBlockY()-spaY;
    		Z = this.plugin.getServer().getOnlinePlayers()[I].getLocation().getBlockZ()-spaXZ;
    		minY = Y;
    		minZ = Z;
    		maxX = this.plugin.getServer().getOnlinePlayers()[I].getLocation().getBlockX()+spaXZ;
    		maxY = this.plugin.getServer().getOnlinePlayers()[I].getLocation().getBlockY()+spaY;
    		maxZ = this.plugin.getServer().getOnlinePlayers()[I].getLocation().getBlockZ()+spaXZ;
    		while(X < maxX) {
    			Y = minY;
    			while(Y < maxY) {
    				Z = minZ;
        			while(Z < maxZ) {
        				foundValidBlock = false;
        				foundBlock = this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y, Z);
        				for(J=0;J<spreadBlocks.length;J++) {
        					if(foundBlock.toString().equalsIgnoreCase(spreadBlocks[J].toString())) {
        						foundValidBlock = true;
        					}
        				}
        				if(foundValidBlock) {
        					patchCount = 0;
        					pmnX = X-maxPatchSize;
        					pmxX = X+maxPatchSize;
        					while(pmnX < pmxX) {
	        					pmnY = Y-Math.round(maxPatchSize/3);
	        					pmxY = Y+Math.round(maxPatchSize/3);
	        					while(pmnY < pmxY) {
		        					pmnZ = Z-maxPatchSize;
		        					pmxZ = Z+maxPatchSize;
		        					while(pmnZ < pmxZ) {
		        						if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(pmnX, pmnY, pmnZ) == foundBlock) {
		        							patchCount = patchCount + 1;
		        						}
		        						pmnZ = pmnZ + 1;
		        					}
	        						pmnY = pmnY + 1;
	        					}
        						pmnX = pmnX + 1;
        					}
        					if(patchCount <= maxPatchSize) {
	        					if (
	        						(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X-1, Y, Z) == 0) ||
	        						(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X+1, Y, Z) == 0) ||
	        						(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y, Z-1) == 0) ||
	        						(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y, Z+1) == 0)
	        					) {
        							if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X-1, Y, Z) == 0) {
        								if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X-1, Y-1, Z) == this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y-1, Z)) {
        									speadOptions.put(possibleSpreads+"X", X-1);
        									speadOptions.put(possibleSpreads+"Y", Y);
        									speadOptions.put(possibleSpreads+"Z", Z);
        									speadOptions.put(possibleSpreads+"T", foundBlock);
        									possibleSpreads = possibleSpreads + 1;
        								}
        							}
        							if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X+1, Y, Z) == 0) {
        								if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X+1, Y-1, Z) == this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y-1, Z)) {
        									speadOptions.put(possibleSpreads+"X", X+1);
        									speadOptions.put(possibleSpreads+"Y", Y);
        									speadOptions.put(possibleSpreads+"Z", Z);
        									speadOptions.put(possibleSpreads+"T", foundBlock);
        									possibleSpreads = possibleSpreads + 1;
        								}
        							}
        							if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y, Z-1) == 0) {
        								if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y-1, Z-1) == this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y-1, Z)) {
        									speadOptions.put(possibleSpreads+"X", X);
        									speadOptions.put(possibleSpreads+"Y", Y);
        									speadOptions.put(possibleSpreads+"Z", Z-1);
        									speadOptions.put(possibleSpreads+"T", foundBlock);
        									possibleSpreads = possibleSpreads + 1;
        								}
        							}
        							if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y, Z+1) == 0) {
        								if(this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y-1, Z+1) == this.plugin.getServer().getWorlds()[0].getBlockTypeIdAt(X, Y-1, Z)) {
        									speadOptions.put(possibleSpreads+"X", X);
        									speadOptions.put(possibleSpreads+"Y", Y);
        									speadOptions.put(possibleSpreads+"Z", Z+1);
        									speadOptions.put(possibleSpreads+"T", foundBlock);
		        							possibleSpreads = possibleSpreads + 1;
        								}
        							}
        						}
        					}
        				}
            			Z = Z + 1;
            		}
        			Y = Y + 1;
        		}
    			X = X + 1;
    		}
    		I = I + 1;
    	}
    	if(possibleSpreads > 0) {
    		for(J=0;J<spreadBlocks.length;J++) {
		    	rn2 = generator.nextInt(possibleSpreads);
		    	X = speadOptions.get(rn2+"X");
		    	Y = speadOptions.get(rn2+"Y");
		    	Z = speadOptions.get(rn2+"Z");
		    	T = speadOptions.get(rn2+"T");
		    	try{
		    		this.plugin.getServer().getWorlds()[0].getBlockAt(X, Y, Z).setTypeId(T);
		    	} catch (Exception e) {
				} finally {
			    	speadOptions.clear();
		    	}
    		}
    		possibleSpreads--;
    	}
    }
    
    public void log(String logText) {
    	System.out.println(logText);
    }
}
