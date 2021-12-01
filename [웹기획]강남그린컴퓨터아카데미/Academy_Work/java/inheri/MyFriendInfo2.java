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

}

/******** ���� ********/
class MyFriendInfo2 {
	public static void main(String[] args)  {
		FriendHandler fh = new FriendHandler(10);
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("********* �޴� ���� *********");
			System.out.println("1. �б� ģ�� ����");
			System.out.println("2. ��ȸ ģ�� ����");
			System.out.println("3. ��ü ���� ���");
			System.out.println("4. �⺻ ���� ���");
			System.out.println("5. ���α׷� ����");
			System.out.print("�޴� ���� >> ");

			int num = sc.nextInt();

			switch (num) {
				case 1: case 2:		fh.friendAdd(num);		break;
				case 3:				
					fh.showAllData();	
					System.out.println("======= ��ü ģ�� ���� =======" );
					break;
				case 4:				
					fh.showBasicData();	
					System.out.println("-------- �⺻ ģ�� ���� -------" );
					break;
				case 5:				
					System.out.println("���α׷��� �����մϴ�"); return;
			}
		}
	}
}
