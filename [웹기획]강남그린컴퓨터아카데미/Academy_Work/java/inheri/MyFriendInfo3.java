/*
인명록 : MyFriendInfo3 : 모든 인스턴스 변수는 private으로 선언
사람 : Friend : 이름, 성별, 나이, 전화
학교 인맥 : SchoolFriend : 학교, 졸업연도, 전공
사회 인맥 : SocialFriend : 회사, 소속, 직급

1. 학교 인맥 추가
2. 사회 인맥 추가
3. 전체 정보 출력
4. 기본 정보 출력 - 이름, 성별, 나이, 학교(회사)
5. 이름 검색 - 이름을 받아 같은 이름의 사람 정보 전체를 보여주기 
			(두 명 이상일 경우 모두 보여주기1, 첫번째로 일치하는 사람만 보여주기2)
9. 프로그램 종료
*/

import java.util.Scanner; 

class Friend {
	private String name, gender, phone;
	private int age;
	
	public Friend(String name, String gender, String phone, int age) {
		// 인스턴스 생성과 동시에 값이 들어감
		this.name	= name;
		this.gender = gender;
		this.phone	= phone;
		this.age	= age;
	}
	public void showAllData() {
		System.out.println("이름 : " + name);
		System.out.println("성별 : " + gender + ", 나이 : " + age);
		System.out.println("전화 : " + phone);
	}
	public void showBasicData() {}

	public String getName() { return name; }
	public String getGender() { return gender; }
	public String getPhone() { return phone; }
	public int getAge() { return age; }
}

class SchoolFriend extends Friend {
	private String school, major;
	private int year;
	public SchoolFriend(String name, String gender, String phone, int age, String school, String major, int year) {
		super(name, gender, phone, age);
		this.school = school;
		this.major	= major;
		this.year	= year;
	}
	public void showAllData() {
		super.showAllData();
		System.out.println("학교 : " + school);
		System.out.println("전공 : " + major);
		System.out.println("졸업 : " + year);
	}
	public void showBasicData() {  
		System.out.println("이름 : " + this.getName());
		System.out.println("성별 : " + this.getGender() + ", 나이 : " + this.getAge());
		System.out.println("학교 : " + school);
	}
}

class SocialFriend extends Friend {
	private String company, division, position;
	public SocialFriend(String name, String gender, String phone, int age, String company, String division, String position) {
		super(name, gender, phone, age);
		this.company	= company;
		this.division	= division;
		this.position	= position;
	}
	public void showAllData() {
		super.showAllData();
		System.out.println("회사 : " + company);
		System.out.println("부서 : " + division);
		System.out.println("소속 : " + position);
	}
	public void showBasicData() { 
		System.out.println("이름 : " + this.getName());
		System.out.println("회사 : " + company);
		System.out.println("소속 : " + position);
	}
}

class FriendHandler {
	private Friend[] friends;
	private int idx = 0;
	private Friend firstFriend;			// 첫번째 검색된 정보
	private Friend lastFriend;			// 마지막 검색된 정보
	private boolean isNullInfo = true;	// 정보 있는지 없는지 여부 체크

	public FriendHandler(int num) {
		friends = new Friend[num];
	}
	public void friendAdd(int menu) {
		String 	name, gender, phone, school, major, company, division, position;
		int age, year;

		Scanner sc = new Scanner(System.in);
		System.out.print("이름 : " );		name	= sc.nextLine();
		System.out.print("나이 : " );		age		= sc.nextInt();	sc.nextLine();
		System.out.print("성별 : " );		gender	= sc.nextLine();
		System.out.print("전화 : " );		phone	= sc.nextLine();

		if (menu == 1) {
			System.out.print("학교 : " );		school	= sc.nextLine();
			System.out.print("전공 : " );		major	= sc.nextLine();
			System.out.print("졸업 : " );		year	= sc.nextInt();	sc.nextLine();
			friends[idx] = new SchoolFriend(name, gender, phone, age, school, major, year);
		} else {
			System.out.print("회사 : " );		company		= sc.nextLine();
			System.out.print("소속 : " );		division	= sc.nextLine();
			System.out.print("부서 : " );		position	= sc.nextLine();
			friends[idx] = new SocialFriend(name, gender, phone, age, company, division, position);
		}
		idx++;
		System.out.println("※ 입력이 완료되었습니다!! ※\n\n");
	}
	public void showAllData() {
		for (int i = 0; i < idx ; i++) {
			friends[i].showAllData();
			System.out.println("");
		}
	}
	public void showBasicData() {
		for (int i = 0; i < idx ; i++) {
			friends[i].showBasicData();
			System.out.println("");
		}
	}

