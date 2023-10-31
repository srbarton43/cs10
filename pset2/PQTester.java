public class PQTester
{
    public static void main(String[] args) {
        /*
        Dot root = new Dot(5, 10);
        PointQuadtree<Dot> tree = new PointQuadtree<Dot>(root, 0, 0, 20, 20);
        tree.insert(new Dot (2, 5));
        tree.insert(new Dot (7, 15));
        tree.insert(new Dot (15, 5));
        tree.insert(new Dot (18, 13));


        System.out.println(tree.size());
        System.out.println(tree.allPoints());
        */
        Dot root = new Dot(6, 6);
        PointQuadtree<Dot> tree = new PointQuadtree<Dot>(root, 0, 0, 12, 12);
        tree.insert(new Dot(3,3));
        tree.insert(new Dot(9,3));
        tree.insert(new Dot(3,9));
        tree.insert(new Dot(9,9));
        System.out.println(tree.size());
        System.out.println(tree.allPoints());

        String string = "Sam";
        System.out.println(string.hashCode());
    }
}
