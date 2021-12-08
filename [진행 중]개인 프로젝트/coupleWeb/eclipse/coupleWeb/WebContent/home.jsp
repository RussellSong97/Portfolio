<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="./css/reset.css">
<link rel="stylesheet" type="text/css" href="./css/main.css">
<script src="https://kit.fontawesome.com/05383ba415.js" crossorigin="anonymous"></script>
<script src="./jquery-3.6.0.js"></script>
 
</head>
<body>
<h2>커플 기념일 및 다이어리 기능 사이트</h2>
<!-- 메뉴 영역 시작 -->
<nav class="navbar">
	<div class="logo">
		<a href="#"><input type="image" src="./img/heart.png" alt="로고" width="100" height="50"/></a>
	</div>
		<ul class="navbar_menu">
			<li><a href="#">Home</a></li>
			<li><a href="#">Date</a></li>
			<li><a href="#">Photo</a></li>
			<li><a href="#">Diary</a></li>
			<li><a href="#">Setting</a></li>
			<li><a href="#"><i class="far fa-bell"></i></a></li>
		</ul>
</nav>
<!-- 메뉴 영역 종료 -->


<!-- 내용 영역 시작 -->
<div class="content">
	<ul class="couple_profile">
		<li id="couples"><br />이름1 ♡ 이름2<br /></li>
		<li id="cpDate">사귄 일 수<br /></li>
		<li id="csEvent">다가오는 기념일<br /></li>
	</ul>
	<p class="weather">날씨 API 영역</p>
	<!-- <p class="photoZone">연인과의 사진 영역, 서서히 사라지면서 다른 사진으로 바뀔지 혹은 고정일지 설정을 통해 변경 가능</p>-->
	<ul class="photoZone">
		<li class="photo1">연인과의 사진 영역, 서서히 사라지면서 </li>
		<li class="photo2">다른 사진으로 바뀔지 혹은 고정일지 설정을 통해 변경 가능</li>
	</ul>
</div>
<!-- 내용 영역 종료 -->
</body>
</html>