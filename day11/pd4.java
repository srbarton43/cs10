import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class pd4
{
    private Map<String, ArrayList<String>> students;

    public pd4(String name)
    {
        students = new TreeMap<String, ArrayList<String>>();
        students.put(name, new ArrayList<>());
    }

    public static void main(String[] args) {
        pd4 c1 = new pd4("Sam Barton");
        c1.students.get("Sam Barton").add("CS10");
        System.out.println(c1.students);
    }
}
