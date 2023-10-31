import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Based off of Pollack.java written by Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 *
 * @author Sam Barton 9/17/21
 */
public class Pollock2 extends DrawingGUI
{
    private static final int width = 800, height = 600; // setup: window size
    private static final int numBlobs = 20000;			// setup: how many blobs
    private static final int numToMove = 5000;			// setup: how many blobs to animate each frame

    private BufferedImage result;						// the picture being painted
    private ArrayList<Blob> blobs;						// the blobs representing the picture

    public Pollock2() {
        super("Pollock2", width, height);

        result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create a bunch of random blobs.
        blobs = new ArrayList<Blob>();
        for (int i=0; i<numBlobs; i++) {
            int x = (int)(width*Math.random());
            int y = (int)(height*Math.random());
            // Create a blob with a random color
            Color color = new Color((int) ( 16777216 * Math.random() ) );
            blobs.add(new BouncingPixel(x, y, width, height, color));
            //blobs.add(new WanderingPixel(x, y, 1, color));
        }

        // Timer drives the animation.
        startTimer();
    }

    /**
     * DrawingGUI method, here just drawing all the blobs
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(result, 0, 0, null);
        for (Blob blob : blobs) {
            blob.draw(g);
        }
    }

    /**
     * DrawingGUI method, here moving some blobs
     * the blob leaves a trail of its given color
     */
    @Override
    public void handleTimer() {
        for (int b = 0; b < numToMove; b++) {
            // Pick a random blob, leave a trail where it is, and ask it to move.
            Blob blob = blobs.get((int)(Math.random()*blobs.size()));
            int x = (int)blob.getX(), y = (int)blob.getY();
            // Careful to stay within the image
            if (x>=0 && x<width && y>=0 && y<height) {
                // Leave a trail of the blob's color
                Color color = ( (BouncingPixel)blob ).getColor();
                //Color color = ( (WanderingPixel)blob ).getColor();
                result.setRGB(x, y, color.getRGB());
            }
            blob.step();
        }
        // Now update the drawing
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Pollock2();
            }
        });
    }
}
