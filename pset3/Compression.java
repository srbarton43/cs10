import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Compression
{
    HashMap<Character, Integer> freqTable;
    HashMap<Character, String> huffTable;
    BinaryTree<HuffTreeNode> huffTree;

    public HashMap<Character, Integer> getFreqTable(){return freqTable;}
    public BinaryTree<HuffTreeNode> getHuffTree(){return huffTree;}

    public HashMap<Character, String> getHuffTable() {return huffTable;}

    public BufferedReader setInput(String filename)
    {
        BufferedReader input = null;
        try
        {
            input = new BufferedReader(new FileReader(filename));
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        return input;
    }
    /**
     * creates a frequency table for each character in parameter file
     * assigns table to instance variable
     */
    public void createFrequencies(BufferedReader input)
    {
        freqTable = new HashMap<Character, Integer>();
        while(true)
        {
            int i=-1;
            try // try reading the next char of the file
            {
                i = input.read();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
            if(i==-1) break;
            char c = (char)i;
            if(!freqTable.containsKey(c)) freqTable.put(c, 1); // if this is first instance of char, add it to table with freq of 1
            else freqTable.replace(c, freqTable.get(c)+1); // else increment the existing frequency for the char
        }
        try {input.close();}
        catch(IOException e){System.out.println(e);}
    }

    /**
     * creates the Huffman tree of HuffTreeNodes and sets it to instance variable
     */
    public void createHuffTree()
    {
        PriorityQueue<BinaryTree<HuffTreeNode>> queue = new PriorityQueue<BinaryTree<HuffTreeNode>>(new TreeComparator());
        for(char c: freqTable.keySet())
            queue.add(new BinaryTree<HuffTreeNode>(new HuffTreeNode(c, freqTable.get(c)))); // adds single node trees to
        while(queue.size()>1)
        {
            BinaryTree<HuffTreeNode> t1 = queue.poll(); // lowest priority
            BinaryTree<HuffTreeNode> t2 = queue.poll(); // second-lowest priority
            BinaryTree<HuffTreeNode> t = new BinaryTree<HuffTreeNode>(new HuffTreeNode('x', -1), t1, t2); // interior node
            t.getData().setFrequency(t1.getData().getFrequency()+t2.getData().getFrequency()); // sets frequency to sum of frequencies
            queue.add(t); // adds t to PQueue
        }
        huffTree = queue.poll(); // sets huffTree to last element in PQueue
        if(!huffTree.hasLeft()&&!huffTree.hasRight())
            huffTree = new BinaryTree<HuffTreeNode>(new HuffTreeNode('x', 0), huffTree, null);
    }

    /**
     * creates table with characters and bitCode
     */
    public void createHuffTable()
    {
        huffTable = new HashMap<>();
        createTableHelper(huffTree, "");
    }

    /**
     * private helper method for createHuffTable
     *
     * @param node current treeNode
     * @param path current path
     */
    private void createTableHelper(BinaryTree<HuffTreeNode> node, String path)
    {
        if(!node.hasRight()&&!node.hasLeft())
            huffTable.put(node.data.getValue(), path);
        if(node.hasLeft()) createTableHelper(node.getLeft(), path+"0");
        if(node.hasRight()) createTableHelper(node.getRight(), path+"1");
    }
    public void compress(String inptPathname, String otptPathname)
    {
        BufferedReader input = setInput(inptPathname);
        createFrequencies(input);
        if(freqTable.size()<1)
        {
            System.out.println("eh");
        }
        createHuffTree();
        createHuffTable();
        input = setInput(inptPathname);
        BufferedBitWriter output = null;
        try
        {
            output = new BufferedBitWriter(otptPathname);
        }
        catch(IOException e) {System.out.println(e);}
        while(true) {
            int i = -1;
            try // try reading the next char of the file
            {
                i = input.read();
            } catch (IOException e) {
                System.out.println(e);
            }
            if (i == -1) break;
            char c = (char) i;
            String bitString = huffTable.get(c);
            for (char c1 : bitString.toCharArray()) {
                try {
                    if (c1 == '0') output.writeBit(false);
                    else output.writeBit(true);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        try
        {
            output.close();
            input.close();
        } catch(IOException e) {System.out.println(e);}
    }
    public void decompress(String inputFileName, String outputFileName) {
        BufferedBitReader input = null;
        try {
            input = new BufferedBitReader(inputFileName);
        }
        catch(IOException e) {
            System.out.println(e);
        }

        boolean bit = false;
        BinaryTree<HuffTreeNode> currNode = huffTree;
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(outputFileName));
        }
        catch(IOException e) {
            System.out.println(e);
        }
        while (input.hasNext()) {
            try {
                bit = input.readBit();
            }
            catch(IOException e) {
                System.out.println(e);
                break;
            }
            if (!bit) currNode = currNode.getLeft();
            else currNode = currNode.getRight();
            if (!currNode.hasLeft() && !currNode.hasRight()) {
                try {
                    output.write(currNode.getData().getValue());
                }
                catch(IOException e) {
                    System.out.println(e);
                }
                currNode = huffTree;
            }
        }
        try{                                //close input and output for storage
            output.close();
            input.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }
}
