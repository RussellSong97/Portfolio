class ToStringData {
	private String name, city;
	public ToStringData(String name, String city) {
		this.name = name;
		this.city = city;
	}
	public String toString() {
		// Object Ŭ������ �޼ҵ带 �������̵��Ѱ����� ��ü��� �� ������ �����͸� ���� ���ڿ��� ����
		String tmp = "�̸� : " + name + " \n���� : " + city;
		return tmp;
	}
}
class ToString {
	public static void main(String[] args)  {
		ToStringData ts = new ToStringData("ȫ�浿", "����");
		System.out.println(ts);		// ToStringData@28a418fc
	}
}
