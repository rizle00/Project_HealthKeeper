<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>회원가입 페이지</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/resources/css/member/join.css">
<script type="text/javascript" src="/resources/js/member/daum.js"></script>
<script type="text/javascript" src="/resources/js/member/pavalidation.js"></script>
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
    									</div>
                  							<span class="email_input_re_1">사용가능한 이메일입니다.</span>
                  							<span class="email_input_re_2">이메일이 존재합니다.</span>
                  							<span class="final_id_ck">이메일을 입력해주세요.</span>
									</div>
									
                            		<div class="form-group">
                                		<label for="password_input">비밀번호</label>
                                		<input type="password" name="PW" id="patient_pw" class="form-control" placeholder="비밀번호">
                            			<span class="final_pw_ck">비밀번호를 입력해주세요.</span>
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="password_confirm_input">비밀번호 확인</label>
                                		<input type="password" name="PW_CHK" id="patient_pw_chk" class="form-control" placeholder="비밀번호 확인">
                            			<span class="final_pwck_ck">비밀번호 확인을 입력해주세요.</span>
                            			<span class="pwck_input_re_1">비밀번호가 일치합니다.</span>
                            			<span class="pwck_input_re_2">비밀번호가 일치하지 않습니다.</span>
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="name_input">이름</label>
                                		<input type="text" name="NAME" id="patient_name" class="form-control" placeholder="이름">
                            			<span class="final_name_ck">이름을 입력해주세요.</span>
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="phone_input">전화번호</label>
                                		<input type="text" name="PHONE" id="patient_phone" class="form-control" placeholder="전화번호">
                            			<span class="final_phone_ck">휴대전화 번호를 입력해주세요.</span>
                            		</div>
                            		
                            		<div class="form-group">
                                		<label for="birth_input">생년월일</label>
                                		<input type="date" name="BIRTH" id="patient_birth" class="form-control" placeholder="생년월일">
                            			<span class="final_date_ck">생년월일을 입력해주세요.</span>
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
                            			<span class="final_addr_ck">주소를 입력해주세요.</span>
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
    									<span class="final_gender_ck">성별을 입력해주세요.</span>
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
										<span class="final_blood_ck">혈액형을 입력해주세요.</span>
									</div>
									
    								<div class="form-group">
    									<input type="hidden" id="role" name="ROLE" value="m">
									</div>
									
                            		<div class="text-center">
                                		<button type="button" id="join_button" class="btn btn-primary btn-lg">회원가입</button>
                            		</div>
                        			</form>
                        		</div>
                        		
                        <!-- 보호자 회원가입 -->
                        <div class="tab-pane fade" id="guardian_registration" role="tabpanel" aria-labelledby="guardian-tab">
                        	<form id="guardian_join_form" method="post">
                        		<div class="form-group">
    								<label for="email_input">아이디</label>
    							<div class="input-group">
        							<input type="email" name="EMAIL" id="guardian_email" class="form-control custom-class-name" placeholder="아이디">
    							</div>
    								<span class="gemail_input_re_1">사용가능한 이메일입니다.</span>
                  					<span class="gemail_input_re_2">이메일이 존재합니다.</span>
                  					<span class="gu_final_id_ck">이메일을 입력해주세요.</span>
                  					
								</div>
								
                            	<div class="form-group">
                                	<label for="password_input">비밀번호</label>
                                	<input type="password" name="PW" id="guardian_pw" class="form-control" placeholder="비밀번호">
                            	</div>
                            		<span class="gu_final_pw_ck">비밀번호를 입력해주세요.</span>
                            	
                            	<div class="form-group">
                                	<label for="password_confirm_input">비밀번호 확인</label>
                                	<input type="password" name="PW_CHK" id="guardian_pw_ck" class="form-control" placeholder="비밀번호 확인">
                            	</div>
                            		<span class="gu_final_pwck_ck">비밀번호 확인을 입력해주세요.</span>
                            	
                            	<div class="form-group">
                                	<label for="name_input">이름</label>
                                	<input type="text" name="NAME" id="guardian_name" class="form-control" placeholder="이름">
                            	</div>
                            		<span class="gu_final_name_ck">이름을 입력해주세요.</span>
                            	
                            	<div class="form-group">
                                	<label for="phone_input">전화번호</label>
                                	<input type="text" name="PHONE" id="guardian_phone" class="form-control" placeholder="전화번호">
                            	</div>
                            		<span class="gu_final_phone_ck">휴대전화 번호를 입력해주세요.</span>
                            	
                            	<div class="form-group">
                                	<label for="birth_input">생년월일</label>
                                	<input type="date" name="BIRTH" id="guardian_birth" class="form-control" placeholder="생년월일">
                            	</div>
                            		<span class="gu_final_date_ck">생년월일을 입력해주세요.</span>
                            	
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
                            		<span class="gu_final_addr_ck">주소를 입력해주세요.</span>
                            		
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
										<span class="gu_final_gender_ck">성별을 입력해주세요.</span>

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
										<span class="gu_final_blood_ck">혈액형을 입력해주세요.</span>
									
                            	<div class="form-group">
    									<input type="hidden" id="role" name="ROLE" value="m">
									</div>
                            	<div class="text-center">
                                	<button type="button" id="guardian_join_button" class="btn btn-primary btn-lg">회원가입</button>
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

