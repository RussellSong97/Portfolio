import java.util.*;

/******** 친구들의 기본 정보를 저장할 클래스 ********/
class Friend {
	String name, phone, addr;	// 친구들의 기본정보인 이름, 전화, 주소의 정보를 담을 인스턴스 변수

	/* 생성자 */
	public Friend(String name, String phone, String addr) {  // 받아온 인수들로 인스턴스 변수를 채움
		this.name	= name;
		this.phone	= phone;
		this.addr	= addr;
	}

	/* 전체 정보 출력 */
	public void showData() {
		System.out.println("이름 : " + name);
		System.out.println("전화 : " + phone);
		System.out.println("주소 : " + addr);
	}

	/* 기본 정보 출력 */
	public void showBasicinfo() { }
	// 하위클래스에서 오버라이딩 하려고 상위클래스에서 인위적으로 만들어 놓은 메소드
}

/******** 고교친구 - Friend의 자식(상속) ********/
class HighFriend extends Friend {
	String work;

	/* 생성자 */
	public HighFriend(String name, String phone, String addr, String work) {
		super(name, phone, addr);
		this.work = work;
	}

	/* 인스턴스에 저장된 모든 정보를 보여주는 메소드 */
	public void showData() {
		super.showData();	// 기본 정보를 출력하는 상위클래스의 showData() 메소드 호출 (오버라이딩)
		System.out.println("직업 : " + work); 
	}

	/* 기본 정보 출력 */
	public void showBasicinfo() { 
		System.out.println("이름 : " + name);
		System.out.println("전화 : " + phone);
	}
}

/******** 대학친구 - Friend의 자식(상속) ********/
class UnivFriend extends Friend {
	String major;
	
	/* 생성자 */
	public UnivFriend(String name, String phone, String addr, String major) {
		super(name, phone, addr);
		this.major = major;
	}

	/* 인스턴스에 저장된 모든 정보를 보여주는 메소드 */
	public void showData() {
		super.showData();
		System.out.println("전공 : " + major); 
	} 

	/* 기본 정보 출력 */
	public void showBasicinfo() { 
		System.out.println("이름 : " + name);
		System.out.println("전화 : " + phone);
		System.out.println("전공 : " + major);
	}
}

/******** 핸들러의 의미? ********/
class FriendInfoHandler {
	private Friend[] myFriends;		
	// Friend 클래스 형 인스턴스만 저장할 수 있는 배열 myFriends 선언 (친구 정보들을 저장할 저장공간)
	private int numOfFriends;
	// myFriends 배열의 인덱스 번호이자 저장된 친구 수를 의미하는 변수
	
	/* 생성자 */
	public FriendInfoHandler(int num) {	// 생성자로서 배열의 크기를 받아와 생성함
		myFriends = new Friend[num];	// 배열의 크기(10)를 인수로 받아와 myFriends 배열 생성 - 10명의 친구 정보를 저장할 수 있음
		numOfFriends = 0;				// 현재 저장된 친구 수 이자 myFriends 배열에 저장할 친구정보를 넣을 인덱스 번호
	}

	/* myFriends 배열에 추가할 친구정보 인스턴스를 받아 myFriends 배열에 추가하는 메소드(친구 정보 저장하는 클래스) */
	private void addFriendInfo(Friend fren) {
		// FriendInfoHandler 클래스 내부에서만 사용하므로 'private' 으로 선언 
		// 매개변수가 Friend 형인 이유는? HighFriend 와 UnivFriend 모두 받아야 하므로 둘의 상위 클래스형으로 선언함
		// 매개변수가 Friend 형이 아니면 모든 종류의 친구정보 수 만큼 addFriendInfo() 메소드를 따로 만들어야 함
		myFriends[numOfFriends] = fren;		// 배열에 받아온 친구 정보 추가
		numOfFriends++;						// 다음 친구 정보를 저장할 인덱스 번호를 만듦(현재 저장된 친구 숫자이기도 함)
	} 

