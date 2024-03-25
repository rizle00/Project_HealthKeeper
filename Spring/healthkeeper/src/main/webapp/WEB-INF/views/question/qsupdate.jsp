<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수정 페이지</title>
<link rel="stylesheet" href="/resources/css/question/qsupdate.css">
<script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
	<form id="qsupdateForm" action="/question/qsupdate" method="post">
		<div class="qstable_wrap">
		<div class="qs_title">
			<strong>질문게시판</strong>
			<p>질문을 빠르고 정확하게 안내해드립니다.</p>
		</div>
		
		<div class="qs_writer_wrap">
			<div class="qs_update">
				<div class="category">
					<dl>
						<dt>카테고리</dt>
						<dd>
							<input name="category.NAME" readonly="readonly" value='<c:out value="${pageInfo.category.NAME}"/>' >
						</dd>	
					</dl>
				</div>
				
				<div class="title">
					<dl>
						<dt>제목</dt>
						<dd><input type="text" name="TITLE" value='<c:out value="${pageInfo.TITLE}"/>'></dd>	
					</dl>
				</div>
				
				<div class="info">
					<dl>
						<dt>작성자</dt>
						<dd><input type="text" name="NAME" readonly="readonly" value='<c:out value="${pageInfo.NAME}"/>'></dd>	
					</dl>
					
					<dl>
						<dt>파일첨부</dt>
						<dd>
							<c:forEach var="file" items="${fileList}">
        						<a href="#" onclick="fn_fileDown('${file.FILE_ID}'); return false;">
        						${file.NAME}</a>
    						</c:forEach>
						</dd>	
					</dl>
				</div>
				
				<div class="content">
					<textarea name="CONTENT"><c:out value="${pageInfo.CONTENT}"/></textarea>
				</div>
			</div>
			
			<div class="bt_wrap">
    			<a class="btn" id="qslist_btn">목록 페이지</a> 
				<a class="btn" id="qsupdate_btn">수정완료</a>
				<a class="btn" id="qsdelete_btn">삭제</a>
				<a class="btn" id="cancel_btn">수정취소</a>
			</div>
		</div>
	</div>
	</form>
	<form id="infoForm" action="/question/qsupdate" method="get">
		<input type="hidden" id="que_id" name="QUE_ID" value='<c:out value="${pageInfo.QUE_ID}"/>'>
		<input type="hidden" name="pageNum" value='<c:out value="${qcri.pageNum}"/>'>
        <input type="hidden" name="amount" value='<c:out value="${qcri.amount}"/>'>
        <input type="hidden" name="keyword" value="${qcri.keyword }">
   		<input type="hidden" name="type" value="${qcri.type }">
	</form>
<script>
	let form = $("#infoForm");
	
	// 공지사항 목록화면 이동 js 코드
	$("#qslist_btn").on("click", function(e){
		form.find("#QUE_ID").remove();
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