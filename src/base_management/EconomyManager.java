package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import wrappers.CommandCenter;
import wrappers.Scv;

import java.util.*;

// Handles training new workers and setting worker tasks
public class EconomyManager {
	private Game game;
	private Player player;
	private List<CommandCenter> commandCenterManagers = new ArrayList<CommandCenter>();
	
	public EconomyManager(Game game, Player player){
		this.game = game;
		this.player = player;
	}
	
	
	public void onFrame(Player self) {
		checkCommandCenters();
		assignUnclaimedCommandCenters();
		assignUnclaimedWorkers();
		// Run command centers
	    for (CommandCenter commandCenter : commandCenterManagers) {
	    	commandCenter.onFrame();
	    }
	}
	
	// If we find an unclaimed command center, create a new manager for it
	void assignUnclaimedCommandCenters() {
		List<Unit> claimedCommandCenterUnits = getClaimedCommandCenterUnits();
		for (Unit unit : player.getUnits()) {
			if (unit.getType() == UnitType.Terran_Command_Center){
				if (!claimedCommandCenterUnits.contains(unit)){
					commandCenterManagers.add(new CommandCenter(game, player, unit));
					System.out.println("Adding new command center");
				}
			}
		}
	}
	
	// assign unclaimed workers to nearest command center
	void assignUnclaimedWorkers() {
		List<Unit> claimedScvUnits = getClaimedScvUnits();
		for (Unit unit : player.getUnits()){
			if (unit.getType().isWorker()){
				if (!claimedScvUnits.contains(unit)){
					CommandCenter closestCenter = null;
					for (CommandCenter commandCenterManager : commandCenterManagers){
						if (closestCenter == null){
							closestCenter = commandCenterManager;
						} else if (unit.getDistance(commandCenterManager.getCommandCenter()) < unit.getDistance(closestCenter.getCommandCenter())) {
							closestCenter = commandCenterManager;
						}
					}
					if (closestCenter != null) {
						closestCenter.assignWorker(new Scv(game, player, unit));
					}
					
				}
			}
		}
	}
	
	// Gets the units attached to all claimed command centers
	List<Unit> getClaimedCommandCenterUnits() {
		List<Unit> claimedCommandCenters = new ArrayList<Unit>();
		for (CommandCenter commandCenterManager : commandCenterManagers){
			claimedCommandCenters.add(commandCenterManager.getCommandCenter());
		}
		return claimedCommandCenters;
	}
	
	// Gets all workers that are currently claimed by command centers
	List<Scv> getClaimedScvs() {
		List<Scv> claimedScvs = new ArrayList<Scv>();
		for (CommandCenter manager : commandCenterManagers) {
			claimedScvs.addAll(manager.getWorkers());
		}
		return claimedScvs;
	}
	
	// Returns the units of all claimed SCVs
	List<Unit> getClaimedScvUnits() {
		List<Unit> units = new ArrayList<Unit>();
		for (Scv scv : getClaimedScvs()) {
			units.add(scv.getUnit());
		}
		return units;
	}
	
	// Removes dead command centers from the list
	void checkCommandCenters() {
		List<CommandCenter> removeList = new ArrayList<CommandCenter>();
		for (CommandCenter commandCenterManager : commandCenterManagers) {
			if (!commandCenterManager.getCommandCenter().exists()) {
				removeList.add(commandCenterManager);
			}
		}
		commandCenterManagers.removeAll(removeList);
	}
	
}
