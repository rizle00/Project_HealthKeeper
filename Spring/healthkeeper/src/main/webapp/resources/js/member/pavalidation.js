// 환자 회원가입 유효성 검사 js 코드
$(document).ready(function(){
    $("#join_button").click(function(){

        // 입력값 변수
        var id = $("#patient_email").val(); 
        var pw = $("#patient_pw").val(); 
        var pwck = $("#patient_pw_chk").val(); 
        var name = $("#patient_name").val(); 
        var phone = $("#patient_phone").val(); 
        var date = $("#patient_birth").val(); 
        var addr = $("[name=ADDRESS_DETAIL]").val(); 
        var gender = $("input[name='GENDER']:checked").val();
        var blood = $("input[name='BLOOD']:checked").val(); 

        // 아이디 유효성 검사
        if(id == ""){
            $('.final_id_ck').css('display','block');
            idChk = false;
        }else{
            $('.final_id_ck').css('display','none');
            idChk = true;
        }
        
        // 비밀번호 유효성 검사
        if(pw == ""){
            $('.final_pw_ck').css('display','block');
            pwChk = false;
        }else{
            $('.final_pw_ck').css('display','none');
            pwChk = true;
        }
        
        // 비밀번호 확인 유효성 검사
        if(pwck == ""){
            $('.final_pwck_ck').css('display','block');
            pwckChk = false;
        }else{
            $('.final_pwck_ck').css('display','none');
            pwckChk = true;
        }

        // 이름 유효성 검사
        if(name == ""){
            $('.final_name_ck').css('display','block');
            nameChk = false;
        } else {
            $('.final_name_ck').css('display','none');
            nameChk = true;
        }

        // 전화번호 유효성 검사
        if(phone == ""){
            $('.final_phone_ck').css('display','block');
            phoneChk = false;
        }else{
            $('.final_phone_ck').css('display','none');
            phoneChk = true;
        }
        
        // 생년월일 유효성 검사
        if(date == ""){
            $('.final_date_ck').css('display','block');
            dateChk = false;
        }else{
            $('.final_date_ck').css('display','none');
            dateChk = true;
        }

        // 주소 유효성 검사
        if(addr == ""){
            $('.final_addr_ck').css('display','block');
            addrChk = false;
        }else{
            $('.final_addr_ck').css('display','none');
            addrChk = true;
        }

        // 성별 유효성 검사
        if (!gender) {
            $('.final_gender_ck').css('display', 'block');
            genderChk = false;
        }else {
            $('.final_gender_ck').css('display', 'none');
            genderChk = true;
        }

        // 혈액형 유효성 검사
        if (!blood) {
            $('.final_blood_ck').css('display', 'block');
            bloodChk = false;
        } else {
            $('.final_blood_ck').css('display', 'none');
            bloodChk = true;
        }

        /* 최종 유효성 검사 */
        if(idChk&&pwChk&&pwckChk&&nameChk&&phoneChk&&dateChk&&addrChk&&genderChk&&bloodChk){
            $("#patient_join_form").attr("action", "/member/join");
            $("#patient_join_form").submit();
        }else{
            return false;
        } 
        
    });

    // 입력값이 변경될 때마다 각 입력란의 유효성을 검사하여 처리
    $("#patient_email").on("input", function() {
        if($(this).val() != "") {
            $('.final_id_ck').css('display','none');
        } else {
            $('.final_id_ck').css('display','block');
        }
    });

    $("#patient_pw").on("input", function() {
        if($(this).val() != "") {
            $('.final_pw_ck').css('display','none');
        } else {
            $('.final_pw_ck').css('display','block');
        }
    });

    $("#patient_pw_chk").on("input", function() {
        if($(this).val() != "") {
            $('.final_pwck_ck').css('display','none');
        } else {
            $('.final_pwck_ck').css('display','block');
        }
    });

    $("#patient_name").on("input", function() {
        if($(this).val() != "") {
            $('.final_name_ck').css('display','none');
        } else {
            $('.final_name_ck').css('display','block');
        }
    });

    $("#patient_phone").on("input", function() {
        if($(this).val() != "") {
            $('.final_phone_ck').css('display','none');
        } else {
            $('.final_phone_ck').css('display','block');
        }
    });

    $("#patient_birth").on("input", function() {
        if($(this).val() != "") {
            $('.final_date_ck').css('display','none');
        } else {
            $('.final_date_ck').css('display','block');
        }
    });

    $("[name=ADDRESS_DETAIL]").on("input", function() {
        if($(this).val() != "") {
            $('.final_addr_ck').css('display','none');
        } else {
            $('.final_addr_ck').css('display','block');
        }
    });

    $("input[name='GENDER']").on("change", function() {
        if ($("input[name='GENDER']:checked").val() !== undefined) {
            $('.final_gender_ck').css('display', 'none');
        } else {
            $('.final_gender_ck').css('display', 'block');
        }
    });

    $("input[name='BLOOD']").on("change", function() {
        if ($("input[name='BLOOD']:checked").val() !== undefined) {
            $('.final_blood_ck').css('display', 'none');
        } else {
            $('.final_blood_ck').css('display', 'block');
        }
    });
});

// 환자 비밀번호 확인 일치 유효성 검사 
 
 $("#patient_pw_chk").on("propertychange change keyup paste input", function(){
 
    var pw = $("#patient_pw").val();
    var pwck = $("#patient_pw_chk").val();
    $('.final_pwck_ck').css('display', 'none');
 
    if(pw == pwck){
        $('.pwck_input_re_1').css('display','block');
        $('.pwck_input_re_2').css('display','none');
        pwckcorCheck = true;
    }else{
        $('.pwck_input_re_1').css('display','none');
        $('.pwck_input_re_2').css('display','block');
        pwckcorCheck = false;
    }        
    
});