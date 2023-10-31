/** Demonstrate memory allocation for objects
 * 
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019
 *
 */
public class MemoryAllocationObjects {

	public static void main(String[] args) {
		//declare Blob objects
		Blob0 alice = new Blob0();
		Blob0 bob; //notice no new keyword
		bob = alice; //bob equals alice
		Blob0 charlie = new Blob0();
		System.out.println("alice.x="+alice.x+
				" bob.x="+bob.x);
		
		//update alice's x
		alice.setX(3);
		System.out.println("alice.x="+alice.x+
				" bob.x="+bob.x);
		
		//printing objects implicitly calls toString()
		System.out.println("alice="+alice+
				" bob="+bob+" charlie="+charlie);
	}
}
