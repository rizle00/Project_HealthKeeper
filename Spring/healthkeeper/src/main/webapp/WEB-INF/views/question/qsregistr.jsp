<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>질문게시판</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/css/question/qsregistr.css">
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
<style>
/* Additional custom styles */
body {
    background-color: #f8f9fa;
    font-family: Arial, sans-serif;
}

.input_wrap {
    margin-bottom: 15px;
}

.label {
    font-weight: bold;
}

.cate_wrap {
    margin-top: 10px;
}

.qsbtn {
    background-color: #007bff;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
}

.qsbtn:hover {
    background-color: #0056b3;
}
</style>
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
<form action="/question/qsregistr" method="post">
<h3 class="h1 mt-5 text-center" style="color: black;">질문게시판 등록</h3>
    <div class="container mt-5">
        <div class="input_wrap">
            <label class="label">제목</label>
            <input class="form-control" name="TITLE">
        </div>
        <div class="input_wrap">
            <label class="label">내용</label>
            <textarea class="form-control" rows="3" name="CONTENT"></textarea>
        </div>
        <div class="input_wrap">
            <label class="label">작성자</label>
            <input class="form-control" name="MEMBER_ID">
        </div>
        <div class="input_wrap">
    <label class="form-control" style="display: none;">카테고리 선택</label>
    <div class="cate_wrap">
    <span>질문하실 카테고리를 선택해주세요</span>
    <select class="form-control cate1" >
    <option selected value="1">선택</option>
    <c:forEach items="${catelist}" var="category">
        <option value="${category.CATEGORY_ID}">${category.NAME}</option>
    </c:forEach>
</select>
</div>
</div>
        <button class="btn qsbtn">등록</button>
    </div>
</form>
<jsp:include page="/WEB-INF/views/include/footer.jsp"/>
<script>

$(document).ready(function(){
	console.log('${catelist}');
});
</script>
</body>
</html>