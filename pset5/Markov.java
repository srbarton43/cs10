import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * object which represents Markov Model using map of transitions and observations
 *
 * @author Sam Barton and Alexander Huang-Menders
 */
public class Markov {
    private static final double UNSEEN_PEN = -15.5; // score if unobserved word+POS pairing
    private final HashMap<String, HashMap<String, Double>> transitions;
    private final HashMap<String, HashMap<String, Double>> observations;
    public static String[] POS = {"#", "ADJ", "ADV", "CNJ", "DET", "EX", "FW", "MOD", "N", "NP", "NUM", "PRO",
            "P", "TO", "UH", "V", "VD", "VG", "VN", "WH", "VBZ", "*",
            ".", ",", "?", "!", "'", "''", "``", "-", "--", //17 punctuation regexes
            ":", ";", "(", ")", "[", "]", "...", "/"};
    public static Set<String> punctuation = Set.of(".", ",", "?", "!", "'", "''", "``", "-", "--",
            ":", ";", "(", ")", "[", "]", "...", "/");
    public Markov() {
        transitions = new HashMap<>();
        observations = new HashMap<>();
    }
    public HashMap<String, HashMap<String, Double>> getObservations() {return observations;}
    public HashMap<String, HashMap<String, Double>> getTransitions() {return transitions;}
    // returns transition score
    public double tranScore(String curState, String nextState) { // returns score for a transition between POSs
        return transitions.get(curState).get(nextState);
    }
    // returns observation score
    public double obsvScore(String pos, String observation) {
        if(observations.containsKey(pos)&&observations.get(pos).containsKey(observation))
            return observations.get(pos).get(observation);
        return UNSEEN_PEN;
    }

    /**
     * creates MM from preformatted training data (we think of data as in tables)
     *
     * @param trans transitions table
     * @param obsv observations table
     */
    public void train(HashMap<String, HashMap<String, Integer>> trans, HashMap<String, HashMap<String, Integer>> obsv) {
        for(int r = 0; r < POS.length; r++) {
            HashMap<String,Integer> row = trans.get(POS[r]); // current 'row' in transition 'table'
            if (row == null) continue; //protect from null row
            int total = 0;
            Set<String> poses = row.keySet();
            for (String pos : poses) total += row.get(pos); // calculates total occurrences in row
            HashMap<String,Double> map = new HashMap<>();
            for (String pos : poses) { // calculates weight for each transition
                map.put(pos, Math.log((double)row.get(pos)/(double)total));
            }
            transitions.put(POS[r], map); // adds row with weights to transitions map
        }

        for(int r = 1; r < POS.length; r++) { // 1 to 19 cuz no '#'
            HashMap<String, Integer> row = obsv.get(POS[r]); // current 'row' in transition 'table'
            if (row == null) continue; //protect from null row
            int total = 0;
            Set<String> words = row.keySet();
            for (String word : words) total += row.get(word); // calculates total occurrences in row
            HashMap<String,Double> map = new HashMap<>();
            for (String word : words) { // calculates weight for each observation
                map.put(word, Math.log((double)row.get(word)/(double)total));
            }
            observations.put(POS[r], map); // adds row with weights to observations map
        }
    }

    public static void main(String[] args) {
        Markov test = new Markov(); // testing using hard-build MM
        test.transitions.put("#", new HashMap<>());
        test.transitions.get("#").put("N", 2.0);
        test.transitions.get("#").put("NP", 4.0);
        test.transitions.put("N", new HashMap<>());
        test.transitions.get("N").put("V", 3.0);
        test.transitions.get("N").put("VD", 2.0);
        test.transitions.put("NP", new HashMap<>());
        test.transitions.get("NP").put("V", 3.0);
        test.transitions.get("NP").put("VD", 2.0);
        test.transitions.put("V", new HashMap<>());
        test.transitions.get("V").put("N", 3.0);
        test.transitions.get("V").put("ADJ", 2.0);
        test.transitions.put("VD", new HashMap<>());
        test.transitions.get("VD").put("N", 3.0);
        test.transitions.get("VD").put("ADJ", 2.0);

        test.observations.put("NP", new HashMap<>());
        test.observations.get("NP").put("bob", 30.0);
        test.observations.put("PRO", new HashMap<>());
        test.observations.get("PRO").put("he", 30.0);

        String[] sentence = {"bob", "has", "covid-19"};
        System.out.println(Arrays.toString(Viterbi.decode(test, sentence)));
        sentence = new String[]{"he", "had", "cold", "feet"};
        System.out.println(Arrays.toString(Viterbi.decode(test, sentence)));

    }
}
