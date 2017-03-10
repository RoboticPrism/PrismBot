package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;

// Handles building structures according to the build order manager
public class BuildManager {
	private Game game;
    private Player self;
    private TilePosition commandCenterLocation;
    
    private BuildLocationManager buildLocationManager;
    
    private List<BuildOrder> buildQueue = new ArrayList<BuildOrder>();
    private List<TilePosition> validSupplyDepotLocations = new ArrayList<TilePosition>();
    private List<TilePosition> validProductionLocations = new ArrayList<TilePosition>();
    
    private static int supplyCapOffset = 2; // How close to supply cap before we assign a new depot 
    private static int scheduledSupply = 0; // How many supply depots do we have pending?
    
    public BuildManager(Game game, Player self, TilePosition commandCenterLocation) {
    	this.game = game;
    	this.self = self;
    	this.commandCenterLocation = commandCenterLocation;
    	
    	this.buildLocationManager = new BuildLocationManager(game, self, commandCenterLocation);
    	System.out.println("Starting Supply Depot Search");
    	this.validSupplyDepotLocations = this.buildLocationManager.findValidSupplyDepotLocations();
    	this.validProductionLocations = this.buildLocationManager.findValidProductionLocations();
//    	this.buildOrderManager = new BuildOrderManager(game, self);
    }
    
    public void onFrame() {
    	updateBuildQueue();
    	debugDraw();
    }
    
    public void debugDraw() {
    	for (TilePosition tilePos : validSupplyDepotLocations){
    		game.drawBoxMap(tilePos.toPosition().getX(), 
    						tilePos.toPosition().getY(), 
    						tilePos.toPosition().getX()+96, 
    						tilePos.toPosition().getY()+64, 
    						bwapi.Color.Green);
    	}
    }
    
    void updateBuildQueue() {
    	checkSupplyDepot();
    }
    
    void checkSupplyDepot() {
    	System.out.println(self.supplyUsed()+"+"+supplyCapOffset+"/"+self.supplyTotal()+"+"+scheduledSupply);
    	if (self.supplyUsed() + supplyCapOffset >= self.supplyTotal() + scheduledSupply) {
    		buildSupplyDepot();
    	}
    }
    
    void buildSupplyDepot() {
    	if (!validSupplyDepotLocations.isEmpty()) {
    		buildQueue.add(new BuildOrder(UnitType.Terran_Supply_Depot, validSupplyDepotLocations.get(0)));
    		scheduledSupply += 16;
    		System.out.println("Scheduling Supply Depot");
    	}
    }
}
