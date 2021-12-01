class Aa {
	public String toString() {
		return "class Aa";
	}
}

class Bb {
	public String toString() {
		return "class Bb"; 
	}
}

class InstanceTypeShow {
	int showCnt = 0;
	public <T> void showInstType(T inst) {
		// 현 메소드 호출시 메개변수의 자료형을 제네릭으로 지정하여 사용하겠다는 의미
		System.out.println(inst);
		showCnt++;
	}
	void showPrintCnt() { System.out.println("show count : " + showCnt ); }
}
class GenericMethod {
	public static void main(String[] args) {
		Aa a = new Aa();		Bb b = new Bb();
		InstanceTypeShow show = new InstanceTypeShow();

		show.<Aa>showInstType(a);	
		// showInstType()  메소드의 인수로 Aa형 인스턴스를 가져간다는 의미로 <Aa>는 생략해도 됨
		// 컴파일러가 메소드 호출 시 전달되는 인수의 자료형을 근거로 정보를 판단할 수 있으므로 생략가가능
		show.<Bb>showInstType(b);
		show.showPrintCnt();
	}
}
