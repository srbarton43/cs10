import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Alexander Huang-Menders and Sam Barton, CS10, Fall 2021
 * Kevin Bacon degrees of separation game
 */
public class Bacon {
    private static HashMap<Integer,String> actors;          //hashmaps to keep store actor id and name
    private static HashMap<Integer,String> movies;          //map to store movie id and name
    private static HashMap<String, ArrayList<String>> actorMovies; //map to store movie name and actors names in it
    private static Graph<String, Set<String>> graph; //relationship graph <movie name, set<actors names>>

    private static String center;
    private static Graph<String, Set<String>> bfs;
    private static double avgSeparation;
    private static ArrayList<String> sortedAvg;
    private static HashMap<String, Double> avgSepMap;
    private static ArrayList<String> sortedDeg;
    private static HashMap<String,Integer> degMap;
    /*
    Commands:
    c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation
    d <low> <high>: list actors sorted by degree, with degree between low and high
    i: list actors with infinite separation from the current center
    p <name>: find path from <name> to current center of the universe
    s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high
    u <name>: make <name> the center of the universe
    q: quit game
     */
    public static void main(String[] args) {
        buildGraph();
        Scanner input = new Scanner(System.in);

        updateCenter("Kevin Bacon");


        while (true) {
            String command = input.nextLine();
            if (command.equals("c")) {
                System.out.print("Top actors by average separation (bottom if negative): ");
                try {
                    int num = Integer.parseInt(input.nextLine());
                    System.out.println(topAvgToStr(topAvg(num)));
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input - number required");
                }
            }
            else if (command.equals("d")) {
                System.out.print("Enter low: ");
                int low = 0;
                try {
                    low = Integer.parseInt(input.nextLine());
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input - number required");
                    continue;
                }
                System.out.print("Enter high: ");
                int high = 0;
                try {
                    high = Integer.parseInt(input.nextLine());
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input - number required");
                    continue;
                }
                if (low > high) continue;
                ArrayList<String> degList = new ArrayList<String>();
                for (String actor : sortedDeg) {
                    if (degMap.get(actor) >= low && degMap.get(actor) <= high) {
                        degList.add(actor);
                    }
                }
                System.out.println(degList);
            }
            else if (command.equals("i")) {
                Set<String> unreachable = new HashSet<String>();
                for (String actor : graph.vertices()) {
                    unreachable.add(actor);
                }
                for (String actor : bfs.vertices()) {
                    unreachable.remove(actor);
                }
                System.out.println(unreachable);
            }
            else if (command.equals("p")) {
                System.out.print("Enter actor name: ");
                String name = input.nextLine();
                if (!graph.hasVertex(name)) {
                    System.out.println("Invalid actor name");
                }
                else if (!bfs.hasVertex(name)) {
                    System.out.println("No path exists");
                }
                else {
                    ArrayList<String> path = (ArrayList<String>) GraphLibrary.getPath(bfs, name);
                    System.out.println(name + "'s number is " + (path.size()-1));
                    for (int i = 0; i < path.size()-1; i++) {
                        String a1 = path.get(i);
                        String a2 = path.get(i+1);
                        System.out.println(a1 + " appeared in " + graph.getLabel(a1, a2) + " with " + a2);
                    }
                }
            }
            else if (command.equals("s")) {
                System.out.print("Enter low: ");
                int low = 0;
                try {
                    low = Integer.parseInt(input.nextLine());
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input - number required");
                    continue;
                }
                System.out.print("Enter high: ");
                int high = 0;
                try {
                    high = Integer.parseInt(input.nextLine());
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input - number required");
                    continue;
                }
                if (low > high) continue;
                ArrayList<String> valid = new ArrayList<String>();
                separationRecursion(center, valid, 0, low, high);
                System.out.println(valid);
            }
            else if (command.equals("u")) {
                System.out.print("Enter actor name: ");
                String name = input.nextLine();
                if (!graph.hasVertex(name)) {
                    System.out.println("Invalid actor name");
                }
                else {
                    updateCenter(name);
                }
            }
            else if (command.equals("q")) {
                System.out.println("Thank you for playing");
                break;
            }
            else {
                System.out.println("Invalid Command");
            }
        }
    }

    private static void updateCenter(String name) {
        center = name;
        bfs = GraphLibrary.bfs(graph, center);
        avgSeparation = GraphLibrary.averageSeparation(bfs, center);

        int num = graph.numVertices();
        int numConnected = bfs.numVertices();
        System.out.println(center + " is the center of the acting universe, connected to " + numConnected + "/" +
                num + " actors with average separation " + avgSeparation);
    }


