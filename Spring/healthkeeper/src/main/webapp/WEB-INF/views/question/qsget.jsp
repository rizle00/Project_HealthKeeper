<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>조회 페이지</title>
<link rel="stylesheet" href="/resources/css/question/qsget.css">
<script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
	<div class="gstable_wrap">
		<div class="qs_title"> 
			<strong>질문게시판</strong>
			<p>질문을 빠르고 정확하게 안내해드립니다.</p>
		</div>
		
		<!-- 질문게시판 제목 -->
		<div class="qs_get_wrap">
			<div class="qs_get">
				<div class="title">
					<c:out value="${pageInfo.TITLE}"/>
				</div>
				
		<!-- 질문게시판 INFO  -->
			<div class="qs_info">
				<dl>
					<dt>카테고리</dt>
					<dd><c:out value="${pageInfo.category.NAME}"/></dd>
				</dl>
				<dl>
					<dt>작성자</dt>
					<dd><c:out value="${pageInfo.NAME}"/></dd>
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
			<div class="qs_content">
				<c:out value="${pageInfo.CONTENT}"/>
			</div>
			
		<!-- 질문게시판 파일목록 -->
			<div class="qs_file_list">
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
	
	<!-- 게시물 끝 -->
		<%-- <div id="answer">
 			<ol class="answerList">
 				<c:forEach items="${anlist}" var="anlist">
 			<li>
  				<p>
   					작성자 : ${anlist.ADMIN_ID}<br />
   					작성 날짜 :  <fmt:formatDate value="${anlist.TIME}" pattern="yyyy-MM-dd" />
  				</p>
  
  				<p>${anlist.CONTENT}</p>
 			</li>
 				</c:forEach>   
 			</ol>
		</div> --%>
	
	<%-- <section class="answerForm">
		<form role="form" method="post" autocomplete="off">
  		<input type="hidden" id="QUE_ID" name="QUE_ID" value="${pageInfo.QUE_ID}" />
  		<input type="hidden" id="pageNum" name="pageNum" value="${qcri.pageNum}"> 
  		<input type="hidden" id="amount" name="amount" value="${qcri.amount}"> 
  		<input type="hidden" id="keyword" name="keyword" value="${qcri.keyword}"> 
  		<input type="hidden" id="type" name="type" value="${qcri.type}"> 
  		
  		<p><label for="admin_id">작성자</label><input type="text" id="admin_id" name="ADMIN_ID" /></p>
 		<p><label for="content">댓글 내용</label><textarea id="content" name="CONTENT"></textarea></p>
 		<p>
  		<button type="button" class="ansSubmit">작성</button>
  		
	</form>
	</section> --%>
	<div class="qs_btn_wrap">
		<a class="list_btn" id="qslist_btn">목록 페이지</a> 
		<a class="update_btn" id="qsupdate_btn">수정 하기</a>
	</div>

	<form id="qsinfoForm" action="/question/qsupdate" method="get">
		<input type="hidden" id="que_id" name="QUE_ID" value='<c:out value="${pageInfo.QUE_ID}"/>'>
		<input type="hidden" name="pageNum" value='<c:out value="${qcri.pageNum}"/>'>
        <input type="hidden" name="amount" value='<c:out value="${qcri.amount}"/>'>
        <input type="hidden" name="keyword" value="${qcri.keyword }">
        <input type="hidden" name="type" value="${qcri.type }">
	</form>
<script>
	let form = $("#qsinfoForm");
	let qform = $("#replyForm");
	
	// 게시판 목록화면 이동 js 코드
	$("#qslist_btn").on("click", function(e){
		form.find("#QUE_ID").remove();
		form.attr("action", "/question/qslist");
		form.submit();
	});
	
	// 조회 페이지에서 조회 후 마우스로 뒤로가기 누를시 id값 중복안되게 하는 쿼리
	window.addEventListener('popstate', function(event) {
    // 현재 페이지의 URL에서 쿼리 매개변수 제거
    history.replaceState(null, null, window.location.pathname);
});
	
	// 게시판 수정화면 이동 js 코드
	$("#qsupdate_btn").on("click", function(e){
		form.attr("action", "/question/qsupdate");
		form.submit();
	});	
	
	
	// 댓글작성 js코드
	 var formObj = $(".answerForm form[role='form']");
        
  		$(".ansSubmit").click(function(){
   		formObj.attr("action", "/question/answerWrite");
   		formObj.submit();
  	});
	
	// 게시판 첨부파일 다운로드 js 코드
	function fn_fileDown(fileId) {
        var url = "/question/fileDown?FILE_ID=" + fileId;
        window.location.href = url;
    }
</script>	

</body>
</html>
