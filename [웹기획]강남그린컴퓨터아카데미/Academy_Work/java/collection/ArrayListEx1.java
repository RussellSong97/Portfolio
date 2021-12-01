import java.util.*;

class ArrayListEx1 {
	public static void main(String[] args) {
		ArrayList list1 = new ArrayList(10);
		list1.add(new Integer(5));		list1.add(new Integer(4));		list1.add(new Integer(2));
		list1.add(new Integer(0));		list1.add(new Integer(1));		list1.add(new Integer(3));
		// list1에 Integer클래스의 인스턴스를 저장 - 5 4 2 0 1 3

		ArrayList list2 = new ArrayList(list1.subList(1, 4));
		// list1의 1번 인덱스 부터 4 - 1 인덱스까지 잘라내어 list2로 생성

		print(list1, list2);	// list1 : [5, 4, 2, 0, 1, 3], list2 : [4, 2, 0]
		
		System.out.println("Collections.sort 후 print");
		Collections.sort(list1);		Collections.sort(list2);	// Collections 클래스의 sort() 메소드를 이용하여 오름차순 정렬을 함
		print(list1, list2);	// list1 : [0, 1, 2, 3, 4, 5], list2 : [0, 2, 4] 
		
		System.out.println("list1.containsAll(list2) : " + list1.containsAll(list2));		// true
		// list1.containsAll(list2) : list1에 list2의 데이터들이 모두 들어 있는지 여부

		list2.add("B");		list2.add("C");		list2.add(3, "A");
		// list2의 맨 뒤에 "B" 와 "C" 추가			list2의 3번 인덱스 자리에 "A"를 추가 (기존 데이터는 뒤로 밀림)
		print(list1, list2);	// list1 : [0, 1, 2, 3, 4, 5],  list2 : [0, 2, 4, A, B, C]

		list2.set(3, "AA");
		// list2의 3번 인덱스 자리에 "AA"를 덮어쓰기
		print(list1, list2);	// list1 : [0, 1, 2, 3, 4, 5],  list2 : [0, 2, 4, AA, B, C]
		
		System.out.println("list1.retainAll(list2) : " + list1.retainAll(list2));			// true
		// list1.retainAll(list2) : list1에서 list2의 데이터와 동일한 데이터들만 남기고 나머지는 삭제
		// true : list1에서 삭제한 데이터가 있다는 의미
		System.out.println("list1.retainAll(list2) : " + list1.retainAll(list2));			// false
		// false : list1에서 삭제한 데이터가 없다는 의미
		print(list1, list2);	// list1 : [0, 2, 4], list2 : [0, 2, 4, AA, B, C]


		for (int i = list2.size() - 1 ; i >= 0 ; i-- ) {
			// list2.size() : list2에 들어있는 데이터의 개수로 1을 빼면 데이터가 들어 있는 마지막 인덱스가 됨
			if (list1.contains(list2.get(i))) {
				// list1.contains(list2.get(i)) : list1에 list2의 i인덱스에 해당하는 데이터가 들어 있으면 
				list2.remove(i);	// list2의 i인덱스에 해당하는 데이터 삭제
			}
		}	// 여러 데이터 삭제시 거꾸로 루프를 돌면서 삭제하는 것이 효율적
		// 단, 본 예제는 removeAll() 메소드를 사용해도 됨
		print(list1, list2); // list1 : [0, 2, 4], list2 : [AA, B, C]
	}

	public static void print(ArrayList list1, ArrayList list2) {
		System.out.println("list1 : " + list1);
		System.out.println("list2 : " + list2);
		System.out.println();
	}
}