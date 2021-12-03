interface TV {
	public void onTV();
}

class TVImpl {
	public void onTV() { System.out.println("영상 출력 중"); }
}


interface Computer {
	public void dataReceive();
}

class ComputerImpl {
	public void dataReceive() { System.out.println("영상 데이터 수신 중"); }
}

class IPTV implements TV , Computer { 
	ComputerImpl	com = new ComputerImpl();
	TVImpl			tv	= new TVImpl();

	public void dataReceive() {
		com.dataReceive();
	}

	public void onTV() {
		tv.onTV();
	}

	public void powerOn() { 
		dataReceive();
		onTV();
	}
}

class MultiInheri2 {
	public static void main(String[] args) {
		IPTV iptv = new IPTV();
		System.out.println("--- iptv ---");
		iptv.powerOn();

		TV tv = iptv;
		Computer com = iptv; 

		((IPTV)tv).powerOn();
		((IPTV)com).powerOn(); 
	}
}
