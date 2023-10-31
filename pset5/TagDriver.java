import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * driver class for pset5
 * implements concole functionality to test viterbi tagging
 *
 * @author Sam Barton and Alexander Huang-Menders
 */
public class TagDriver {
    private final Markov markov;

    // creates and trains new markov model with training pathnames as parameters
    public TagDriver(String sentencesPathname, String tagsPathname) {
        markov = new Markov();
        train(sentencesPathname, tagsPathname);
    }

    /**
     * uses files to train markov model
     *
     * @param sentencesPathname pathname for sentences
     * @param tagsPathname pathname for tags
     */
    public void train(String sentencesPathname, String tagsPathname) {
        HashMap<String, HashMap<String, Integer>> transitionTable = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> observationTable = new HashMap<>();

        // read in training files
        BufferedReader sentenceReader = null;
        BufferedReader tagsReader = null;
        try {
            sentenceReader = new BufferedReader(new FileReader(sentencesPathname));
            tagsReader = new BufferedReader(new FileReader(tagsPathname));
        } catch(FileNotFoundException e) {System.out.println(e);}

        // now read files line by line
        while(true) {
            String sentenceLine = "";
            String tagsLine = "";
            try {
                sentenceLine = sentenceReader.readLine();
                tagsLine = tagsReader.readLine();
                if(sentenceLine==null||tagsLine==null) break;
            } catch(IOException e) {System.out.println(e);}
            sentenceLine = sentenceLine.toLowerCase();
            String[] observations = sentenceLine.split(" "); // creates the arrays of observations in the line
            String[] tags = tagsLine.split(" "); // creates the arrays of tags in the line

            // build the tables
            for(int i = 0; i < observations.length; i++) { //DELETE loop currently includes the period in observations
                String curPos, nextPos, observation; // current part of speech, next POS, current observation
                if(i == 0) curPos = "#";
                else curPos = tags[i-1];
                nextPos = tags[i];
                observation = observations[i];
                // tags
                if (!transitionTable.containsKey(curPos)) transitionTable.put(curPos, new HashMap<>());
                Map<String, Integer> curTransRow = transitionTable.get(curPos);
                if (!curTransRow.containsKey(nextPos)) curTransRow.put(nextPos, 1); // if not transitions yet for curPOS
                else curTransRow.put(nextPos, curTransRow.get(nextPos) + 1); // else increment count

                // observations
                if (!observationTable.containsKey(nextPos)) observationTable.put(nextPos, new HashMap<>());
                Map<String, Integer> curObsvRow = observationTable.get(nextPos);
                if (!curObsvRow.containsKey(observation))
                    curObsvRow.put(observation, 1); // if not observations yet for curPOS
                else curObsvRow.put(observation, 1 + curObsvRow.get(observation)); // else increment count
            }
        }
        try {
            sentenceReader.close();
            tagsReader.close();
        } catch (IOException e) {System.out.println(e);}
        markov.train(transitionTable, observationTable);
    }

    /**
     * tests accuracy of viterbi tagging algorithm on test file pair
     *
     * @param sentencePath pathname for sentences file
     * @param tagPath pathname for tags file
     * @return returns the percentage of correct tags for the test file
     */
    public double fileTest (String sentencePath, String tagPath) {
        BufferedReader sentenceReader = null;
        BufferedReader tagsReader = null;
        try {
            sentenceReader = new BufferedReader(new FileReader(sentencePath));
            tagsReader = new BufferedReader(new FileReader(tagPath));
        } catch(FileNotFoundException e) {System.out.println(e); return -1.0;}

        int correct = 0;
        int total = 0;
        while(true) {
            String sentenceLine = "";
            String tagsLine = "";
            try {
                sentenceLine = sentenceReader.readLine();
                tagsLine = tagsReader.readLine();
                if (sentenceLine == null || tagsLine == null) break;
            } catch (IOException e) {
                System.out.println(e);
            }
            sentenceLine = sentenceLine.toLowerCase();
            String[] tags = tagsLine.split(" ");
            String[] results = Viterbi.decode(markov, sentenceLine.split(" "));
            for (int i = 0; i < results.length; i++) {
                total++;
                if (tags[i].equals(results[i])) correct++;
                //else System.out.println(tags[i] + "|" + results[i] + "  ");
            }
        }
        System.out.println("Correct: " + correct + " Total: " + total);
        try {
            sentenceReader.close();
            tagsReader.close();
        } catch (IOException e) {System.out.println(e);}
        return (double)correct/(double)total;
    }

    public static void main(String[] args) {
        TagDriver driver = new TagDriver("inputs/pset5/brown-train-sentences.txt",
                "inputs/pset5/brown-train-tags.txt"); // training data as parameters

        System.out.println("Type a sentence to tag Parts of Speech; q to quit; t to enter files for testing");
        while(true) { // command line functionality
            Scanner input = new Scanner(System.in);
            String line = input.nextLine();
            if(line.equals("q")) break;
            else if(line.equals("t")) { // file based test method
                System.out.print("Enter sentences pathname: ");
                String sentencePath = input.nextLine();
                System.out.print("Enter tags pathname: ");
                String tagsPath = input.nextLine();
                double accuracy = driver.fileTest(sentencePath, tagsPath);
                if(!(accuracy==-1.0))
                    System.out.println("Markov is " + (((int)(100000*accuracy))/(double)1000) + "% accurate for  test data");
            }
            else { // enter your own sentence
                line = line.toLowerCase();
                String[] lineArr = line.split(" ");
                String[] tags = Viterbi.decode(driver.markov, lineArr);
                List<String> formatter = new ArrayList<>();
                for (int i = 0; i < tags.length; i++) { // formatting stuff
                    formatter.add(lineArr[i]);
                    formatter.add("/");
                    formatter.add("[" + tags[i] + "] ");
                }
                String taggedSentence = "";
                for (String s : formatter) taggedSentence += s;
                System.out.println(taggedSentence);
            }
        }

    }
}
