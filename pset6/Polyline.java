import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 */
public class Polyline implements Shape {
	private ArrayList<Segment> segments;
	private Color color;
	private int xi, yi;

	public Polyline(int xi, int yi, Color color) {
		this.xi = xi;
		this.yi = yi;
		this.color = color;
		segments = new ArrayList<Segment>();
		addSegment(xi, yi);
	}

	/**
	 * add segment from mouse click
	 */
	public void addSegment(int x2, int y2) {
		segments.add(new Segment(xi,yi,x2,y2,color));
		xi = x2;
		yi = y2;
	}

	/**
	 * move polyline by dx,dy
	 */
	@Override
	public void moveBy(int dx, int dy) {
		for (Segment segment : segments) {
			segment.moveBy(dx,dy);
		}
	}

	/**
	 * return color of polyline
	 */
	@Override
	public Color getColor() {return color;}

	/**
	 * set color of polyline
	 */
	@Override
	public void setColor(Color color) {
		for (Segment segment : segments) {
			this.color = color;
			segment.setColor(color);
		}
	}
	
	@Override
	public boolean contains(int x, int y) {
		for (Segment segment : segments) {
			if (segment.contains(x,y)) return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		for (Segment segment : segments) {
			segment.draw(g);
		}
	}

	@Override
	public String toString() {
		String result = "polyline ";
		for (Segment segment : segments) {
			result += segment.toString() + " ";
		}
		result += color.getRGB();
		return result;
	}
}
