class Student {
	private int stdID, korean, math, art, by, bm, bd;
	public Student(int s, int k, int m, int a, int by, int bm, int bd) {
		stdID = s;		korean = k;		math = m;		art = a;
		this.by = by;		this.bm = bm;	this.bd = bd;
		// 멤버변수와 매개변수의 이름이 같을 때 this를 사용하여 멤버변수를 구분해 줌
	}
	public Student(int s, int k, int m, int by, int bm, int bd) {
		this(s, k, m, 0, by, bm, bd);
		// 매개변수로 값들을 받은 뒤 그 값을 인수로 하여 다른 생성자를 호출
	}
	public void showInfo() {
		System.out.println("학번 : " + stdID);
		System.out.println("생일 : " + by + "/" + bm + "/" + bd);
		System.out.println("국어 : " + korean);
		System.out.println("수학 : " + math);
		if (art == 0) {
			System.out.println("미술 수업 안듣는 학생");
		} else {
			System.out.println("미술 : " + art);
		}
	}
}

class Overloading {
	public static void main(String[] args) {
		Student std1 = new Student(2012003, 98, 78, 88, 2010, 01, 01);
		Student std2 = new Student(2012010, 85, 68, 2005, 11, 26);
		std1.showInfo();
		std2.showInfo();
	}
}