	/* 친구 정보를 입력받아 저장하는 메소드 */
	public void addFriend(int choice) {
		String name, phone, addr, work, major;		// 친구정보를 받을 변수 선언
		
		Scanner sc = new Scanner(System.in);
		System.out.print("이름 : ");		name = sc.nextLine();
		System.out.print("전화 : ");		phone = sc.nextLine();
		System.out.print("주소 : ");		addr = sc.nextLine();
		// 친구의 기본정보로 고교와 대학 모두 가지는 공통정보 (상위클래스인 Friend클래스의 인스턴스 변수)

		if (choice == 1) {	// 고교 친구 정보 입력시 
			System.out.print("직업 : ");		work = sc.nextLine();		// 고교 친구 전용 정보를 입력받음
			addFriendInfo(new HighFriend(name, phone, addr, work));		
			// 이름 없는 HighFriend형 인스턴스 생성한 후 그 인스턴스를 인수로 하여 addFriendInfo() 메소드 호출
		} else  {
			System.out.print("전공 : ");		major = sc.nextLine();
			addFriendInfo(new UnivFriend(name, phone, addr, major));
		}
		System.out.println("---- 입력 완료! ----\n");
	}

	/* myFriends 배열에 저장되어 있는 모든 친구들의 정보를 보여주는 메소드 - showData() 메소드 이용 */
	public void showAllData() {
		for (int i = 0; i < numOfFriends ; i++ ) {
			// myFriends.length 는 불가(java.lang.NullPointerException 오류)
			// new Friend[10] - 배열 10까지 미리 만들어놨기 때문에 3개의 친구가 등록되어 있더라도 배열의 length 는 10 
			// 0번 부터 배열에 들어있는 자료까지(numOfFriends) 루프를 돌아야 함
			myFriends[i].showData();
			// myFriends[i] : myFriends 배열의 i 인덱스에 '들어있는' Friend형 인스턴스
			// .showData() :  안에 있는 showData() 메소드 호출
			System.out.println();
		}
	}

	/* 기본 정보 출력 */
	public void showBasicData() {
		for (int i = 0; i < numOfFriends ; i++ ) {

		/* 오버로딩이 안될 경우 형변환으로 처리할 수도 있음
			// 상위클래스 Friend 에 showBasicinfo() 가 없을 경우
			// if, else if 와 같이 형변환을 해줘야 함
			if (myFriends[i] instanceof HighFriend) {
				((HighFriend)myFriends[i]).showBasicinfo();
			} else if (myFriends[i] instanceof UnivFriend) {
				((UnivFriend)myFriends[i]).showBasicinfo();
			} else {
				// 
			}
		*/
			myFriends[i].showBasicinfo();	
			
			// myFriends 배열의 i 인덱스에 들어있는 Friend 형 인스턴스 안에 있는 showBasicinfo() 메소드 호출
			System.out.println();
		}
	}
}

/******** 메인 ********/
class MyFriendInfo {
	public static void main(String[] args)  {
		FriendInfoHandler handler = new FriendInfoHandler(10);
		// FriendInfoHandler 형 인스턴스 handler 를 선언 및 생성

		while (true) {	// 무한루프를 돌면서 프로그램 대기 상태를 유지
			System.out.println("********* 메뉴 선택 *********");
			System.out.println("1. 고교 친구 저장");
			System.out.println("2. 대학 친구 저장");
			System.out.println("3. 전체 정보 출력");
			System.out.println("4. 기본 정보 출력");
			System.out.println("5. 프로그램 종료");
			System.out.print("메뉴 선택 >> ");

			Scanner sc = new Scanner(System.in);
			int choice = sc.nextInt();	// 프로그램 시작시 동작 후 대기하는 곳
			// 사용자가 입력한 메뉴번호가 choice 변수에 저장

			switch (choice) {
				case 1: case 2:
					handler.addFriend(choice);		break;
				case 3:
					handler.showAllData();			break;
				case 4:
					handler.showBasicData();		break;
				case 5:
					System.out.println("프로그램을 종료합니다");
					return;
			}
		}
	}
}
