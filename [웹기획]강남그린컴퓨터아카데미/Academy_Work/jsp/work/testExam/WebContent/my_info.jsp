<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
	#fdSet { width: 380px; }
	.info_tb { width:100%; }
	.info_tb input[type=text] { width:250px; } 
	
</style>
<script>
function onlyNum(obj) {
	//특정 컨트롤에 숫자만 입력되도록 하는 함수로 숫자가 아닌 값이 입력될 경우 값을 모두 삭제 시킴
	if (isNaN(obj.value) == true)  {
		obj.value = "";
	}
}

var arrImg 		= ["jpg", "gif", "png", "jpeg"];
var arrOffice	= ["hwp", "doc", "docx", "xls", "xlsx", "ppt", "pptx"];
function isFileExt(str, arrType) { 
	var arrTmp = str.split("\\");
	fileNameTmp =  arrTmp[arrTmp.length - 1]; 
	var ext = str.substring(str.lastIndexOf(".") + 1);
	for (var i = 0; i < arrType.length; i++ ) { if (ext == arrType[i]) return true; }
	return false;
} 

function checkForm() { 
	var dfrm = document.frm;		var msg = "";  
	var name = dfrm.name.value;		var birth = dfrm.birth.value;
	var file1 = dfrm.file1.value; 	var file2 = dfrm.file2.value;
	var file3 = dfrm.file3.value; 	var file4 = dfrm.file4.value;
	
	if (name == "") { alert("'이름'을 입력하세요");	return false; } 
	if (birth == "") { alert("'생년월일'을 입력하세요");	return false; } 
	if (file1 == "") { alert("'증명사진'의 파일을 선택하세요."); return false; } 
	else if(!isFileExt(file1, arrImg)) { 
		alert("'증명사진'의 파일은 [" + arrImg + "] 파일만 선택 가능합니다. 다시 선택해주세요."); return false;
	}  
	if (file2 == "") { alert("'이력서'의 파일을 선택하세요."); return false; } 
	else if(!isFileExt(file2, arrOffice)) { 
		alert("'이력서'의 파일은 [" + arrOffice + "] 파일만 선택 가능합니다. 다시 선택해주세요."); return false;
	}  
	if (file3 == "") { alert("'증빙서류1'의 파일을 선택하세요."); return false; } 
	else if(!isFileExt(file3, arrOffice)) { 
		alert("'증빙서류1'의 파일은 [" + arrOffice + "] 파일만 선택 가능합니다. 다시 선택해주세요."); return false;
	}  
	if (file4 == "") { alert("'증빙서류2'의 파일을 선택하세요."); return false; }
	else if(!isFileExt(file4, arrOffice)) { 
		alert("'증빙서류2'의 파일은 [" + arrOffice + "] 파일만 선택 가능합니다. 다시 선택해주세요."); return false;
	}   
	dfrm.submit(); 
}
</script>
</head>
<body>
<fieldset id="fdSet">
	<legend>개인 정보 등록 폼</legend>
	<form name="frm" action="my_info_proc.jsp" method="post" enctype="multipart/form-data" >
		<table class="info_tb" cellpadding="5" cellspacing="5">
			<tr>
				<th><label for="name">이름</label></th>
				<td><input type="text" name="name" placeholder="예) 홍길동" value="" />
			</tr>
			<tr>
				<th><label for="birth">생년월일</label></th>
				<td><input type="text" name="birth" placeholder="숫자만 입력 예)880504"  onkeyup="onlyNum(this);" maxlength="6" value="" />
			</tr>
			<tr>
				<th>증명사진</th>
				<td><input type="file" name="file1" id="file1" /></td>
			</tr>
			<tr>
				<th>이력서</th>
				<td><input type="file" name="file2" id="file2" /></td>
			</tr>
			<tr>
				<th>증빙서류1</th>
				<td><input type="file" name="file3" id="file3" /></td>
			</tr>
			<tr>
				<th>증빙서류2</th>
				<td><input type="file" name="file4" id="file4" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="정보 등록"  onclick="checkForm();"/>&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="reset" value="다시 입력" />
				</td>
			</tr>
		</table>
	</form>
</fieldset>	
</body>
</html>