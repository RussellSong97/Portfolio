class SwitchEx {
	public static void main(String[] args) {
		int score = 88;
		/*
		score가 60 이상이면 'D', 70 이상이면 'C', 80 이상이면 'B', 90 이상이면 'A', 그 외에는 모두 'F'
		*/

		char result;

		switch (score / 10) {
			case 9:
				result = 'A';
				break;
			case 8:
				result = 'B';
				break;
			case 7:
				result = 'C';
				break;
			case 6:
				result = 'D';
				break;
			default : 
				result = 'F';
		}

		System.out.println("score[" + score + "]는 [" + result + "]");
	}
}
