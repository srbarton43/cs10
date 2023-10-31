import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015.
 * @author CBK, Spring 2016, explicit rectangle.
 * @author CBK, Fall 2016, generic with Point2D interface.
 * @author Sam Barton and Alex Huang-Menders Fall 2021
 * 
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 *
	 * @param p2 is the Point2D object to insert
	 */

	public void insert(E p2) {
		if(p2.getX()<x1||p2.getX()>=x2||p2.getY()<y1||p2.getY()>=y2) {
			System.out.println("invalid insert at ("+p2.getX()+","+p2.getY()+")");
			return; // if not within bounds then return
		}
		insertHelper(p2); // calls recursive helper method
	}

	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		int size = 1;
		for(int i = 1; i <= 4; i++) // checks each child
			if(getChild(i) != null) size += getChild(i).size();
		return size;
	}
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	public List<E> allPoints() {
		List<E> result = new ArrayList<E>();
		traverse(result); // recursive helper method
		return result;
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		List<E> result = new ArrayList<E>();
		findInCircleHelper(result, cx, cy, cr);
		return result;
	}

	// returns the int value of the quadrant for a point and its current node
	public int getQuadrant(E p) {
		if(p.getX()>point.getX()&&p.getY()<=point.getY()) return 1;
		if(p.getX()<=point.getX()&&p.getY()<=point.getY()) return 2;
		if(p.getX()<=point.getX()&&p.getY()>point.getY()) return 3;
		return 4;

	}

	//.recursive helper method for inserting new Point2D into tree
	private void insertHelper(E p2) {
		int quad = getQuadrant(p2); // get the quadrant of the point
		if (!hasChild(quad)) { // if the quadrant has no child, add one
			if (quad == 1) c1 = new PointQuadtree<E>(p2, (int) point.getX(), y1, x2, (int) point.getY());
			else if (quad == 2) c2 = new PointQuadtree<E>(p2, x1, y1, (int) point.getX(), (int) point.getY());
			else if (quad == 3) c3 = new PointQuadtree<E>(p2, x1, (int) point.getY(), (int) point.getX(), y2);
			else if (quad == 4) c4 = new PointQuadtree<E>(p2, (int) point.getX(), (int) point.getY(), x2, y2);
		} else
			getChild(quad).insertHelper(p2); // else call insert on the child in the point's quadrant
	}

	// recursive helper method for the allPoints method
	private void traverse(List<E> list) {
		list.add(point);
		for(int i = 1; i <= 4; i++)
			if(hasChild(i)) getChild(i).traverse(list);
	}

	// recursive helper method for findInCircle
	private void findInCircleHelper(List<E> list, double cx, double cy, double cr) {
		if(Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)) {
			if(Geometry.pointInCircle(point.getX(),point.getY(), cx, cy, cr)) { // adds point to list if in circle
				list.add(point);
			}
			for(int i = 1; i <= 4; i++)
				if(hasChild(i)) getChild(i).findInCircleHelper(list, cx, cy, cr); // recursively calls on children
		}
	}
}
