package penoplatinum.ui;

/**
 * Dashboard
 * 
 * GUI showing information about sensors:
 * - lightsensor value and interpretation
 * - sonarsensor angle and distance
 * - barcode and barcode interpretation
 * 
 * Author: Team Platinum
 */


import java.awt.*; 
import java.awt.geom.*; 
import javax.swing.*; 

import java.net.URL;

public class Dashboard extends JPanel {

  // light sensor
  private int lightValue =  0;
  private int lightColor =  0;

  private Polygon greyTriangle;
  private Polygon colorTriangle;

  // sonar sensor
  private double angle      = -1;
  private int    distance   = -1;

  private Image robot;
  
  // barcode
  private int barcode    = -1;
  private int direction  = -1;

  private Image goForward;
  private Image goLeft;
  private Image goRight;

  private Font font;
  
  public Dashboard() {
    this.setupCanvas();
    this.setupWidgets();
    this.setupImages();
  }
  
  private void setupCanvas() {
    this.setPreferredSize( new Dimension( 680, 440 ) );
    this.setBackground(Color.white);
  }
  
  private void setupWidgets() {
    this.greyTriangle  = new Polygon( new int[]{100,200,100},
                                      new int[]{ 50, 50,150}, 3);
    this.colorTriangle = new Polygon( new int[]{200,200,100},
                                      new int[]{ 50,150,150}, 3);
  }
  
  private void setupImages() {
    this.goForward = this.setupImage("go-forward");
    this.goLeft    = this.setupImage("go-left");
    this.goRight   = this.setupImage("go-right");
    this.robot     = this.setupImage("robot150");
  }
  
  private Image setupImage(String name) {
    URL resource = this.getClass().getResource("./images/" + name + ".png");
    ImageIcon ii = new ImageIcon(resource);
    return ii.getImage();    
  }
  
  // lightsensor value (0-1024), color interpretation (white, black or brown)
  public void updateLight( int lightValue, int lightColor ) {
    this.lightValue = lightValue;
    this.lightColor = lightColor;
    this.repaint();
  }

  // angle is expressed in degrees zero-based facing north
  // convert it to radians and make it zero-based facing east
  public void updateSonar( int angle, int distance ) {
    this.angle    = Math.toRadians(angle + 90);
    this.distance = distance;
    this.repaint();
  }

  // barcode is a number of which the bitstring represents the barcode
  // direction is the action the robot will perform based on this barcode
  public void updateBarcode( int barcode, int direction ) {
    this.barcode    = barcode;
    this.direction  = direction;
    this.repaint();
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    this.setupFont(g2d);
    this.renderLightValue(g2d);
    this.renderBarcode(g2d);
    this.renderDirection(g2d);
    this.renderSonar(g2d);

    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }
  
  private void setupFont(Graphics2D g2d) {
    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    int fontSize = (int)Math.round(32 * screenRes / 72.0);

    this.font = new Font("Arial", Font.BOLD, fontSize);    
    g2d.setFont(this.font);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                         RenderingHints.VALUE_ANTIALIAS_ON);
  }
  
  private void renderLightValue(Graphics2D g2d) {
    this.renderGreyTriangle(g2d);
    this.renderColorTriangle(g2d);
    this.renderLightValueLabel(g2d);
  }
  
  private void renderGreyTriangle(Graphics2D g2d) {
    int g = this.lightValue / 4;
    g2d.setPaint(new Color(g,g,g));
    g2d.fill(this.greyTriangle);
  }

  private void renderColorTriangle(Graphics2D g2d) {
    switch(this.lightColor) {
      case UIView.WHITE: g2d.setPaint(Color.white); break;
      case UIView.BLACK: g2d.setPaint(Color.black); break;
      default: g2d.setPaint(new Color(205,165,100));
    }
    g2d.fill(this.colorTriangle);
  }
 
  private void renderLightValueLabel(Graphics2D g2d) {
    this.drawCenteredText( g2d, "" + this.lightValue, Color.red, 
                           50, 0, 200, 200 );
  }
  
  private void renderBarcode(Graphics2D g2d) {
    if( this.barcode >= 0 ) {
      this.drawCenteredText( g2d, this.getBarcode(), new Color(44,89,156), 
                             350, 50, 150, 32 );
    }
  }

  private String getBarcode() {
    String bits = "";
    for( int i=0; i<8; i++ ) {
      bits += (( this.barcode & (1<<i) ) != 0) ? "1" : "0";
    }
    return bits;
  }

  private void renderDirection(Graphics2D g2d) {
    switch(this.direction) {
      case UIView.GO_LEFT : 
        this.renderImage(g2d, this.goLeft, 350, 100 );
        break;
      case UIView.GO_RIGHT:
        this.renderImage(g2d, this.goRight, 350, 100 );
        break;
      case UIView.GO_FORWARD:
        this.renderImage(g2d, this.goForward, 350, 100 );
        break;      
      default:
        // do nothing
    }
  }

  private void renderSonar(Graphics2D g2d) {
    if( this.angle >= 0 ) {
      this.renderImage(g2d, this.robot, 75, 275 );
      int d = (int)this.distance / 2;
      d = d > 200 ? 200 : d;
      int x = 150+(int)(Math.cos(this.angle)*d);
      int y = 350-(int)(Math.sin(this.angle)*d);
      g2d.setStroke(new BasicStroke(4F));
      g2d.setColor(Color.red);
      g2d.draw(new Line2D.Float(150, 350, x, y ));
      this.drawCenteredText( g2d, ""+this.distance, Color.red, 
                             x, y, 100, 32 );
    }
  }
  
  private void drawCenteredText(Graphics2D g2d, String text, Color color,
                                int x, int y,
                                int width, int height ) {
    FontMetrics fm   = g2d.getFontMetrics(this.font);
    java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g2d);

    int textHeight = (int)(rect.getHeight()); 
    int textWidth  = (int)(rect.getWidth());

    // Center text horizontally and vertically
    x += (width  - textWidth)  / 2;
    y += (height - textHeight) / 2  + fm.getAscent();

    g2d.setPaint(color);
    g2d.drawString(text, x, y);
  }

  private void renderImage(Graphics2D g2d, Image image, int x, int y) {
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( x, y );
    g2d.drawImage( image, affineTransform, this );
  }
  
}