<!-- 다음 주소연동 js -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>

$("#patient_email").on("propertychange change keyup paste input", function(){
    var email = $('#patient_email').val(); // 입력된 이메일 값
    var data = {email : email}; // 서버로 보낼 데이터
    
    // 이메일 형식을 검사하는 정규식
    var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    // 이메일 형식이 올바르지 않을 경우
    if (!emailRegex.test(email)) {
        $('.email_input_re_1').css("display","none");
        $('.email_input_re_2').css("display","inline-block");
        return; // 가입 절차를 중단하고 함수를 종료
    }
    
    // 이메일 중복 검사 AJAX 요청
    $.ajax({
        type : "post",
        url : "/member/memberIdChk",
        data : data,
        success : function(result){
            if(result != 'fail'){
                $('.email_input_re_1').css("display","inline-block");
                $('.email_input_re_2').css("display","none");
            } else {
                $('.email_input_re_2').css("display","inline-block");
                $('.email_input_re_1').css("display","none");
            }
            
            // 유효성 검사가 완료되면 유효성 문구를 숨깁니다.
            if ($('.email_input_re_1').css("display") == "none" && $('.email_input_re_2').css("display") == "none") {
                $('.email_input_re_1, .email_input_re_2').css("display", "none");
            }
        }
    });
});

//보호자 아이디 중복검사 폼
$("#guardian_email").on("propertychange change keyup paste input", function(){

console.log("keyup 테스트");	

var email = $('#guardian_email').val();			// .id_input에 입력되는 값
var data = {email : email}				// '컨트롤에 넘길 데이터 이름' : '데이터(.id_input에 입력되는 값)'

$.ajax({
	type : "post",
	url : "/member/memberIdChk2",
	data : data,
	success : function(result){
		if(result != 'fail'){
			$('.gemail_input_re_1').css('display','inline-block');
			$('.gemail_input_re_2').css('display','none');
		}else{
			$('.gemail_input_re_2').css('display','inline-block');
			$('.gemail_input_re_1').css('display','none');
		}
	} // success 종료
}); // ajax 종료

});// function 종료

