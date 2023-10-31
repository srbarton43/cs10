import java.util.*;

/**
 * Library for graph analysis
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * 
 */
public class GraphLib {
	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		ArrayList<V> walk = new ArrayList<>();
		walk.add(start);
		randomWalkHelper(g, start, steps-1, walk);
		return walk;
	}
	private static <V,E> void randomWalkHelper(Graph<V,E> g, V node, int steps, ArrayList<V> list) {
		if(steps<0||g.outDegree(node)<1) return;
		int rand = (int)(Math.random() * g.outDegree(node));
		int x = 0;
		for(V cur: g.outNeighbors(node)) {
			if(x==rand) {
				list.add(cur);
				randomWalkHelper(g,cur,steps-1, list);
			}
			x++;
		}
	}
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		ArrayList<V> list = new ArrayList<>();
		for(V node: g.vertices()) list.add(node);
		list.sort(new InNeighborsComparator(g));
		return list;
	}
}
