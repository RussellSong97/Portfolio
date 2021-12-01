<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="./css/reset.css">
<link rel="stylesheet" type="text/css" href="./css/style.css">
<script src="https://kit.fontawesome.com/05383ba415.js" crossorigin="anonymous"></script>
<script src="./jquery-3.6.0.js"></script>
<!--  카카오톡 로그인 API 영역 시작 -->
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script>
	window.Kakao.init('deba720baa8ab03aacc2c032ca0d19e2');
	function kakaoLogin(){
		window.Kakao.Auth.login({
			scope:'profile_nickname, profile_image, account_email, birthday',
			success: function(authObj){
				console,lof(authObj);
				window.Kakao.API.request({
					url:'/v2/user/me',
					success: res => { 
						const kakao_account = res.kakao_account;
						console.log(kakao_account);
					}
				});
			}
		});
	}
</script>
<!--  카카오톡 로그인 API 영역 종료 -->
<script>
	$(document).ready(function() {
		$("#first_introduce").one("click", function() {
			$('#first_introduce').fadeOut(1000);
			$('#second_introduce').fadeIn(5000);
			$('#login_select').fadeIn(10000);
		});
	});
	
</script>
</head>
<body>
<div>
	<p class="font" id="first_introduce">처음이신 건가요?</p>
	<p class="font" id="second_introduce">서비스를 사용하고 싶으시면 로그인 하셔야 합니다.</p>
	<ul class="font" id="login_select">
		<li class="font" id="origin"><a href="./member/memJoin.jsp">회원가입</a></li>
		<li class="font" id="kakao"><a href="javascript:kakaoLogin();">카카오톡</a></li>
		<li class="font" id="naver"><a href="#">네이버</a></li>
		<li class="font" id="login"><br /><br />이미 회원이시면<br /><a href="#">로그인</a></li>
	</ul>	
</div>
</body>
</html>