import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder2 {
	private static int maxColorDiff = 25;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder2() {
		this.image = null;
	}

	public RegionFinder2(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	public static int getMaxColorDiff(){return maxColorDiff;}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 *
	 * @param targetColor Color object which is the targeted color to find regions for
	 */
	public void findRegions(Color targetColor) {
		regions = new ArrayList<ArrayList<Point>>(); // initialize the regions ArrayList
		// BufferedImage to represent pixels already visited
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				Color c = new Color(image.getRGB(x, y));
				if(colorMatch(c, targetColor) && visited.getRGB(x, y) == 0) {
					visited.setRGB(x, y, 1);
					ArrayList<Point> curRegion = new ArrayList<Point>();
					ArrayList<Point> stack = new ArrayList<Point>();
					stack.add(new Point(x, y));

					// region finding algorithm
					while(!stack.isEmpty()) {
						Point curPoint = stack.remove(stack.size() - 1); // removes top point from stack
						int cx = curPoint.x, cy = curPoint.y;
						Point neighbor;
						Color tempColor;

						// checks each of the 8 neighbors and adds them to the "stack" if they are right color and not already visited
						for(int i = -1; i <= 1; i++) {
							for (int j = -1; j <=1; j++) {
								if(cx+i<0||cx+i>=image.getWidth()||cy+j<0||cy+j>=image.getHeight()) {
									break;
								}
								else {
									neighbor = new Point(cx+i, cy+j);
									tempColor = getColorAtPoint(neighbor);
									if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
										stack.add(neighbor);
										visited.setRGB(neighbor.x, neighbor.y, 1);
									}
								}
							}
						}
						curRegion.add(curPoint); // adds removed point to the current region ArrayList
					}
					if(curRegion.size() >= minRegion) { // if the current region is bigger than the minimum region size, add it to regions
						regions.add(curRegion);
					}
				}
			}
		}
	}

	// returns Color of the image at point p
	private Color getColorAtPoint(Point p) {
		return new Color(image.getRGB(p.x, p.y));
	}

	/**
	 * manipulates maxColorDiff instance variable capped at 10
	 *
	 * @param n amount to change maxColorDiff by
	 *          positive value means growing
	 *          negative value means shrinking
	 */
	public void changeMaxColorDiff(int n) {
		if(maxColorDiff > 10) maxColorDiff += n;
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		int c1R = c1.getRed();			//get RGB components of color 1
		int c1G = c1.getGreen();
		int c1B = c1.getBlue();
		int c2R = c2.getRed();			//get RGB components of color 2
		int c2G = c2.getGreen();
		int c2B = c2.getBlue();
		//use Euclidean geometry to find difference between colors and compare to predetermined threshold (maxColorDiff)
		return Math.sqrt(Math.pow((c1R-c2R), 2) + Math.pow((c1G-c2G), 2) + Math.pow((c1B-c2B), 2)) <= maxColorDiff;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		int max = 0;
		ArrayList<Point> largest = null;
		for (ArrayList<Point> region : regions) {		//loop through each region in regions and find largest
			if (region.size() > max) {					//updates the largest region as larger regions are found
				max = region.size();
				largest = region;
			}
		}
		return largest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		for (ArrayList<Point> region : regions) {
			Color color = new Color((int) (Math.random()*16777216));	//generates random color for each region
			for (Point point : region) {								//sets each point (pixel) in region to color
				recoloredImage.setRGB(point.x, point.y, color.getRGB());
			}
		}
	}
}
