class MethodReturn3 {
	public static void main(String[] args) {
		char grade = calGrade(78);
		System.out.println("학점은 '" + grade +"' 학점 입니다.");
	}

	public static char calGrade(int score) {
		if (score >= 90) {
			return 'A';
		} else if (score >= 80) {
			return 'B';
		} else if (score >= 70) {
			return 'C';
		} else if (score >= 60) {
			return 'D';
		} else {
			return 'F';
		} 
		// if문 안에서 return 할 경우 반드시 else문이 있거나 if문 밖에서 return 해야 함
	}
}
