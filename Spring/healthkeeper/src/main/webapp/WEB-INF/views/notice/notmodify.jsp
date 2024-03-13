<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수정 페이지</title>
<link rel="stylesheet" href="/resources/css/notice/notmodify.css">
<script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>
</head>
<body>
<h1>수정 페이지</h1>
<form id="notmodifyForm" action="/notice/notmodify" method="post" >
    <div class="input_wrap">
        <label>게시판 번호</label>
        <input name="NOTICE_ID" readonly="readonly" value='<c:out value="${pageInfo.NOTICE_ID}"/>' >
    </div>
    <div class="input_wrap">
        <label>게시판 제목</label>
        <input name="TITLE" value='<c:out value="${pageInfo.TITLE}"/>' >
    </div>
    <div class="input_wrap">
        <label>게시판 내용</label>
        <textarea rows="3" name="CONTENT"><c:out value="${pageInfo.CONTENT}"/></textarea>
    </div>
    <div class="input_wrap">
        <label>게시판 작성자</label>
        <input name="MEMBER_ID" readonly="readonly" value='<c:out value="${pageInfo.MEMBER_ID}"/>' >
    </div>
    <div class="input_wrap">
        <label>게시판 등록일</label>
        <input name="TIME" readonly="readonly" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${pageInfo.TIME}"/>' >
    </div>
    <div class="fileIndex">
        <!-- 기존 파일 목록 표시 -->
        <c:forEach var="file" items="${fileList}" varStatus="var">
            <div>
                <input type="hidden" id="FILE_ID_${var.index}" name="FILE_ID_${var.index}" value="${file.FILE_ID}">
                <input type="hidden" id="FILE_NAME_${var.index}" name="FILE_NAME_${var.index}" value="${file.FILE_NAME}">
                <a href="#" class="fileName">${file.FILE_NAME}</a>(${file.FILE_SIZE}KB)
                <button class="fileDelBtn" type="button">삭제</button><br>
            </div>
        </c:forEach>
    </div>
    <div class="btn_wrap">
        <a class="btn" id="notlist_btn">목록 페이지</a> 
        <a class="btn" id="notmodify_btn">수정완료</a>
        <a class="btn" id="notdelete_btn">삭제</a>
        <a class="btn" id="cancel_btn">수정취소</a>
        <!-- 파일 추가 버튼 -->
        <button type="button" class="fileAdd_btn">파일추가</button>
    </div>
</form>
<form id="infoForm" action="/notice/notmodify" method="get">
    <input type="hidden" id="NOTICE_ID" name="NOTICE_ID" value='<c:out value="${pageInfo.NOTICE_ID}"/>'>
    <input type="hidden" name="pageNum" value='<c:out value="${ncri.pageNum}"/>'>
    <input type="hidden" name="amount" value='<c:out value="${ncri.amount}"/>'>
    <input type="hidden" name="type" value="${ncri.type }">
    <input type="hidden" name="keyword" value="${ncri.keyword }"> 
    <input type="hidden" id="fileIdDel" name="fileIdDel[]" value=""> 
    <input type="hidden" id="fileNameDel" name="fileNameDel[]" value=""> 
</form>
<script>
$(document).ready(function() {
    // 파일 추가 버튼 클릭 시 파일 입력 필드 추가
    $(".fileAdd_btn").on("click", function() {
        $(".fileIndex").append('<div><input type="file" name="file"></div>');
    });

    // 파일 삭제 버튼 클릭 시 해당 파일 필드 제거 및 파일 삭제 목록에 추가
    $(document).on("click", ".fileDelBtn", function() {
        // 파일의 ID와 이름 가져오기
        var fileId = $(this).siblings("input[type=hidden]").val();
        var fileName = $(this).siblings("a").text();

        // fileIdDel[]와 fileNameDel[]에 해당 파일의 ID와 이름 추가
        $("#infoForm #fileIdDel").append("<input type='hidden' name='fileIdDel[]' value='" + fileId + "'>");
        $("#infoForm #fileNameDel").append("<input type='hidden' name='fileNameDel[]' value='" + fileName + "'>");

        // 해당 파일 필드 제거
        $(this).parent().remove();
    });
    
    

    // 수정완료 버튼 클릭 시 폼 제출
    $("#notmodify_btn").on("click", function() {
        $("#notmodifyForm").submit();
    });

    // 삭제 버튼 클릭 시 확인 후 삭제 처리
    $("#notdelete_btn").on("click", function() {
        if (confirm("정말 삭제하시겠습니까?")) {
            $("#notmodifyForm").attr("action", "/notice/notdelete");
            $("#notmodifyForm").attr("method", "post");
            $("#notmodifyForm").submit();
        }
    });

    // 취소버튼 클릭 시 이전 페이지로 이동
    $("#cancel_btn").on("click", function(e) {
        e.preventDefault(); // 기본 동작 방지
        history.back(); // 브라우저 이전 페이지로 이동
    });

    // 목록페이지 이동 버튼
    $("#notlist_btn").on("click", function(e) {
        e.preventDefault(); // 기본 동작 방지
        location.href = "/notice/notlist"; // 목록 페이지로 이동
    });

    // 파일 추가 시 확인 메시지 띄우기
    $(document).on("change", "input[type=file]", function() {
        if (!confirm("파일을 추가하시겠습니까?")) {
            $(this).val(""); // 파일 입력 필드 비우기
        }
    });

    // 파일 삭제 시 확인 메시지 띄우기
    $(document).on("click", ".fileDelBtn", function() {
        if (!confirm("파일을 삭제하시겠습니까?")) {
            return false; // 기본 동작 방지
        }
    });
});
</script>    
</body>
</html>