public class BlobAlias {
	public static void main(String[] args) {
		Blob0 b1 = new Blob0();
		Blob0 b2 = new Blob0();
		Blob0 b3 = b1;		// same object!
		b1.setX(3);
		System.out.println(b3.getX()); // => prints 3
		System.out.println("b1:" + b1);
		System.out.println("b2:" + b2);
		System.out.println("b3:" + b3);
	}
}
