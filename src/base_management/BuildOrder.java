package base_management;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import java.util.*;
import wrappers.Scv;

// Represents an order to build a specific unit at a specific location
public class BuildOrder{
	private Game game;
	private UnitType buildType;
	private TilePosition buildLocation;
	private Scv builder;
	
	public BuildOrder(Game game, UnitType buildType, TilePosition buildLocation){
		this.game = game;
		this.buildType = buildType;
		this.buildLocation = buildLocation;
	}
	
	// Set the worker that is building this building
	public void setBuilder(Scv builder){
		this.builder = builder;
	}
	
	// Get this builder if it is still alive
	public Scv getBuilder() {
		return builder;
	}
	
	public Unit getBuildUnit() {
		if (builder != null) {
			return builder.getUnit().getBuildUnit();
		} else {
			return null;
		}
	}
	
	public UnitType getBuildType() {
		return buildType;
	}
	
	public TilePosition getBuildLocation() {
		return buildLocation;
	}
	
	// Check if the building is finished
	public boolean isBuildDone() {
		if (getBuildUnit() == null){
			return false;
		} else {
			return getBuildUnit().getRemainingBuildTime() == 0;
		}
	}
	
	
	
	
	
}