	//5. 이름 검색 - 이름을 받아 같은 이름의 사람 정보 전체를 보여주기 (두 명 이상일 경우 모두 보여주기) 
	public void findInfo(int num) {
		String 	searchName;
		int resultCount;

		if (idx == 0) {	// 배열에 저장된 사람이 하나도 없을 경우
			System.out.print("검색할 대상이 없습니다.\n\n" );	
			return;
		}
		
		if (isNullInfo) {
			System.out.println();
			System.out.println("ㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍ" );
			System.out.println("ㆍㆍㆍㆍㆍ 이름 검색 ㆍㆍㆍㆍㆍ" );
			System.out.println("ㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍㆍ" );
			System.out.println();
		}
		

		// 이름을 입력받아서 
		Scanner sc = new Scanner(System.in);
		System.out.print(" ▶ 검색할 이름을 입력하세요.   ▶▶▶   " );		
		searchName	= sc.nextLine();
		System.out.println();

		System.out.println("※ 이름 '" + searchName + "' 로 검색한 결과 LIST ※\n");
		resultCount = searchInfoView(searchName);

		if (resultCount > 0 ) {
			System.out.println("※ 검색 결과 : " + resultCount + " 건 \n");
			System.out.println();

			if (num == 5) {
				System.out.println("※ 가장 첫번째로 검색된 정보입니다. ※");
				searchFirstInfoView();
			} else {
				System.out.println("※ 가장 마지막으로 검색된 정보입니다. ※");
				searchLastInfoView();
			}
			System.out.println();
			isNullInfo = true;
		}  else {
			System.out.println("※ 입력된 이름의 정보가 없습니다.\n다시 입력해주세요.");
			isNullInfo = false;
			findInfo(num);
		}

	} 
 
	// 검색한 이름 정보 찾아 모두 출력 뷰
	public int searchInfoView(String searchName) { 
		int cnt = 0;
		boolean isFirst = true;

		for (int i = 0; i < idx ; i++) {
			if (searchName.equals(friends[i].getName())) {		// 돌면서 같으면 정보 출력
				friends[i].showAllData();
				lastFriend = friends[i];						// 돌면서 lastFriend에 계속 덮어쓰면서 저장
				if (isFirst) {
					firstFriend= friends[i];					// firstFriend 에 처음 검색된 friend 입력 후 false - 한번만 실행됨
					isFirst = false;
				}
				System.out.println();
				cnt++;	// 같은 이름 갯수 체크
			}
		}
		return cnt;
	}

	// 검색한 이름 첫번째 사람만 보여주기
	public void searchFirstInfoView() {
		firstFriend.showAllData(); 
	}

	// 검색한 이름 마지막번째 사람만 보여주기
	public void searchLastInfoView() {
		lastFriend.showAllData(); 
	}

}

/******** 메인 ********/
class MyFriendInfo3 {
	public static void main(String[] args)  {
		FriendHandler fh = new FriendHandler(10);
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("***************************");
			System.out.println("******** 메뉴 선택 ********");
			System.out.println("***************************");
			System.out.println("1. 학교 친구 저장");
			System.out.println("2. 사회 친구 저장");
			System.out.println("3. 전체 정보 출력");
			System.out.println("4. 기본 정보 출력");
			System.out.println("5. 이름 검색");
			System.out.println("6. 마지막 일치자 검색");
			System.out.println("9. 프로그램 종료");
			System.out.println("***************************");
			System.out.print("▶ 메뉴 선택 ▶▶▶▶▶ ");

			int num = sc.nextInt();

			System.out.println(); 

			switch (num) {
				case 1: case 2:		
					System.out.println("===========================" );
					System.out.println("      친구 정보 저장" );
					System.out.println("---------------------------" );
					fh.friendAdd(num);		
					break;
				case 3:				
					System.out.println("===========================" );
					System.out.println("     전체 친구 정보 출력" );
					System.out.println("---------------------------" );
					fh.showAllData();	
					break;
				case 4:				
					System.out.println("===========================" );
					System.out.println("    기본 친구 정보 출력" );
					System.out.println("---------------------------" );
					fh.showBasicData();	
					break;
				case 5:	case 6: 
					fh.findInfo(num);
					break; 
				case 9:				
					System.out.println("프로그램을 종료합니다"); return;
			}

			
			System.out.println();
		}
	}
}
