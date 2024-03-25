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
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp"/>
<form action="/question/qsregistr" method="post" enctype="multipart/form-data">
	<div class="qstable_wrap">
		<div class="qs_title">
			<strong>질문게시판</strong>
			<p>질문을 빠르고 정확하게 안내해드립니다.</p>
		</div>
		
		<div class="qs_writer_wrap">
			<div class="qs_writer">
				<div class="category">
					<dl>
						<dt>카테고리</dt>
						<dd>
							<select class="category" name="category.CATEGORY_ID">
            					<option selected value="none">선택</option>
        							<c:forEach items="${catelist}" var="category">
            					<option value="${category.CATEGORY_ID}">${category.NAME}</option>
        							</c:forEach>
    						</select>
						</dd>	
					</dl>
				</div>
				
				<div class="title">
					<dl>
						<dt>제목</dt>
						<dd><input type="text" name="TITLE" placeholder="제목입력"></dd>	
					</dl>
				</div>
				
				<div class="info">
					<dl>
						<dt>작성자</dt>
						<dd><input type="text" name="MEMBER_ID" placeholder="작성자 입력"></dd>	
					</dl>
					
					<dl>
						<dt>파일첨부</dt>
						<dd><input type="file" name="file"></dd>	
					</dl>
				</div>
				
				<div class="content">
					<textarea name="CONTENT" placeholder="내용입력"></textarea>
				</div>
			</div>
			
			<div class="bt_wrap">
    			<button type="submit" class="btn_qsbtn">등록</button>
    			<button type="button" class="cancel_btn">취소</button>
			</div>
		</div>
	</div>
</form>
<script>
	// 카테코리 출력 js 코드
		let catelist = JSON.parse('${catelist}');

		let cate1Array = new Array();
		let cate1Obj = new Object();
		let cateSelect1 = $(".cate1");

	// 카테고리 배열 초기화 메서드
	function makeCateArray(obj,array,catelist){
    	for(let i = 0; i < catelist.length; i++){
        	obj = new Object();
        	obj.name = catelist[i].name;
        	array.push(obj);                
    	}
	}

	// 배열 초기화
		makeCateArray(cate1Obj,cate1Array,catelist);

		for(let i = 0; i < cate1Array.length; i++){
    		cateSelect1.append("<option value='"+cate1Array[i].category_ID+"'>" + cate1Array[i].name + "</option>");
		}
		
		// 취소버튼 클릭 시 이전 페이지로 이동
	    $('.cancel_btn').on("click", function(e) {
        e.preventDefault(); // 기본 동작 방지
        location.href = "/question/qslist"; // 목록 페이지로 이동
    });
</script>
</body>
</html>