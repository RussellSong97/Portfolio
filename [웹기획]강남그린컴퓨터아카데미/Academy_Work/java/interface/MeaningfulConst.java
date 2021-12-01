import java.util.*;

interface Week {
	int MON = 1, TUE = 2, WED = 3, THU = 4, FRI = 5, SAT = 6, SUN = 7;
	// interface에서 정의하는 변수들은 자동으로 public static final 로 선언됨
	// 어디서든 접근이 가능하고, 인스턴스 생성없이 사용가능하며, 상수로 값 변경이 볼가능하다는 특징을 가짐
}

class MeaningfulConst {
	public static void main(String[] args) {
		System.out.println("오늘의 요일을 선택하세요.");
		System.out.println("1-월, 2-화, 3-수, 4-목, 5-금, 6-토, 7-일");
		System.out.print("선택 : ");

		Scanner sc = new Scanner(System.in);
		int sel = sc.nextInt();

		switch(sel) {
			case Week.MON :
				System.out.println("월요일 입니다."); break;
			case Week.TUE :
				System.out.println("화요일 입니다."); break;
			case Week.WED :
				System.out.println("수요일 입니다."); break;
			case Week.THU :
				System.out.println("목요일 입니다."); break;
			case Week.FRI :
				System.out.println("금요일 입니다."); break;
			case Week.SAT :
				System.out.println("토요일 입니다."); break;
			case Week.SUN :
				System.out.println("일요일 입니다."); break; 
		}

	}
}
