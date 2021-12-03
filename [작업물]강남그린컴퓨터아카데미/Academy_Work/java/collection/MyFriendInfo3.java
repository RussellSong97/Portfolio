/*
인명록 : MyFriendInfo2 : 모든 인스턴스 변수는 private으로 선언
사람 : Friend : 이름, 성별, 나이, 전화
학교 인맥 : SchoolFriend : 학교, 졸업연도, 전공
사회 인맥 : SocialFriend : 회사, 소속, 직급

1. 학교 인맥 추가
2. 사회 인맥 추가
3. 전체 정보 출력
4. 기본 정보 출력 - 이름, 성별, 나이, 학교(회사)
5. 이름 검색 - 이름을 받아 같은 이름의 사람 정보 전체를 보여주기
   (두 명 이상일 경우 모두 보여주기1, 첫번째로 일치하는 사람만 보여주기2)
6. 이름 검색(마지막 일치자 검색)
9. 프로그램 종료
*/
import java.util.*;

class Friend {
   private String name, gender, phone;
   private int age;
   public Friend(String name, String gender, String phone, int age) {
      this.name = name;      this.gender = gender;
      this.phone = phone;      this.age = age;
   }
   public void showAllData() {
      System.out.println("이름 : " + name);
      System.out.println("성별 : " + gender + " / 나이 : " + age);
      System.out.println("전화 : " + phone);
   }
   public void showBasicData() {}
   public String getName() { return name; }
   public String getGender() { return gender; }
   public String getPhone() { return phone; }
   public int getAge() { return age; }
}

class SchoolFriend extends Friend {
   private String school, major;
   private int year;
   public SchoolFriend(String n, String g, String p, int a, String s, String m, int y) {
      super(n, g, p , a);
      school = s;      major = m;      year = y;
   }
   public void showAllData() {
      super.showAllData();
      System.out.println("학교 : " + school);
      System.out.println("전공 : " + major);
      System.out.println("졸업 : " + year);
   }
   public void showBasicData() {
      System.out.println("이름 : " + getName());
      System.out.println("성별 : " + getGender() + " / 나이 : " + getAge());
      System.out.println("학교 : " + school);
   }
}

class SocialFriend extends Friend {
   private String company, division, position;
   public SocialFriend(String n, String g, String p, int a, String c, String d, String position) {
      super(n, g, p , a);
      company = c;      division = d;      this.position = position;
   }
   public void showAllData() {
      super.showAllData();
      System.out.println("회사 : " + company);
      System.out.println("소속 : " + division);
      System.out.println("직급 : " + position);
   }
   public void showBasicData() {
      System.out.println("이름 : " + getName());
      System.out.println("회사 : " + company);
      System.out.println("소속 : " + division);
   }
}

class FriendHandler {
   private ArrayList<Friend> friends;
   // 친구정보 인스턴스를 저장할 friends 선언 (Friend형 인스턴스만 저장할 수 있도록 제네릭 설정)
   // 컬렉션 선언 및 생성 시 제네릭으로 자료형을 지정하면 Object형이 아닌 지정한 자료형 데이터로 인식하게 되어 형변환이 필요없음
   public FriendHandler(int num) {
      friends = new ArrayList<Friend>(num);
   }
   public void friendAdd(int menu) {
      String name, gender, phone, school, major, company, division, position;
      int age, year;

      Scanner sc = new Scanner(System.in);
      System.out.print("이름 : ");      name = sc.nextLine();
      System.out.print("나이 : ");      age = sc.nextInt();
      sc.nextLine();   // nextInt() 뒤에 nextLine()사용시 빈 nextLine()을 하나 해줘야 함
      System.out.print("성별 : ");      gender = sc.nextLine();
      System.out.print("전화 : ");      phone = sc.nextLine();

      if (menu == 1) {
         System.out.print("학교 : ");      school = sc.nextLine();
         System.out.print("전공 : ");      major = sc.nextLine();
         System.out.print("졸업 : ");      year = sc.nextInt();

         friends.add(new SchoolFriend(name, gender, phone, age, school, major, year));
      } else {
         System.out.print("회사 : ");      company = sc.nextLine();
         System.out.print("소속 : ");      division = sc.nextLine();
         System.out.print("직급 : ");      position = sc.nextLine();

         friends.add(new SocialFriend(name, gender, phone, age, company, division, position));
      }
      System.out.println("입력완료!!\n");
   }
   public void showAllData() {
      for (int i = 0 ; i < friends.size() ; i++) {
         friends.get(i).showAllData();
         System.out.println("전체 친구 정보");
      }
   }
   public void showBasicData() {
      for (int i = 0 ; i < friends.size() ; i++) {
         friends.get(i).showBasicData();
         System.out.println("전체 친구 정보");
      }
   }
   public void searchData(int num) {
      if (friends.size() == 0) {   // 배열에 저장된 사람이 하나도 없을 경우
         System.out.println("검색할 대상이 없습니다.");
         return;
      }

      String schName;      // 검색어로 사용될 이름을 저장할 변수
      Scanner sc = new Scanner(System.in);
      System.out.print("검색할 이름 : ");      schName = sc.nextLine();

      boolean isSearch = false;   // 검색 성공여부를 저장할 변수
      if (num == 5) {   // 순방향 검색(첫번째 일치하는 이름 찾기)
         for (int i = 0 ; i < friends.size() ; i++) {
            if (schName.equals(friends.get(i).getName())) {
               friends.get(i).showAllData();
               isSearch = true;
               break;   // 이름이 같은 사람이 여럿 있어도 한 명만 추출할 때 사용
            }
         }
      } else {   // 역방향 검색(마지막 일치하는 이름 찾기)
         for (int i = friends.size() - 1 ; i >= 0 ; i--) {
            if (schName.equals(friends.get(i).getName())) {
               friends.get(i).showAllData();
			   // ((Friend)friends.get(i))
               isSearch = true;
               break;
            }
         }
      }

      if (!isSearch) {
         System.out.println("찾는 이름이 없습니다.");
         searchData(num);
         // 자기 자신을 다시 호출하는 것을 재귀호출이라 함
         // 재귀호출 사용시 반드시 빠져나올 수 있는 조건을 사용해야 함
      }
   }
}

class MyFriendInfo3 {
   public static void main(String[] args) {
      FriendHandler fh = new FriendHandler(10);
      Scanner sc = new Scanner(System.in);
      while (true) {
         System.out.println("*** 메뉴 선택 ***");
         System.out.println("1. 학교 친구 저장");
         System.out.println("2. 대학 친구 저장");
         System.out.println("3. 전체 정보 출력");
         System.out.println("4. 기본 정보 출력");
         System.out.println("5. 이름 검색");
         System.out.println("6. 이름 검색(마지막 일치자 검색)");
         System.out.println("9. 프로그램 종료");
         System.out.print("메뉴 선택 >> ");

         int num = sc.nextInt();
         switch (num) {
            case 1: case 2:
               fh.friendAdd(num);   break;
            case 3:
               fh.showAllData();   break;
            case 4:
               fh.showBasicData();   break;
            case 5: case 6:
               fh.searchData(num);   break;
            case 9:
               System.out.println("프로그램 종료");   return;
         }
      }
   }
}