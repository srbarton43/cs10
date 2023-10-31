/**
 * Class for SA1
 *
 * extends Blob and overrides the Constructor with two parameters and overrides step method
 * @author Sam Barton 09/15/2021
 */
public class SemiWanderer extends Blob
{
    protected final int maxCount;
    protected int stepCount = 0;

    /**
     * calls Blob constructor, radius is default value (5)
     * sets stepCount to random integer form 4 to 9 inclusive
     *
     * @param x initial x coordinate
     * @param y initial y coordinate
     */
    public SemiWanderer(double x, double y)
    {

        super(x, y);
        maxCount = 9 - (int) ( 6 * Math.random() ); // picks random integer between 4 and 9 inclusive
        System.out.println(stepCount);
    }

    /**
     * overrides step method in Blob.java
     * when steps between last velocity change reaches maxCount, the velocity is set to a random value
     *      with dx and dy between 1 and -1 inclusive
     */
    public void step()
    {
        stepCount++;
        if(stepCount > maxCount)
        {
            stepCount = 0;
            setVelocity( ( 2 * ( Math.random() - .5 ) ), ( 2 * ( Math.random() - .5 ) ) );
        }

        x += dx;
        y += dy;
    }

}