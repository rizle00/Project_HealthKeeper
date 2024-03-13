<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
<link rel="stylesheet" href="/resources/css/member/login.css">
</head>
<jsp:include page="/WEB-INF/views/include/header.jsp" />
<body>
	<section class="forms-section">
  		<div class="forms">
    		<div class="form-wrapper is-active">
      			<button type="button" class="switcher switcher-login">
        			환자 로그인
        			<span class="underline"></span>
      			</button>
      		<form class="form form-login" id="login_form" method="post">
        		<fieldset>
          			<legend>Please, enter your email and password for login.</legend>
          				<div class="input-block">
            				<label for="login-email">E-mail</label>
            				<input id="login-email" name="EMAIL" type="email" required>
          				</div>
          				
          				<div class="input-block">
            				<label for="login-password">Password</label>
            				<input id="login-password" name="PW" type="password" required>
          				</div>
        		</fieldset>
        		<button type="submit" class="login_button">Login</button>
      		</form>
    		</div>
    		<div class="form-wrapper">
      			<button type="button" class="switcher switcher-signup">
        			보호자 로그인
        			<span class="underline"></span>
      			</button>
      			<form class="form form-signup" id="login_form" method="post">
        			<fieldset>
          				<legend>Please, enter your email, password and password confirmation for sign up.</legend>
          			<div class="input-block">
            			<label for="signup-email">E-mail</label>
            			<input id="signup-email" name="EMAIL" type="email" required>
          			</div>
          			
          			<div class="input-block">
            			<label for="signup-password">Password</label>
            			<input id="signup-password" name="PW" type="password" required>
          			</div>
        			</fieldset>
        			<button type="submit" class="login_button">Login</button>
      			</form>
    		</div>
  		</div>
	</section>
<script>
	// 로그인 버튼 클릭메서드
	$(".login_button").click(function(){
		$("#login_form").attr("action", "/member/login");
		$("#login_form").submit();
	});
	
	// 창 화면전환 js 코드
	const switchers = [...document.querySelectorAll('.switcher')]

	switchers.forEach(item => {
		item.addEventListener('click', function() {
			switchers.forEach(item => item.parentElement.classList.remove('is-active'))
			this.parentElement.classList.add('is-active')
		})
	})
</script>
</body>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
</html>