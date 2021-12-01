class BasicBox {
	public void basicWrap() { System.out.println("basic wrap"); }
}

class PaperBox extends BasicBox {
	public void paperWrap() { System.out.println("paper wrap"); }
}

class GoldBox extends PaperBox {
	public void goldWrap() { System.out.println("gold wrap"); }
}

class InstanceOf {
	public static void main(String[] args) {
		BasicBox box1 = new BasicBox();
		PaperBox box2 = new PaperBox();
		GoldBox  box3 = new GoldBox();

		wrapBox(box1);
		wrapBox(box2);
		wrapBox(box3);
	}
	public static void wrapBox(BasicBox box) {
		// 매개변수를 BasicBox로 선언했기 때문에 하위클래스인 PaperBox와 GoldBox형 인스턴스도 받을 수 있음

		// instanceof 연산의 조건 검색은 최하위클래스 부터 해야함
		if (box instanceof GoldBox) {
			// 받아온 box 인스턴스가 GoldBox형으로 형변환이 가능하면 
			((GoldBox)box).goldWrap();
			// box를 GoldBox형으로 형변환 후 goldWrap() 메소드 호출
		} else if (box instanceof PaperBox) {
			// 받아온 box 인스턴스가 PaperBox형으로 형변환이 가능하면
			((PaperBox)box).paperWrap();
			// box를 PaperBox형으로 형변환 후 paperWrap() 메소드 호출
		} else {
			box.basicWrap();
			// box는 BasicBox형이므로 그 안의 basicWrap() 메소드 호출
		}
		
	}
}
