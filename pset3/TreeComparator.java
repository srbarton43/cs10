import java.util.Comparator;

public class TreeComparator implements Comparator<BinaryTree<HuffTreeNode>>
{
    public int compare(BinaryTree<HuffTreeNode> a, BinaryTree<HuffTreeNode> b)
    {
        if(a.getData().getFrequency()>b.getData().getFrequency()) return 1;
        if(a.getData().getFrequency()<b.getData().getFrequency()) return -1;
        return 0;
    }
}
