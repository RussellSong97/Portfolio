/*
�θ�� : MyFriendInfo3 : ��� �ν��Ͻ� ������ private���� ����
��� : Friend : �̸�, ����, ����, ��ȭ
�б� �θ� : SchoolFriend : �б�, ��������, ����
��ȸ �θ� : SocialFriend : ȸ��, �Ҽ�, ����

1. �б� �θ� �߰�
2. ��ȸ �θ� �߰�
3. ��ü ���� ���
4. �⺻ ���� ��� - �̸�, ����, ����, �б�(ȸ��)
5. �̸� �˻� - �̸��� �޾� ���� �̸��� ��� ���� ��ü�� �����ֱ� 
			(�� �� �̻��� ��� ��� �����ֱ�1, ù��°�� ��ġ�ϴ� ����� �����ֱ�2)
9. ���α׷� ����
*/

import java.util.Scanner; 

class Friend {
	private String name, gender, phone;
	private int age;
	
	public Friend(String name, String gender, String phone, int age) {
		// �ν��Ͻ� ������ ���ÿ� ���� ��
		this.name	= name;
		this.gender = gender;
		this.phone	= phone;
		this.age	= age;
	}
	public void showAllData() {
		System.out.println("�̸� : " + name);
		System.out.println("���� : " + gender + ", ���� : " + age);
		System.out.println("��ȭ : " + phone);
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
		System.out.println("�б� : " + school);
		System.out.println("���� : " + major);
		System.out.println("���� : " + year);
	}
	public void showBasicData() {  
		System.out.println("�̸� : " + this.getName());
		System.out.println("���� : " + this.getGender() + ", ���� : " + this.getAge());
		System.out.println("�б� : " + school);
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
		System.out.println("ȸ�� : " + company);
		System.out.println("�μ� : " + division);
		System.out.println("�Ҽ� : " + position);
	}
	public void showBasicData() { 
		System.out.println("�̸� : " + this.getName());
		System.out.println("ȸ�� : " + company);
		System.out.println("�Ҽ� : " + position);
	}
}

class FriendHandler {
	private Friend[] friends;
	private int idx = 0;
	private Friend firstFriend;			// ù��° �˻��� ����
	private Friend lastFriend;			// ������ �˻��� ����
	private boolean isNullInfo = true;	// ���� �ִ��� ������ ���� üũ

