class Computer {
	String owner;
	public Computer(String name) { owner = name; }
	public void calculate() { System.out.println("요청 내용을 계산합니다."); }
}

class Notebook extends Computer {	// Notebook은 일종의 Computer이다.
	// computer 클래스를 상속받으므로써 Computer클래스의 기능들을 기본적으로 갖추고 있는 Notebook클래스가 됨
	int battery;
	public Notebook(String name, int chag) {
		super(name);
		battery = chag;
	}
	public void charging() { battery += 5; }
	public void movingCal() {
		if (battery < 1 ){
			System.out.println("충전이 필요합니다.");
			return;
		}
		System.out.print("이동하면서 ");
		calculate();
		battery--;
	}
}

class TabletNote extends Notebook {
	String pen;
	public TabletNote(String name, int chag, String pen) {
		super(name, chag);
		this.pen = pen;
	}
	public void write(String penInfo) {
		if (battery < 1 ){
			System.out.println("충전이 필요합니다."); return;
		}
		if (pen.equals(penInfo)) {
			System.out.println("등록된 펜이 아닙니다."); return;
		}
		System.out.println("필기 내용을 처리합니다."); 
		battery--;
	}
}

class ISAInheritance {
	public static void main(String[] args) {
		Notebook nt = new Notebook("홍길동", 5);
		TabletNote tn = new TabletNote("홍길동", 5, "ISE-123-456");

		nt.movingCal();
		tn.write("ISE-123-456");
	}
}
