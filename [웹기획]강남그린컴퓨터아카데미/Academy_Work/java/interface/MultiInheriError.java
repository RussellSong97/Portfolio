class TV {
	public void onTV() { System.out.println("영상 출력 중"); }
}

class Computer {
	public void dataReceive() { System.out.println("영상 데이터 수신 중"); }
}

class IPTV extends TV, Computer {
	// 자바에서는 다중상속을 지원하지 않으므로 오류 발생
	public void powerOn() { 
		dateReceive();
		onTV();
	}
}

class MultiInheriError {
	public static void main(String[] args) {
		IPTV iptv = new IPTV();
		iptv.powerOn();

		TV tv = iptv;
		Computer com = iptv; 
	}
}
