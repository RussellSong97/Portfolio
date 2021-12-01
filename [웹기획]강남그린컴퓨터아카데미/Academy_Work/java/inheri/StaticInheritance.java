class Adder {
	public static int val = 0;
	public void add(int num) { val += num; }
}

class AddFriend extends Adder {
	public void friendAdd(int num) { val += num; }
	public void showVal() { System.out.println(val); }
}

class StaticInheritance {
	public static void main(String[] args) {
		Adder ad = new Adder();
		AddFriend af = new AddFriend();
		ad.add(1);
		af.friendAdd(3);
		AddFriend.val += 5;
		// static도 상속이 되어 하위클래스에서 상위클래스의 static멤버에 자유롭게 접근가능
		af.showVal();  // 9	
	}
}
