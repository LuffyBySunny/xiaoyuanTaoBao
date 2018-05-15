package com.example.droodsunny.taobao;

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

import com.example.droodsunny.taobao.Unit.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class registeActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button registeButton;
    private Toolbar mToolbar;
    boolean cancel;
    // Check for a valid password, if the user entered one.
    View focusView;



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
           cancel=false;
           focusView=null;
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
            }else{
              BmobQuery<User> bmobQuery=new BmobQuery<>();
              //查询所有的用户名
              bmobQuery.addQueryKeys("Email");
              //要在回掉方法里面
              bmobQuery.findObjects(new FindListener<User>() {
                  @Override
                  public void done(List<User> list, BmobException e) {
                      if(list!=null){
                          for(User user:list){
                              if(user.getEmail().equals(email)){
                                  mEmailView.setError("该用户名已经存在");
                                  focusView=mEmailView;
                                  cancel=true;
                              }
                          }
                      }
                      if(!cancel){
                          User user=new User();
                          user.setEmail(email);
                          user.setPassword(password);
                          user.save(new SaveListener<String>() {
                              @Override
                              public void done(String s, BmobException e) {
                                  if(e==null){
                                      Toast.makeText(v.getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                  }else {
                                      Toast.makeText(v.getContext(),"网络错误，注册失败",Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });
                      }else {
                          focusView.requestFocus();
                      }
                  }
              });
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
    /*查询用户名，并保存*/

}



