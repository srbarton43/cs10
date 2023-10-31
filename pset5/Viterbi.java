import java.util.*;

/**
 * includes viterbi decoding algorithm for tagging POS
 *
 * @author Sam Barton and Alexander Huang-Menders
 */
public class Viterbi {

    /**
     * viterbi algorithm to tag POS on a sentence
     *
     * @param markov the trained markov model
     * @param observations the sentence already formatted as  STRING ARRAY
     * @return returns an array of parts of speech associated with the observations
     */
    public static String[] decode(Markov markov, String[] observations) {
        Set<String> curStates = new HashSet<>();
        curStates.add("#");
        Map<String, Double> curScores = new HashMap<>();
        curScores.put("#", 0.0);
        Map<String, HashMap<String, Double>> trans = markov.getTransitions();
        List<Map<String, String>> predecessor = new ArrayList<>(observations.length); // pred of nextState @ i is curr

        for(int i = 0; i < observations.length; i++){ // loops through each observation
            String observation = observations[i];
            boolean isPunctuation = Markov.punctuation.contains(observation);
            Set<String> nextStates = new HashSet<>();
            Map<String, Double> nextScores = new HashMap<>();
            for(String curState: curStates) { // each current state
                if(trans.get(curState)==null) continue;
                for(String nextState: trans.get(curState).keySet()) { // next states from current state in transitions
                    if(!isPunctuation&&Markov.punctuation.contains(nextState)) continue;
                    nextStates.add(nextState);
                    double nextScore = curScores.get(curState) + // calculates the score from curState to nextState
                            markov.tranScore(curState, nextState) +
                            markov.obsvScore(nextState, observation);
                    if(!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)) { // should add to next scores
                        nextScores.put(nextState, nextScore);
                        Map<String, String> map;
                        if(predecessor.size() <= i) {
                            map = new HashMap<>();
                            predecessor.add(map);
                        } else map = predecessor.get(i);
                        map.put(nextState, curState); // keep track of predecessor of nextState at observation i
                    }
                }
            }
            curStates = nextStates;
            curScores = nextScores;
        }

        // backtracking process to get list:

        double maxScore = Integer.MIN_VALUE;
        String mLikelyPOS = "";
        for(String state: curStates) { // get the state with the highest score of final observation
            double score = curScores.get(state);
            if(score > maxScore) {
                maxScore = score;
                mLikelyPOS = state;
            }
        }
        String[] tagList = new String[observations.length];
        for(int i = observations.length - 1; i >= 0; i--) { // backtrack
            tagList[i] = mLikelyPOS;
            mLikelyPOS = predecessor.get(i).get(mLikelyPOS);
        }
        return tagList;
    }
}
