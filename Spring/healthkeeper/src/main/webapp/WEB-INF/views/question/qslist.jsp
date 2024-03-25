<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>질문게시판</title>
<link rel="stylesheet" href="/resources/css/question/qslist.css">
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
<div class="qstable_wrap">
	<div class="qs_title">
		<strong>질문게시판</strong>
		<p>질문을 빠르고 정확하게 안내해드립니다.</p>
	</div>
	
	<div class="qs_list_wrap">
		<div class="qs_list">
			<div class="top">
				<div class="bno">번호</div>
				<div class="category">카테고리</div>
				<div class="title">제목</div>
				<div class="writer">작성자</div>
				<div class="regdate">작성일</div>
				<div class="views">조회수</div>
			</div>
			<c:forEach items="${qslist}" var="qslist">
				<div>
					<div class="bno"><c:out value="${qslist.QUE_ID}"/></div>
					<div class="category"><c:out value="${qslist.CATEGORY_NAME}"/></div>
					<div class="title">
						<a class="move" href='<c:out value="${qslist.QUE_ID}"/>'>
        					<c:out value="${qslist.TITLE}"/>
    					</a>
					</div>
					<div class="writer"><c:out value="${qslist.NAME}"/></div>
					<div class="regdate"><fmt:formatDate pattern="yyyy/MM/dd" value="${qslist.TIME}"/></div>
					<div class="views"><c:out value="${qslist.READ_CNT}"/></div>
				</div>
			</c:forEach>
		</div>
	</div>
	<!-- 검색기능 구현 -->
	<div class="search_wrap">
        <div class="search_area">
        	<select name="type">
                <option value="" <c:out value="${qpageMake.qcri.type == null?'selected':'' }"/>>--</option>
                <option value="T" <c:out value="${qpageMake.qcri.type eq 'T'?'selected':'' }"/>>제목</option>
                <option value="C" <c:out value="${qpageMake.qcri.type eq 'C'?'selected':'' }"/>>내용</option>
                <option value="W" <c:out value="${qpageMake.qcri.type eq 'W'?'selected':'' }"/>>작성자</option>
                <option value="TC" <c:out value="${qpageMake.qcri.type eq 'TC'?'selected':'' }"/>>제목 + 내용</option>
                <option value="TW" <c:out value="${qpageMake.qcri.type eq 'TW'?'selected':'' }"/>>제목 + 작성자</option>
                <option value="TCW" <c:out value="${qpageMake.qcri.type eq 'TCW'?'selected':'' }"/>>제목 + 내용 + 작성자</option>
            </select>  
            <input type="text" name="keyword" value="${qpageMake.qcri.keyword }">
            <button>Search</button>
        </div>
    </div>
	
	<!-- 번호페이지 구현 -->
	<div class="pageInfo_wrap" >
        <div class="pageInfo_area">
        	<ul id="pageInfo" class="pageInfo">
        	<!-- 이전페이지 버튼 -->
                <c:if test="${qpageMake.prev}">
                    <li class="pageInfo_btn previous"><a href="${qpageMake.startPage-1}">←</a></li>
                </c:if>
 			<!-- 각 번호 페이지 버튼 -->
                <c:forEach var="num" begin="${qpageMake.startPage}" end="${qpageMake.endPage}">
                	<li class="pageInfo_btn ${qpageMake.qcri.pageNum == num ? "active":"" }"><a href="${num}">${num}</a></li>
                </c:forEach>
             <!-- 다음페이지 버튼 -->
                <c:if test="${qpageMake.next}">
                    <li class="pageInfo_btn next"><a href="${qpageMake.endPage + 1 }">→</li>
                </c:if> 
            </ul>
        </div>
    </div>
    
    <div class="bt_wrap">
      <a href="/question/qsregistr" class="on">등록</a>
    </div>
    
	<form id="moveForm" method="get">
		<input type="hidden" name="pageNum" value="${qpageMake.qcri.pageNum }">
        <input type="hidden" name="amount" value="${qpageMake.qcri.amount }">
        <input type="hidden" name="keyword" value="${qpageMake.qcri.keyword }">
        <input type="hidden" name="type" value="${qpageMake.qcri.type }">
    </form>
</div>
<script>
	// 공지사항 등록, 수정, 삭제, 알람창 js 코드
	$(document).ready(function(){
	
		let result = '<c:out value="${result}"/>';
		checkAlert(result);
		function checkAlert(result){
		
			if(result === ''){
				return;
			}
			if(result === "registr success"){
				alert("질문이 등록되었습니다.");
			}
			
			if(result === "update success"){
	            alert("수정이 완료되었습니다.");
	        }
			
			if(result === "qsdelete success"){
	            alert("삭제가 완료되었습니다.");
	        }
		}	
	});
	
	// 공지사항 목록페이지 이동 js 코드
	let moveForm = $("#moveForm");
	 
    $(".move").on("click", function(e){
        e.preventDefault();
        moveForm.find("[name=QUE_ID]").remove();
        
        moveForm.append("<input type='hidden' name='QUE_ID' value='"+ $(this).attr("href")+ "'>");
        moveForm.attr("action", "/question/qsget");
        moveForm.submit();
    });
    
    // 페이지 이동 js 코드
    $(".pageInfo a").on("click", function(e){
 
        e.preventDefault();
        moveForm.find("input[name='pageNum']").val($(this).attr("href"));
        moveForm.attr("action", "/question/qslist");
        moveForm.submit();
    });
    
    // 검색버튼 작동 js 코드
    $(".search_area button").on("click", function(e) {
    e.preventDefault();
    let type = $(".search_area select").val();
    let keyword = $(".search_area input[name='keyword']").val();

    // 검색 종류와 검색어 모두가 비어있는 경우
    if (!type && !keyword) {
        moveForm.find("input[name='type']").val('');
        moveForm.find("input[name='keyword']").val('');
        moveForm.find("input[name='pageNum']").val(1);
        moveForm.submit();
        return;
    }
    // 검색어가 비어있는 경우
    if (!keyword) {
        alert("키워드를 입력하세요.");
        return false;
    }
    // 검색 종류를 선택하지 않은 경우
    if (!type) {
        alert("검색 종류를 선택하세요.");
        return false;
    }

    moveForm.find("input[name='type']").val(type);
    moveForm.find("input[name='keyword']").val(keyword);
    moveForm.find("input[name='pageNum']").val(1);
    moveForm.submit();
});

	$(".search_area input[name='keyword']").on("keypress", function(e) {
    	if (e.which === 13) { // Enter 키 눌렀을 때
        	e.preventDefault();
        	let type = $(".search_area select").val();
        	let keyword = $(".search_area input[name='keyword']").val();

        // 검색 종류와 검색어 모두가 비어있는 경우
        if (!type && !keyword) {
            moveForm.find("input[name='type']").val('');
            moveForm.find("input[name='keyword']").val('');
            moveForm.find("input[name='pageNum']").val(1);
            moveForm.submit();
            return;
        }
        // 검색어가 비어있는 경우
        if (!keyword) {
            alert("키워드를 입력하세요.");
            return false;
        }
        // 검색 종류를 선택하지 않은 경우
        if (!type) {
            alert("검색 종류를 선택하세요.");
            return false;
        }

        moveForm.find("input[name='type']").val(type);
        moveForm.find("input[name='keyword']").val(keyword);
        moveForm.find("input[name='pageNum']").val(1);
        moveForm.submit();
    }
});
</script>
</body>
</html>