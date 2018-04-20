package com.example.droodsunny.taobao;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.bmob.v3.Bmob;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */

    private static final int requestcode=1;
    private static final int requestcode1=2;


    public static LoginActivity sLoginActivity;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private  UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox remember;
    private CheckBox autologin;


   private SharedPreferences autoLogin;
   private SharedPreferences rememberP;
   private SharedPreferences messageUser;
   private SharedPreferences users;

   private static boolean Login=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sLoginActivity=this;
        //初始化Bmob

        Bmob.initialize(this, "1ac735ea998d3f3e71b3c082f96eb4fd");
        // Set up the login form.
        mEmailView =  findViewById(R.id.email);

        autologin=findViewById(R.id.autoLogin);
        remember=findViewById(R.id.remember);

        //如果选自动登录，必定选记住密码
        autologin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!autologin.isChecked()){
                return;
            }
            remember.setChecked(true);
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setTitle(" ");
        }

        toolbar.findViewById(R.id.registe).setOnClickListener(view->{
            Intent intent =new Intent(LoginActivity.this,registeActivity.class);
           startActivityForResult(intent,requestcode);



        });

        //拿到存储的信息
        users=getSharedPreferences("users",MODE_PRIVATE);
        mPasswordView =  findViewById(R.id.password);


        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);


        //登录
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mEmailSignInButton.setOnClickListener(view ->
                attemptLogin());

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        boolean cancel = false;
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //拿到所有的用户名
        Map usersMap=users.getAll();
        Set usersSet=usersMap.keySet();


        View focusView = null;
        // Check for a valid password, if the user entered one.
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Log.d("seterror","error");
            mEmailView.setError("请输入Email");
            focusView = mEmailView;
            cancel = true;
        } else if(!ifexists(email,usersSet)){
            mEmailView.setError("没有该用户");
            focusView=mEmailView;
            cancel=true;
        }else if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError("请输入密码");
            focusView = mPasswordView;
            cancel = true;
    }else if(!ifccorrect(email,password,usersMap)){
        mPasswordView.setError("密码错误");
        focusView = mPasswordView;
        cancel = true;
    }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

            if(focusView!=null) {
                focusView.requestFocus();
            }
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //现在已经是正确的用户名和密码
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        /*mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });*/
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        //取出缓存
        try {
            SharedPreferences ifRemember = getSharedPreferences("rememberPassword", MODE_PRIVATE);
           boolean  ifRem = ifRemember.getBoolean("ifRemember", false);
            SharedPreferences ifAutoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
           boolean  ifAuto = ifAutoLogin.getBoolean("ifAuto", false);
            SharedPreferences usermessage = getSharedPreferences("messageUser", MODE_PRIVATE);
            Map map = usermessage.getAll();
            Set set = map.keySet();
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                arrayList.add((String) iterator.next());
            }
            //只记住最近一次登陆的用户
            String userEmail = arrayList.get(0);
            String userPassword = usermessage.getString(userEmail, null);
            if (ifRem && ifAuto) {
                mEmailView.setText(userEmail);
                mPasswordView.setText(userPassword);
                remember.setChecked(true);
                autologin.setChecked(true);
                //每次自动登陆之后就清空缓存



                //尝试登录
               attemptLogin();

            } else if (ifRem) {
                remember.setChecked(true);
                mEmailView.setText(userEmail);
                mPasswordView.setText(userPassword);

            }else {
                mEmailView.setText(userEmail);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onStart();
    }
    //*
    // 判断是否有该用户*/
    private boolean ifexists(String email,Set set){
        for(Object o : set){
            if(email.equals(o)) {
                return true;
            }
        }
        return false;
    }
    /*判断密码是否正确*/
    private boolean ifccorrect(String email,String password,Map map){
        return map.get(email).equals(password);
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private  class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //应该放这里查询,返回查询结果
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //如果任务被取消
           if(isCancelled()){
                return null;
           }

            return true;
        }
        @Override
        //得到 doInBackground的返回结果
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            Login=true;
          // showProgress(false);
            if (success) {
                //登陆成功
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("username",mEmail);
                intent.putExtra("password",mPassword);
                 //登录,如果没记住密码，密码清空
                 if(!remember.isChecked()){
                    mPasswordView.setText(null);
                }
                  startActivityForResult(intent,requestcode1);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==requestcode){
           if(resultCode==RESULT_OK){
               //从注册页面返回
               mEmailView.setText(data.getStringExtra("email"));
               mPasswordView.setText(data.getStringExtra("password"));
           }
       }
       if (requestCode==requestcode1){
           showProgress(false);
       }
    }


    //销毁之前记住当前登陆的用户信息
    @Override
    protected void onDestroy() {
        //save state
        //保存记住密码状态
        if(remember.isChecked()&&Login){
            rememberP=getSharedPreferences("rememberPassword",MODE_PRIVATE);
            SharedPreferences.Editor editor=rememberP.edit();
            editor.putBoolean("ifRemember",true);
            editor.apply();
        }
        //保存自动登录状态
        if(autologin.isChecked()&&Login){
            autoLogin=getSharedPreferences("autoLogin",MODE_PRIVATE);
            SharedPreferences.Editor editor= autoLogin.edit();
            editor.putBoolean("ifAuto",true);
            editor.apply();
        }
        if(Login){
            //保存上次登陆的用户
            SharedPreferences usermessage = getSharedPreferences("messageUser", MODE_PRIVATE);
            SharedPreferences.Editor editor=usermessage.edit();
            editor.putString(mEmailView.getText().toString(),mPasswordView.getText().toString());
            editor.apply();
        }
        //activity销毁的时候就停止AsyncTask

        if(mAuthTask!=null&&mAuthTask.getStatus()== AsyncTask.Status.RUNNING){
            mAuthTask.cancel(true);
        }
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        Login=false;
        super.onResume();
    }
}

