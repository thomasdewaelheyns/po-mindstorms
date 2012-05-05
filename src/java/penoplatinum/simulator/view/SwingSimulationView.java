package penoplatinum.simulator.view;

/**
 * SwingSimulationView
 * 
 * Implementation of SimulationView using Swing to setup a visual 
 * representation of the SimulationEnvironment.
 * 
 * @author: Team Platinum
 */

import java.util.ArrayList;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JFrame;

import penoplatinum.grid.SwingGridView;
import penoplatinum.map.Map;

import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.tiles.SectorDraw;


public class SwingSimulationView extends JFrame implements SimulationView {
  
  static boolean lockHeight = true;

  private Board board;
  private ArrayList<SwingGridView> grids = new ArrayList<SwingGridView>();
  private GroupLayout layout;
  private Map map;

  public SwingSimulationView() {
    this.setupWindow();
    this.setupBoard();
  }

  private void setupBoard() {
    this.layout = new GroupLayout(this.getContentPane());
    this.layout.setAutoCreateGaps(true);
    this.layout.setAutoCreateContainerGaps(false);
    this.setLayout(layout);
    this.board = new Board();
    this.setupLayout();
  }

  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setUndecorated(false); 
    this.setSize(800, 400);
    this.setLocationRelativeTo(null);
    this.setLocation(0, 0);
    this.setTitle("Simulator");
    this.setResizable(true);
    this.setVisible(true);
  }
  
  private void rescaleGrid(){
    int gridSectorSize = (int) (((double)Sector.SIZE*(double)Board.SCALE*(double)Board.scaleRatio)/2.0);
    for(SwingGridView grid: grids){
      grid.setSectorSize(gridSectorSize);
      grid.getBoard().calculateRatio(this.getWidth(), this.getHeight());
    }
  }
  
  private void rescaleBoard(){
    double width = this.getWidth();
    double width2 = ((width-20)/2.0)/(double)map.getWidth();
    Board.scaleRatio = (double)width2/(double)(Sector.SIZE*Board.SCALE);
    if(SwingSimulationView.lockHeight) {
      this.setSize((int)width, (int)((double)map.getHeight()*(double) Board.SCALE*Board.scaleRatio *(double)Sector.SIZE)+35);
    }
    this.rescaleGrid();
  }

  public void showMap(Map map) {
    if( map == null ) { return; }

    this.map = map;
    this.rescaleBoard();
    this.board.showMap(map);
  }

  public void updateRobots() {
    this.rescaleBoard();
    this.board.updateRobots();
    this.updateGrids();
    
    // pass on control for a little while
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      System.err.println(e);
    }
  }
  
  private void updateGrids() {
    for(SwingGridView gridView : this.grids) {
      // gridView.refresh();
    }
  }

  public void addRobot(ViewRobot r) {
    this.board.addRobot(r);
  }

  public Board getBoard() {
    return this.board;
  }
  
  public void addGrid(SwingGridView gridView){
    this.grids.add(gridView);
    this.rescaleGrid();
    this.setupLayout();
  }
  
  private void setupLayout(){
    ParallelGroup pGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER, true);
    ParallelGroup pGroup2 = layout.createParallelGroup(GroupLayout.Alignment.CENTER, true);
    GroupLayout.SequentialGroup sGroup = layout.createSequentialGroup();
    GroupLayout.SequentialGroup sGroup2 = layout.createSequentialGroup();
    for(int i =0; i<grids.size(); i++){
      if(i<2){
        pGroup.addComponent(grids.get(i).getBoard(), 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        sGroup.addComponent(grids.get(i).getBoard(), 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
      }
      else{
        pGroup2.addComponent(grids.get(i).getBoard(), 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        sGroup2.addComponent(grids.get(i).getBoard(), 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
      }
    }
    GroupLayout.SequentialGroup sGroup3 = layout.createSequentialGroup();
    sGroup3.addGroup(pGroup);
    sGroup3.addGroup(pGroup2);
    
    ParallelGroup pGroup3 = layout.createParallelGroup(GroupLayout.Alignment.CENTER, true);
    pGroup3.addGroup(sGroup);
    pGroup3.addGroup(sGroup2);
    layout.setHorizontalGroup(
            layout.createSequentialGroup()
              .addComponent(this.board, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(sGroup3)
            );
            
    
    layout.setVerticalGroup(
            layout.createParallelGroup()
            .addComponent(this.board, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pGroup3)
            );
    this.getContentPane().repaint();
  }
}