// 보호자 회원가입 유효성 검사 js 코드
	$(document).ready(function(){
    	$("#guardian_join_button").click(function(){
    	
    	// 보호자 유효성 검사 입력값 변수
    	var gu_id = $("#guardian_email").val(); 
    	var gu_pw = $("#guardian_pw").val(); 
    	var gu_pwck = $("#guardian_pw_ck").val(); 
    	var gu_name = $("#guardian_name").val(); 
    	var gu_phone = $("#guardian_phone").val(); 
    	var gu_date = $("#guardian_birth").val(); 
    	var gu_addr = $("[name=ADDRESS_DETAIL]").val(); 
    	var gu_gender = $("input[name='GENDER']:checked").val();
    	var gu_blood = $("input[name='BLOOD']:checked").val();
    	
    	// 보호자 아이디 유효성 검사
        if(gu_id == ""){
        	console.log(false)
        	$('.gu_final_id_ck').css('display','block');
        	gu_idChk = false;
        }else{
        	console.log(true)
        	$('.gu_final_id_ck').css('display','none');
        	gu_idChk = true;
        }
        
        // 보호자 비밀번호 유효성 검사
        if(gu_pw == ""){
        	$('.gu_final_pw_ck').css('display','block');
        	gu_pwChk = false;
        }else{
        	$('.gu_final_pw_ck').css('display','none');
        	gu_pwChk = true;
        }
        
        // 보호자 비밀번호 확인 유효성 검사
        if(gu_pwck == ""){
        	$('.gu_final_pwck_ck').css('display','block');
        	gu_pwckChk = false;
        }else{
        	$('.gu_final_pwck_ck').css('display','none');
        	gu_pwckChk = true;
        }
        
        // 보호자 이름 유효성 검사
        if(gu_name == ""){
        	$('.gu_final_name_ck').css('display','block');
        	gu_nameChk = false;
        }else{
        	$('.gu_final_name_ck').css('display','none');
        	gu_nameChk = true;
        }
        
        // 보호자 전화번호 유효성 검사
        if(gu_phone == ""){
        	$('.gu_final_phone_ck').css('display','block');
        	gu_phoneChk = false;
        }else{
        	$('.gu_final_phone_ck').css('display','none');
        	gu_phoneChk = true;
        }
        
        // 보호자 생년월일 유효성 검사
        if(gu_date == ""){
        	$('.gu_final_date_ck').css('display','block');
        	gu_dateChk = false;
        }else{
        	$('.gu_final_date_ck').css('display','none');
        	gu_dateChk = true;
        }
        
        
        // 보호자 주소 유효성 검사
        if(gu_addr == ""){
        	$('.gu_final_addr_ck').css('display','block');
        	gu_addrChk = false;
        }else{
        	$('.gu_final_addr_ck').css('display','none');
        	gu_addrChk = true;
        }
        
        // 보호자 성별 유효성 검사
        if (!gu_gender) {
    		$('.gu_final_gender_ck').css('display', 'block');
    		gu_genderChk = false;
		}else {
    		$('.gu_final_gender_ck').css('display', 'none');
    		gu_genderChk = true;
		}
        
     // 보호자 혈액형 유효성 검사
	    if (!blood) {
        	$('.gu_final_blood_ck').css('display', 'block');
        	gu_bloodChk = false;
    	}else {
        	$('.gu_final_blood_ck').css('display', 'none');
        	gu_bloodChk = true;
    	}
	


        
        /* 보호자 최종 유효성 검사 */
        if(gu_idChk&&gu_pwChk&&gu_pwckChk&&gu_nameChk&&gu_phoneChk&&gu_dateChk&&gu_addrChk&&gu_genderChk&&gu_bloodChk){
        	$("#guardian_join_form").attr("action", "/member/join");
            $("#guardian_join_form").submit();
        }else{
 	       return false;
        } 
        
    });
    	
    	// 입력값이 변경될 때마다 각 입력란의 유효성을 검사하여 처리
    	$("#guardian_email").on("input", function() {
        	if($(this).val() != "") {
            	$('.gu_final_id_ck').css('display','none');
        	}else {
            	$('.gu_final_id_ck').css('display','block');
        	}
    	});
    	
    	 $("#guardian_pw").on("input", function() {
    	        if($(this).val() != "") {
    	            $('.gu_final_pw_ck').css('display','none');
    	        } else {
    	            $('.gu_final_pw_ck').css('display','block');
    	        }
    	    });

    	    $("#guardian_pw_ck").on("input", function() {
    	        if($(this).val() != "") {
    	            $('.gu_final_pwck_ck').css('display','none');
    	        } else {
    	            $('.gu_final_pwck_ck').css('display','block');
    	        }
    	    });

    	    $("#guardian_name").on("input", function() {
    	        if($(this).val() != "") {
    	            $('.gu_final_name_ck').css('display','none');
    	        } else {
    	            $('.gu_final_name_ck').css('display','block');
    	        }
    	    });

    	    $("#guardian_phone").on("input", function() {
    	        if($(this).val() != "") {
    	            $('.gu_final_phone_ck').css('display','none');
    	        } else {
    	            $('.gu_final_phone_ck').css('display','block');
    	        }
    	    });

    	    $("#guardian_birth").on("input", function() {
    	        if($(this).val() != "") {
    	            $('.gu_final_date_ck').css('display','none');
    	        } else {
    	            $('.gu_final_date_ck').css('display','block');
    	        }
    	    });

    	    $("[name=ADDRESS_DETAIL]").on("input", function() {
    	        if($(this).val() != "") {
    	            $('.gu_final_addr_ck').css('display','none');
    	        } else {
    	            $('.gu_final_addr_ck').css('display','block');
    	        }
    	    });

    	    $("input[name='GENDER']").on("change", function() {
    	        if ($("input[name='GENDER']:checked").val() !== undefined) {
    	            $('.gu_final_gender_ck').css('display', 'none');
    	        } else {
    	            $('.gu_final_gender_ck').css('display', 'block');
    	        }
    	    });

    	    $("input[name='BLOOD']").on("change", function() {
    	        if ($("input[name='BLOOD']:checked").val() !== undefined) {
    	            $('.gu_final_blood_ck').css('display', 'none');
    	        } else {
    	            $('.gu_final_blood_ck').css('display', 'block');
    	        }
    	    });
    	});
    	


	
// 환자 , 보호자 회원가입 탭 이동 js 코드
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