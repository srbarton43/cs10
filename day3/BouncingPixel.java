import java.awt.*;

/**
 * A blob that moves in a particular direction, but bounces off the walls.
 *
 * @author Sam Barton 9/17/21
 */
public class BouncingPixel extends Bouncer {
	private Color color;

	/**
	 * Initializes with the coordinates size of 1.0, and Color c
	 */
	public BouncingPixel(double x, double y, int xmax, int ymax, Color c) {
		super(x, y, xmax, ymax);
		this.setR(1);
		color = c;
	}

	// returns the color
	public Color getColor() {
		return color;
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}
}
