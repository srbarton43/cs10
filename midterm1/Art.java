public class Art
{
    protected String name;
    protected double priceMil;

    public Art(String name, double price)
    {
        this.name = name;
        this.priceMil = price;
    }

    public String getName() {return this.name;}
    public double getPriceMil() {return this.priceMil;}

    public String toString()
    {
        String result = name;
        result += " valued at ";
        result += "$" + priceMil + "M";
        return result;
    }
}
