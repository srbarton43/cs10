public class GraphLibTest
{
    public static void main(String[] args) {
        Graph<String, Character> test = new AdjacencyMapGraph<String, Character>();

        test.insertVertex("A");
        test.insertVertex("B");
        test.insertVertex("C");
        test.insertVertex("D");
        test.insertVertex("E");
        test.insertDirected("A", "B", 'd');
        test.insertDirected("A", "C", 'd');
        test.insertDirected("A", "D", 'd');
        test.insertDirected("A", "E", 'd');
        test.insertDirected("B", "A", 'd');
        test.insertDirected("B", "C", 'd');
        test.insertDirected("C", "A", 'd');
        test.insertDirected("C", "B", 'd');
        test.insertDirected("C", "D", 'd');
        test.insertDirected("E", "B", 'd');
        test.insertDirected("E", "C", 'd');

        System.out.println("The graph:");
        System.out.println(test);

        System.out.println("\nNumber of InNeighbors");
        for(String node: test.vertices())
            System.out.print(node+ ": " + test.inDegree(node) +"\t");

        System.out.println("\nVertices sorted by InDegree");
        System.out.println(GraphLib.verticesByInDegree(test));

        System.out.println("\nRandom Walk:");
        System.out.println(GraphLib.randomWalk(test, "C", 2));
    }
}