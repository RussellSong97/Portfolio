import java.util.*;

class HashMapEx1 {
	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<String, String>();
		// key와 value 모두 String 인스턴스만 저장받는 HashMap 생성
		map.put("test", "1234");
		map.put("abcd", "1111");
		map.put("abcd", "1234");
		// HashMap에 데이터 저장(동일한 키가 존재할 경우 나중에 입력한 값으로 덮어쓰기가 됨)

		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("id와 password를 입력하세요.");
			System.out.print("id : ");			String id = sc.nextLine().trim();
			System.out.print("password : ");	String pwd = sc.nextLine().trim();
			// trim() : 문자열의 양 끝에 있는 공백을 제거해주며, 기본적으로 사용자가 입력하는 문자열에는 사용하는 것이 좋다.
			System.out.println();

			if (!map.containsKey(id)) {
			// map.containsKey(id) : map의 키들중에 지정한 id와 동일한 키가 있는지 여부
				System.out.println("존재하지 않는 id입니다. 다시 입력하세요.");
			} else { 
				if (!map.get(id).equals(pwd)) {
				// map.get(id) : map에서 id 값을 키로 사용하는 값(value)
					System.out.println("비밀번호가 틀렸습니다. 다시 입력하세요.");
				} else {
					System.out.println("로그인 성공");
					break;
				}
			}
		}
	}
}
