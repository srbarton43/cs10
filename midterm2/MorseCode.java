import java.util.HashMap;

public class MorseCode {
    BinaryTree<String> root;

    public MorseCode() {
        root = new BinaryTree<String>(null);
    }

    public void addCode(Character c, String code) {
        BinaryTree<String> cur = root;
        for(char ch: code.toCharArray()) {
            if(ch == '.') {
                if(!cur.hasLeft()) cur.setLeft(new BinaryTree<String>(null));
                cur = cur.getLeft();
            }
            else {
                if (!cur.hasRight()) cur.setRight(new BinaryTree<String>(null));
                cur = cur.getRight();
            }
        }
        cur.setData(c.toString());
    }

    public char getCharacter(String code) throws Exception{
        char ch;
        BinaryTree<String> cur = root;
        for(char c: code.toCharArray()) {
            if(c == '.') {
                if(!cur.hasLeft()) throw new Exception();
                else cur = cur.getLeft();
            } else {
                if(!cur.hasRight()) throw new Exception();
                else cur = cur.getRight();
            }
        }
        if(cur.getData() == null) throw new Exception();
        ch = cur.getData().charAt(0);
        return ch;
    }

    public HashMap<Character, String> getCodeMap(char c) throws Exception {
        if(root.isLeaf()) throw new Exception();
        HashMap<Character, String> codeMap = new HashMap<>();
        String code = "";
        recHelp(codeMap, root, "");
        return codeMap;
    }
    private void recHelp(HashMap<Character, String> map, BinaryTree<String> node, String code) {
        if(node.getData() != null) map.put(node.getData().charAt(0), code);
        if(node.hasLeft()) recHelp(map, node.getLeft(), code+".");
        if(node.hasRight()) recHelp(map, node.getRight(), code+"-");
    }
}
