package com.kmj.sunrinsaekki.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.kmj.sunrinsaekki.GraphAPI;
import com.kmj.sunrinsaekki.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final AccessToken accessToken = AccessToken.getCurrentAccessToken(); //앱에 저장 된토큰 가져오기
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected() || mobile.isConnected()) {
            if (isLoggedIn) {
                GraphAPI.getMyFriends(accessToken);
                GraphAPI.getMyInformation(accessToken,SplashActivity.this);
                MainActivity.getFromFireBase();
                Log.e("isLoggedIn", "True" + accessToken);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SplashActivity.this, "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 2000);
            }
            else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        } else {
            Toast.makeText(SplashActivity.this, "네트워크 연결 확인하고 다시 실행하세요.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    finish();
                }
            }, 2000);

        }
    }
}
