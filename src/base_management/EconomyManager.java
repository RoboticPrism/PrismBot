package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;

// Handles training new workers and setting worker tasks
public class EconomyManager {
	private Game game;
	private Player self;
	private List<TerranCommandCenterManager> commandCenterManagers = new ArrayList<TerranCommandCenterManager>();
	
	public EconomyManager(Game game, Player self){
		this.game = game;
		this.self = self;
	}
	
	
	public void onFrame(Player self) {
		checkCommandCenters();
		assignUnclaimedCommandCenters();
		assignUnclaimedWorkers();
		// Run command centers
	    for (TerranCommandCenterManager commandCenter : commandCenterManagers) {
	    	commandCenter.onFrame();
	    }
	}
	
	// If we find an unclaimed command center, create a new manager for it
	void assignUnclaimedCommandCenters() {
		List<Unit> claimedCommandCenterUnits = getClaimedCommandCenterUnits();
		for (Unit unit : self.getUnits()) {
			if (unit.getType() == UnitType.Terran_Command_Center){
				if (!claimedCommandCenterUnits.contains(unit)){
					commandCenterManagers.add(new TerranCommandCenterManager(game, self, unit));
					System.out.println("Adding new command center");
				}
			}
		}
	}
	
	// assign unclaimed workers to nearest command center
	void assignUnclaimedWorkers() {
		List<Unit> claimedWorkerUnits = getClaimedWorkerUnits();
		for (Unit unit : self.getUnits()){
			if (unit.getType().isWorker()){
				if (!claimedWorkerUnits.contains(unit)){
					TerranCommandCenterManager closestCenter = null;
					for (TerranCommandCenterManager commandCenterManager : commandCenterManagers){
						if (closestCenter == null){
							closestCenter = commandCenterManager;
						} else if (unit.getDistance(commandCenterManager.getCommandCenter()) < unit.getDistance(closestCenter.getCommandCenter())) {
							closestCenter = commandCenterManager;
						}
					}
					if (closestCenter != null) {
						closestCenter.assignWorker(unit);
					}
					
				}
			}
		}
	}
	
	// Gets the units attached to all claimed command centers
	List<Unit> getClaimedCommandCenterUnits() {
		List<Unit> claimedCommandCenters = new ArrayList<Unit>();
		for (TerranCommandCenterManager commandCenterManager : commandCenterManagers){
			claimedCommandCenters.add(commandCenterManager.getCommandCenter());
		}
		return claimedCommandCenters;
	}
	
	// Gets all workers that are currently claimed by command centers
	List<Unit> getClaimedWorkerUnits() {
		List<Unit> claimedWorkers = new ArrayList<Unit>();
		for (TerranCommandCenterManager manager : commandCenterManagers) {
			claimedWorkers.addAll(manager.getWorkers());
		}
		return claimedWorkers;
	}
	
	// Removes dead command centers from the list
	void checkCommandCenters() {
		List<TerranCommandCenterManager> removeList = new ArrayList<TerranCommandCenterManager>();
		for (TerranCommandCenterManager commandCenterManager : commandCenterManagers) {
			if (!commandCenterManager.getCommandCenter().exists()) {
				removeList.add(commandCenterManager);
			}
		}
		commandCenterManagers.removeAll(removeList);
	}
	
}