	public FriendHandler(int num) {
		friends = new Friend[num];
	}
	public void friendAdd(int menu) {
		String 	name, gender, phone, school, major, company, division, position;
		int age, year;

		Scanner sc = new Scanner(System.in);
		System.out.print("�̸� : " );		name	= sc.nextLine();
		System.out.print("���� : " );		age		= sc.nextInt();	sc.nextLine();
		System.out.print("���� : " );		gender	= sc.nextLine();
		System.out.print("��ȭ : " );		phone	= sc.nextLine();

		if (menu == 1) {
			System.out.print("�б� : " );		school	= sc.nextLine();
			System.out.print("���� : " );		major	= sc.nextLine();
			System.out.print("���� : " );		year	= sc.nextInt();	sc.nextLine();
			friends[idx] = new SchoolFriend(name, gender, phone, age, school, major, year);
		} else {
			System.out.print("ȸ�� : " );		company		= sc.nextLine();
			System.out.print("�Ҽ� : " );		division	= sc.nextLine();
			System.out.print("�μ� : " );		position	= sc.nextLine();
			friends[idx] = new SocialFriend(name, gender, phone, age, company, division, position);
		}
		idx++;
		System.out.println("�� �Է��� �Ϸ�Ǿ����ϴ�!! ��\n\n");
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

	//5. �̸� �˻� - �̸��� �޾� ���� �̸��� ��� ���� ��ü�� �����ֱ� (�� �� �̻��� ��� ��� �����ֱ�) 
	public void findInfo(int num) {
		String 	searchName;
		int resultCount;

		if (idx == 0) {	// �迭�� ����� ����� �ϳ��� ���� ���
			System.out.print("�˻��� ����� �����ϴ�.\n\n" );	
			return;
		}
		
		if (isNullInfo) {
			System.out.println();
			System.out.println("��������������������������������" );
			System.out.println("���������� �̸� �˻� ����������" );
			System.out.println("��������������������������������" );
			System.out.println();
		}
		

		// �̸��� �Է¹޾Ƽ� 
		Scanner sc = new Scanner(System.in);
		System.out.print(" �� �˻��� �̸��� �Է��ϼ���.   ������   " );		
		searchName	= sc.nextLine();
		System.out.println();

		System.out.println("�� �̸� '" + searchName + "' �� �˻��� ��� LIST ��\n");
		resultCount = searchInfoView(searchName);

		if (resultCount > 0 ) {
			System.out.println("�� �˻� ��� : " + resultCount + " �� \n");
			System.out.println();

			if (num == 5) {
				System.out.println("�� ���� ù��°�� �˻��� �����Դϴ�. ��");
				searchFirstInfoView();
			} else {
				System.out.println("�� ���� ���������� �˻��� �����Դϴ�. ��");
				searchLastInfoView();
			}
			System.out.println();
			isNullInfo = true;
		}  else {
			System.out.println("�� �Էµ� �̸��� ������ �����ϴ�.\n�ٽ� �Է����ּ���.");
			isNullInfo = false;
			findInfo(num);
		}

	} 
 
	// �˻��� �̸� ���� ã�� ��� ��� ��
	public int searchInfoView(String searchName) { 
		int cnt = 0;
		boolean isFirst = true;

		for (int i = 0; i < idx ; i++) {
			if (searchName.equals(friends[i].getName())) {		// ���鼭 ������ ���� ���
				friends[i].showAllData();
				lastFriend = friends[i];						// ���鼭 lastFriend�� ��� ����鼭 ����
				if (isFirst) {
					firstFriend= friends[i];					// firstFriend �� ó�� �˻��� friend �Է� �� false - �ѹ��� �����
					isFirst = false;
				}
				System.out.println();
				cnt++;	// ���� �̸� ���� üũ
			}
		}
		return cnt;
	}

	// �˻��� �̸� ù��° ����� �����ֱ�
	public void searchFirstInfoView() {
		firstFriend.showAllData(); 
	}

	// �˻��� �̸� ��������° ����� �����ֱ�
	public void searchLastInfoView() {
		lastFriend.showAllData(); 
	}

}

/******** ���� ********/
class MyFriendInfo3 {
	public static void main(String[] args)  {
		FriendHandler fh = new FriendHandler(10);
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("***************************");
			System.out.println("******** �޴� ���� ********");
			System.out.println("***************************");
			System.out.println("1. �б� ģ�� ����");
			System.out.println("2. ��ȸ ģ�� ����");
			System.out.println("3. ��ü ���� ���");
			System.out.println("4. �⺻ ���� ���");
			System.out.println("5. �̸� �˻�");
			System.out.println("6. ������ ��ġ�� �˻�");
			System.out.println("9. ���α׷� ����");
			System.out.println("***************************");
			System.out.print("�� �޴� ���� ���������� ");

			int num = sc.nextInt();

			System.out.println(); 

			switch (num) {
				case 1: case 2:		
					System.out.println("===========================" );
					System.out.println("      ģ�� ���� ����" );
					System.out.println("---------------------------" );
					fh.friendAdd(num);		
					break;
				case 3:				
					System.out.println("===========================" );
					System.out.println("     ��ü ģ�� ���� ���" );
					System.out.println("---------------------------" );
					fh.showAllData();	
					break;
				case 4:				
					System.out.println("===========================" );
					System.out.println("    �⺻ ģ�� ���� ���" );
					System.out.println("---------------------------" );
					fh.showBasicData();	
					break;
				case 5:	case 6: 
					fh.findInfo(num);
					break; 
				case 9:				
					System.out.println("���α׷��� �����մϴ�"); return;
			}

			
			System.out.println();
		}
	}
}
