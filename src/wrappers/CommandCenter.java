package wrappers;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;

import base_management.BuildManager;
import base_management.BuildOrder;

public class CommandCenter extends UnitWrapper {
	private List<Scv> myWorkers = new ArrayList<Scv>();
	private List<Unit> myMineralPatches = new ArrayList<Unit>();
	private List<Unit> myVespeneGeysers = new ArrayList<Unit>();
	private BuildManager buildManager;
	
	public CommandCenter(Game game, Player player, Unit unit){
		super(game, player, unit);
		this.buildManager = new BuildManager(game, player, this);
		// Add all nearby mineral patches to this base
		for(Unit mineralPatch : game.getStaticMinerals()){
			if (mineralPatch.getPoint().getDistance(unit.getPoint()) < 300) {
				myMineralPatches.add(mineralPatch);
			}
		}
	}
	
	public void onFrame() {
		buildWorkers();
		checkWorkers();
		managerIdleWorkers();
		buildManager.onFrame();
		if (DebugMode) {
			debugDraw();
		}
		for(Scv scv : myWorkers) {
			scv.onFrame();
		}
	}
	
	// Get the command center attached to this object
	public Unit getCommandCenter() {
		return this.getUnit();
	}
	
	// Get the workers attached to this object
	public List<Scv> getWorkers() {
		return this.myWorkers;
	}
	
	// Give this worker to this command center
	public void assignWorker(Scv scv) {
		if (scv.getUnit().getType().isWorker()) {
			myWorkers.add(scv);
		}
	}
	
	// Checks through the worker list to remove dead workers from the list
	void checkWorkers(){
		List<Scv> removeList = new ArrayList<Scv>();
		for (Scv scv : myWorkers){
			if (!scv.getUnit().exists()){
				removeList.add(scv);
			}
		}
		myWorkers.removeAll(removeList);
	}
	
	// Tries to build workers if we need more and can afford more
	void buildWorkers() {
		//if there's enough minerals, we aren't currently training workers, and we don't have too many workers, train a worker
        if (player.minerals() >= 50 
        		&& myWorkers.size() < (myMineralPatches.size() * 3)
        		&& unit.getTrainingQueue().size() == 0) {
            unit.train(UnitType.Terran_SCV);
        }
	}
	
	// Give idle workers tasks
	void managerIdleWorkers() {
		for (Scv scv : myWorkers) {
			if (scv.getUnit().isIdle()) {
				scv.getUnit().gather(myMineralPatches.get(0));
			}
		}
	}
	
	void debugDraw() {
		game.drawCircleMap(unit.getX(), unit.getY(), 50, bwapi.Color.Yellow);
		for(Scv scv : myWorkers) {
			game.drawLineMap(unit.getX(), unit.getY(), scv.getUnit().getX(), scv.getUnit().getY(), bwapi.Color.Yellow);
		}
	}	
}