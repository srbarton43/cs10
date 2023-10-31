import java.awt.*;

/**
 * A blob that moves randomly and has a color
 * added a get color method for functionality in pollock
 */
public class WanderingPixel extends Wanderer {
	private Color color;
	
	public WanderingPixel(double x, double y, double r, Color c) {
		super(x, y, r);
		this.color = c;
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
