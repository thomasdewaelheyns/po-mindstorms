package penoplatinum.simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import penoplatinum.simulator.view.Board;
import penoplatinum.simulator.view.ViewRobot;

public class RemoteViewRobot implements ViewRobot {

  private RemoteEntity original;

  public RemoteViewRobot(RemoteEntity original) {
    this.original = original;
  }

  @Override
  public void trackMovement(Graphics2D g2d) {
    g2d.setColor(Color.yellow);
    g2d.drawLine(this.getX(), this.getY(), this.getX(), this.getY());
  }

  @Override
  public void renderRobot(Graphics2D g2d, ImageObserver board) {
    // render robot
    AffineTransform affineTransform = new AffineTransform();
    affineTransform.setToTranslation(this.getX() - 20, this.getY() - 20);
    affineTransform.rotate(-1 * Math.toRadians(this.getDirection()), 20, 20);
    g2d.drawImage(ColorLink.getFileByColor(ColorLink.getColorByName(this.original.getEntityName())), affineTransform, board);

  }

  @Override
  public void renderSonar(Graphics2D g2d) {
//    
//    if( this.getDistances() == null ) { return; }
//    for( int i=0; i<this.getDistances().size()-1; i++ ) {
//      int angle = (int)(this.getAngles().get(i)) + this.getDirection();
//      double rads = Math.toRadians(angle+90);
//      int dx = (int)(Math.cos(rads) * this.getDistances().get(i)) * Board.SCALE;
//      int dy = (int)(Math.sin(rads) * this.getDistances().get(i)) * Board.SCALE;
//      g2d.draw(new Line2D.Float(this.getX(), this.getY(), this.getX() + dx, this.getY() - dy));
//    }
  }

  @Override
  public int getX() {
    return ((int) original.getPosX()) * Board.SCALE;
  }

  @Override
  public int getY() {
    return ((int) original.getPosY()) * Board.SCALE;
  }

  @Override
  public int getDirection() {
    return (int) original.getDir();
  }
}
