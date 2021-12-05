/*
abstract method : 메소드의 선언부만 있고, 정의되지 못한 메소드
	 ex) public abstract 리턴타입 메소드명(); -> 몸통(중괄호 부분)이 없음
 - 사용하는 이유 : 상속의 관계를 형성시키기 위한 상위클래스로 사용
 - 클래스이나 인스턴스를 생성할 수는 없음

 추상화 클래스
  - abstract class를 상속받은 클래스는 무조건 abstract class의 abstract method를 오버라이딩해서 구현
	구현하지 못하면 상속받은 하위클래스도 abstract class로 선언해야함
  - 문법
  abstract class 클래스명 {하나 이상의 abstract 메소드, ...}
*/

interface PersonaleNumberStorage {
	void addPersonalInfo(String name, String perNum);
	String searchName(String perNum);
	// interface 에서 선언하는 모든 메소드는 자동으로 public abstract 로 선언됨
	// interface에서는 절대 구현된 메소드를 정의할 수 없고 무조건 public abstract 로
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
// interface인 PersonaleNumberStorage를 implements했으므로 PersonaleNumberStorage 안에 있는
// 메소드를 모두 오버라이딩 하여 완성해야 함 - 안하면 오류 발생
	PersonalNumInfo[] perArr;
	int numOfPerInfo;
	public PersonaleNumberStorageImpl(int sz){
		perArr = new PersonalNumInfo[sz];
		numOfPerInfo = 0;
	}
	public void addPersonalInfo(String name, String perNum){
	// interface에 있는 abstract메소드를 오버라이딩한 것으로 무조건 public으로 지정해야 함
		perArr[numOfPerInfo] = new PersonalNumInfo(name, perNum);
		numOfPerInfo++;
	}
	public String searchName(String perNum){
	// interface에 있는 abstract메소드를 오버라이딩한 것으로 무조건 public으로 지정해야 함
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
		storage.addPersonalInfo("홍길동", "950000-1122333");
		storage.addPersonalInfo("전우치", "970000-1122333");

		System.out.println(storage.searchName("950000-1122333"));
		System.out.println(storage.searchName("970000-1122333"));
	}
}





/*
	interface PersonaleNumberStorage {
	 // 개인정보 저장을 위한 기능을 가진 클래스로 메소드들이 abstract 이므로 클래스도 abstract 클래스
		public abstract void addPersonalInfo(String name, String perNum);
		public abstract String searchName(String perNum);
		// 메소드들이 abstract 로 선언되어 상속받는 하위클래스에서 구현하도록 함
	}

	class PersonalNumInfo {
	 // 개인정보를 저장하는 클래스로 정보저장용 클래스 - 한 명의 이름과 번호를 저장
		private	String name, number;
		public PersonalNumInfo(String name, String number){
			this.name = name;
			this.number = number;
		}
		String getName(){ return name; }
		String getNumber(){ return number; }
		// private 으로 선언된 변수들의 참조를 위한 getter 메소드들
	}


	class PersonaleNumberStorageImpl extends PersonaleNumberStorage { 
		// PersonaleNumberStorage클래스를 상속받음
		// 단, PersonaleNumberStorage클래스가 abstract클래스이므로 
		//  반드시 PersonaleNumberStorage클래스 내의 abstract메소드들을 모두 오버라이딩 해야함
		// 하나라도 오버라이딩을 안하면 PersonaleNumberStorageImpl클래스도 abstract로 선언해야 함.
		PersonalNumInfo[] perArr;
		int numOfPerInfo;
		public PersonaleNumberStorageImpl(int sz){
			perArr = new PersonalNumInfo[sz];
			// 사람들의 개인정보를 담은 PersonalNumInfo 형 인스턴스를 저장할 배열을 생성 
			numOfPerInfo = 0;
			// perArr의 인덱스겸 들어있는 데이터 개수를 나타내는 변수
		}
		public void addPersonalInfo(String name, String perNum){
			// abstract클래스인 personaleNumberStorage의 의 abstract메소드였던 addPersonalInfo를 오버라이딩	 함.
			perArr[numOfPerInfo] = new PersonalNumInfo(name, perNum);
			numOfPerInfo++;
		}

		public String searchName(String perNum){
		// 상위클래스의 abstract메소드를 오버라이딩한 메소드
			for (int i = 0; i < numOfPerInfo ; i++) {
				if (perNum.equals(perArr[i].getNumber())) {
				// 인수로 받아온 번호와 perArr배열의 i인덱스에 해당하는 인스턴스의 getNumber()메소드로 추출한	 번호가 같으면
					return perArr[i].getName();
					// 같으면 해당 인스턴스에서 이름을 추출하여 리턴하고 메소드 종료
				}
			}
			return null;
		}

	}

	class AbstractInterface2 {
		public static void main(String[] args) {
			PersonaleNumberStorage storage = new PersonaleNumberStorageImpl(100);
			// PersonaleNumberStorage는 abstract클래스이므로 인스턴스의 생성은 personalNumberStorageImpl 클	래스로 함
			storage.addPersonalInfo("홍길동", "950000-1122333");
			storage.addPersonalInfo("전우치", "970000-1122333");

			System.out.println(storage.searchName("950000-1122333"));
			System.out.println(storage.searchName("970000-1122333"));
		}
	}
*/