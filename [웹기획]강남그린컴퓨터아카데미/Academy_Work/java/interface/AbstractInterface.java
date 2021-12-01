abstract class PersonalNumberStorage {
// 개인 정보저장을 위한 기능을 가진 클래스로 메소드들이 abstract이므로 클래스도 abstract클래스
	public abstract void addPersonalInfo(String name, String pNum);
	public abstract String searchName(String pNum);
	// 메소드들이 abstract로 선언되어 상속받는 하위클래스에서 구현하도록 함
}

/*
	개인정보를 저장하는 클래스로 정보저장용 클래스 - 한 명의 이름과 번호를 저장
	String name, number
	생성자, getter
*/
class PersonalNumInfo {
	String name, number;
	public PersonalNumInfo(String name, String pNum) {
		this.name = name;
		number = pNum;
	}
	String getName() { return name; } 
	String getNumber() { return number; } 
	// private으로 선언된 변수들의 참조를 위한 getter 메소드들
}

class PersonalNumberStorageImpl extends PersonalNumberStorage {
// abstract 클래스인 PersonalNumberStorage 를 상속받았으므로 반드시 PersonalNumberStorage 안에 있는 
// abstract 메소드를 모두 오버라이딩하여 완성해야 함 - 안할거면 현 클래스도 abstract 로 선언해야 함
	PersonalNumInfo[] perArr;
	int numOfPerInfo;
	public PersonalNumberStorageImpl(int sz) {
		perArr = new PersonalNumInfo[sz]; // 사람들의 개인정보를 담은 PersonalNumInfo형 인스턴스를 저장할 배열을 생성
		numOfPerInfo = 0;	// perArr의 인덱스겸 들어있는 데이터 개수를 나타내는 변수
	}
	public void addPersonalInfo(String name, String pNum)  {	// 상위클래스의 abstract 메소드를 오버라이딩한 메소드
		perArr[numOfPerInfo] = new PersonalNumInfo(name, pNum);	// 받아온 이름과 번호를 이용하여 PersonalNumInfo형 인스턴스를 생성한 후 perArr 배열에 저장
		numOfPerInfo++;
	}
	public String searchName(String pNum) {						// 상위클래스의 abstract 메소드를 오버라이딩한 메소드
		for (int i = 0; i < numOfPerInfo ; i++ ) {
			if (pNum.equals(perArr[i].getNumber())) {			
				// 인수로 받아온 번호와 perArr배열의 i 인덱스에 해당하는 인스턴스의 getNumber()메소드로 추출한 번호가 같으면
				return perArr[i].getName();
				// 같으면 해당 인스턴스에서 이름을 추출하여 리턴하고 메소드 종료
			}
		}
		return null;	// 찾는 값이 없으면 null을 리턴
		// null은 비어있는 객체(인스턴스)를 의미하므로 리턴타입이 기본자료형만 아니면 어떤 리턴타입이라도 사용할 수 있음
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
