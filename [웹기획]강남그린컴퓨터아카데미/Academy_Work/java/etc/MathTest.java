class MathTest {
	public static void main(String[] args) {

		System.out.println("¿øÁÖÀ² : " + Math.PI);
		System.out.println("5ÀÇ Á¦°ö±Ù : " + Math.sqrt(5));
		System.out.println("·Î±× 25 : " + Math.log(25));
		System.out.println("2ÀÇ 4Á¦°ö : " + Math.pow(2, 4));

		System.out.println();
		double radian45 = Math.toRadians(45);
		System.out.println("½ÎÀÎ 45 : " + Math.sin(radian45));
		System.out.println("ÄÚ½ÎÀÎ 45 : " + Math.cos(radian45));
		System.out.println("ÅºÁ¨Æ® 45 : " + Math.tan(radian45));

	}
}