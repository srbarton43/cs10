public class HuffTreeNode
{
    private char value;
    private int frequency;
    public HuffTreeNode(char v, int freq)
    {
        value = v;
        frequency = freq;
    }
    public char getValue() {return value;}
    public int getFrequency() {return frequency;}
    public void setValue(char value) {this.value = value;}
    public void setFrequency(int frequency) {this.frequency = frequency;}

    public boolean equals(HuffTreeNode other)
    {
        return (value == other.getValue())&&(frequency == other.getFrequency());
    }
    public String toString()
    {
        return "(val: " + value + ", frequency: " + frequency + ")";
    }
}
