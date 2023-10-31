import java.util.*;

public class Course {

    List<Course> prereqs;
    String name;

    public Course(String name) {
        this.name = name;
        prereqs = new ArrayList<>();
    }
    public String getName() {return name;}

    public void addPrereq(Course course) {
        prereqs.add(course);
    }

    public List<Course> getPreqs() {
        return prereqs;
    }

    public String toString() {
        return name;
    }

    public static AdjacencyMapGraph<Course, String> buildGraph(List<Course> courses) {
        AdjacencyMapGraph<Course, String> graph = new AdjacencyMapGraph<>();

        for(Course course: courses) {
            if(!graph.hasVertex(course)) graph.insertVertex(course);
            for(Course prereq: course.getPreqs()) {
                if(!graph.hasVertex(prereq)) graph.insertVertex(prereq);
                graph.insertDirected(prereq, course, "");
            }
        }
        return graph;
    }

    public static String createCourseList(AdjacencyMapGraph<Course, String> courses) {
        Stack<Course> stack = new Stack<>();
        ArrayList<Course> list = new ArrayList<>();
        HashMap<Course, Integer> inDegree = new HashMap<>(); // keeps track of in degree for vertices

        for(Course course: courses.vertices()) {
            if(courses.inDegree(course) == 0) stack.push(course);
            inDegree.put(course, courses.inDegree(course)); // add in degrees to in degree ADT
        }

        while(!stack.isEmpty()) { //modified DFS
            Course c = stack.pop();
            list.add(c);
            for(Course next: courses.outNeighbors(c)) {
                inDegree.put(next, inDegree.get(next) - 1); // decrement in degree for next courses
                if(inDegree.get(next) == 0) stack.push(next); // if in degree is zero you can take course!
            }
        }

        return list.toString();
    }

    public static void main(String[] args) {
        Course cs1 = new Course("CS1");
        Course cs2 = new Course ("CS2"); cs2.addPrereq(cs1);

        Course cs3 = new Course ("CS3");
        cs3.addPrereq(cs1); cs3.addPrereq(cs2);

        Course cs4 = new Course ("CS4");
        cs4.addPrereq(cs2); cs4.addPrereq(cs3);

        Course cs5 = new Course ("CS5"); cs5.addPrereq(cs2);

        Course cs6 = new Course ("CS6");
        cs6.addPrereq(cs3); cs6.addPrereq(cs5);

        List<Course> courseList = new ArrayList<Course>();
        courseList.add(cs1); courseList.add(cs2); courseList.add(cs3);
        courseList.add(cs4); courseList.add(cs5); courseList.add(cs6);

        Graph<Course, String> graph = buildGraph(courseList);
        System.out.println(graph);

        System.out.println(createCourseList((AdjacencyMapGraph<Course, String>) graph));
    }

}
