package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import wrappers.CommandCenter;
import wrappers.Scv;
import wrappers.UnitWrapper;

import java.util.*;

// Handles building structures according to the build order manager
public class BuildManager {
	private Game game;
    private Player player;
    private CommandCenter commandCenter;
    
    private BuildLocationManager buildLocationManager;
    
    private List<BuildOrder> buildQueue = new ArrayList<BuildOrder>();
    private List<TilePosition> validSupplyDepotLocations = new ArrayList<TilePosition>();
    private List<TilePosition> validProductionLocations = new ArrayList<TilePosition>();
    
    private static int supplyCapOffset = 3; // How close to supply cap before we assign a new depot 
    private static int scheduledSupply = 0; // How many supply depots do we have pending?
    
    public BuildManager(Game game, Player player, CommandCenter commandCenter) {
    	this.game = game;
    	this.player = player;
    	this.commandCenter = commandCenter;
    	
    	this.buildLocationManager = new BuildLocationManager(game, player, commandCenter.getUnit().getTilePosition());
    	System.out.println("Starting Supply Depot Search");
    	this.validSupplyDepotLocations = this.buildLocationManager.findValidSupplyDepotLocations();
    	this.validProductionLocations = this.buildLocationManager.findValidProductionLocations();
//    	this.buildOrderManager = new BuildOrderManager(game, player);
    }
    
    public void onFrame() {
    	updateBuildQueue();
    	manageBuilds();
    	if (UnitWrapper.DebugMode) {
    		debugDraw();
    	}
    }
    
    public List<BuildOrder> getBuildQueue() {
    	return buildQueue;
    }
    
    void updateBuildQueue() {
    	checkSupplyDepot();
    }
    
    void checkSupplyDepot() {
    	if (player.supplyUsed() + supplyCapOffset >= player.supplyTotal() + scheduledSupply) {
    		buildSupplyDepot();
    	}
    }
    
    void buildSupplyDepot() {
    	if (!validSupplyDepotLocations.isEmpty()) {
    		buildQueue.add(new BuildOrder(game, UnitType.Terran_Supply_Depot, validSupplyDepotLocations.get(0)));
    		scheduledSupply += 16;
    		System.out.println("Scheduling Supply Depot");
    	}
    }
    
    // Gets the next build order to work on
    public BuildOrder getNextAvailableBuild() {
    	for (BuildOrder buildOrder : buildQueue) {
    		if (buildOrder.getBuilder() == null){
    			return buildOrder;
    		}
    	}
    	return null;
    }
    
    public void manageBuilds() {
    	List<BuildOrder> removeList = new ArrayList<BuildOrder>();
    	for (BuildOrder buildOrder : buildQueue) {
    		// if we can afford the build and no ones making it...
			if (buildOrder.getBuildType().mineralPrice() <= player.minerals() &&
					buildOrder.getBuildType().gasPrice() <= player.gas()){
				if (buildOrder.getBuilder() == null) {
					// if the unit hasn't started construction yet, start it
					if (buildOrder.getBuildUnit() == null) {
						System.out.println("Starting Build on " + buildOrder.getBuildType() + " at " + buildOrder.getBuildLocation());
						Scv scv = commandCenter.getWorkers().get(0); 
						scv.build(buildOrder);
						buildOrder.setBuilder(scv);
					} 
					// otherwise construction was halted and we need to resume with a new builder
					else {
						
					}
				} else {
					if (buildOrder.getBuildUnit() == null) { 
						buildOrder.getBuilder().build(buildOrder);
					}
				}
			}
			if (buildOrder.isBuildDone()) {
				removeList.add(buildOrder);
			}
    	}
    	buildQueue.removeAll(removeList);
    }
    
    public void debugDraw() {
//    	for (TilePosition tilePos : validSupplyDepotLocations){
//    		game.drawBoxMap(tilePos.toPosition().getX(), 
//    						tilePos.toPosition().getY(), 
//    						tilePos.toPosition().getX()+96, 
//    						tilePos.toPosition().getY()+64, 
//    						bwapi.Color.Green);
//    	}
    	for (BuildOrder buildOrder : buildQueue) {
    		game.drawBoxMap(buildOrder.getBuildLocation().getX() * 32,
		    				buildOrder.getBuildLocation().getY() * 32,
		    				buildOrder.getBuildLocation().getX() * 32 + 96,
		    				buildOrder.getBuildLocation().getY() * 32 + 96,
		    				bwapi.Color.Green);
    		if (buildOrder.getBuilder() != null) {
    			game.drawLineMap(buildOrder.getBuildLocation().getX() * 32 + 48, 
    							 buildOrder.getBuildLocation().getY() * 32 + 48,
    							 buildOrder.getBuilder().getUnit().getX(),
    							 buildOrder.getBuilder().getUnit().getY(),
    							 bwapi.Color.Green);
    		}
    	}
    }
}