    private static void buildGraph() {
        System.out.println("Processing Data...");
        actors = new HashMap<Integer,String>();
        movies = new HashMap<Integer,String>();
        actorMovies = new HashMap<String,ArrayList<String>>();
        graph = new AdjacencyMapGraph<String, Set<String>>();
        sortedAvg = new ArrayList<String>();
        sortedDeg = new ArrayList<String>();

        BufferedReader actorReader = null;
        BufferedReader movieReader = null;
        BufferedReader actorMovieReader = null;
        try {
            actorReader = new BufferedReader(new FileReader("inputs/actors.txt"));
            movieReader = new BufferedReader(new FileReader("inputs/movies.txt"));
            actorMovieReader = new BufferedReader(new FileReader("inputs/movie-actors.txt"));
        }
        catch(FileNotFoundException e) {
            System.out.println(e);
        }

        //reads and stores actor id and name data
        while (true) {
            try {
                String line = actorReader.readLine();
                if (line == null) break;
                String[] split = line.split("\\|");
                actors.put(Integer.parseInt(split[0]), split[1]);
                graph.insertVertex(split[1]);
                sortedAvg.add(split[1]);
                sortedDeg.add(split[1]);
            }
            catch (IOException e) {System.out.println(e);}
        }

        //reads and stores movie id and name data in movies HashMap
        while (true) {
            try {
                String line = movieReader.readLine();
                if (line == null) break;
                String[] split = line.split("\\|");
                movies.put(Integer.parseInt(split[0]), split[1]);
            }
            catch (IOException e) {System.out.println(e);}
        }

        //reads and stores movie names and actors names in the actorMovies HashMap
        while (true) {
            try {
                String line = actorMovieReader.readLine();
                if (line == null) break;
                String[] split = line.split("\\|");
                int movieID = Integer.parseInt(split[0]);
                int actorID = Integer.parseInt(split[1]);
                if (actorMovies.containsKey(movies.get(movieID))) {
                    actorMovies.get(movies.get(movieID)).add(actors.get(actorID));
                }
                else {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(actors.get(actorID));
                    actorMovies.put(movies.get(movieID), list);
                }
            }
            catch (IOException e) {System.out.println(e);}
        }

        System.out.println("Building Costar Graph...");
        //create graph of actor relations with actor names as vertices and sets of all shared movies as edges
        Set<Map.Entry<String,ArrayList<String>>> entrySet = actorMovies.entrySet();
        for (Map.Entry<String,ArrayList<String>> entry : entrySet) {
            ArrayList<String> names = entry.getValue();
            for (int i = 0; i < names.size(); i++) {
                for (int x = i+1; x < names.size(); x++) {
                    if (!graph.hasEdge(names.get(i), names.get(x))) {
                        graph.insertUndirected(names.get(i), names.get(x), new HashSet<String>());
                    }
                    Set set = graph.getLabel(names.get(i), names.get(x));
                    set.add(entry.getKey());
                }
            }
        }

        System.out.println("Analyzing graph...");
        //generate avgSepMap and sortedAvg
        avgSepMap = new HashMap<String,Double>();
        for (String actor : sortedAvg) {
            avgSepMap.put(actor, GraphLibrary.averageSeparation(GraphLibrary.bfs(graph,actor), actor));
        }
        sortedAvg.sort((String a1, String a2) -> Double.compare(avgSepMap.get(a1),avgSepMap.get(a2)));

        //generate sortedDegree
        degMap = new HashMap<String,Integer>();
        for (String actor : sortedDeg) {
            degMap.put(actor, graph.outDegree(actor));
        }
        sortedDeg.sort((String a1, String a2) -> degMap.get(a1) - degMap.get(a2));
    }

    private static void separationRecursion (String node, ArrayList<String> valid, int separation, int low, int high) {
        if (separation >= low && separation <= high) {
            valid.add(node);
        }
        for (String out : bfs.inNeighbors(node)) {
            if (separation != high) separationRecursion(out, valid, separation+1, low, high);
        }
    }
    private static List<String> topAvg(int num) {
        if(Math.abs(num)>sortedAvg.size()||num==0) return null;
        List<String> list;
        if(num>0) {
            list = sortedAvg.subList(0, num);
        } else {
            list = sortedAvg.subList(sortedAvg.size()+num, sortedAvg.size());
            Collections.reverse(list);
        }
        return list;
    }
    private static String topAvgToStr(List<String> list) {
        String str = "[";
        for(String actor: list) {
            str+="(" + actor + ", " + avgSepMap.get(actor) + "), ";
        }
        str = str.substring(0, str.length()-2);
        return str+"]";
    }
}
