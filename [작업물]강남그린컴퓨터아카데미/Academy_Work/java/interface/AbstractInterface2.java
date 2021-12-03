/*
abstract method : �޼ҵ��� ����θ� �ְ�, ���ǵ��� ���� �޼ҵ�
	 ex) public abstract ����Ÿ�� �޼ҵ��(); -> ����(�߰�ȣ �κ�)�� ����
 - ����ϴ� ���� : ����� ���踦 ������Ű�� ���� ����Ŭ������ ���
 - Ŭ�����̳� �ν��Ͻ��� ������ ���� ����

 �߻�ȭ Ŭ����
  - abstract class�� ��ӹ��� Ŭ������ ������ abstract class�� abstract method�� �������̵��ؼ� ����
	�������� ���ϸ� ��ӹ��� ����Ŭ������ abstract class�� �����ؾ���
  - ����
  abstract class Ŭ������ {�ϳ� �̻��� abstract �޼ҵ�, ...}
*/

interface PersonaleNumberStorage {
	void addPersonalInfo(String name, String perNum);
	String searchName(String perNum);
	// interface ���� �����ϴ� ��� �޼ҵ�� �ڵ����� public abstract �� �����
	// interface������ ���� ������ �޼ҵ带 ������ �� ���� ������ public abstract ��
}

class PersonalNumInfo
{
	String name, number;
	PersonalNumInfo(String name, String number){
		this.name = name;
		this.number = number;
	}
	String getName(){ return name; }
	String getNumber(){ return number; }
}

class PersonaleNumberStorageImpl implements PersonaleNumberStorage { 
// interface�� PersonaleNumberStorage�� implements�����Ƿ� PersonaleNumberStorage �ȿ� �ִ�
// �޼ҵ带 ��� �������̵� �Ͽ� �ϼ��ؾ� �� - ���ϸ� ���� �߻�
	PersonalNumInfo[] perArr;
	int numOfPerInfo;
	public PersonaleNumberStorageImpl(int sz){
		perArr = new PersonalNumInfo[sz];
		numOfPerInfo = 0;
	}
	public void addPersonalInfo(String name, String perNum){
	// interface�� �ִ� abstract�޼ҵ带 �������̵��� ������ ������ public���� �����ؾ� ��
		perArr[numOfPerInfo] = new PersonalNumInfo(name, perNum);
		numOfPerInfo++;
	}
	public String searchName(String perNum){
	// interface�� �ִ� abstract�޼ҵ带 �������̵��� ������ ������ public���� �����ؾ� ��
		for (int i = 0; i < numOfPerInfo ; i++) {
			if (perNum.equals(perArr[i].getNumber())) {
				return perArr[i].getName();
			}
		}
		return null;
	}
}

class AbstractInterface2
{
	public static void main(String[] args) 
	{
		PersonaleNumberStorage storage = new PersonaleNumberStorageImpl(100);
		storage.addPersonalInfo("ȫ�浿", "950000-1122333");
		storage.addPersonalInfo("����ġ", "970000-1122333");

		System.out.println(storage.searchName("950000-1122333"));
		System.out.println(storage.searchName("970000-1122333"));
	}
}





/*
	interface PersonaleNumberStorage {
	 // �������� ������ ���� ����� ���� Ŭ������ �޼ҵ���� abstract �̹Ƿ� Ŭ������ abstract Ŭ����
		public abstract void addPersonalInfo(String name, String perNum);
		public abstract String searchName(String perNum);
		// �޼ҵ���� abstract �� ����Ǿ� ��ӹ޴� ����Ŭ�������� �����ϵ��� ��
	}

	class PersonalNumInfo {
	 // ���������� �����ϴ� Ŭ������ ��������� Ŭ���� - �� ���� �̸��� ��ȣ�� ����
		private	String name, number;
		public PersonalNumInfo(String name, String number){
			this.name = name;
			this.number = number;
		}
		String getName(){ return name; }
		String getNumber(){ return number; }
		// private ���� ����� �������� ������ ���� getter �޼ҵ��
	}


	class PersonaleNumberStorageImpl extends PersonaleNumberStorage { 
		// PersonaleNumberStorageŬ������ ��ӹ���
		// ��, PersonaleNumberStorageŬ������ abstractŬ�����̹Ƿ� 
		//  �ݵ�� PersonaleNumberStorageŬ���� ���� abstract�޼ҵ���� ��� �������̵� �ؾ���
		// �ϳ��� �������̵��� ���ϸ� PersonaleNumberStorageImplŬ������ abstract�� �����ؾ� ��.
		PersonalNumInfo[] perArr;
		int numOfPerInfo;
		public PersonaleNumberStorageImpl(int sz){
			perArr = new PersonalNumInfo[sz];
			// ������� ���������� ���� PersonalNumInfo �� �ν��Ͻ��� ������ �迭�� ���� 
			numOfPerInfo = 0;
			// perArr�� �ε����� ����ִ� ������ ������ ��Ÿ���� ����
		}
		public void addPersonalInfo(String name, String perNum){
			// abstractŬ������ personaleNumberStorage�� �� abstract�޼ҵ忴�� addPersonalInfo�� �������̵�	 ��.
			perArr[numOfPerInfo] = new PersonalNumInfo(name, perNum);
			numOfPerInfo++;
		}

		public String searchName(String perNum){
		// ����Ŭ������ abstract�޼ҵ带 �������̵��� �޼ҵ�
			for (int i = 0; i < numOfPerInfo ; i++) {
				if (perNum.equals(perArr[i].getNumber())) {
				// �μ��� �޾ƿ� ��ȣ�� perArr�迭�� i�ε����� �ش��ϴ� �ν��Ͻ��� getNumber()�޼ҵ�� ������	 ��ȣ�� ������
					return perArr[i].getName();
					// ������ �ش� �ν��Ͻ����� �̸��� �����Ͽ� �����ϰ� �޼ҵ� ����
				}
			}
			return null;
		}

	}

	class AbstractInterface2 {
		public static void main(String[] args) {
			PersonaleNumberStorage storage = new PersonaleNumberStorageImpl(100);
			// PersonaleNumberStorage�� abstractŬ�����̹Ƿ� �ν��Ͻ��� ������ personalNumberStorageImpl Ŭ	������ ��
			storage.addPersonalInfo("ȫ�浿", "950000-1122333");
			storage.addPersonalInfo("����ġ", "970000-1122333");

			System.out.println(storage.searchName("950000-1122333"));
			System.out.println(storage.searchName("970000-1122333"));
		}
	}
*/