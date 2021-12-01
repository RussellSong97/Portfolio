/* 회원 정보 관리 - MemberManage.java
회원정보(아이디, 암호, 이름)를 저장할 클래스 - MemberInfo
정보를 입력받고, 저장 처리 및 각종 기능을 담당할 클래스 - MemeberHandler
메뉴
1. 회원 등록 - 회원 정보를 입력받아 ArrayList에 저장 (제네릭 사용, 아이디 중복체크)
2. 전체 회원정보 보기
3. 회원 검색 - 아이디로 검색하여 정보 보여줌
4. 로그인 - 아이디와 암호를 입력받아 로그인 처리 (성공 : 로그인 되었습니다 / 실패 : 로그인에 실패했습니다 후 다시 로그인 시도)
5. 프로그램 종료
*/

import java.util.*;

class MemberInfo {	// 회원정보 저장용 클래스
	private String id, pwd, name;

	public MemberInfo(String id, String pwd, String name) {
		// MemberInfo클래스의 생성자로 인스턴스 생성시 회원정보를 받아옴
		this.id = id;
		this.pwd = pwd;
		this.name = name;
	}

	public void setPwd(String pwd) { this.pwd = pwd; } // 암호 변경용 메소드
	public String getId() { return id; }		// id를 받아오는 메소드
	public String getpwd() { return pwd; }		// pwd를 받아오는 메소드
	public String getName() { return name; }	// name을 받아오는 메소드

	public void showInfo() {
		System.out.println("아이디 : " + id );
		System.out.println("비밀번호 : " + pwd );
		System.out.println("이름 : " + name );
	}
}


class MemeberHandler {	// 회원정보(MemberInfo 클래스의 인스턴스)를 입력받고 처리하는 클래스
	private ArrayList<MemberInfo> members;		// MemberInfo 형 인스턴스만 저장하는 회원정보 저장용 ArrayList
	// 재네릭으로 MemberInfo 형만 지정했으므로 members에서 객체를 참조할 때 MemberInfo 형으로 형변환을 하지 않아도 됨
	public MemeberHandler(int num) {
		members = new ArrayList<MemberInfo>(num);
	}
	
	// 회원정보를 사용자에게 받아 저장하는 메소드
	public void saveMember() {
		String id, pwd, name;

		Scanner sc = new Scanner(System.in);
		System.out.print("아이디 : ");	id = sc.nextLine();
		if (isDupID(id)) {
			System.out.println("이미 사용중인 아이디 입니다.");
			saveMember();
			return;
		}

		System.out.print("비번 : ");		pwd = sc.nextLine();
		System.out.print("이름 : ");		name = sc.nextLine();

		members.add(new MemberInfo(id, pwd, name));
		System.out.println("회원 가입을 축하합니다!!\n");
	}

	// 검색어를 받아 회원정보를 검색하는 메소드
	public void searchMember() {
		if (members.size() == 0) {   // 배열에 저장된 사람이 하나도 없을 경우
			System.out.println("검색할 대상이 없습니다.");
			return;
		}
 
		Scanner sc = new Scanner(System.in);
		System.out.print("검색할 아이디 : ");      
		String schId = sc.nextLine();

		boolean isSearch = false;   // 검색 성공여부를 저장할 변수
		for (int i = 0 ; i < members.size() ; i++) {
			if (members.get(i).getId().indexOf(schId) != -1) {
			// members.get(i).getId() : ArrayList 인 members 안의 i 인덱스에 해당하는 MemberInfo 형 인스턴스의 getId() 메소드를 실행하라 - 해당 인스턴스가 가진 id 정보를 String으로 리턴
			// members.get(i).getId()로 받아온 아이디 안에 사용자가 입력한 검색어가 들어 있으면 
				members.get(i).showInfo();
				// 해당 회원정보를 showInfo() 메소드를 통해 보여줌
				isSearch = true; 
			}
		}
		if (!isSearch) {
			System.out.println("찾는 이름이 없습니다.");
			searchMember();
		}
	}

	// 아이디와 암호를 받아 로그인을 하는 메소드
	public void loginMember() {
		String id, pwd;
		Scanner sc = new Scanner(System.in);

		System.out.print("아이디 : ");     id = sc.nextLine();
		System.out.print("비밀번호 : ");	pwd = sc.nextLine();
		boolean isLogin = false;

		
		for (int i = 0 ; i < members.size() ; i++) {
			if (members.get(i).getId().equals(id) && members.get(i).getpwd().equals(pwd)) {
				isLogin = true; 
				break;
			}
		}

		if (isLogin) {
			System.out.println("로그인 되었습니다\n"); 
		} else {
			System.out.println("로그인에 실패했습니다. 다시 로그인 해주세요.\n"); 
			loginMember();
		}
	} 

	// 아이디 중복 체크하는 메소드
	public boolean isDupID(String id) {
		for (int i = 0 ; i < members.size() ; i++) {
			if (members.get(i).getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	// 전체 회원정보를 보여주는 메소드
	public void showAllInfo() {
		for (int i = 0 ; i < members.size() ; i++) {
			members.get(i).showInfo();
		}
	}

}

class MemberManage {
	public static void main(String[] args) {
		MemeberHandler handler = new MemeberHandler(10);

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("----- [메뉴] ------");
			System.out.println("1. 회원 등록");
			System.out.println("2. 전체 회원정보 보기");
			System.out.println("3. 회원 검색");
			System.out.println("4. 로그인");
			System.out.println("5. 프로그램 종료");
			System.out.println("------------------");

			int choice = sc.nextInt();
			switch (choice) {
				case 1:
					handler.saveMember();	break;
				case 2:
					handler.showAllInfo();	break;
				case 3:
					handler.searchMember();	break; 
				case 4:
					handler.loginMember();	break; 
				case 5:
					System.out.println("프로그램 종료"); return;			
			}
		}

		
	}
}
