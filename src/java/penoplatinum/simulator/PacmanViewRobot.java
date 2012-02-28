package penoplatinum.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import penoplatinum.simulator.view.Board;
import penoplatinum.simulator.view.ViewRobot;

public class PacmanViewRobot implements ViewRobot{
  
    // cached images
  public static Image robot;
  
  private PacmanEntity original;

  public PacmanViewRobot(PacmanEntity original) {
    this.original = original;
  }   

  @Override
  public void trackMovement(Graphics2D g2d) {
    g2d.setColor(Color.yellow);
    g2d.drawLine( this.getX(), this.getY(), this.getX(), this.getY());
  }
  
  @Override
  public void renderRobot(Graphics2D g2d, ImageObserver board) { 
    // render robot
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( this.getX() - 20, this.getY() - 20 );
    affineTransform.rotate( -1 * Math.toRadians(this.getDirection()), 20, 20 ); 
    g2d.drawImage( RemoteViewRobot.robot, affineTransform, board );
    
  }
  
  @Override
  public void renderSonar(Graphics2D g2d) {
    //Pacman has no sonar
  }

  @Override
  public int getX() {
    return ((int) original.getPosX())*Board.SCALE;
  }

  @Override
  public int getY() {
    return ((int) original.getPosY())*Board.SCALE;
  }

  @Override
  public int getDirection() {
    return (int) original.getDir();
  }  
}
