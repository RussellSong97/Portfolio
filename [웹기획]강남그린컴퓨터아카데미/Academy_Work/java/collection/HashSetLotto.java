import java.util.*;

class HashSetLotto {
	public static void main(String[] args) {
		// HashSet을 이용하여 1~45사이의 정수 6개를 중복되지 않게 저장 후 출력 - 정수는 랜덤
		Set set = new HashSet();
		for (int i = 0; set.size() < 6 ; i++ ) {	// set에 6개의 숫자가 저장될 때까지 루프를 돔
			int num = (int)(Math.random() * 45) + 1;
			set.add(new Integer(num));
			// 랜덤으로 추출한 숫자를 set에 저장(중복된 값이 있으면 저장이 안됨)
		}
		System.out.println(set);

		// set에 들어있는 숫자들을 오름차순 정렬하여 출력
		List list = new LinkedList(set);
		// Set인 set을 List로 변환하여 list를 생성
		Collections.sort(list);
		// Collections 클래스의 sort() 메소드는 매개변수의 자료형이 List이므로 set을 바로 쓰지 않고 list로 변경하여 정렬
		System.out.println(list);
	}
}
