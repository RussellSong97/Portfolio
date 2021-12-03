import java.util.*;

class AgeInputException extends Exception {
	public AgeInputException(){
		super("��ȿ���� �ʴ� ���̰� �ԷµǾ����ϴ�.");
	}
}

class ExceptionDefine2 {
	public static void main(String[] args) throws AgeInputException {
		// throws AgeInputException : �� �޼ҵ忡�� AgeInputException ���ܰ� �߻��ص� ó������ �ʰڴٴ� �ǹ̷� ����ó���� �� �޼ҵ带 ȣ���� ��(JVM)���� �ѱ�� ��� 
		//  - ����ó���� try-catch������ �ؾ� ��

		System.out.print("���̸� �Է��ϼ��� : ");
		int age = readAge();
		System.out.println("����� " + age + "�� �Դϴ�.");
	}

	public static int readAge() throws AgeInputException {

		Scanner sc = new Scanner(System.in);
		int age = sc.nextInt();
		if (age < 0) {			// ���̰� ������ ��������
			AgeInputException excpt = new AgeInputException();
			throw excpt;
		}
		return age;
	}
}