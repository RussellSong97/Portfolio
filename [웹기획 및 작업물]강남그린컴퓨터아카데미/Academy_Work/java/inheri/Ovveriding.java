class Speaker {
	private int volumn;
	public void showCurrentState() {
		System.out.println("���� ũ��: " + volumn);
	}

	public void setVolume(int vol) {
		volumn = vol;
	}
}

class BaseSpeaker extends Speaker {
	private int base;
	public void showCurrentState() {		// ����Ŭ������ �޼ҵ带 �������̵� �� ��
		super.showCurrentState();
		// �������̵��� �޼ҵ忡���� ����Ŭ������ ���� �޼ҵ带 ȣ���� �� ����
		System.out.println("���̽� ũ�� : " + base);
	}
	public void setBase(int base) {
		this.base = base;
	}
}

class Ovveriding {
	public static void main(String[] args) {
		BaseSpeaker bs = new BaseSpeaker();
		bs.setVolume(10);
		bs.setBase(20);
		bs.showCurrentState();
	}
}
