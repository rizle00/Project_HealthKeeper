<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/resources/css/faq/faqlist.css">
<script src="https://code.jquery.com/jquery-3.4.1.js"
    integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
    crossorigin="anonymous"></script>
</head>
<jsp:include page="/WEB-INF/views/include/header.jsp" />
<body>
<a href="/faq/faqregistr">게시판 등록</a>
    <form id="faqupdateForm" action="/faq/faqupdate" method="post">
        <div id="faq">
            <h2>FAQ</h2>
            <ol class="qna-list accordion">
                <c:forEach items="${faqlist}" var="faq">
                    <li class="qna-item">
                        <div class="question-article">
                            <a href="#!" class="question btn-fold"> <strong class="blind">질문:</strong>
                                <span class="q">Q</span><c:out value="${faq.TITLE}"/>
                            </a>
                        </div>
                        <div class="answer-article">
                            <strong class="blind">답변</strong>
                            <div id="answer-cnt">
                                <p><c:out value="${faq.CONTENT}"/></p>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ol>
            <div class="btn_wrap">
		<a class="btn" id="faqlist_btn">목록 페이지</a> 
		<a class="btn" id="faqupdate_btn">수정완료</a>
		<a class="btn" id="cancel_btn">수정취소</a>
	</div>
        </div>
    </form>
    <form id="infoForm" action="/faq/faqupdate" method="get">
		<input type="hidden" id="FAQ_ID" name="FAQ_ID" value='<c:out value="${pageInfo.FAQ_ID}"/>'>
	</form>
<script>
// 글 목록을 접었다 폈다 하는 js코드
  $(function() {
    $('.btn-fold').on(
      'click',
      function() {
      if(!$(this).closest('.qna-item').hasClass('on')) {
       	                $('.qna-item').removeClass('on')
                        .find('.answer-article').hide();
                        var oft = $(this).offset().top;
                        $('html,body').stop().animate({
                        scrollTop : oft - 250
                        }, 300)
                        $(this).closest('.qna-item').addClass('on').find(
                        '.answer-article').slideDown(300)

                    } else {
                        $(this).closest('.qna-item').removeClass('on').find(
                                '.answer-article').slideUp(300)
                    }
                })
    });
    
  	let form = $("#infoForm");        // 페이지 이동 form(리스트 페이지 이동, 조회 페이지 이동)
	let mForm = $("#faqupdateForm");    // 페이지 데이터 수정 from

	// 목록 페이지 이동 버튼
	$("#faqlist_btn").on("click", function(e){
  	form.find("#FAQ_ID").remove();
  	form.attr("action", "/faq/faqlist");
  	form.submit();
	});

	// 수정 하기 버튼
	$("#faqupdate_btn").on("click", function(e){
  	mForm.submit();
	});

	/* 취소 버튼 */
	$("#cancel_btn").on("click", function(e){
  	form.attr("action", "/faq/faqlist");
  	form.submit();
	});
</script>
</body>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</html>