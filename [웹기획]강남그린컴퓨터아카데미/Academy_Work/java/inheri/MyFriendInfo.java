import java.util.*;

/******** ģ������ �⺻ ������ ������ Ŭ���� ********/
class Friend {
	String name, phone, addr;	// ģ������ �⺻������ �̸�, ��ȭ, �ּ��� ������ ���� �ν��Ͻ� ����

	/* ������ */
	public Friend(String name, String phone, String addr) {  // �޾ƿ� �μ���� �ν��Ͻ� ������ ä��
		this.name	= name;
		this.phone	= phone;
		this.addr	= addr;
	}

	/* ��ü ���� ��� */
	public void showData() {
		System.out.println("�̸� : " + name);
		System.out.println("��ȭ : " + phone);
		System.out.println("�ּ� : " + addr);
	}

	/* �⺻ ���� ��� */
	public void showBasicinfo() { }
	// ����Ŭ�������� �������̵� �Ϸ��� ����Ŭ�������� ���������� ����� ���� �޼ҵ�
}

/******** ��ģ�� - Friend�� �ڽ�(���) ********/
class HighFriend extends Friend {
	String work;

	/* ������ */
	public HighFriend(String name, String phone, String addr, String work) {
		super(name, phone, addr);
		this.work = work;
	}

	/* �ν��Ͻ��� ����� ��� ������ �����ִ� �޼ҵ� */
	public void showData() {
		super.showData();	// �⺻ ������ ����ϴ� ����Ŭ������ showData() �޼ҵ� ȣ�� (�������̵�)
		System.out.println("���� : " + work); 
	}

	/* �⺻ ���� ��� */
	public void showBasicinfo() { 
		System.out.println("�̸� : " + name);
		System.out.println("��ȭ : " + phone);
	}
}

/******** ����ģ�� - Friend�� �ڽ�(���) ********/
class UnivFriend extends Friend {
	String major;
	
	/* ������ */
	public UnivFriend(String name, String phone, String addr, String major) {
		super(name, phone, addr);
		this.major = major;
	}

	/* �ν��Ͻ��� ����� ��� ������ �����ִ� �޼ҵ� */
	public void showData() {
		super.showData();
		System.out.println("���� : " + major); 
	} 

	/* �⺻ ���� ��� */
	public void showBasicinfo() { 
		System.out.println("�̸� : " + name);
		System.out.println("��ȭ : " + phone);
		System.out.println("���� : " + major);
	}
}

/******** �ڵ鷯�� �ǹ�? ********/
class FriendInfoHandler {
	private Friend[] myFriends;		
	// Friend Ŭ���� �� �ν��Ͻ��� ������ �� �ִ� �迭 myFriends ���� (ģ�� �������� ������ �������)
	private int numOfFriends;
	// myFriends �迭�� �ε��� ��ȣ���� ����� ģ�� ���� �ǹ��ϴ� ����
	
	/* ������ */
	public FriendInfoHandler(int num) {	// �����ڷμ� �迭�� ũ�⸦ �޾ƿ� ������
		myFriends = new Friend[num];	// �迭�� ũ��(10)�� �μ��� �޾ƿ� myFriends �迭 ���� - 10���� ģ�� ������ ������ �� ����
		numOfFriends = 0;				// ���� ����� ģ�� �� ���� myFriends �迭�� ������ ģ�������� ���� �ε��� ��ȣ
	}

	/* myFriends �迭�� �߰��� ģ������ �ν��Ͻ��� �޾� myFriends �迭�� �߰��ϴ� �޼ҵ�(ģ�� ���� �����ϴ� Ŭ����) */
	private void addFriendInfo(Friend fren) {
		// FriendInfoHandler Ŭ���� ���ο����� ����ϹǷ� 'private' ���� ���� 
		// �Ű������� Friend ���� ������? HighFriend �� UnivFriend ��� �޾ƾ� �ϹǷ� ���� ���� Ŭ���������� ������
		// �Ű������� Friend ���� �ƴϸ� ��� ������ ģ������ �� ��ŭ addFriendInfo() �޼ҵ带 ���� ������ ��
		myFriends[numOfFriends] = fren;		// �迭�� �޾ƿ� ģ�� ���� �߰�
		numOfFriends++;						// ���� ģ�� ������ ������ �ε��� ��ȣ�� ����(���� ����� ģ�� �����̱⵵ ��)
	} 

