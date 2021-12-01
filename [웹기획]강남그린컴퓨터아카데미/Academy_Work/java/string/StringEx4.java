class StringEx4 {
	public static void main(String[] args) {
		String file1 = "test.jpg";
		String file2 = "test.gif";
		String file3 = "test.zip";
		String file4 = "test.docx";

		isImg(file1);	isImg(file2);	isImg(file3);	isImg(file4);	
	}

	public static void isImg(String file) {
		String ext = file.substring(file.lastIndexOf('.') + 1);
		// 파일명의 마지막 점의 위치 다음부터 끝까지 잘라내어 ext에 저장함(확장자)
		switch (ext) {
			case "jpg" : case "gif" : case "png" : case "jpeg" :
				System.out.println(file + " 파일은 이미지 파일입니다.");
				break;
			default:
				System.out.println(file + " 파일은 이미지 파일이 아닙니다.");
		
		}
	}
}
