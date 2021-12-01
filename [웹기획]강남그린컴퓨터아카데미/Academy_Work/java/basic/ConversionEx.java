class ConversionEx {
	public static void main(String[] args) {
		int num1 = 'A', num2 = 'Z', num3 = 'a', num4 = 'z';
		int num5 = '0', num6 = '9', num7 = '°¡', num8 = 'ÆR';

		System.out.println("A : " + num1 + ", Z : " +num2 + ", a : " + num3 + ", z : " + num4);
		// A : 65, Z : 90, a : 97, z : 122
		System.out.println("0 : " + num5 + ", 9 : " +num6 + ", °¡ : " + num7 + ", ÆR : " + num8);
		// 0 : 48, 9 : 57, °¡ : 44032, ÆR : 55203

		double d1 = 3.14;
		int i1 = (int)d1;		// ¸í½ÃÀû Çüº¯È¯
		System.out.println("d1 : " + d1 + ", i1 : " +i1 );	// d1 : 3.14, i1 : 3
	}
}