	/* ģ�� ������ �Է¹޾� �����ϴ� �޼ҵ� */
	public void addFriend(int choice) {
		String name, phone, addr, work, major;		// ģ�������� ���� ���� ����
		
		Scanner sc = new Scanner(System.in);
		System.out.print("�̸� : ");		name = sc.nextLine();
		System.out.print("��ȭ : ");		phone = sc.nextLine();
		System.out.print("�ּ� : ");		addr = sc.nextLine();
		// ģ���� �⺻������ ���� ���� ��� ������ �������� (����Ŭ������ FriendŬ������ �ν��Ͻ� ����)

		if (choice == 1) {	// �� ģ�� ���� �Է½� 
			System.out.print("���� : ");		work = sc.nextLine();		// �� ģ�� ���� ������ �Է¹���
			addFriendInfo(new HighFriend(name, phone, addr, work));		
			// �̸� ���� HighFriend�� �ν��Ͻ� ������ �� �� �ν��Ͻ��� �μ��� �Ͽ� addFriendInfo() �޼ҵ� ȣ��
		} else  {
			System.out.print("���� : ");		major = sc.nextLine();
			addFriendInfo(new UnivFriend(name, phone, addr, major));
		}
		System.out.println("---- �Է� �Ϸ�! ----\n");
	}

	/* myFriends �迭�� ����Ǿ� �ִ� ��� ģ������ ������ �����ִ� �޼ҵ� - showData() �޼ҵ� �̿� */
	public void showAllData() {
		for (int i = 0; i < numOfFriends ; i++ ) {
			// myFriends.length �� �Ұ�(java.lang.NullPointerException ����)
			// new Friend[10] - �迭 10���� �̸� �������� ������ 3���� ģ���� ��ϵǾ� �ִ��� �迭�� length �� 10 
			// 0�� ���� �迭�� ����ִ� �ڷ����(numOfFriends) ������ ���ƾ� ��
			myFriends[i].showData();
			// myFriends[i] : myFriends �迭�� i �ε����� '����ִ�' Friend�� �ν��Ͻ�
			// .showData() :  �ȿ� �ִ� showData() �޼ҵ� ȣ��
			System.out.println();
		}
	}

	/* �⺻ ���� ��� */
	public void showBasicData() {
		for (int i = 0; i < numOfFriends ; i++ ) {

		/* �����ε��� �ȵ� ��� ����ȯ���� ó���� ���� ����
			// ����Ŭ���� Friend �� showBasicinfo() �� ���� ���
			// if, else if �� ���� ����ȯ�� ����� ��
			if (myFriends[i] instanceof HighFriend) {
				((HighFriend)myFriends[i]).showBasicinfo();
			} else if (myFriends[i] instanceof UnivFriend) {
				((UnivFriend)myFriends[i]).showBasicinfo();
			} else {
				// 
			}
		*/
			myFriends[i].showBasicinfo();	
			
			// myFriends �迭�� i �ε����� ����ִ� Friend �� �ν��Ͻ� �ȿ� �ִ� showBasicinfo() �޼ҵ� ȣ��
			System.out.println();
		}
	}
}

/******** ���� ********/
class MyFriendInfo {
	public static void main(String[] args)  {
		FriendInfoHandler handler = new FriendInfoHandler(10);
		// FriendInfoHandler �� �ν��Ͻ� handler �� ���� �� ����

		while (true) {	// ���ѷ����� ���鼭 ���α׷� ��� ���¸� ����
			System.out.println("********* �޴� ���� *********");
			System.out.println("1. �� ģ�� ����");
			System.out.println("2. ���� ģ�� ����");
			System.out.println("3. ��ü ���� ���");
			System.out.println("4. �⺻ ���� ���");
			System.out.println("5. ���α׷� ����");
			System.out.print("�޴� ���� >> ");

			Scanner sc = new Scanner(System.in);
			int choice = sc.nextInt();	// ���α׷� ���۽� ���� �� ����ϴ� ��
			// ����ڰ� �Է��� �޴���ȣ�� choice ������ ����

			switch (choice) {
				case 1: case 2:
					handler.addFriend(choice);		break;
				case 3:
					handler.showAllData();			break;
				case 4:
					handler.showBasicData();		break;
				case 5:
					System.out.println("���α׷��� �����մϴ�");
					return;
			}
		}
	}
}
