package com.example.healthkeeper.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.healthkeeper.common.CommonConn;
import com.example.healthkeeper.databinding.ActivityLoginBinding;
import com.example.healthkeeper.main.LoginBeforeActivity;
import com.example.healthkeeper.main.MainActivity;
import com.google.gson.Gson;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfileResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    /* 화면 전환 -> intent
    아이디 비밀번호 -> finish() xx
    회원가입 -> finish()*/

    /*
    https://developer.android.com/training/transitions/start-activity?hl=ko
    intent animation
    */
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NaverIdLoginSDK.INSTANCE.initialize(this, "g7xZnTgyOkVBFJ6ZtVS7", "KgZQDe7BOj","healthkeeper");
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvJoin.setOnClickListener(v -> {
            Intent  intent = new Intent(this, JoinTypeActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            guardianlogin(binding.userId.getText().toString(),binding.userPw.getText().toString());
        });

        binding.tvFindId.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindIdActivity.class);
            startActivity(intent);
        });

        binding.tvFindPw.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindPwActivity.class);
            startActivity(intent);
        });

        binding.cardvKakaoLogin.setOnClickListener(v -> {
            loginWithKakaoAccount(this);
        });

        naverLogin();

    }

    public void guardianlogin(String guardian_id,String guardian_pw){
        /*로그인 유지*/
        SharedPreferences preference = getSharedPreferences("PROJECT_MEMBER",MODE_PRIVATE);
        CommonConn conn = new CommonConn("andlogin",this);

        conn.addParamMap("guardian_id" , guardian_id);
        conn.addParamMap("guardian_pw",guardian_pw);

        conn.onExcute((isResult, data) -> {
            Log.d("로그인", "guardianlogin: "+data);
            GuardianMemberVO vo = new Gson().fromJson(data, GuardianMemberVO.class);


            if(vo ==null){
                Toast.makeText(this,"아이디 또는 패스워드 틀림",Toast.LENGTH_SHORT).show();

            }else{
                /*로그인 유지를 위한 정보 setting*/
                SharedPreferences.Editor editor = preference.edit();
                editor.putString("user_id",vo.getGuardian_id());
                editor.commit();

                /*기존에 켜져있는 액티비티 종료*/
                LoginBeforeActivity lba = (LoginBeforeActivity)LoginBeforeActivity._loginBeforeActivity;
                lba.finish();

                /*화면 전환*/
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getHashKey(){
        PackageInfo packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(packageInfo == null)
            Log.e("KeyHash", "getHashKey: null");

        for(Signature signature : packageInfo.signatures){
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    public void loginWithKakaoAccount(Context context){
        KakaoSdk.init(this,"1cf34851d43903b60a3c465f4245ef4f");//{NATIVE_APP_KEY}

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable error) {
                if(error == null){
                    Log.d("kakao login", "accesstoken: "+ oAuthToken.getAccessToken());
                    Log.d("kakao login", "refreshtoken: "+ oAuthToken.getRefreshToken());

                    Log.d("kakao login", "token id: "+ oAuthToken.getIdToken());
                    UserApiClient.getInstance().me((user, throwable) -> {
                        if(throwable ==null){
                            Log.d("kakao login", "invoke: "+user.getKakaoAccount().getProfile().getNickname());


                        }else{
                            Log.e("kakao login", "invoke: "+ throwable.getMessage() );
                        }
                        return null;
                    });
                }else{
                    Log.d("kakao", "invoke: "+ error.getMessage());
                }

                return null;
            }
        };
        if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)){
            Log.d("kakao Login", "loginWithKakaoAccount: 카카오톡이 설치되어있음");
            UserApiClient.getInstance().loginWithKakaoTalk(context,callback);
        }else{
//            Log.d("kakao login", "loginWithKakaoAccount: 설치안되어있음");
            UserApiClient.getInstance().loginWithKakaoAccount(context,callback);
        }
    }

    public void naverLogin(){
        final String TAG= "naevr Login";
        binding.btnNaverLogin.setOAuthLogin(new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: " + NaverIdLoginSDK.INSTANCE.getAccessToken());
                new NidOAuthLogin().callProfileApi(new NidProfileCallback<NidProfileResponse>() {
                    @Override
                    public void onSuccess(NidProfileResponse nidProfileResponse) {
                        guardianlogin(nidProfileResponse.getProfile().getEmail(),null);
                        Log.d("네이버", "onSuccess: " + nidProfileResponse.getProfile().getEmail());
                        Log.d("네이버", "onSuccess: " + nidProfileResponse.getProfile().getMobile());
                        Log.d("네이버", "onSuccess: " + nidProfileResponse.getProfile().getName());
                        Log.d("네이버", "onSuccess: " + nidProfileResponse.getProfile().getNickname());
                        Log.d("네이버", "onSuccess: " + nidProfileResponse.getProfile().getProfileImage());

                    }

                    @Override
                    public void onFailure(int i, @NonNull String s) {

                    }

                    @Override
                    public void onError(int i, @NonNull String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, @NonNull String s) {

            }

            @Override
            public void onError(int i, @NonNull String s) {

            }
        });
    }
}