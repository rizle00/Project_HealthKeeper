<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FAQ 등록페이지</title>
</head>
<body>
<form action="/faq/faqregistr" method="post">
	<div class="input_wrap">
		<label>질문</label>
		<input name="TITLE">
	</div>

	<div class="input_wrap">
		<label>답변</label>
		<input name="CONTENT">
	</div>
	
	<div class="input_wrap">
		<label>작성자</label>
		<input name="member.MEMBER_ID">
	</div>
	<button class="faqbtn">등록</button>
</form>
</body>
</html>