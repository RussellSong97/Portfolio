class SwitchEx {
	public static void main(String[] args) {
		int score = 88;
		/*
		score�� 60 �̻��̸� 'D', 70 �̻��̸� 'C', 80 �̻��̸� 'B', 90 �̻��̸� 'A', �� �ܿ��� ��� 'F'
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

		System.out.println("score[" + score + "]�� [" + result + "]");
	}
}
