import java.util.*;

class IteratorListEx {
	public static void main(String[] args) {
		ArrayList orig = new ArrayList(10);
		ArrayList copy1 = new ArrayList(10);
		ArrayList copy2 = new ArrayList(10);

		for (int i = 0; i < 10; i++) orig.add(i + "");

		Iterator it = orig.iterator();
		while (it.hasNext()){
			copy1.add(it.next());
		}

		System.out.println("= orig에서 copy1로 복사 =");
		System.out.println("orig : " + orig + "\ncopy1 : " + copy1);			
		// orig : [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		// copy1 : [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

		it = orig.iterator();	// iterator는 재사용이 안되므로 다시 사용하려면 재생성해야 함
		while (it.hasNext()){
			copy2.add(it.next());
			it.remove();	// next()로 불러온 요소를 삭제
		}

		System.out.println();
		System.out.println("= orig에서 copy2로 이동 =");
		System.out.println("orig : " + orig + "\ncopy2 : " + copy2);
		// orig : []
		// copy2 : [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

		ListIterator lit = copy1.listIterator();
		
		System.out.println();
		System.out.println("= ListIterator 순방향 출력 =");

		while (lit.hasNext()){
			System.out.print(lit.next() + " ");			// 0 1 2 3 4 5 6 7 8 9
		} 

		System.out.println();
		System.out.println();
		System.out.println("= ListIterator 역방향 출력 =");
		while (lit.hasPrevious()){
			System.out.print(lit.previous() + " ");	 	// 9 8 7 6 5 4 3 2 1 0
		}
		
	}
}
