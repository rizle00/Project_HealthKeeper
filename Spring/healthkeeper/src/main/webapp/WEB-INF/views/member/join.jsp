<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/resources/css/member/join.css">
</head>
<jsp:include page="/WEB-INF/views/include/header.jsp" />
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
    					<ul class="nav nav-tabs card-header-tabs">
        					<li class="nav-item">
            					<a class="nav-link active patient-tab" id="patient-tab" data-bs-toggle="tab" 
            					   href="#patient_registration" role="tab" aria-controls="patient_registration" 
            					   aria-selected="true">환자 회원가입</a>
        					</li>
        					<li class="nav-item">
            					<a class="nav-link guardian-tab" id="guardian-tab" data-bs-toggle="tab" 
            					   href="#guardian_registration" role="tab" aria-controls="guardian_registration" 
            					   aria-selected="false">보호자 회원가입</a>
        					</li>
    					</ul>
					</div>
                    <div class="card-body">
                    	<div class="tab-content">
                    		<div class="tab-pane fade show active" id="patient_registration" role="tabpanel" 
                    		     aria-labelledby="patient-tab">
                    		    <!-- 환자 회원가입 -->
                         		<form id="patient_join_form" method="post">
                            		<div class="form-group">
    									<label for="email_input">아이디</label>
    									<div class="input-group">
                  						<input type="email" name="EMAIL" id="patient_email" class="form-control" placeholder="아이디">
        									<div class="input-group-append">
            									<button class="btn btn-outline-secondary" type="button" onclick="checkDuplicate()">중복 확인</button>
        									</div>
    									</div>
									</div>
									
                            		<div class="form-group">
                                		<label for="password_input">비밀번호</label>
                                		<input type="password" name="PW" id="patient_pw" class="form-control" placeholder="비밀번호">
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="password_confirm_input">비밀번호 확인</label>
                                		<input type="password" name="PW_CHK" id="patient_pw_chk" class="form-control" placeholder="비밀번호 확인">
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="name_input">이름</label>
                                		<input type="text" name="NAME" id="patient_name" class="form-control" placeholder="이름">
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="phone_input">전화번호</label>
                                		<input type="text" name="PHONE" id="patient_phone" class="form-control" placeholder="전화번호">
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="birth_input">생년월일</label>
                                		<input type="date" name="BIRTH" id="patient_birth" class="form-control" placeholder="생년월일">
                            		</div>
                            		
                            		<div class="form-group">
    									<input type="hidden" id="social" name="SOCIAL" value="x">
									</div>
									
									<div class="form-group">
    									<input type="hidden" id="alarm" name="ALARM" value="y">
									</div>
									
                            		<div class="form-group">
                                		<label>주소</label>
                                	<div class="input-group mb-3">
                                    	<input type="text" name="ADDRESS" class="form-control" readonly="readonly" placeholder="주소">
                                    <div class="input-group-append">
                                        <button class="btn btn-outline-primary" type="button" onclick="execution_daum_address()">주소찾기</button>
                                    </div>
                                	</div>
                                		<input type="text" name="ADDRESS_DETAIL" class="form-control" readonly="readonly" placeholder="나머지주소">
                            		</div>
                            		
                            		<div class="form-group">
    									<label>성별</label>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="GENDER" id="femaleGender" value="남">
        								<label class="form-check-label" for="femaleGender">남</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="GENDER" id="maleGender" value="여">
        								<label class="form-check-label" for="maleGender">여</label>
    								</div>
									</div>

									<div class="form-group">
    									<label>혈액형</label>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeA" value="A">
        								<label class="form-check-label" for="bloodTypeA">A</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeB" value="B">
        								<label class="form-check-label" for="bloodTypeB">B</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeO" value="O">
        								<label class="form-check-label" for="bloodTypeO">O</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeAB" value="AB">
        								<label class="form-check-label" for="bloodTypeAB">AB</label>
    								</div>
									</div>
									
    								<div class="form-group">
    									<input type="hidden" id="role" name="ROLE" value="m">
									</div>
									
                            		<div class="text-center">
                                		<button type="submit" name="join_button" class="btn btn-primary btn-lg">회원가입</button>
                            		</div>
                        			</form>
                        		</div>
                        		
                        <!-- 보호자 회원가입 -->
                        <div class="tab-pane fade" id="guardian_registration" role="tabpanel" aria-labelledby="guardian-tab">
                        	<form id="guardian_join_form" method="post">
                        		<div class="form-group">
    								<label for="email_input">아이디</label>
    							<div class="input-group">
        							<input type="email" name="EMAIL" id="guardian_email" class="form-control" placeholder="아이디">
        						<div class="input-group-append">
            						<button class="btn btn-outline-secondary" type="button" onclick="checkDuplicate()">중복 확인</button>
        						</div>
    							</div>
								</div>
								
                            	<div class="form-group">
                                	<label for="password_input">비밀번호</label>
                                	<input type="password" name="PW" id="guardian_pw" class="form-control" placeholder="비밀번호">
                            	</div>
                            	
                            	<div class="form-group">
                                	<label for="password_confirm_input">비밀번호 확인</label>
                                	<input type="password" name="PW_CHK" id="guardian_pw_ck" class="form-control" placeholder="비밀번호 확인">
                            	</div>
                            	
                            	<div class="form-group">
                                	<label for="name_input">이름</label>
                                	<input type="text" name="NAME" id="guardian_name" class="form-control" placeholder="이름">
                            	</div>
                            	
                            	<div class="form-group">
                                	<label for="phone_input">전화번호</label>
                                	<input type="text" name="PHONE" id="guardian_phone" class="form-control" placeholder="전화번호">
                            	</div>
                            	
                            	<div class="form-group">
                                	<label for="birth_input">생년월일</label>
                                	<input type="date" name="BIRTH" id="guardian_birth" class="form-control" placeholder="생년월일">
                            	</div>
                            	
                            	<div class="form-group">
    								<input type="hidden" id="social" name="SOCIAL" value="x">
								</div>
								
								<div class="form-group">
    								<input type="hidden" id="alarm" name="ALARM" value="y">
								</div>
								
                            	<div class="form-group">
                                	<label>주소</label>
                                <div class="input-group mb-3">
                                    <input type="text" name="ADDRESS" class="form-control" readonly="readonly" placeholder="주소">
                                    <div class="input-group-append">
                                        <button class="btn btn-outline-primary" type="button" onclick="execution_daum_address()">주소찾기</button>
                                    </div>
                                </div>
                                <input type="text" name="ADDRESS_DETAIL" class="form-control" readonly="readonly" placeholder="나머지주소">
                            	</div>
                            	<div class="form-group">
    									<label>성별</label>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="GENDER" id="femaleGender" value="남">
        								<label class="form-check-label" for="femaleGender">남</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="GENDER" id="maleGender" value="여">
        								<label class="form-check-label" for="maleGender">여</label>
    								</div>
									</div>

									<div class="form-group">
    									<label>혈액형</label>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeA" value="A">
        								<label class="form-check-label" for="bloodTypeA">A</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeB" value="B">
        								<label class="form-check-label" for="bloodTypeB">B</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeO" value="O">
        								<label class="form-check-label" for="bloodTypeO">O</label>
    								</div>
    								<div class="form-check form-check-inline">
        								<input class="form-check-input" type="radio" name="BLOOD" id="bloodTypeAB" value="AB">
        								<label class="form-check-label" for="bloodTypeAB">AB</label>
    								</div>
									</div>
                            	<div class="form-group">
    									<input type="hidden" id="role" name="ROLE" value="m">
									</div>
                            	<div class="text-center">
                                	<button type="submit" name="join_button" class="btn btn-primary btn-lg">회원가입</button>
                            	</div>
                        	</form>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<jsp:include page="/WEB-INF/views/include/footer.jsp" />
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
<script>
/* 다음 주소 연동 */
function execution_daum_address(){
 
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
        	// 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
             // 주소변수 문자열과 참고항목 문자열 합치기
                addr += extraAddr;
            
            } else {
            	addr += ' ';
            }
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $("[name=ADDRESS]").val(data.zonecode);    // 대체가능
            $("[name=ADDRESS]").val(addr);            // 대체가능
            // 상세주소 입력란 disabled 속성 변경 및 커서를 상세주소 필드로 이동한다.
            $("[name=ADDRESS_DETAIL]").attr("readonly",false);
            $("[name=ADDRESS_DETAIL]").focus();
        }
    }).open();    
}
// 아이디 중복검사 폼
function checkDuplicate() {
        var email = $('#email_input').val(); // 이메일 입력란의 값을 가져옴
        $.ajax({
            url: 'checkDuplicateEmail', // 중복 확인을 위한 서버의 URL
            type: 'POST',
            data: {email: email}, // 이메일 값을 서버로 전송
            success: function(response) {
                if (response === 'duplicate') {
                    alert('중복된 이메일입니다.');
                } else {
                    alert('사용 가능한 이메일입니다.');
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패: ' + status + ', ' + error);
            }
        });
    }

// 회원가입 폼
$(document).ready(function(){
    // Change form action and submit for email-based login
    $("#login_button").click(function(){
        $("#patient_join_form").attr("action", "/member/join"); // Adjust action URL for email-based login
        $("#patient_join_form").submit();
    });
});


$(document).ready(function(){
    $('#patient-tab').on('click', function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $('#guardian-tab').on('click', function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
});
</script>
</html>