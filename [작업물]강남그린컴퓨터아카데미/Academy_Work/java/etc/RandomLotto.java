/* Random클래스를 이용하여 로또번호 생성기 작업(1~45까지의 숫자 6개, 중복불가) */
import java.util.*;

class RandomLotto {
	public static void main(String[] args) {
		int [] arrLotto = new int[6];
		Random rnd = new Random();

		for (int i = 0; i < arrLotto.length ; i++) {
			arrLotto[i] = rnd.nextInt(45) + 1;	// 0 ~ 44 + 1 => 1 ~ 45
			for (int j = 0; j < i ; j++ ) {
				if (arrLotto[i] == arrLotto[j]) {
					i--; break;
				}
			}
		}

		for (int i = 0; i < arrLotto.length ; i++) {
			System.out.print(arrLotto[i] + " ");
		}

	}
}
