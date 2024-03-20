<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FAQ 목록</title>
    <link rel="stylesheet" href="/resources/css/faq/faqlist.css">
    <script src="https://code.jquery.com/jquery-3.4.1.js"
        integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
        crossorigin="anonymous"></script>
</head>
<jsp:include page="/WEB-INF/views/include/header.jsp" />

<body>
    <a href="/faq/faqregistr">게시판 등록</a>
    <form id="infoForm" action="/faq/faqupdate" method="get">
    <div id="faq">
        <h2>FAQ</h2>
        <ol class="qna-list accordion">
            <c:forEach items="${faqlist}" var="faq">
                <li class="qna-item">
                    <div class="question-article">
                        <a href="#" class="question btn-fold">
                            <strong class="blind">질문:</strong>
                            <span class="q">Q</span><span id="titleText_${faq.FAQ_ID}"><c:out value="${faq.TITLE}"/></span>
                        </a>
                    </div>
                    <div class="answer-article">
                        <strong class="blind">답변</strong>
                        <div id="answer-cnt_${faq.FAQ_ID}">
                            <p><c:out value="${faq.CONTENT}"/></p>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ol>
    </div>
     </form>
    <!-- 번호페이지 구현 -->
	<div class="pageInfo_wrap" >
        <div class="pageInfo_area">
        	<ul id="pageInfo" class="pageInfo">
        	<!-- 이전페이지 버튼 -->
                <c:if test="${fpageMake.prev}">
                    <li class="pageInfo_btn previous"><a href="${fpageMake.startPage-1}">Previous</a></li>
                </c:if>
 			<!-- 각 번호 페이지 버튼 -->
                <c:forEach var="num" begin="${fpageMake.startPage}" end="${fpageMake.endPage}">
                	<li class="pageInfo_btn ${fpageMake.fcri.pageNum == num ? "active":"" }"><a href="${num}">${num}</a></li>
                </c:forEach>
             <!-- 다음페이지 버튼 -->
                <c:if test="${fpageMake.next}">
                    <li class="pageInfo_btn next"><a href="${fpageMake.endPage + 1 }">Next</a></li>
                </c:if> 
            </ul>
        </div>
    </div>
    <form id="moveForm" method="get">
		<input type="hidden" name="pageNum" value="${fpageMake.fcri.pageNum }">
        <input type="hidden" name="amount" value="${fpageMake.fcri.amount }">     
    </form>
   
<script>
// 글 목록을 접었다 폈다 하는 js코드
$(function() {
    $('.btn-fold').on('click', function() {
        var $qnaItem = $(this).closest('.qna-item');
        if (!$qnaItem.hasClass('on')) {
            $('.qna-item').removeClass('on').find('.answer-article').hide();
            var offsetTop = $(this).offset().top;
            $('html,body').stop().animate({
                scrollTop : offsetTop - 250
            }, 300)
            $qnaItem.addClass('on').find('.answer-article').slideDown(300);
        } else {
            $qnaItem.removeClass('on').find('.answer-article').slideUp(300);
        }
    });
});

//페이지 이동 js 코드
let moveForm = $("#moveForm");

$(".pageInfo a").on("click", function(e){

    e.preventDefault();
    moveForm.find("input[name='pageNum']").val($(this).attr("href"));
    moveForm.attr("action", "/faq/faqlist");
    moveForm.submit();
    
});

let form = $("#infoForm");

//게시판 수정화면 이동 js 코드
$("#faqupdate_btn").on("click", function(e){
 form.attr("action", "/faq/faqupdate");
 form.submit();
});
</script>
</body>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</html>