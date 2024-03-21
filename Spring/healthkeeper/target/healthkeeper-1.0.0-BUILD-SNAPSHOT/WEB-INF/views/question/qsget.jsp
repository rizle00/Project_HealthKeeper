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
<h1>조회 페이지</h1>
	<div class="input_wrap">
		<label>번호</label>
		<input name="que_id" readonly="readonly" value='<c:out value="${pageInfo.QUE_ID}"/>' >
	</div>
    <div class="input_wrap">
        <label>질문</label>
        <input name="CATEGORY_ID" readonly="readonly" value='<c:out value="${pageInfo.category.NAME}"/>' >
    </div>
	<div class="input_wrap">
		<label>제목</label>
		<input name="TITLE" readonly="readonly" value='<c:out value="${pageInfo.TITLE}"/>' >
	</div>
	<div class="input_wrap">
		<label>내용</label>
		<textarea rows="3" name="CONTENT" readonly="readonly"><c:out value="${pageInfo.CONTENT}"/></textarea>
	</div>
	<div class="input_wrap">
		<label>작성자</label>
		<input name="MEMBER_ID" readonly="readonly" value='<c:out value="${pageInfo.MEMBER_ID}"/>' >
	</div>
	<div class="input_wrap">
		<label>등록일</label>
		<input name="TIME" readonly="readonly" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${pageInfo.TIME}"/>' >
	</div>
	<hr>
	<span>파일 목록</span>
	<div class="form-group" style="border: 1px solid #dbdbdb;">
    	<c:forEach var="file" items="${fileList}">
        	<a href="#" onclick="fn_fileDown('${file.FILE_ID}'); return false;">
        	${file.NAME}</a>
    	</c:forEach>
	</div>
	
	<!-- 게시물 끝 -->
		<div id="answer">
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
		</div>
	
	<section class="answerForm">
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
	</section>
	<hr>

	<div class="btn_wrap">
		<a class="btn" id="qslist_btn">목록 페이지</a> 
		<a class="btn" id="qsupdate_btn">수정 하기</a>
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
	
	// 댓글수정 js코드
	$(".replyUpdateBtn").on("click", function(e){
		form.attr("action", "/question/replyupdate");
		form.submit();
	});
	
	// 댓글삭제 js코드
	$(".replyDeleteBtn").on("click", function() {
    var qrno = $(this).attr("data-qrno"); // 삭제할 댓글의 번호 가져오기

    // 삭제 여부를 확인하는 모달 창 표시
    if (confirm("댓글을 삭제하시겠습니까?")) {
        // 사용자가 확인을 선택한 경우에만 AJAX 요청을 보냄
        $.ajax({
            type: "POST",
            url: "/question/replydelete",
            data: { QRNO: qrno }, // QRNO를 데이터로 전송
            success: function(response) {
                // 성공적으로 처리된 경우 실행할 코드
                // 예를 들어, 성공 메시지를 표시하거나 페이지를 리로드할 수 있습니다.
                alert("댓글이 삭제되었습니다.");
                location.reload(); // 페이지 리로드
            },
            error: function(xhr, status, error) {
                // 오류 발생 시 실행할 코드
                alert("댓글 삭제 중 오류가 발생했습니다.");
                console.error(xhr.responseText);
            }
        });
    }
});
	
	// 게시판 첨부파일 다운로드 js 코드
	function fn_fileDown(fileId) {
        var url = "/question/fileDown?FILE_ID=" + fileId;
        window.location.href = url;
    }
</script>	

</body>
</html>
