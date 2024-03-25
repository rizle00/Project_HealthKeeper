<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<link rel="stylesheet" href="/resources/css/notice/notlist.css">
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
<div class="not_table_wrap">
	<div class="not_title">
		<strong>공지사항</strong>
		<p>공지사항을 안내해드립니다.</p>
	</div>
	
	<div class="not_list_wrap">
		<div class="not_list">
			<div class="top">
				<div class="bno">번호</div>
				<div class="title">제목</div>
				<div class="writer">작성자</div>
				<div class="regdate">작성일</div>
				<div class="views">조회수</div>
			</div>
			<c:forEach items="${notlist}" var="list">
				<div>
					<div class="bno"><c:out value="${list.NOTICE_ID}"/></div>
					<div class="title">
						<a class="move" href='<c:out value="${list.NOTICE_ID}"/>'>
        					<c:out value="${list.TITLE}"/>
    					</a>
					</div>
					<div class="writer"><c:out value="${list.NAME}"/></div>
					<div class="regdate"><fmt:formatDate pattern="yyyy/MM/dd" value="${list.TIME}"/></div>
					<div class="views"><c:out value="${list.READ_CNT}"/></div>
				</div>
			</c:forEach>
		</div>
	</div>
	<!-- 검색기능 구현 -->
	<div class="search_wrap">
        <div class="search_area">
        	<select name="type">
                <option value="" <c:out value="${npageMake.ncri.type == null?'selected':'' }"/>>--</option>
                <option value="T" <c:out value="${npageMake.ncri.type eq 'T'?'selected':'' }"/>>제목</option>
                <option value="C" <c:out value="${npageMake.ncri.type eq 'C'?'selected':'' }"/>>내용</option>
                <option value="W" <c:out value="${npageMake.ncri.type eq 'W'?'selected':'' }"/>>작성자</option>
                <option value="TC" <c:out value="${npageMake.ncri.type eq 'TC'?'selected':'' }"/>>제목 + 내용</option>
                <option value="TW" <c:out value="${npageMake.ncri.type eq 'TW'?'selected':'' }"/>>제목 + 작성자</option>
                <option value="TCW" <c:out value="${npageMake.ncri.type eq 'TCW'?'selected':'' }"/>>제목 + 내용 + 작성자</option>
            </select>  
            <input type="text" name="keyword" value="${npageMake.ncri.keyword }">
            <button>Search</button>
        </div>
    </div>
	
	<!-- 번호페이지 구현 -->
	<div class="pageInfo_wrap" >
        <div class="pageInfo_area">
        	<ul id="pageInfo" class="pageInfo">
        	<!-- 이전페이지 버튼 -->
                <c:if test="${npageMake.prev}">
                    <li class="pageInfo_btn previous"><a href="${npageMake.startPage-1}">←</a></li>
                </c:if>
 			<!-- 각 번호 페이지 버튼 -->
                <c:forEach var="num" begin="${npageMake.startPage}" end="${npageMake.endPage}">
                	<li class="pageInfo_btn ${npageMake.ncri.pageNum == num ? "active":"" }"><a href="${num}">${num}</a></li>
                </c:forEach>
             <!-- 다음페이지 버튼 -->
                <c:if test="${npageMake.next}">
                    <li class="pageInfo_btn next"><a href="${npageMake.endPage + 1 }">→</li>
                </c:if> 
            </ul>
        </div>
    </div>
    
    <div class="bt_wrap">
      <a href="/notice/notregistr" class="on">등록</a>
    </div>
    
	<form id="moveForm" method="get">
		<input type="hidden" name="pageNum" value="${npageMake.ncri.pageNum }">
        <input type="hidden" name="amount" value="${npageMake.ncri.amount }">
        <input type="hidden" name="keyword" value="${npageMake.ncri.keyword }">
        <input type="hidden" name="type" value="${npageMake.ncri.type }">     
    </form>
</div>
<script>
	// 공지사항 등록시 알람창 js 코드
	$(document).ready(function(){
	
		let result = '<c:out value="${result}"/>';
		checkAlert(result);
		function checkAlert(result){
		
			if(result === ''){
				return;
			}
			if(result === "registr success"){
				alert("공지사항이 등록되었습니다.");
			}
			
			if(result === "notmodify success"){
	            alert("수정이 완료되었습니다.");
	        }
			
			if(result === "notdelete success"){
	            alert("삭제가 완료되었습니다.");
	        }
		}	
	});
	
	// 공지사항 목록페이지 이동 js 코드
	let moveForm = $("#moveForm");
	 
    $(".move").on("click", function(e){
        e.preventDefault();
        
        moveForm.append("<input type='hidden' name='NOTICE_ID' value='"+ $(this).attr("href")+ "'>");
        moveForm.attr("action", "/notice/notget");
        moveForm.submit();
    });
    
    // 페이지 이동 js 코드
    $(".pageInfo a").on("click", function(e){
 
        e.preventDefault();
        moveForm.find("input[name='pageNum']").val($(this).attr("href"));
        moveForm.attr("action", "/notice/notlist");
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