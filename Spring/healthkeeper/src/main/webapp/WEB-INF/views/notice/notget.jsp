<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>조회 페이지</title>
<link rel="stylesheet" href="/resources/css/notice/notget.css">
<script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>
</head>
<body>
<h1>조회 페이지</h1>
	<div class="input_wrap">
		<label>공지사항 번호</label>
		<input name="NOTICE_ID" readonly="readonly" value='<c:out value="${pageInfo.NOTICE_ID}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 제목</label>
		<input name="TITLE" readonly="readonly" value='<c:out value="${pageInfo.TITLE}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 내용</label>
		<textarea rows="3" name="CONTENT" readonly="readonly"><c:out value="${pageInfo.CONTENT}"/></textarea>
	</div>
	<div class="input_wrap">
		<label>공지사항 작성자</label>
		<input name="MEMBER_ID" readonly="readonly" value='<c:out value="${pageInfo.MEMBER_ID}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 등록일</label>
		<input name="TIME" readonly="readonly" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${pageInfo.TIME}"/>' >
	</div>
	<hr>
	<%-- <span>파일 목록</span>
	<div class="form-group" style="border: 1px solid #dbdbdb;">
    	<c:forEach var="file" items="${fileList}">
        	<a href="#" onclick="fn_fileDown('${file.FILE_ID}'); return false;">
        	${file.FILE_NAME}</a>(${file.FILE_SIZE} KB)<br>
    	</c:forEach>
	</div> --%>
	<hr>

	<div class="btn_wrap">
		<a class="btn" id="notlist_btn">목록 페이지</a> 
		<a class="btn" id="notmodify_btn">수정 하기</a>
	</div>
	<form id="infoForm" action="/notice/notmodify" method="get">
		<input type="hidden" id="NOTICE_ID" name="NOTICE_ID" value='<c:out value="${pageInfo.NOTICE_ID}"/>'>
		<input type="hidden" name="pageNum" value='<c:out value="${ncri.pageNum}"/>'>
        <input type="hidden" name="amount" value='<c:out value="${ncri.amount}"/>'>
        <input type="hidden" name="type" value="${ncri.type }">
        <input type="hidden" name="keyword" value="${ncri.keyword }">  
	</form>
<script>
	let form = $("#infoForm");
	
	// 공지사항 목록화면 이동 js 코드
	$("#notlist_btn").on("click", function(e){
		form.find("#NOTICE_BNO").remove();
		form.attr("action", "/notice/notlist");
		form.submit();
	});
	
	// 공지사항 수정화면 이동 js 코드
	$("#notmodify_btn").on("click", function(e){
		form.attr("action", "/notice/notmodify");
		form.submit();
	});	
	
	// 공지사항 첨부파일 다운로드 js 코드
	/* function fn_fileDown(fileId) {
        var url = "/notice/fileDown?FILE_ID=" + fileId;
        window.location.href = url;
    } */
</script>	
</body>
</html>