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

}

/******** 메인 ********/
class MyFriendInfo2 {
	public static void main(String[] args)  {
		FriendHandler fh = new FriendHandler(10);
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("********* 메뉴 선택 *********");
			System.out.println("1. 학교 친구 저장");
			System.out.println("2. 사회 친구 저장");
			System.out.println("3. 전체 정보 출력");
			System.out.println("4. 기본 정보 출력");
			System.out.println("5. 프로그램 종료");
			System.out.print("메뉴 선택 >> ");

			int num = sc.nextInt();

			switch (num) {
				case 1: case 2:		fh.friendAdd(num);		break;
				case 3:				
					fh.showAllData();	
					System.out.println("======= 전체 친구 정보 =======" );
					break;
				case 4:				
					fh.showBasicData();	
					System.out.println("-------- 기본 친구 정보 -------" );
					break;
				case 5:				
					System.out.println("프로그램을 종료합니다"); return;
			}
		}
	}
}
