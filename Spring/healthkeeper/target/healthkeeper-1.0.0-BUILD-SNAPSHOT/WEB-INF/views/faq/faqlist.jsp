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
   <div class="table_wrap">
	<a href="/faq/faqregistr" class="top_btn">게시판 등록</a>
	<table>
		<thead>
			<tr>
				<th class="title_width" style="text-align: center; font-size: 26px;">자주묻는 질문</th>
			</tr>
		</thead>
			<c:forEach items="${faqlist}" var="list">
            <tr>
                <td style="text-align: center;">
    				<a class="move" href='<c:out value="${list.FAQ_ID}"/>'>
        				<c:out value="${list.TITLE}"/>
    				</a>
				</td>
            </tr>
        </c:forEach>
	</table>
</div>
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
    	<input type="hidden" name="pageNum" value="${fpageMake.fcri.pageNum}">
        <input type="hidden" name="amount" value="${fpageMake.fcri.amount}">     
	</form>
<script>

 $(document).ready(function(){
	 let result = '<c:out value="${result}"/>';
	 checkAlert(result);
	 function checkAlert(result){
		 if(result ===''){
			 return;
		 }
		 if(result === "faqregistr success"){
			 alert("FAQ 게시글이 등록되었습니다.")
		 }
		 if(result === "faqupdate success"){
	            alert("수정이 완료되었습니다.");
	     }
		 if(result === "faqdelete success"){
	            alert("삭제가 완료되었습니다.");
	     }
	 } 
 });

//FAQ 게시판 목록페이지 이동 js 코드
	let moveForm = $("#moveForm");
	 
 $(".move").on("click", function(e){
     e.preventDefault();
     
     moveForm.append("<input type='hidden' name='FAQ_ID' value='"+ $(this).attr("href")+ "'>");
     moveForm.attr("action", "/faq/faqget");
     moveForm.submit();
 });
 
 // 페이지 이동 js 코드

$(".pageInfo a").on("click", function(e){

    e.preventDefault();
    moveForm.find("input[name='pageNum']").val($(this).attr("href"));
    moveForm.attr("action", "/faq/faqlist");
    moveForm.submit();
    
});

//게시판 수정화면 이동 js 코드
$("#faqupdate_btn").on("click", function(e){
 form.attr("action", "/faq/faqupdate");
 form.submit();
});
</script>
</body>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</html>