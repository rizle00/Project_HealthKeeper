<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/resources/css/notice/notget.css">
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
</head>
<body>
<h1>조회 페이지</h1>

	<div class="input_wrap">
		<label>FAQ 게시판 제목</label>
		<input name="TITLE" readonly="readonly" value='<c:out value="${pageInfo.TITLE}"/>' >
	</div>
	
	<div class="input_wrap">
		<label>FAQ 게시판 내용</label>
		<textarea rows="3" name="CONTENT" readonly="readonly"><c:out value="${pageInfo.CONTENT}"/></textarea>
	</div>
		
	<div class="btn_wrap">
		<a class="btn" id="faqlist_btn">목록 페이지</a> 
		<a class="btn" id="faqupdate_btn">수정 하기</a>
	</div>
	<form id="infoForm" action="/faq/faqupdate" method="get">
		<input type="hidden" id="faq_id" name="FAQ_ID" value='<c:out value="${pageInfo.FAQ_ID}"/>'>
		<input type="hidden" name="pageNum" value="${fpageMake.fcri.pageNum}">
        <input type="hidden" name="amount" value="${fpageMake.fcri.amount}">
	</form>
<script>
	let form = $("#infoForm");
	
	$("#faqlist_btn").on("click", function(e){
		form.find("#FAQ_ID").remove();
		form.attr("action", "/faq/faqlist");
		form.submit();
	});
	
	$("#faqupdate_btn").on("click", function(e){
		form.attr("action", "/faq/faqupdate");
		form.submit();
	});	
</script>	
</body>
</html>