package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;

// Finds where to build base items and records valid build locations
class BuildLocationManager {
	private Game game;
    private Player self;
    private TilePosition commandCenterLocation;
    
    private int baseWidthTiles = 20;
    private int baseHeightTiles = 20;
    
    public BuildLocationManager(Game game, Player self, TilePosition commandCenterLocation) {
    	this.game = game;
    	this.self = self;
    	this.commandCenterLocation = commandCenterLocation;
    }
    
    // sets the list of current valid supply depot locations
    public List<TilePosition> findValidSupplyDepotLocations() {
    	List<TilePosition> valid_list = new ArrayList<TilePosition>();
    	int halfBaseWidth = baseWidthTiles/2;
    	int halfBaseHeight = baseHeightTiles/2;
    	int itemWidth = 3;
    	int itemHeight = 2;
    	for(int x_offset = -halfBaseWidth; x_offset < halfBaseWidth; x_offset += itemWidth) {
    		for(int y_offset = -halfBaseHeight; y_offset < halfBaseHeight; y_offset += itemHeight) {
				TilePosition potentialLocation = getPositionOffset(x_offset, y_offset);
        		if (game.canBuildHere(potentialLocation, UnitType.Terran_Supply_Depot)) {
        			valid_list.add(potentialLocation);
        			System.out.println("Found Depot Location "+potentialLocation.getX()+", "+potentialLocation.getY());
        		}
        	}
    	}
    	return valid_list;
    }
    
    // sets the list of current valid production building (barracks, etc.) locations
    public List<TilePosition> findValidProductionLocations(){
    	List<TilePosition> valid_list = new ArrayList<TilePosition>();
    	
    	
    	return valid_list;
    }
    
    TilePosition getPositionOffset(int x, int y){
    	return new TilePosition(commandCenterLocation.getX() + x, commandCenterLocation.getY() + y);
    }
    
    
}
