import java.util.*;

/**
 * @author Alexander Huang-Menders and Sam Barton, CS10, Fall 2021
 * Problem Set 4
 * Graph library for degrees of separation calculations
 */
public class GraphLibrary {

    /**
     * breadth first search of graph to generate shortest paths tree
     * @param g graph to search
     * @param source starting vertex for breadth first source
     * @param <V> vertex generic type
     * @param <E> edge generic type
     * @return graph representing a path tree
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
        Graph<V,E> pathTree = new AdjacencyMapGraph<V,E>();       //instantiate Graph to return

        Set<V> visited = new HashSet<V>();      //set to keep track visited nodes
        Queue<V>  queue = new LinkedList<>();   //queue to implement BFS

        queue.add(source); //add starting vertex to queue and mark visited
        visited.add(source);
        pathTree.insertVertex(source); //add starting vertex to graph tree

        while (!queue.isEmpty()) { //loop through all reachable vertices by removing nodes and adding neighbors
            V node = queue.remove();
            for (V neighbor : g.outNeighbors(node)) {
                if (!visited.contains(neighbor)) { //add neighboring nodes to visited and queue if not visited
                    visited.add(neighbor);
                    queue.add(neighbor);
                    pathTree.insertVertex(neighbor);
                    pathTree.insertDirected(neighbor, node, null);
                }
            }
        }

        return pathTree;
    }

    /**
     * @param tree graph representing shortest paths tree
     * @param v starting vertex
     * @return list representing shortest path from vertex towards "center of universe"
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
        List<V> path = new ArrayList<V>();     //list to keep track of path
        path.add(v);

        Iterator<V> iterator = tree.outNeighbors(v).iterator(); //iterator to find parent vertex
        V node = v;
        while(true) {       //loops up through the tree until the root vertex is found
            if (iterator.hasNext()) {
                node = iterator.next();
                path.add(node); //adds parent vertex to path
                iterator = tree.outNeighbors(node).iterator();  //update outNeighbors iterator
            }
            else {
                break;
            }
        }

        return path;
    }

    /**
     * @param graph
     * @param subgraph subgraph of graph
     * @return set of vertices in graph not reachable in subgraph
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        Set<V> subVerts = new HashSet<V>();     //set of vertices in subgraph
        Set<V> missing = new HashSet<V>();      //set of all vertices in graph
        for (V v : subgraph.vertices()) {       //fill subVerts set
            subVerts.add(v);
        }
        for (V v : graph.vertices()) {      //for each vertex in graph, if not contained in subgraph, add to missing
            if (!subVerts.contains(v)) missing.add(v);
        }
        return missing;
    }

    /**
     * calculates average
     * @param tree graph representing shortest paths tree
     * @param root root node of tree
     * @return average path length representing average degrees of separation
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
        int totalPathLengths = pathLengthsRec(tree, root, 0); //total length of all paths
        int numChildren = tree.numVertices()-1; //number of children vertices in the tree
        return ((double) totalPathLengths)/((double) numChildren); //average path length
    }

    /**
     * Helper method to handle averageSeparation recursion
     * loops through tree and calculates total length of all paths
     */
    private static <V,E> int pathLengthsRec(Graph<V,E> tree, V node, int pathLength) {
        int length = pathLength;    //returns path length of this node plus path length of all children nodes

        for (V v : tree.inNeighbors(node)) {
            length+= pathLengthsRec(tree, v, pathLength+1);
        }
        return length;
    }

}
