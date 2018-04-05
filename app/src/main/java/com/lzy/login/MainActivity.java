package com.lzy.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lzy.login_library.LoginUtil;
import com.lzy.login_library.annotation.CheckIfLoginAndLoginAndBackToContinue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void userProfile(View view) {
        startActivity(new Intent(this,UserProfileActivity.class));
    }

    public void logout(View view) {
        LoginUtil.login(false);

    }
    @CheckIfLoginAndLoginAndBackToContinue
    public void zan(View view) {
        Toast.makeText(this,"点赞",Toast.LENGTH_SHORT).show();
    }
}
