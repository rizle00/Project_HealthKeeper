<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수정 페이지</title>
<link rel="stylesheet" href="/resources/css/question/qsget.css">
<script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>
</head>
<body>
<h1>수정 페이지</h1>
	<form id="qsupdateForm" action="/question/qsupdate" method="post">
	<div class="input_wrap">
		<label>공지사항 번호</label>
		<input name="QS_BNO" readonly="readonly" value='<c:out value="${pageInfo.QS_BNO}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 제목</label>
		<input name="QS_TITLE" value='<c:out value="${pageInfo.QS_TITLE}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 내용</label>
		<textarea rows="3" name="QS_CONTENT" ><c:out value="${pageInfo.QS_CONTENT}"/></textarea>
	</div>
	<div class="input_wrap">
		<label>공지사항 작성자</label>
		<input name="QS_WRITER" readonly="readonly" value='<c:out value="${pageInfo.QS_WRITER}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 등록일</label>
		<input name="QS_REGDATE" readonly="readonly" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${pageInfo.QS_REGDATE}"/>' >
	</div>
	<hr>
	<span>파일 목록</span>
	<div class="form-group" style="border: 1px solid #dbdbdb;">
    	<c:forEach var="file" items="${fileList}">
        	<a href="#" onclick="fn_fileDown('${file.FILE_ID}'); return false;">
        	${file.FILE_NAME}</a>(${file.FILE_SIZE} KB)<br>
    	</c:forEach>
	</div>
	<hr>

	<div class="btn_wrap">
		<a class="btn" id="qslist_btn">목록 페이지</a> 
		<a class="btn" id="qsupdate_btn">수정완료</a>
		<a class="btn" id="qsdelete_btn">삭제</a>
		<a class="btn" id="cancel_btn">수정취소</a>
	</div>
	</form>
	<form id="infoForm" action="/question/qsupdate" method="get">
		<input type="hidden" id="QS_BNO" name="QS_BNO" value='<c:out value="${pageInfo.QS_BNO}"/>'>
		<input type="hidden" name="pageNum" value='<c:out value="${qcri.pageNum}"/>'>
        <input type="hidden" name="amount" value='<c:out value="${qcri.amount}"/>'>
        <input type="hidden" name="keyword" value="${qcri.keyword }">
   		<input type="hidden" name="type" value="${qcri.type }">
	</form>
<script>
	let form = $("#infoForm");
	
	// 공지사항 목록화면 이동 js 코드
	$("#qslist_btn").on("click", function(e){
		form.find("#QS_BNO").remove();
		form.attr("action", "/question/qslist");
		form.submit();
	});
	
	// 수정완료 버튼 클릭 시 폼 제출
    $("#qsupdate_btn").on("click", function() {
        $("#qsupdateForm").submit();
    });	
	
	// 취소버튼 클릭 시 이전 페이지로 이동
    $("#cancel_btn").on("click", function(e) {
        e.preventDefault(); // 기본 동작 방지
        history.back(); // 브라우저 이전 페이지로 이동
    });
	
 	// 삭제 버튼 클릭 시 확인 후 삭제 처리
    $("#qsdelete_btn").on("click", function() {
        if (confirm("정말 삭제하시겠습니까?")) {
            $("#qsupdateForm").attr("action", "/question/qsdelete");
            $("#qsupdateForm").attr("method", "post");
            $("#qsupdateForm").submit();
        }
    });
	
	// 공지사항 첨부파일 다운로드 js 코드
	function fn_fileDown(fileId) {
        var url = "/notice/fileDown?FILE_ID=" + fileId;
        window.location.href = url;
    }
</script>	
</body>
</html>