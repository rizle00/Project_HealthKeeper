<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.4.1.js"
    integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
    crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/resources/css/main.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container px-5">
                    <a class="navbar-brand" href="/main">Health KeePer</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                         <!-- 로그인 하지 않은 상태 -->
                            <c:if test="${ member == null }">
                            	<li class="nav-item"><a class="nav-link" href="<c:url value='/question/qslist'/>">질문게시판</a></li>
                            	<li class="nav-item"><a class="nav-link" href="contact.html">통계</a></li>
                            	<li class="nav-item"><a class="nav-link" href="<c:url value='/notice/notlist'/>">공지사항</a></li>
                            	<li class="nav-item"><a class="nav-link" href="<c:url value='/faq/faqlist'/>">FAQ</a></li>
                            	<li class="nav-item"><a class="nav-link" href="<c:url value='/member/login'/>">로그인</a></li>
                            	<li class="nav-item"><a class="nav-link" href="<c:url value='/member/join'/>">회원가입</a></li>
                            </c:if>
                            
                         <!-- 로그인한 상태 -->
							<c:if test="${member != null }">
    							<li class="nav-item"><a class="nav-link" href="<c:url value='/question/qslist'/>">질문게시판</a></li>
    							<li class="nav-item"><a class="nav-link" href="contact.html">통계</a></li>
    							<li class="nav-item"><a class="nav-link" href="<c:url value='/notice/notlist'/>">공지사항</a></li>
    							<li class="nav-item"><a class="nav-link" href="<c:url value='/faq/faqlist'/>">FAQ</a></li>
    							<li class="nav-item dropdown">
    								<span class="nav-link dropdown-toggle" id="navbarDropdown" role="button" 
    									  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        								  [ ${member.NAME}님 반갑습니다. ]
    								</span>
    							<div class="dropdown-menu" aria-labelledby="navbarDropdown">
        							<a class="dropdown-item" href="/member/logout" onclick="logout(event)">로그아웃</a>
    							</div>
								</li>
							</c:if>
                         </ul>
                    </div>
                </div>
            </nav>
<script>

	// 드롭바 js코드
	$(document).ready(function(){
    	$('.dropdown-toggle').dropdown();
	});
	
	// 로그아웃 모달창 js 코드
	function logout(event) {
        event.preventDefault(); // 링크의 기본 동작을 막음

        if (confirm("${member.NAME}님 로그아웃 하시겠습니까?")) {
            window.location.href = event.target.getAttribute('href'); // 로그아웃 링크로 이동
        }
    }
</script>
</body>
</html>