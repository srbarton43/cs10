import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 *
 */
public class CamPaint2 extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder2 finder;			// handles the finding
	//private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	private ArrayList<Brush> brushes;
	private boolean paused;
	private boolean eyedrop;
	private boolean adding;

	private class Brush extends RegionFinder2 {
		private Color targetColor;
		private ArrayList<Point> brush;

		public Brush(Color targetColor) {
			super();
			this.targetColor = targetColor;
			brush = new ArrayList<Point>();
		}
	}
	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint2() {
		finder = new RegionFinder2();
		eyedrop = false;
		paused = false;
		brushes = new ArrayList<Brush>();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	protected void deleteBrushes() {
		brushes = new ArrayList<Brush>();
		clearPainting();
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
		else if (displayMode == 'r' && !brushes.isEmpty()) {		//displays recolored image from RegionFinder class
			for(Brush b: brushes) {
				g.drawImage(b.getRecoloredImage(), 0, 0, null);
			}
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
		if(!brushes.isEmpty()) {
			for(Brush b: brushes) {   //check to ensure target color and webcam image exist
				b.setImage(image);                        //generate brush region from RegionFinder class
				b.findRegions(b.targetColor);
				b.recolorImage();
				b.brush = b.largestRegion();
				if (!paused) { // check to see that brush is not null AND not paused
					for (Point point : b.brush) {                                        //update painting with brush stroke
						painting.setRGB(point.x, point.y, paintColor.getRGB());
					}
				}
			}
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		if(eyedrop) { // sets brush color to clicked pixel (like eyedropper in photoshop)
			paintColor = new Color(image.getRGB(x, y));
			eyedrop = false;
		} else if(brushes.isEmpty()) {
			brushes.add(new Brush(new Color(image.getRGB(x, y)))); //sets track color to color of pixel at mouse press
			System.out.println(brushes.get(0).targetColor.toString());
		}
		else if(adding) {
			brushes.add(new Brush(new Color(image.getRGB(x, y)))); //sets track color to color of pixel at mouse press
			System.out.println(brushes.get(brushes.size()-1).targetColor.toString());
		}
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
		else if(k == 'd') {
			deleteBrushes();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else if(k == '[') { // decrease maxColorDiff by 5
			finder.changeMaxColorDiff(-5);
			System.out.println("Max Color Difference: "+ RegionFinder2.getMaxColorDiff());
		}
		else if(k == ']') { // increase maxColorDiff by 5
			finder.changeMaxColorDiff(5);
			System.out.println("Max Color Difference: "+ RegionFinder2.getMaxColorDiff());
		}
		else if(k == 'i') { // toggle eyedrop mode
			eyedrop = !eyedrop;
		}
		else if(k == '-') {
			adjustAlpha(-10);
		}
		else if(k == '+') {
			adjustAlpha(10);
		}
		else if(k == ' ') { // toggle pause painting
			paused = !paused;
		}
		else if(k == 'a') {
			adding = !adding;
			System.out.println("adding is " + adding);
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	/**
	 * adjusts alpha of paint according to parameter
	 *
	 * @param n int determining change of alpha
	 */
	private void adjustAlpha(int n) {
		int alpha = paintColor.getAlpha();
		int r = paintColor.getRed();
		int g = paintColor.getGreen();
		int b = paintColor.getBlue();
		try {
			paintColor = new Color(r, g, b, alpha + n);
			System.out.println("alpha: " + alpha);
		} catch(Exception e) {
			System.out.println("invalid alpha value");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint2();
			}
		});
	}
}
