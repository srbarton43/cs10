public class Painting extends Art
{
    protected String paintType, paintStyle;

    public Painting(String name, double value, String paintStyle, String paintType)
    {
        super(name, value);
        this.paintStyle = paintStyle;
        this.paintType = paintType;
    }

    @Override
    public String toString()
    {
        String result = super.toString();
        result += " , style " + paintStyle;
        result += " , type " + paintType;
        return result;
    }
}
