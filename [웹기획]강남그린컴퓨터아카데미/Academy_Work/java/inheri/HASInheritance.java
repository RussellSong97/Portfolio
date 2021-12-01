class Gun {
	int bullet;
	public Gun(int num) { bullet = num; }
	public void shot() {
		System.out.println("BBANG!!");
		bullet--;
	}
}

class Police extends Gun {	// 모든 Police는 Gun을 소유한다는 관계가 성립할 때 가능한 상속
	// 만약 Gun을 소유하지 않는 Police가 있고, 그 인스턴스를 생성한다면 문제가 됨
	int handcuff;
	public Police(int num,  int bcuff) {
		super(num);
		handcuff = bcuff;
	}
	public void putHandcuff() {
		System.out.println("SNAP!");
		handcuff--;
	}
}

class HASInheritance {
	public static void main(String[] args) {
		Police pman = new Police(5, 3);
		pman.shot();
		pman.putHandcuff();
	}
}
