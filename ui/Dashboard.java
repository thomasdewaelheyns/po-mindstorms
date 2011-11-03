/**
 * Dashboard
 * 
 * Author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import java.awt.*; 
import java.awt.geom.*; 
import java.awt.event.*; 
import java.awt.image.*; 
import javax.swing.*; 

import java.net.URL;

public class Dashboard extends JPanel {

  private int lightValue = 0;
  private int lightColor = 0;
  private int barcode    = -1;
  private int direction  = -1;

  private Polygon greyTriangle;
  private Polygon colorTriangle;
  
  private Image goForward;
  private Image goLeft;
  private Image goRight;
  
  private Font font;
  
  public Dashboard() {
    this.setupWidgets();
    this.setupImages();
  }
  
  private void setupWidgets() {
    this.greyTriangle  = new Polygon( new int[]{100,200,100},
                                      new int[]{100,100,200}, 3);
    this.colorTriangle = new Polygon( new int[]{200,200,100},
                                      new int[]{100,200,200}, 3);
  }
  
  private void setupImages() {
    this.goForward = this.setupImage("go-forward");
    this.goLeft    = this.setupImage("go-left");
    this.goRight   = this.setupImage("go-right");
  }
  
  private Image setupImage(String name) {
    URL resource = this.getClass().getResource("./images/" + name + ".png");
    ImageIcon ii = new ImageIcon(resource);
    return ii.getImage();    
  }
  
  public void update( int lightValue, int lightColor, 
                      int barcode, int direction )
  {
    this.lightValue = lightValue;
    this.lightColor = lightColor;

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

    FontMetrics fm   = g2d.getFontMetrics(this.font);
    java.awt.geom.Rectangle2D rect = 
      fm.getStringBounds(""+this.lightValue, g2d);

    int textHeight = (int)(rect.getHeight()); 
    int textWidth  = (int)(rect.getWidth());
    int panelHeight= 200;
    int panelWidth = 200;

    // Center text horizontally and vertically
    int x = (panelWidth  - textWidth)  / 2;
    int y = (panelHeight - textHeight) / 2  + fm.getAscent();

    g2d.setPaint(Color.green);
    g2d.drawString(""+this.lightValue, 50+x, 50+y);
  }

  private void renderBarcode(Graphics2D g2d) {
    if( this.barcode > 0 ) {
      g2d.drawString(this.getBarcode(), 350, 132);
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
        this.renderImage(g2d, this.goLeft, 350, 200 );
        break;
      case UIView.GO_RIGHT:
        this.renderImage(g2d, this.goRight, 350, 200 );
        break;
      case UIView.GO_FORWARD:
        this.renderImage(g2d, this.goForward, 350, 200 );
        break;      
      default:
        // do nothing
    }
  }
  
  private void renderImage(Graphics2D g2d, Image image, int x, int y) {
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( x, y );
    g2d.drawImage( image, affineTransform, this );
  }
}
