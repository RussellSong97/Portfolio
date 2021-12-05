import java.util.*;

class AgeInputException extends Exception{
	//Exception 클래스를 상속 받았음으로 AgeInputException클래스는 예외클래스임
	// 나이입력시 유효하지 않은 값이 들어왔을ㅇ 경우 발생시킬 예외클래스
	public AgeInputException(){
		super("유효하지 않는 나이가 입력되었습니다.");
		// 상위클래스인 Exception 클래스의 생성자를 호출하면서 
		// 예외 발생시 보여줄 메시지를 인수로 가져감
	}
}

class ExceptionDefine {
	public static void main(String[] args) {
		System.out.print("나이를 입력하세요 : ");
		try {
			int age = readAge();
			System.out.println("당신은 " + age + "세 입니다.");
		} catch (AgeInputException e) {
			System.out.println(e.getMessage());
		}
	}

	public static int readAge() throws AgeInputException{
		// throws AgeInputException : 현 메소드에서 AgeInputException 예외가 발생해도 처리하지 않겠다는 의미로
		// 예외처리를 현 메소드를 호출한 곳으로 넘기는 명령 
		//  - 예외처리는 try-catch문으로 해야 함

		Scanner sc = new Scanner(System.in);
		int age = sc.nextInt();
		if (age < 0){
			// 나이가 음수로 들어왔으면
			AgeInputException excpt = new AgeInputException();
			// AgeInputException 형 인스턴스 excpt를 생성하는 것이자 AgeInputException 예외를 생성하는 것
			// 예외발생이 아니라 생성하는 것
			throw excpt;
			// excpt예외가 발생하였음을 JVM에 알리고, 예외처리 매카니즘을 실행 
			// throw excpt가 실행되면 JVM의 예외처리 메카니즘이 동작하게 됨
		}
		return age;
	}
}