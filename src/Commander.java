import base_management.BuildManager;
import base_management.EconomyManager;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;

// The main bot class, runs all managers
public class Commander extends DefaultBWListener {

    private Mirror mirror = new Mirror();
    private Game game;
    private Player self;
    
    private EconomyManager economyManager;
    private BuildManager buildManager;

    public void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();
        economyManager = new EconomyManager(game, self);
        
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }

    }

    @Override
    public void onFrame() {

        economyManager.onFrame(self);
    	
        //game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");
        
        for (Unit myUnit : self.getUnits()) {
        	units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");
        	game.drawLineMap(myUnit.getPosition().getX(), 
        					 myUnit.getPosition().getY(), 
        					 myUnit.getOrderTargetPosition().getX(),
        					 myUnit.getOrderTargetPosition().getY(), 
        					 bwapi.Color.Yellow);
        }

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
    }

    public static void main(String[] args) {
        new Commander().run();
    }
}