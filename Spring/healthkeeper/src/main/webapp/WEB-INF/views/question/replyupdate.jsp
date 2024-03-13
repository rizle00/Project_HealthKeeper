<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.4.1.js" 
    integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" 
    crossorigin="anonymous"></script>
    <title>게시판</title>
</head>

<body>

    <div id="root">
        <header>
            <h1>게시판</h1>
        </header>

        <section id="container">
            <form id="updateForm" method="post" action="/question/replyupdate">
                <input type="hidden" name="QS_BNO" value="${replyupdate.QS_BNO}" readonly="readonly"/>
                <input type="hidden" id="qrno" name="QRNO" value="${replyupdate.QRNO}" />
                <input type="hidden" id="pageNum" name="pageNum" value="${qcri.pageNum}"> 
                <input type="hidden" id="amount" name="amount" value="${qcri.amount}"> 
                <input type="hidden" id="keyword" name="keyword" value="${qcri.keyword}"> 
                <input type="hidden" id="type" name="type" value="${qcri.type}"> 
                <table>
                    <tbody>
                        <tr>
                            <td>
                                <label for="content">댓글 내용</label><input type="text" id="content" name="CONTENT" value="${replyupdate.CONTENT}"/>
                            </td>
                        </tr>    

                    </tbody>            
                </table>
                <div>
                    <button type="submit" class="update_btn">저장</button>
                    <button type="button" class="cancel_btn">취소</button>
                </div>
            </form>
        </section>
        <hr />
    </div>
    <script>
    $(document).ready(function(){
        var formObj = $("form[name='updateForm']");
        
        $(".cancel_btn").on("click", function(){
            // 이전 페이지로 이동하는 동작
            location.href = "/question/gsget?QS_BNO=${pageInfo.QS_BNO}"
                            + "&pageNum=${qcri.pageNum}"
                            + "&amount=${qcri.amount}"
                            + "&keyword=${qcri.keyword}"
                            + "&type=${qcri.type}";
        });

        // 수정완료 버튼 클릭 시 폼 제출
        $(".update_btn").on("click", function() {
            formObj.submit();
        });
    });
</script>
</body>
</html>