import java.util.Comparator;

public class InNeighborsComparator implements Comparator {
    Graph g;
    public InNeighborsComparator(Graph g) {
        this.g = g;
    }
    public int compare(Object o1, Object o2) {
        return g.inDegree(o2) - g.inDegree(o1);
    }
}
