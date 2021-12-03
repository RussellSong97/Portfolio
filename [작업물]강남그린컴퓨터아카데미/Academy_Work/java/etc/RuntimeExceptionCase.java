class RuntimeExceptionCase {
	public static void main(String[] args) {
		try {
			int[] arr = new int[2];
			arr[-1] = 20;
		} catch (ArrayIndexOutOfBoundsException e) {
			// 사용하려는 인덱스 번호가 배열의 인덱스 범위를 벗어났을 때 발생하는 예외
			System.out.println(e.getMessage());
		}

		
		try {
			Object obj = new int[10];
			String str = (String)obj;
		} catch (ClassCastException e) {
			// 불가능한 형변환을 실행시 발생하는 예외
			System.out.println(e.getMessage());
		}

		
		try {
			int[] arr = new int[-10];
		} catch (NegativeArraySizeException e) {
			// 배열의 크기를 음수로 지정했을 때 발생하는 예외
			System.out.println(e.getMessage());
		}

		
		try {
			String str = null;
			int len = str.length();
		} catch (NullPointerException e) {
			// 비어있는 객체를 사용하려 했을 때 발생하는 예외
			System.out.println(e.getMessage());
		}

	}
}
