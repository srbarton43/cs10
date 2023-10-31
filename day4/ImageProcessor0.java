import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Sam Barton, September 20, 2021, wrote invertSquare method
 */
public class ImageProcessor0 {
	private BufferedImage image;		// the current image being processed

	/**
	 * @param image		the original
	 */
	public ImageProcessor0(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 *
	 * @param cx the center of the square x coord
	 * @param cy the center of the square y coord
	 * @param r radius of the square
	 *
	 * this method inverts each pixel in a square of radius r
	 */
	public void invertBrush(int cx, int cy, int r) {
		for (int y = Math.max(0, cy-r); y < Math.min(image.getHeight(), cy+r); y++) {
			for (int x = Math.max(0, cx - r); x < Math.min(image.getWidth(), cx + r); x++){
				Color color = new Color(image.getRGB(x, y));
				int red = 255 - color.getRed();
				int green = 255 - color.getGreen();
				int blue = 255 - color.getBlue();
				color = new Color (red, green, blue);
				image.setRGB(x, y, color.getRGB());
			}
		}
	}
}
