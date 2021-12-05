abstract class PersonalNumberStorage {
	public abstract void addPersonalInfo(String name, String pNum);
	public abstract String searchName(String pNum);
}

class PersonalNumInfo {
	String name, number;
	public PersonalNumInfo(String name, String pNum) {
		this.name = name;
		number = pNum;
	}
	String getName() { return name; } 
	String getNumber() { return number; } 
}

class PersonalNumberStorageImpl extends PersonalNumberStorage {
	PersonalNumberInfo[] perArr;
	int numOfPerInfo;
	public PersonalNumberStorageImpl(int sz) {
		perArr = new PersonalNumInfo[sz];
		numOfPerInfo = 0;
	}
	public void addPersonalInfo(String name, String pNum)  {
		perArr[numOfPerInfo] = new PersonalNumInfo(name, pNum);
		numOfPerInfo++;
	}
}

class AbstractInterface {
	public static void main(String[] args) {
		PersonalNumberStorage storage = new PersonalNumberStorageImpl(100);
		storage.addPersonalInfo("홍길동", "950000-1234567"); 
		storage.addPersonalInfo("전우치", "970000-1234567"); 

		System.out.println(storage.searchName("950000-1234567"));
		System.out.println(storage.searchName("970000-1234567"));
	}
}
