import java.util.*;

class HashSetEx1 {
	public static void main(String[] args) {
		Object[] objArr = {"1", new Integer(1), "2", "2", "3", "3", "4", "4", "4"};
		Set set = new HashSet();
		for (int i = 0; i < objArr.length ; i++ ) {
			set.add(objArr[i]);
			// objArr배열의 값들을 set에 추가(단, 중복되는 데이터는 하나만 추가)
		}

		System.out.println(set);	// [1, 1, 2, 3, 4]

		// -- loop 돌면서 하나씩 출력
		Iterator it = set.iterator();
		while (it.hasNext()){
			System.out.print(it.next() + " ");	// 1 1 2 3 4
		}
		// Set은 인덱스가 없으므로 for문을 사용하지 않고 Iterator를 이용하여 루프를 돌림
	}
}
