public class Discovery {
  public static void main(String[] args) {

    GridView goalView    = new SwingGridView()
                            .changeTitle("Goal")
                            .changeLocation(100,100);

    ProxyAgent[] proxies   = { new ProxyAgent(), new ProxyAgent(),
                               new ProxyAgent(), new ProxyAgent() };
    Grid goalGrid = new Grid().load(args[0])
                              .displayOn(goalView)
                              .clearAgents();
    goalGrid.getSector(2,2).putAgent(new StaticTargetAgent(), Bearing.E);
    goalGrid.getSector(0,0).putAgent(proxies[0], Bearing.E);
    goalGrid.getSector(9,0).putAgent(proxies[1], Bearing.S);
    goalGrid.getSector(0,9).putAgent(proxies[2], Bearing.N);
    goalGrid.getSector(9,9).putAgent(proxies[3], Bearing.W);

    goalGrid.show();

    GridView[] views  = new GridView[4];
    Agent[]    agents = new Agent[4];
    Grid[]     grids  = new Grid[4];
    Queue      queue  = new Queue();

    queue.subscribe(new MessageSpy()); // a logger to follow the conversation

    // create 4 DiscoveryAgents
    for(int d=0;d<4;d++) {
      views[d] = new SwingGridView().changeTitle("Discoverer " + d)
                                    .changeLocation(300+(d*200),100);
      agents[d] = new DiscoveryAgent("discoverer" + d).setProxy(proxies[d]);
      grids[d] = new Grid().displayOn(views[d])
                           .addSector(new Sector().setCoordinates(0,0) )
                           .getSector(0,0)
                             .putAgent(agents[d], Bearing.N)
                             .getGrid()
                           .show();
      queue.subscribe(agents[d].getMessageHandler());
    }

    System.out.println("*** ready, press return to start...");
    try { System.in.read(); } catch(Exception e) {}

    while( ! ( agents[0].isHolding() && agents[1].isHolding() &&
               agents[2].isHolding() && agents[3].isHolding() ) )
    {
      
      for(int d=0;d<4;d++) {
        grids[d].moveAgents();
        CD.apply(grids[2]);
        grids[d].show();
      }

      CD.apply(goalGrid);
      goalGrid.show();

      try { Thread.sleep(Integer.parseInt(args[1])); } catch(Exception e) {}
    }
    
    System.out.println("*** done, press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
