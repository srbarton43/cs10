import java.util.ArrayList;

public class Gallery
{
    ArrayList<Art> gallery;
    protected String name;
    protected double maxWeight;

    public Gallery(String name, double maxWeight)
    {
        this.name = name;
        this.maxWeight = maxWeight;
        gallery = new ArrayList<Art>();
    }

    public void add(Art art)
    {
        gallery.add(art);
    }

    public void remove(Art art)
    {
        for(Art cur: gallery)
        {
            if(cur.getName().equals(art.getName())) gallery.remove(cur);
        }
    }

    public boolean checkWeight()
    {
        double sum = 0;
        for(Art art: gallery)
        {
            try
            {
                sum += ((Sculpture)art).getWeight();
            }
            catch (Exception e)
            {

            }
        }
        return sum < maxWeight;
    }

    public String toString()
    {
        String result = name + " Art";
        for(Art art: gallery)
            result += "\n" + art.toString();
        return result;
    }
}
