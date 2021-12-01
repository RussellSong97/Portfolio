class ToStringData {
	private String name, city;
	public ToStringData(String name, String city) {
		this.name = name;
		this.city = city;
	}
	public String toString() {
		// Object 클래스의 메소드를 오버라이딩한것으로 객체출력 시 보여줄 데이터를 만들어서 문자열로 리턴
		String tmp = "이름 : " + name + " \n도시 : " + city;
		return tmp;
	}
}
class ToString {
	public static void main(String[] args)  {
		ToStringData ts = new ToStringData("홍길동", "서울");
		System.out.println(ts);		// ToStringData@28a418fc
	}
}
