import java.awt.*;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for blobs
 * @author CBK, Fall 2016, using generic PointQuadtree
 * @author Sam Barton and Alex Huang-Menders Fall 2021
 */
public class CollisionGUI extends DrawingGUI {
	private static final int width=800, height=600;		// size of the universe

	private List<Blob> blobs;						// all the blobs
	private List<Blob> colliders;					// the blobs who collided at this step
	private char blobType = 'b';						// what type of blob to create
	private char collisionHandler = 'c';				// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;							// timer control

	public CollisionGUI() {
		super("super-collider", width, height);

		blobs = new ArrayList<Blob>(); // instantiate the two Lists
		colliders = new ArrayList<Blob>();

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * Adds a blob of the current blobType at the location
	 */
	private void add(int x, int y) {
		if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
	}

	/**
	 * DrawingGUI method, here creating a new blob
	 */
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * DrawingGUI method
	 */
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new blobs at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
				repaint();
			}			
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:"+k);
		}
		else { // set the type for new blobs
			blobType = k;			
		}
	}

	/**
	 * DrawingGUI method, here drawing all the blobs and then re-drawing the colliders in red
	 */
	public void draw(Graphics g) {
		// Ask all the blobs to draw themselves.
		// Ask the colliders to draw themselves in red.
		for(Blob blob: blobs) { // draw all blobs
			int r = (int) blob.getR();
			g.setColor(Color.BLACK);
			g.fillOval((int)blob.getX()-r, (int)blob.getY()-r, 2*r,2*r);
		}
		if(colliders == null) return;
		for(Blob blob: colliders) {  // draw all colliders
			int r = (int) blob.getR();
			g.setColor(Color.RED); // set color of colliders to red
			g.fillOval((int)blob.getX()-r, (int)blob.getY()-r, 2*r,2*r);
		}
	}

	/**
	 * Sets colliders to include all blobs in contact with another blob
	 */
	private void findColliders() {
		// Create the tree
		// For each blob, see if anybody else collided with it
		colliders = new ArrayList<Blob>();
		PointQuadtree<Blob> tree = new PointQuadtree<Blob>(blobs.get(0), 0, 0, width, height);
		for(int i = 1; i < blobs.size(); i++)
			if(!(blobs.get(i).getX()<0||blobs.get(i).getX()>=width||blobs.get(i).getY()<0 // check that blob is on screen
					||blobs.get(i).getY()>=height)) {tree.insert(blobs.get(i));}

		for(Blob blob: blobs) {
			List<Blob> list = tree.findInCircle(blob.x, blob.y, 2*blob.r); // since all blobs are radius r then you can double it
			if(list.size() > 1) {
				for(Blob b: list)
					colliders.add(b); // add all the colliding blobs to colliders
			}
		}
	}

	/**
	 * DrawingGUI method, here moving all the blobs and checking for collisions
	 */
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		for (Blob blob : blobs) {
			blob.step();
		}
		// Check for collisions
		if (blobs.size() > 0) {
			findColliders();
			if (collisionHandler=='d') {
				blobs.removeAll(colliders);
				colliders = null;
			}
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionGUI();
			}
		});
	}
}
