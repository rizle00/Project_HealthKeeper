<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 등록</title>
<link rel="stylesheet" href="/resources/css/notice/notregistr.css">
</head>
<body>
<h1>공지사항 작성페이지</h1>

<form id="noticeForm" action="/notice/notregistr" method="post">
    <div class="input_wrap">
        <label>Title</label>
        <input name="TITLE">
    </div>
    <div class="input_wrap">
        <label>Content</label>
        <textarea rows="3" name="CONTENT"></textarea>
    </div>
    <div class="input_wrap">
        <label>Writer</label>
        <input name="MEMBER_ID">
    </div>
    <div id="fileIndex">
        <div>
            <input type="file" name="file_1">
            <button class="fileDelBtn" type="button">삭제</button>
        </div>
    </div>
    <button type="button" class="fileAdd_btn">파일추가</button>
    <button type="submit" class="notbtn">등록</button>
</form>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
    $(".fileAdd_btn").on("click", function(){
        var fileIndex = $("#fileIndex div").length + 1;
        $("#fileIndex").append("<div><input type='file' style='float:left;' name='file_"+ fileIndex +"'><button type='button' style='float:right;' class='fileDelBtn'>삭제</button></div>");
    });

    $(document).on("click", ".fileDelBtn", function(){
        $(this).parent().remove();
    });
});

// 파일 입력 필드에 변경이 있을 때마다 호출되는 함수
document.getElementById("fileInput").addEventListener("change", function() {
    var fileInput = document.getElementById("fileInput");
    var submitButton = document.getElementById("submitButton");

    // 파일이 선택되었는지 확인
    if (fileInput.files.length > 0) {
        submitButton.disabled = false; // 파일이 선택된 경우 등록 버튼 활성화
    } else {
        submitButton.disabled = true; // 파일이 선택되지 않은 경우 등록 버튼 비활성화
    }
});
</script>
</body>
</html>