import java.util.HashMap;

public class Midterm1Test
{


    public static void main(String[] args) throws Exception {
/*
        SimpleList<Integer> list = new GrowingArray1<Integer>();
        list.add(0,1);
        list.add(1,3);
        list.add(2,-4);
        System.out.println(Midterm1Test.findMinIndex(list));


        SimpleList<Integer> list1 = new GrowingArray1<>();
        list1.add(0,1);
        list1.add(0,2);
        list1.add(0,3);
        list1.add(0,4);
        list1.add(0,5);
        list1.add(0,6);
        list1.add(0,7);
        System.out.println(list1);
*/
        //SearchAndRescue bob = new SearchAndRescue(5, 5);
        //System.out.print(bob.toString());

        HashMap<String, String> map = new HashMap<>();
        System.out.println(map.containsKey("Sam"));
    }

    /**
     * @param list SimpleList of Integer values
     * @return index of item with smallest value
     *
     * @throws Exception if list is empty
     */
    public static Integer findMinIndex(SimpleList<Integer> list) throws Exception
    {
        if(list.size()<1) throw new Exception();
        int minIndex = 0;
        for(int i = 0; i < list.size(); i++)
        {
            int cur = list.get(i);
            if(cur < list.get(minIndex)) minIndex = i;

        }
        return minIndex;
    }


}

