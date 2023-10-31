import java.util.ArrayList;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to hold multiples shapes and their ids
 * @author Alexander Huang-Menders and Sam Barton
 */
public class Sketch {
    //private ArrayList<Shape> shapes;
    private TreeMap<Integer, Shape> shapes;
    int nextID;
    int currID;

    public Sketch() {
        //shapes = new ArrayList<Shape>();
        shapes = new TreeMap<Integer, Shape>();
    }

    public void add(Shape shape) {
        shapes.put(nextID, shape);
        nextID++;
    }

    /**
     * parse input data and add shape to sketch shapes
     * @param input string representing a shape
     */
    public void addShape(String input) {
        System.out.println("INPUT: " + input);
        if (input == null) {return;}
        String[] s = input.split(" ");
        int ID = Integer.parseInt(s[0]);
        if (shapes.get(ID) == null) {
            currID = nextID;
            nextID++;
        }
        else {
            currID = ID;
        }
        if (s[1].equals("ellipse")) {
            shapes.put(currID, new Ellipse(Integer.parseInt(s[2]), Integer.parseInt(s[3]),
                    Integer.parseInt(s[4]), Integer.parseInt(s[5]), new Color(Integer.parseInt(s[6]))));
        }
        else if (s[1].equals("rectangle")) {
            shapes.put(currID, new Rectangle(Integer.parseInt(s[2]), Integer.parseInt(s[3]),
                    Integer.parseInt(s[4]), Integer.parseInt(s[5]), new Color(Integer.parseInt(s[6]))));
        }
        else if (s[1].equals("segment")) {
            shapes.put(currID, new Segment(Integer.parseInt(s[2]), Integer.parseInt(s[3]),
                    Integer.parseInt(s[4]), Integer.parseInt(s[5]), new Color(Integer.parseInt(s[6]))));
        }
        else if (s[1].equals("polyline")) {
            Polyline p = new Polyline(Integer.parseInt(s[3]), Integer.parseInt(s[4]), new Color(Integer.parseInt(s[7])));
            for (int i = 7; i < s.length; i+=6) {
                p.addSegment(Integer.parseInt(s[i-2]), Integer.parseInt(s[i-1]));
            }
            shapes.put(currID, p);
        }
        else if (s[1].equals("delete")) {
            shapes.remove(ID);
            System.out.println("Deleted");
        }
        System.out.println("NUM SHAPES: " + shapes.size());
    }

    public int contains(int x, int y) {
        for (Integer i : shapes.descendingKeySet()) {
            if (shapes.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public void recolor(Point p, Color color) {
        for (Integer i : shapes.descendingKeySet()) {
            if (shapes.get(i).contains((int)p.getX(), (int)p.getY())) {
                shapes.get(i).setColor(color);
                return;
            }
        }
    }

    public void delete(Point p) {
        for (int i = shapes.size()-1; i >= 0; i--) {
            if (shapes.containsKey(i)&&shapes.get(i).contains((int)p.getX(), (int)p.getY())) {
                shapes.remove(i);

                return;
            }
        }
    }

    //public ArrayList<Shape> getShapes() {return shapes;}
    public TreeMap<Integer, Shape> getShapes() {return shapes;}
}
