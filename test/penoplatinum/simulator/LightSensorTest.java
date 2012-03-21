/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.tiles.Sectors;
import org.junit.Assert.*;
import org.junit.Test;
import penoplatinum.simulator.view.Board;

/**
 *
 * @author MHGameWork
 */
public class LightSensorTest {

  /**
   * This test checks whether the values returned by the lightsensor match the 
   * rendered pixel colors in the simulator
   */
  @Test
  public void testLightSensorCorrectness() {

    Sector s = Sectors.N;

    showImages(createTileImage(s), createImageFromLightsensor(s));
    validateLightvalues(s);

  }

  private void validateLightvalues(Sector s) {
    BufferedImage img = createTileImage(s);

    for (int x = 0; x < Sector.SIZE; x++) {
      for (int y = 0; y < Sector.SIZE; y++) {
        final int argb = img.getRGB(x * 2, y * 2);
        int renderedColor = -100;
        if (argb == Board.BLACK.getRGB()) {
          renderedColor = Sector.BLACK;
        } else if (argb == Board.BROWN.getRGB()) {
          renderedColor = Sector.NO_COLOR;
        } else if (argb == Board.WHITE.getRGB()) {
          renderedColor = Sector.WHITE;
        } else {
          // something else is here, for example a wall, this is not relevant
          //   for the lightsensor, so skip this
          continue;
        }


        final int color = s.getColorAt(x, y);
        System.out.println(x + ", " + y + "Light: " + color + " Rendered: " + renderedColor);
        org.junit.Assert.assertEquals(renderedColor, color);
      }
    }
  }

  private BufferedImage createTileImage(Sector s) {
    final BufferedImage img = new BufferedImage(Sector.DRAW_TILE_SIZE, Sector.DRAW_TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = img.createGraphics();
    s.drawTile(g, 1, 1);
    return img;
  }

  private void showImages(final BufferedImage img, final BufferedImage lightImage) throws HeadlessException {
    JFrame f = new JFrame() {

      @Override
      public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 300, 300);
        g.drawImage(img, 40 - 24, 40, null);
        g.drawImage(lightImage, 40 - 24 + 10 + Sector.DRAW_TILE_SIZE, 40, null);
      }
    };
    f.setSize(300, 300);
    f.setVisible(true);
  }

  private BufferedImage createImageFromLightsensor(Sector s) {
    final BufferedImage img = new BufferedImage(Sector.DRAW_TILE_SIZE, Sector.DRAW_TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = img.createGraphics();
    for (int x = 0; x < Sector.SIZE; x++) {
      for (int y = 0; y < Sector.SIZE; y++) {

        int light = s.getColorAt(x, y);
        int color = Color.red.getRGB();

        switch (light) {
          case Sector.NO_COLOR:
            color = Board.BROWN.getRGB();
            break;
          case Sector.WHITE:
            color = Board.WHITE.getRGB();
            break;
          case Sector.BLACK:
            color = Board.BLACK.getRGB();
            break;

        }


        img.setRGB(x * 2, y * 2, color);
        img.setRGB(x * 2 + 1, y * 2, color);
        img.setRGB(x * 2, y * 2 + 1, color);
        img.setRGB(x * 2 + 1, y * 2 + 1, color);
      }

    }

    return img;

  }
}
