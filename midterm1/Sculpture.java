public class Sculpture extends Art
{
    protected String material;
    protected double weight;

    public Sculpture(String name, double value, String mat, double wt)
    {
        super(name, value);
        this.material = mat;
        this.weight = wt;
    }

    public double getWeight(){return this.weight;}

    @Override
    public String toString()
    {
        String result = super.toString();
        result += " , made of " + material;
        result += " , weighs " + weight + " thousand pounds";
        return result;
    }
}
