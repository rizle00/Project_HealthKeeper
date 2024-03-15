package com.example.healthkeeper.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
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


    ActivityLoginBinding binding;
    SharedPreferences preference;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NaverIdLoginSDK.INSTANCE.initialize(this, "g7xZnTgyOkVBFJ6ZtVS7", "KgZQDe7BOj", "healthkeeper");
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
        editor = preference.edit();

        binding.tvJoin.setOnClickListener(v -> {
            Intent intent = new Intent(this, JoinTypeActivity.class);
            startActivity(intent);
        });

        binding.btnLogin.setOnClickListener(v -> {
            login(binding.userId.getText().toString(), binding.userPw.getText().toString());
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

        binding.fake.setOnClickListener(v -> {
            binding.btnNaverLogin.performClick();
        });


        naverLogin();
        setContentView(binding.getRoot());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loginFailed();
    }

    public void login(String email, String pw) {
        /*로그인 유지*/
//        SharedPreferences preference = getSharedPreferences("PROJECT_MEMBER",MODE_PRIVATE);
        CommonConn conn = new CommonConn("andlogin", this);

        conn.addParamMap("email", email);
        conn.addParamMap("pw", pw);

        conn.onExcute((isResult, data) -> {
            Log.d("로그인", "guardianlogin: " + data);
            MemberVO vo = new Gson().fromJson(data, MemberVO.class);


            if (vo == null) {
                Toast.makeText(this, "아이디 또는 패스워드 틀림", Toast.LENGTH_SHORT).show();

            } else {
//                SharedPreferences.Editor editor = preference.edit();


                loginSuccess(vo);
            }
        });
    }


    public void getHashKey() {
        PackageInfo packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (packageInfo == null)
            Log.e("KeyHash", "getHashKey: null");

        for (Signature signature : packageInfo.signatures) {
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

    public void loginWithKakaoAccount(Context context) {
        KakaoSdk.init(this, "1cf34851d43903b60a3c465f4245ef4f");//{NATIVE_APP_KEY}
        Intent intent = new Intent(this, JoinTypeActivity.class);
        intent.putExtra("logintype", "social");

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable error) {
                if (error == null) {
                    UserApiClient.getInstance().me((user, throwable) -> {
                        if (throwable == null) {
                            CommonConn conn = new CommonConn("sociallogin", context);
//                            MemberVO vo = new MemberVO();

                            conn.addParamMap("social", String.valueOf(user.getId()));

                            conn.onExcute((isResult, data) -> {
                                //가입 정보가 없을때 , 간편 회원가입으로 이동
                                if (data.equals("join")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                            .setMessage("가입되어있지 않은 회원입니다. 가입하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    intent.putExtra("social", String.valueOf(user.getId()));
                                                    startActivity(intent);
                                                }
                                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();


                                } else { //로그인 성공
//                                    SharedPreferences preference = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
//                                    MemberVO kakaoVo = new Gson().fromJson(data, MemberVO.class);
//                                    SharedPreferences.Editor editor = preference.edit();
//                                    editor.putString("user_id", kakaoVo.getMember_id());
//                                    editor.putString("user_name", kakaoVo.getName());
//                                    editor.apply();
                                    MemberVO vo = new Gson().fromJson(data, MemberVO.class);
                                    loginSuccess(vo);
                                }
                            });


                        } else {
                            Log.e("kakao login", "invoke: " + throwable.getMessage());
                        }
                        return null;
                    });
                } else {
                    Log.d("kakao", "invoke: " + error.getMessage());
                }

                return null;
            }
        };
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)) {
            Log.d("kakao Login", "loginWithKakaoAccount: 카카오톡이 설치되어있음");
            UserApiClient.getInstance().loginWithKakaoTalk(context, callback);
        } else {
            Log.d("kakao login", "loginWithKakaoAccount: 설치안되어있음");
            UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
        }
    }

    public void naverLogin() {
        Intent intent = new Intent(this, JoinTypeActivity.class);
        final String TAG = "naver Login";
        binding.btnNaverLogin.setOAuthLogin(new OAuthLoginCallback() {
            @Override
            public void onFailure(int i, @NonNull String s) {

            }

            @Override
            public void onError(int i, @NonNull String s) {

            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: " + NaverIdLoginSDK.INSTANCE.getAccessToken());
                new NidOAuthLogin().callProfileApi(new NidProfileCallback<NidProfileResponse>() {
                    @Override
                    public void onFailure(int i, @NonNull String s) {

                    }

                    @Override
                    public void onError(int i, @NonNull String s) {

                    }

                    @Override
                    public void onSuccess(NidProfileResponse nidProfileResponse) {


                        CommonConn conn = new CommonConn("sociallogin", LoginActivity.this);
                        conn.addParamMap("social", nidProfileResponse.getProfile().getId());
                        conn.onExcute((isResult, data) -> {
                            if (data.equals("join")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                        .setMessage("가입되어있지 않은 회원입니다. 가입하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                intent.putExtra("social", String.valueOf(nidProfileResponse.getProfile().getId()));
                                                startActivity(intent);
                                            }
                                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else { //로그인 성공
//                                SharedPreferences preference = getSharedPreferences("PROJECT_MEMBER", MODE_PRIVATE);
//                                MemberVO kakaoVo = new Gson().fromJson(data, MemberVO.class);
////                                SharedPreferences.Editor editor = preference.edit();
//                                editor.putString("user_id", kakaoVo.getMember_id());
//                                editor.putString("user_name", kakaoVo.getName().toString());
//                                editor.putString("user_social", kakaoVo.getSocial().toString());
//                                Log.d("setName", "onSuccess: " + kakaoVo.getName().toString());
//                                editor.apply();
//                                loginSuccess();
                                MemberVO vo = new Gson().fromJson(data, MemberVO.class);
                                loginSuccess(vo);
                            }
                        });
                    }
                });
            }
        });
    }

    private void loginSuccess(MemberVO vo) {
        /*기존에 켜져있는 액티비티 종료*/
//        LoginBeforeActivity lba = (LoginBeforeActivity)LoginBeforeActivity._loginBeforeActivity;
//        lba.getLifecycle();
//        lba.finish();
//
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
//        finish();
        editor.putString("user_id", vo.getMember_id());
        editor.putString("user_name", vo.getName());
        if (vo.getRole().equals("patient")) {
            editor.putString("token", vo.getToken());
            editor.putString("guardian_id", vo.getGuardian_id());
            editor.putString("address", vo.getAddress() + " " + vo.getAddress_detail());
        }


        editor.apply();
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void loginFailed() {
        // 로그인 실패 시
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }
}