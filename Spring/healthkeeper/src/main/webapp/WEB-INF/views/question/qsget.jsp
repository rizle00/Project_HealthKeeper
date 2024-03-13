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
		<label>공지사항 번호</label>
		<input name="bno" readonly="readonly" value='<c:out value="${pageInfo.QS_BNO}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 제목</label>
		<input name="title" readonly="readonly" value='<c:out value="${pageInfo.QS_TITLE}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 내용</label>
		<textarea rows="3" name="content" readonly="readonly"><c:out value="${pageInfo.QS_CONTENT}"/></textarea>
	</div>
	<div class="input_wrap">
		<label>공지사항 작성자</label>
		<input name="writer" readonly="readonly" value='<c:out value="${pageInfo.QS_WRITER}"/>' >
	</div>
	<div class="input_wrap">
		<label>공지사항 등록일</label>
		<input name="regdate" readonly="readonly" value='<fmt:formatDate pattern="yyyy/MM/dd" value="${pageInfo.QS_REGDATE}"/>' >
	</div>
	<hr>
	<span>파일 목록</span>
	<div class="form-group" style="border: 1px solid #dbdbdb;">
    	<c:forEach var="file" items="${fileList}">
        	<a href="#" onclick="fn_fileDown('${file.FILE_ID}'); return false;">
        	${file.FILE_NAME}</a>(${file.FILE_SIZE} KB)<br>
    	</c:forEach>
	</div>
	
	<!-- 댓글 -->
	<div id="reply">
		<ol class="replyList">
			<c:forEach items="${replyList}" var="replyList">
				<li>
					<p>
					작성자 : ${replyList.WRITER}<br />
					작성날짜 : <fmt:formatDate value="${replyList.REGDATE}" pattern="yyyy-MM-dd"/>
					</p>
					<p>${replyList.CONTENT}</p>
					<div>
					<button type="button" class="replyUpdateBtn" data-qrno="${replyList.QRNO}">수정</button>
					<button type="button" class="replyDeleteBtn" data-qrno="${replyList.QRNO}">삭제</button>
					</div>
				</li>
			</c:forEach>
		</ol>
	</div>
	
	<form name="replyForm" method="post">
  		<input type="hidden" id="qs_bno" name="QS_BNO" value="${pageInfo.QS_BNO}" />
  		<input type="hidden" id="pageNum" name="pageNum" value="${qcri.pageNum}"> 
  		<input type="hidden" id="amount" name="amount" value="${qcri.amount}"> 
  		<input type="hidden" id="keyword" name="keyword" value="${qcri.keyword}"> 
  		<input type="hidden" id="type" name="type" value="${qcri.type}"> 

  	<div> 
    	<label for="writer">댓글 작성자</label><input type="text" id="writer" name="WRITER" />
    <br/>
    	<label for="content">댓글 내용</label><input type="text" id="content" name="CONTENT" />
  	</div>
  	<div>
 	 	<button type="button" class="replyWriteBtn">작성</button>
  	</div>
	</form>
	<hr>

	<div class="btn_wrap">
		<a class="btn" id="qslist_btn">목록 페이지</a> 
		<a class="btn" id="qsupdate_btn">수정 하기</a>
	</div>
	

	<form id="qsinfoForm" action="/question/qsupdate" method="get">
		<input type="hidden" id="qs_bno" name="QS_BNO" value='<c:out value="${pageInfo.QS_BNO}"/>'>
		<input type="hidden" name="pageNum" value='<c:out value="${qcri.pageNum}"/>'>
        <input type="hidden" name="amount" value='<c:out value="${qcri.amount}"/>'>
        <input type="hidden" name="keyword" value="${qcri.keyword }">
        <input type="hidden" name="type" value="${qcri.type }">
	</form>
<script>
	let form = $("#qsinfoForm");
	let qform = $("#replyForm");
	
	// 공지사항 목록화면 이동 js 코드
	$("#qslist_btn").on("click", function(e){
		form.find("#QS_BNO").remove();
		form.attr("action", "/question/qslist");
		form.submit();
	});
	
	// 공지사항 수정화면 이동 js 코드
	$("#qsupdate_btn").on("click", function(e){
		form.attr("action", "/question/qsupdate");
		form.submit();
	});	
	
	// 공지사항 첨부파일 다운로드 js 코드
	function fn_fileDown(fileId) {
        var url = "/notice/fileDown?FILE_ID=" + fileId;
        window.location.href = url;
    }
	
	// 댓글작성 js코드
	$(".replyWriteBtn").on("click", function(){
		  var formObj = $("form[name='replyForm']");
		  formObj.attr("action", "/question/replyRegistr");
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
</script>	

</body>
</html>
