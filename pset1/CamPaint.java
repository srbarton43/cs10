import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 *
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder1 finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder1();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting,
	 * depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		if (displayMode == 'w') {
			super.draw(g);		//displays raw webcam footage
		}
		else if (displayMode == 'r' && targetColor != null) {		//displays recolored image from RegionFinder class
			g.drawImage(finder.getRecoloredImage(), 0, 0, null);
		}
		else if (displayMode == 'p') {			//displays drawing created by brush, stored in painting image
			g.drawImage(image, 0, 0, null);
			g.drawImage(painting, 0, 0, null);
		} else {
			super.draw(g);
		}
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage() {
		if (targetColor != null && image != null) {		//check to ensure target color and webcam image exist
			finder.setImage(image);						//generate brush region from RegionFinder class
			finder.findRegions(targetColor);
			finder.recolorImage();
			ArrayList<Point> brush = finder.largestRegion();
			if (brush != null) {
				for (Point point : brush) {                                        //update painting with brush stroke
					painting.setRGB(point.x, point.y, paintColor.getRGB());
				}
			}
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		targetColor = new Color(image.getRGB(x, y));	//sets track color to color of pixel at mouse press
		System.out.println(targetColor.toString());
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}
