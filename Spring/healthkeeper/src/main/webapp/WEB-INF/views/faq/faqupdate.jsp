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
<h1>수정 페이지</h1>
	<form id="faqupdateForm" action="/faq/faqupdate" method="post">
	
	<div class="input_wrap" style="display: none;">
    	<label>FAQ 게시판 번호</label>
    	<input name="FAQ_ID" readonly="readonly" value='<c:out value="${pageInfo.FAQ_ID}"/>' >
	</div>
	
	<div class="input_wrap">
		<label>FAQ 게시판 제목</label>
		<input name="TITLE" value='<c:out value="${pageInfo.TITLE}"/>' >
	</div>
	
	<div class="input_wrap">
		<label>FAQ 게시판 내용</label>
		<textarea rows="3" name="CONTENT" ><c:out value="${pageInfo.CONTENT}"/></textarea>
	</div>
		
	<div class="btn_wrap">
		<a class="btn" id="faqlist_btn">목록 페이지</a> 
		<a class="btn" id="faqupdate_btn">수정완료</a>
		<a class="btn" id="faqdelete_btn">삭제</a>
		<a class="btn" id="cancel_btn">수정취소</a>
	</div>
	
	</form>
	<form id="infoForm" action="/faq/faqupdate" method="get">
		<input type="hidden" id="FAQ_ID" name="FAQ_ID" value='<c:out value="${pageInfo.FAQ_ID}"/>'>
		<input type="hidden" name="pageNum" value="${fpageMake.fcri.pageNum}">
        <input type="hidden" name="amount" value="${fpageMake.fcri.amount}">
	</form>
<script>

$(document).ready(function() {
    let form = $("#infoForm");
    let mForm = $("#faqupdateForm");

    // FAQ 게시판 목록화면 이동 js코드
    $("#faqlist_btn").on("click", function(e) {
        form.find("#FAQ_ID").remove();
        form.attr("action", "/faq/faqlist");
        form.submit();
    });

    // FAQ 게시판 수정완료 버튼 클릭시 폼 제출
    $("#faqupdate_btn").on("click", function(e) {
        mForm.submit();
    });

    // 취소버튼 클릭시 이전페이지 이동 js코드
    $("#cancel_btn").on("click", function(e) {
        form.attr("action", "/faq/faqget");
        form.submit();
    });
    
 	// FAQ 게시판 삭제버튼
    $("#faqdelete_btn").on("click", function(e){
        form.attr("action", "/faq/faqdelete");
        form.attr("method", "post");
        form.submit();
    });
}); 
</script>	
</body>
</html>