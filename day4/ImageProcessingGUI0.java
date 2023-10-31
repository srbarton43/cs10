import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * Load an image and display it
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Sam Barton, September 20, 2021, modified handleKeyPress and handleMouse Press methods
 * 										   added handleMouseMotion method
 */
public class ImageProcessingGUI0 extends DrawingGUI {
	private ImageProcessor0 proc;		// handles the image processing

	private int radius = 10; // radius of brush

	private boolean brushDown; // if true brush is down, false then otherwise

	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public ImageProcessingGUI0(ImageProcessor0 proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
		
	}

	/**
	 * DrawingGUI method, here dispatching on image processing operations
	 */
	@Override
	/**
	 * space bar toggles brush up and down
	 */
	public void handleKeyPress(char op) {
		System.out.println("Handling key '"+op+"'");
		if (op=='s') { // save a snapshot
			saveImage(proc.getImage(), "pictures/snapshot.png", "png");
		}
		else if(op==' ') { // toggle brush up and down
			brushDown = !brushDown;
		}
		else if(op=='+') { // increase brush radius
			radius++;
		}
		else if(op=='-') { // decrease brush radius
			if(radius > 1) radius--;
		}
		else {
			System.out.println("Unknown operation");
		}

		repaint(); // Re-draw, since image has changed
	}

	@Override
	// just creates a single inverted square where mouse is clicked if the brush is up
	public void handleMousePress(int x, int y) {
		if(!brushDown) {
			proc.invertBrush(x, y, radius);
		}
		repaint();
	}

	@Override
	// draws inverted square where cursor is when you move the mouse if brush is down
	public void handleMouseMotion(int x, int y) {
		if(brushDown) {
			proc.invertBrush(x, y, radius);
			repaint();
		}
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage baker = loadImage("pictures/baker.jpg");
				// Create a new processor, and a GUI to handle it
				new ImageProcessingGUI0(new ImageProcessor0(baker));
			}
		});
	}
}
