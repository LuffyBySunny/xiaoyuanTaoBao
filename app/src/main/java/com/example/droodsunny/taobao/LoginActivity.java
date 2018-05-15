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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.droodsunny.taobao.Unit.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
    View focusView = null;

    // UI references.

    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox remember;
    private CheckBox autologin;

    private SharedPreferences usersInfo;




   private static boolean Login=false;
    private EditText mEmailView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sLoginActivity=this;


        mEmailView =  findViewById(R.id.email);



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

        mPasswordView =  findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });
        Button mEmailSignInButton =  findViewById(R.id.email_sign_in_button);
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




        // Check for a valid password, if the user entered one.
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Log.d("seterror","error");
            mEmailView.setError("请输入Email");
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError("请输入密码");
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

            showProgress(true);
            BmobQuery<User> bmobQuery=new BmobQuery<>();
            bmobQuery.addWhereEqualTo("Email",email);
            bmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    showProgress(false);
                    if(e==null){
                        if(!list.get(0).getPassword().equals(password)){
                            mPasswordView.setError("密码错误");
                            mPasswordView.requestFocus();
                        }else {
                            usersInfo=getSharedPreferences("usersInfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor=usersInfo.edit();
                            editor.putString("Email",email);
                            editor.putBoolean("ifLogin",true);
                            editor.apply();
                            Intent intent =new Intent(LoginActivity.this,Main2Activity.class);
                            startActivity(intent);
                        }
                    }else {
                        mEmailView.setError("没有该用户");
                        mEmailView.requestFocus();
                    }

                }
            });
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //*


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private  class UserLoginTask extends AsyncTask<Void, Void, boolean[]> {
        private final String mEmail;
        private final String mPassword;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        @Override
        protected boolean[] doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //应该放这里查询,返回查询结果
             boolean[] results=new boolean[2];

            BmobQuery<User> bmobQuery=new BmobQuery<>();
            bmobQuery.addWhereEqualTo("Email",mEmail);
            bmobQuery.findObjects(new FindListener<User>() {
               @Override
               public void done(List<User> list, BmobException e) {
                   if(e==null){
                       results[0]=true;
                       results[1] = list.get(0).getPassword().equals(mPassword);
                   }else {
                       results[0]=false;
                       results[1]=false;
                   }
               }
           });
           return results;
        }
        @Override
        //得到 doInBackground的返回结果
        protected void onPostExecute(final boolean[] results) {
            mAuthTask = null;
          // showProgress(false);
           if(!results[0]){
               mEmailView.setError("没有该用户");
              mEmailView.requestFocus();
           }else if (!results[1]){
               mPasswordView.setError("密码错误");
               mPasswordView.requestFocus();
           }else {
               //登陆
               //存储已登陆
                  showProgress(false);
                  usersInfo=getSharedPreferences("usersInfo",MODE_PRIVATE);
               SharedPreferences.Editor editor=usersInfo.edit();
               editor.putString("Email",mEmail);
               editor.putBoolean("ifLogin",true);
               editor.apply();
               Intent intent=new Intent(LoginActivity.this,Main2Activity.class);
               startActivity(intent);
               finish();
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
        if(mAuthTask!=null&&mAuthTask.getStatus()== AsyncTask.Status.RUNNING){
            mAuthTask.cancel(true);
        }
        super.onDestroy();
    }
}

