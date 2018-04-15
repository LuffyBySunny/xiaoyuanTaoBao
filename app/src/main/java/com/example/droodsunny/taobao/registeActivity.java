package com.example.droodsunny.taobao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registeActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button registeButton;
    private Toolbar mToolbar;
    boolean cancel=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);



        mEmailView =  findViewById(R.id.email);
        mPasswordView =  findViewById(R.id.password);
        registeButton=findViewById(R.id.email_registe_button);
        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(" ");
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
        registeButton.setOnClickListener(v->{
            // Check for a valid password, if the user entered one.
            View focusView = null;
            String email=mEmailView.getText().toString();
            String password=mPasswordView.getText().toString();

            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError("密码至少五位");
                focusView = mPasswordView;
                cancel = true;
            }

            if (!isEmailValid(email)) {
                mEmailView.setError("Email必须包含@");
                focusView = mEmailView;
                cancel = true;
            }

            if(!cancel) {
                //存储注册信息
                mSharedPreferences = getSharedPreferences("users", MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(email, password);
                editor.apply();
                Intent intent =new Intent();
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                setResult(RESULT_OK,intent);

                Toast.makeText(v.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            }else {
               if(focusView!=null) {
                   focusView.requestFocus();
               }
            }
        });




    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}
