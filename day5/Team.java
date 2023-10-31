public class Team
{
    private String mName;
    private int curScore;

    public Team(String name)
    {
        mName = name;
        curScore = 0;
    }

    public String getName()
    {
        return mName;
    }

    public int getScore()
    {
        return curScore;
    }

    public void score()
    {
        curScore+=2;
    }

    public static void main(String[] args)
    {
        Team wildcat = new Team("Wildcat");
        Team eagle = new Team("Eagle");

        wildcat.score();

        eagle.score();
        eagle.score();

        if(eagle.getScore() > wildcat.getScore())
        {
            System.out.println(eagle.getName());
        }
        else if(eagle.getScore() < wildcat.getScore())
        {
            System.out.println(wildcat.getName());
        }
        else
        {
            System.out.println("tie");
        }
    }
}
