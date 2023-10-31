import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class RecommendationEng {

    private HashMap<String, HashSet<String>> moviesWatched;

    public RecommendationEng(String filename) {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(filename));
        }
        catch(IOException e) {
            System.out.println(e);
        }
        String line = "";
        while(!line.equals(null))
        {
            String name = "";
            try {
                line = input.readLine();
                int i = line.indexOf(':');
                name = line.substring(0, i); // gets the name
                HashSet<String> movies = new HashSet<>();
                while(line.indexOf(',', i+1) != -1) { // terminate when no more commas past the previous comma
                    movies.add(line.substring(i+1, line.indexOf(',', i+1)));
                    i = line.indexOf(',', i+1);
                }
                moviesWatched.put(name, movies);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public double similarity(String personA, String personB) {
        double sim = 0;
        if(moviesWatched.get(personA).isEmpty()&&moviesWatched.get(personA).isEmpty()) return sim; //if union is zero
        double numerator = 0, denominator = 1;

        HashSet<String> pb = moviesWatched.get(personB);
        for(String movie: moviesWatched.get(personA)) {
            if (pb.contains(movie)) numerator++;
        }
        denominator = moviesWatched.get(personA).size() + pb.size();
        sim =  numerator/denominator;
        return sim;
    }

    private class Jacobian {
        protected String name;
        protected double jacobian;
        public Jacobian(String name, double jacobian) {
            this.name = name;
            this.jacobian = jacobian;
        }
    }
    private class JacobianComp implements Comparator<Jacobian> {

        public int compare(Jacobian o1, Jacobian o2) {
            double x = o1.jacobian-o2.jacobian;
            if(x>1) return 1;
            if(x<1) return -1;
            return 0;
        }
    }

    public void reccomended(String name, int k) {
        HashSet<String> movies = new HashSet<>();
        ArrayList<Jacobian> people = new ArrayList<>();
        for(String cName: moviesWatched.keySet()) {
            if(!cName.equals(name)) {
                people.add(new Jacobian(cName, similarity(name, cName)));
            }
        }
        people.sort(new JacobianComp());
        int i = 0;
        while(movies.size() <= 10 && i < people.size()) {
            for(String movie: moviesWatched.get(people.get(i))) {
                movies.add(movie);
            }
            i++;
        }
        String s = "Movies to watch: ";
        for(String movie: movies) {
            s += movie + ", ";
        }

        s = s.substring(0, s.length()-2); // get rid of last comma just cuz
        System.out.println(s);
    }

}
