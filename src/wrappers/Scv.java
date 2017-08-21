package wrappers;
import bwapi.*;
import base_management.BuildOrder;

public class Scv extends UnitWrapper {
	
	public Scv (Game game, Player player, Unit unit) {
		super(game, player, unit);
	}
	
	public void onFrame() {
		if(DebugMode) {
			debugDraw();
		}
	}
	
	public void build(BuildOrder buildOrder) {
		unit.build(buildOrder.getBuildType(), buildOrder.getBuildLocation());
	}
	
	void debugDraw() {
		game.drawCircleMap(unit.getX(), unit.getY(), 20, bwapi.Color.White);
		if(unit.getOrderTarget() != null) {
			game.drawLineMap(unit.getX(), unit.getY(), unit.getOrderTargetPosition().getX(), unit.getOrderTargetPosition().getY(), bwapi.Color.White);
		}
	}
}
