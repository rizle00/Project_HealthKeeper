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

<form action="/notice/notregistr" method="post" enctype="multipart/form-data">
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
    
    <div>
    <input type="file" id="fileInput" name="files" style="display: none;" multiple>
    <label for="fileInput" style="cursor: pointer;">파일 선택</label>
    <span id="fileNames"></span>
    <button type="button" onclick="cancelFileSelection()">취소</button>
</div>
	
    <button type="submit" class="notbtn">등록</button>
</form>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
// 파일 선택 취소 js코드
  document.getElementById('fileInput').addEventListener('change', function() {
        var fileNames = document.getElementById('fileNames');
        fileNames.textContent = ''; // 파일 이름 초기화
        
        if (this.files && this.files.length > 0) {
            for (var i = 0; i < this.files.length; i++) {
                fileNames.textContent += this.files[i].name + ', ';
            }
            // 마지막 쉼표 제거
            fileNames.textContent = fileNames.textContent.slice(0, -2);
        }
    });

    function cancelFileSelection() {
        var fileInput = document.getElementById('fileInput');
        fileInput.value = ''; // 파일 선택 취소
        document.getElementById('fileNames').textContent = ''; // 파일 이름 초기화
    }


</script>
</body>
</html>