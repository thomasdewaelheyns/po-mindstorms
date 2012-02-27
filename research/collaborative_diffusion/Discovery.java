public class Discovery {
  public static void main(String[] args) {

    GridView goalView    = new SwingGridView()
                            .changeTitle("Goal")
                            .changeLocation(100,100);

    ProxyAgent proxy1    = new ProxyAgent();
    ProxyAgent proxy2    = new ProxyAgent();
    ProxyAgent proxy3    = new ProxyAgent();
    ProxyAgent proxy4    = new ProxyAgent();
    Grid goalGrid        = new Grid()
                             .load(args[0])
                             .displayOn(goalView)
                             .clearAgents()
                             .getSector(2,2)
                               .putAgent(new StaticTargetAgent(), Bearing.E)
                               .getGrid()
                             .getSector(0,0)
                               .putAgent(proxy1, Bearing.E)
                               .getGrid()
                             .getSector(9,0)
                               .putAgent(proxy2, Bearing.S)
                               .getGrid()
                             .getSector(0,9)
                               .putAgent(proxy3, Bearing.E)
                               .getGrid()
                             .getSector(9,9)
                               .putAgent(proxy4, Bearing.W)
                               .getGrid()
                             .show();

    GridView currentView1 = new SwingGridView()
                             .changeTitle("Discoverer 1")
                             .changeLocation(300,100);

    Agent discoverer1    = new DiscoveryAgent().setProxy(proxy1);
    Grid currentGrid1    = new Grid()
                             .displayOn(currentView1)
                             .addSector(new Sector().setCoordinates(0,0) )
                             .getSector(0,0)
                               .putAgent(discoverer1, Bearing.N)
                               .getGrid()
                             .show();

    GridView currentView2 = new SwingGridView()
                             .changeTitle("Discoverer 2")
                             .changeLocation(500,100);

    Agent discoverer2    = new DiscoveryAgent().setProxy(proxy2);
    Grid currentGrid2    = new Grid()
                             .displayOn(currentView2)
                             .addSector(new Sector().setCoordinates(0,0) )
                             .getSector(0,0)
                               .putAgent(discoverer2, Bearing.N)
                               .getGrid()
                             .show();

    GridView currentView3 = new SwingGridView()
                             .changeTitle("Discoverer 3")
                             .changeLocation(300,322);

    Agent discoverer3    = new DiscoveryAgent().setProxy(proxy3);
    Grid currentGrid3    = new Grid()
                             .displayOn(currentView3)
                             .addSector(new Sector().setCoordinates(0,0) )
                             .getSector(0,0)
                               .putAgent(discoverer3, Bearing.N)
                               .getGrid()
                             .show();

    GridView currentView4 = new SwingGridView()
                             .changeTitle("Discoverer 4")
                             .changeLocation(500,322);

    Agent discoverer4    = new DiscoveryAgent().setProxy(proxy4);
    Grid currentGrid4    = new Grid()
                             .displayOn(currentView4)
                             .addSector(new Sector().setCoordinates(0,0) )
                             .getSector(0,0)
                               .putAgent(discoverer4, Bearing.N)
                               .getGrid()
                             .show();

    System.out.println("*** ready, press return to start...");
    try { System.in.read(); } catch(Exception e) {}

    while(!discoverer1.isHolding() && !discoverer2.isHolding()) {
      currentGrid1.moveAgents();
      currentGrid2.moveAgents();
      currentGrid3.moveAgents();
      currentGrid4.moveAgents();
      
      CD.apply(currentGrid1);
      CD.apply(currentGrid2);
      CD.apply(currentGrid3);
      CD.apply(currentGrid4);
      
      currentGrid1.show(); // refresh our Grid view to reflect movement
      currentGrid2.show(); // refresh our Grid view to reflect movement
      currentGrid3.show(); // refresh our Grid view to reflect movement
      currentGrid4.show(); // refresh our Grid view to reflect movement

      goalGrid.show();    // refresh to show effect in "real world"

      try { Thread.sleep(Integer.parseInt(args[1])); } catch(Exception e) {}
    }
    
    System.out.println("*** done, press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
