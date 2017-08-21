package wrappers;
import bwapi.*;
import bwapi.Game;
import bwapi.Player;

public class UnitWrapper {
	protected Game game;
	protected Player player;
	protected Unit unit;
	public static boolean DebugMode = true;
	
	UnitWrapper(Game game, Player player, Unit unit) {
		this.game = game;
		this.player = player;
		this.unit = unit;
	}
	
	public Unit getUnit(){
		return unit;
	}
}
