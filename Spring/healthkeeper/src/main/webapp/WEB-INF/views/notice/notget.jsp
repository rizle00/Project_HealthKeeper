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
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
	<div class="not_table_wrap">
		<div class="not_title"> 
			<strong>공지사항</strong>
			<p>공지사항을 안내해드립니다.</p>
		</div>
		
		<!-- 질문게시판 제목 -->
		<div class="not_get_wrap">
			<div class="not_get">
				<div class="title">
					<c:out value="${pageInfo.TITLE}"/>
				</div>
				
		<!-- 질문게시판 INFO  -->
			<div class="not_info">
				<dl>
					<dt>작성자</dt>
					<dd><c:out value="${pageInfo.member.NAME}"/></dd>
				</dl>
				<dl>
					<dt>등록일</dt>
					<dd><fmt:formatDate pattern="yyyy/MM/dd" value="${pageInfo.TIME}"/></dd>
				</dl>
				<dl>
					<dt>조회수</dt>
					<dd><c:out value="${pageInfo.READ_CNT}"/></dd>
				</dl>
			</div>
			
		<!-- 질문게시판 내용 -->
			<div class="not_content">
				<c:out value="${pageInfo.CONTENT}"/>
			</div>
			
		<!-- 질문게시판 파일목록 -->
			<div class="not_file_list">
    			<dl>
        			<c:forEach var="file" items="${fileList}">
            			<dt>첨부파일</dt>
            				<dd>
                				<a href="#" onclick="fn_fileDown('${file.FILE_ID}'); return false;">
                    								 ${file.NAME}
                				</a>
            				</dd>
        			</c:forEach>
    			</dl>
			</div>
		</div>
	</div>
	</div>
			
	<div class="not_btn_wrap">
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

//공지사항 목록화면 이동 js 코드
$("#notlist_btn").on("click", function(e){
 form.find("#NOTICE_ID").remove();
 form.attr("action", "/notice/notlist");
 form.submit();
});

//공지사항 수정화면 이동 js 코드
$("#notmodify_btn").on("click", function(e){
 form.attr("action", "/notice/notmodify");
 form.submit();
});

//공지사항 첨부파일 다운로드 js 코드
function fn_fileDown(fileId) {
 var url = "/notice/fileDown?FILE_ID=" + fileId;
 window.location.href = url;
} 
</script>	

</body>
</html>
