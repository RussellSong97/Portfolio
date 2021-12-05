class BufferBuilder2 {
	public static void main(String[] args) {
		String mail1 = "abcd", mail2 = "@", mail3 = "naver.com";
		/* mail1에 완성된 메일주소가 들어가도록 StringBuilder를 이용하여 작업 */
		StringBuilder sb = new StringBuilder(mail1);
		sb.append(mail2).append(mail3);
		String mail = new String(sb);
		System.out.println(mail); 
	}
}
