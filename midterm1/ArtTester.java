public class ArtTester
{
    public static void main(String[] args) {
        //create gallery

        Gallery gallery = new Gallery("BigGreen Gallery",50);  //gallery name, max weight


//add art to gallery

        gallery.add(new Sculpture("The thinker",2.2,"Bronze",26.2)); //name, value, material, weight

        gallery.add(new Sculpture("David",1.7,"Marble",25.1));       //name, value, material, weight

        gallery.add(new Painting("Mona Lisa",100.5,"Portrait","Oil")); //name, value, style, paint


//print gallery and check weight

        System.out.println(gallery);

        System.out.println("Gallery passes weight check: " + gallery.checkWeight());
    }
}
