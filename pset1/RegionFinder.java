import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 50;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
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

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE
		regions = new ArrayList<ArrayList<Point>>();
		int biggestStack = 0;
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int count = 0;
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				Color c = new Color(image.getRGB(x, y));
				if(colorMatch(c, targetColor) && visited.getRGB(x, y) == 0) {
					visited.setRGB(x, y, 1);
					ArrayList<Point> curRegion = new ArrayList<Point>();

					ArrayList<Point> stack = new ArrayList<Point>();
					stack.add(new Point(x, y));
					String cur = "";


					while(!stack.isEmpty()) {

						Point curPoint = stack.remove(stack.size() - 1);
						int cx = curPoint.x, cy = curPoint.y;
						Point neighbor;
						Color tempColor;

						if(cx>0&&cy>0) {
							neighbor = new Point(cx - 1, cy - 1); //NW
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cy>0) {
							neighbor = new Point(cx, cy - 1); //N
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cx<image.getWidth()-1&&cy>0) {
							neighbor = new Point(cx + 1, cy - 1); //NE
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cx>0) {
							neighbor = new Point(cx - 1, cy); //W
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cx<image.getWidth()-1) {
							neighbor = new Point(cx + 1, cy); //E
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cx>1&&cy<image.getHeight()-1) {
							neighbor = new Point(cx - 1, cy + 1); //SW
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cy<image.getHeight()-1) {
							neighbor = new Point(cx, cy + 1); //S
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}
						if(cx<image.getWidth()-1&&cy<image.getHeight()-1) {
							neighbor = new Point(cx + 1, cy + 1); //SE
							tempColor = getColorAtPoint(neighbor);
							if (colorMatch(tempColor, targetColor) && (visited.getRGB(neighbor.x, neighbor.y) == 0)) {
								stack.add(neighbor);
								visited.setRGB(neighbor.x, neighbor.y, 1);
							}
						}

						curRegion.add(curPoint);
					}
					if(curRegion.size() >= minRegion) {
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
