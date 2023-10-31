import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class BusRoute {
    private static class RouteNode implements Comparable<RouteNode>{
        private String name;
        private int distance;
        private RouteNode predecessor;

        private RouteNode(String name) {
            this.name = name;
            distance = Integer.MAX_VALUE; //init to infinity
            predecessor = null;
        }

        public int compareTo(RouteNode q2) {
            return distance-q2.distance;
        }
        public String getName() { return name; }
        public int getDistance() { return distance; }
        public RouteNode getPredecessor() { return predecessor; }
        public void setDistance(int distance) {
            this.distance=distance;
        }
        public void setPredecessor(RouteNode pred) {
            this.predecessor=pred;
        }
    }

    public static void findRoute(Graph<RouteNode, Integer> map, int penalty, RouteNode home, RouteNode work) {
        Set<RouteNode> curNodes = new HashSet<>();
        curNodes.add(home);
        home.setDistance(0);
        while(!curNodes.isEmpty()) {
            Set<RouteNode> nextNodes = new HashSet<>();
            for (RouteNode node : curNodes) {
                if(node.equals(work)) continue; // also don't backtrack from work
                for(RouteNode next: map.outNeighbors(node)) {
                    if(next.equals(node.getPredecessor())) continue; // don't backtrack or loop back
                    nextNodes.add(next);
                    int distance = node.getDistance() + penalty + map.getLabel(node, next);
                    if(distance < next.getDistance()) {
                        next.setDistance(distance);
                        next.setPredecessor(node);
                    }
                }
            }
            curNodes = nextNodes;
        }
        RouteNode node = work;
        Stack<RouteNode> stack = new Stack<>();
        stack.push(node);
        while(node.getPredecessor() != null) {
            node = node.getPredecessor();
            stack.push(node);
        }
        while(!stack.isEmpty()) { // print out path
            if(stack.peek().equals(work)) System.out.print(stack.pop().getName());
            else System.out.print(stack.pop().getName() + "->");
        }
    }

    public static void main(String [] args) {
        Graph<RouteNode, Integer> busMap =
                new AdjacencyMapGraph<RouteNode, Integer>();
        int transferPenalty = 0;

        //create RouteNodes for graph
        RouteNode Home = new RouteNode("Home");
        RouteNode B = new RouteNode("B");
        RouteNode C = new RouteNode("C");
        RouteNode D = new RouteNode("D");
        RouteNode E = new RouteNode("E");
        RouteNode Work = new RouteNode("Work");

        //add RouteNodes to graph
        busMap.insertVertex(Home);
        busMap.insertVertex(B);
        busMap.insertVertex(C);
        busMap.insertVertex(D);
        busMap.insertVertex(E);
        busMap.insertVertex(Work);

        //add edges between RouteNodes with distance as edge label
        busMap.insertUndirected(Home, B, 3);
        busMap.insertUndirected(Home, C, 5);
        busMap.insertUndirected(B, D, 2);
        busMap.insertUndirected(D, E, 1);
        busMap.insertUndirected(E, Work, 1);
        busMap.insertUndirected(C, Work, 12);
        busMap.insertUndirected(D, Work, 4);

        //TODO complete this method
        findRoute(busMap,transferPenalty,Home,Work);
    }
}