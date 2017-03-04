package base_management;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;

public class TerranCommandCenterManager {
	private Game game;
	private Player self;
	
	private Unit commandCenter;
	private List<Unit> myWorkers = new ArrayList<Unit>();
	private List<Unit> myMineralPatches = new ArrayList<Unit>();
	private List<Unit> myVespeneGeysers = new ArrayList<Unit>();
	
	public TerranCommandCenterManager(Game game, Player self, Unit commandCenter){
		this.game = game;
		this.self = self;
		this.commandCenter = commandCenter;
		// Add all nearby mineral patches to this base
		for(Unit mineralPatch : game.getStaticMinerals()){
			if (mineralPatch.getPoint().getDistance(commandCenter.getPoint()) < 300) {
				myMineralPatches.add(mineralPatch);
			}
		}
	}
	
	public void onFrame() {
		buildWorkers();
		checkWorkers();
		managerIdleWorkers();
	}
	
	// Get the command center attached to this object
	public Unit getCommandCenter() {
		return this.commandCenter;
	}
	
	// Get the workers attached to this object
	public List<Unit> getWorkers() {
		return this.myWorkers;
	}
	
	// Give this worker to this command center
	public void assignWorker(Unit unit) {
		if (unit.getType().isWorker()) {
			myWorkers.add(unit);
		}
	}
	
	// Checks through the worker list to remove dead workers from the list
	void checkWorkers(){
		
	}
	
	// Tries to build workers if we need more and can afford more
	void buildWorkers() {
		//if there's enough minerals, we aren't currently training workers, and we don't have too many workers, train a worker
        if (self.minerals() >= 50 
        		&& myWorkers.size() < (myMineralPatches.size() * 3)
        		&& commandCenter.getTrainingQueue().size() == 0) {
            commandCenter.train(UnitType.Terran_SCV);
        }
	}
	
	// Give idle workers tasks
	void managerIdleWorkers() {
		for (Unit unit : myWorkers) {
			if (unit.isIdle()) {
				unit.gather(myMineralPatches.get(0));
			}
		}
	}
	
	
}