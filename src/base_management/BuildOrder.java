package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;

// Represents an order to build a specific unit at a specific location
public class BuildOrder{
	private UnitType buildType;
	private TilePosition buildLocation;
	private Unit buildUnit;
	private Unit builder;
	
	public BuildOrder(UnitType buildType, TilePosition buildLocation){
		this.buildType = buildType;
		this.buildLocation = buildLocation;
	}
	
	// Set the worker that is building this building
	public void setBuilder(Unit builder){
		this.builder = builder;
	}
	
	// Get this builder if it is still alive
	public Unit getBuilder() {
		if (builder.exists()){
			return builder;
		} else {
			return null;
		}
	}
	
	// Set the associated unit once building has begun
	public void setBuildUnit(Unit buildUnit){
		this.buildUnit = buildUnit;
	}
	
	// Check if the building is finished
	public boolean isBuildDone(){
		if (buildUnit == null){
			return false;
		} else {
			return buildUnit.getRemainingBuildTime() == 0;
		}
	}
	
	
	
	
	
}