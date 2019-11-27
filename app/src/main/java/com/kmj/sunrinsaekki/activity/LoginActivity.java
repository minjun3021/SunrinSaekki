package com.kmj.sunrinsaekki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kmj.sunrinsaekki.GraphAPI;
import com.kmj.sunrinsaekki.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    Boolean isSuccess=true;
    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    void init(){
        Logger.addLogAdapter(new AndroidLogAdapter());
        loginButton=findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","user_friends"));
        callbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if(loginResult.getAccessToken().getDeclinedPermissions().size()>=1){
                    isSuccess=false;
                    Toast.makeText(LoginActivity.this, "친구리스트 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                }
            }

            @Override
            public void onCancel() {
                Logger.e("onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Logger.e("onError");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(isSuccess){
            Intent intent = new Intent(this, MainActivity.class);
            GraphAPI.getMyFriends(AccessToken.getCurrentAccessToken());
            GraphAPI.getMyInformation(AccessToken.getCurrentAccessToken(),this);
            MainActivity.getFromFireBase();
            startActivity(intent);
            finish();
        }
        isSuccess=true;

        super.onActivityResult(requestCode, resultCode, data);
    }
}
