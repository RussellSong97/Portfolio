class BasicBox {
	public void basicWrap() { System.out.println("basic wrap"); }
	public void wrap() { basicWrap(); }
}

class PaperBox extends BasicBox {
	public void paperWrap() { System.out.println("paper wrap"); }
	public void wrap() { paperWrap(); }
}

class GoldBox extends PaperBox {
	public void goldWrap() { System.out.println("gold wrap"); }
	public void wrap() { goldWrap(); }
}

class InstanceOf2 {
	public static void main(String[] args) {
		BasicBox box1 = new BasicBox();
		PaperBox box2 = new PaperBox();
		GoldBox  box3 = new GoldBox();

		wrapBox(box1);
		wrapBox(box2);
		wrapBox(box3);
	}
	public static void wrapBox(BasicBox box) {
		box.wrap();
	}
}
