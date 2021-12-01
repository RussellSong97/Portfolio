class TV {
	public void onTV() { System.out.println("영상 출력 중"); }
}

interface Computer {
	public void dataReceive();
}

class ComputerImpl {
	public void dataReceive() { System.out.println("영상 데이터 수신 중"); }
}

class IPTV extends TV implements Computer { 
	ComputerImpl com = new ComputerImpl();

	public void dataReceive() {
		com.dataReceive();
	}

	public void powerOn() { 
		dataReceive();
		onTV();
	}
}

class MultiInheri1 {
	public static void main(String[] args) {
		IPTV iptv = new IPTV();
		iptv.powerOn();

		TV tv = iptv;
		Computer com = iptv; 
	}
}
