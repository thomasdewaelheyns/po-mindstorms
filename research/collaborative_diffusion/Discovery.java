public class Discovery {
  public static void main(String[] args) {

    GridView goalView    = new SwingGridView()
                            .changeTitle("Goal")
                            .changeLocation(100,100);

    GridView currentView = new SwingGridView()
                             .changeTitle("Current")
                             .changeLocation(300,100);

    Grid goalGrid        = new Grid()
                             .load(args[0])
                             .displayOn(goalView)
                             .clearAgents()
                             .getSector(2,2)
                               .putAgent(new StaticTargetAgent(), Bearing.E)
                               .getGrid()
                             .show();

    Agent discoverer = new DiscoveryAgent(goalGrid);
    Grid currentGrid     = new Grid()
                             .displayOn(currentView)
                             .addSector( new Sector().setCoordinates(0,0) )
                             .getSector(0,0)
                               .putAgent(discoverer, Bearing.N)
                               .getGrid()
                             .show();

    while(!discoverer.isHolding()) {
      currentGrid.moveAgents();
      currentGrid.show(); // refresh our Grid view to reflect movement
      CD.apply(currentGrid);
      try { Thread.sleep(Integer.parseInt(args[1])); } catch(Exception e) {}
    }
    
    System.out.println("*** done, press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
