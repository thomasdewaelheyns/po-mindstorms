public class MiniSimulation {
  public static void main(String[] args) {

    // we use a Grid+View+ProxyAgents to create a mini-simualtor
    GridView goalView    = new SwingGridView()
                            .changeTitle("Goal")
                            .changeLocation(100,100);

    Grid goalGrid = new Grid().load(args[0])
                              .displayOn(goalView)
                              .clearAgents();
    ProxyAgent[] proxies   = { new ProxyAgent(), new ProxyAgent(),
                               new ProxyAgent(), new ProxyAgent() };
    goalGrid.getSector(2,2).putAgent(new StaticTargetAgent(), Bearing.E);
    goalGrid.getSector(0,0).putAgent(proxies[0], Bearing.E);
    goalGrid.getSector(9,0).putAgent(proxies[1], Bearing.S);
    goalGrid.getSector(0,9).putAgent(proxies[2], Bearing.N);
    goalGrid.getSector(9,9).putAgent(proxies[3], Bearing.W);

    goalGrid.show();

    // we instantiate 4 GhostRobots
    GhostRobot robots = new GhostRobot[4];
    // each with its own view
    GridView[] views  = new GridView[4];
    // and a RobotAgent
    GhostRobotAgent[] robotAgents = new GhostRobotAgent[4];
    // and one common Queue (for the Agents)
    Queue queue = new Queue();
    // and one Spy for logging the conversation
    queue.subscribe(new MessageSpy());
        
    for( int r=0; r<4; r++ ) {
      views[d] = new SwingGridView().changeTitle("Discoverer " + d)
                                    .changeLocation(300+(d*200),100);
      robotAgents[d] = new GhostRobotAgent(queue);
      robots[d] = new GhostRobot("discoverer" + d, views[d])
                    .addCommunicationAgent(robotAgents[d]);
    }

    System.out.println("*** ready, press return to start...");
    try { System.in.read(); } catch(Exception e) {}

    while( ! ( robots[0].reachedGoal() && robots[1].reachedGoal() && 
               robots[2].reachedGoal() && robots[3].reachedGoal() ) )
    {
      
      for(int d=0;d<4;d++) {
        robots[d].step();
      }

      goalGrid.show();

      try { Thread.sleep(Integer.parseInt(args[1])); } catch(Exception e) {}
    }
    
    System.out.println("*** done, press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
