package com.example.letspoll;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button login,register;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    //ProgressDialog progressDialog;//kaam nahi kr rha hai isse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               userLogin();
           }
       });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }//end of onCreate function

    private void userLogin(){
        String mail = email.getText().toString().trim();//jo string aa rha hai usko ek variable me daal rhe hai
        String pwd = password.getText().toString().trim();//jo string aa rha hai usko ek variable me daal rhe hai

        if(TextUtils.isEmpty(mail)){
            email.setError("Enter Email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("enter valid email id");
            email.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            password.setError("Enter Password");
            password.requestFocus();
            return;
        }
        if(password.length()<6){
            password.setError("Min length of password is 6 characters");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);//use this
        //progressDialog.setMessage("Logging In...");//yaha pe error generate kr rha hai progressDialog.setMessage pe
        //progressDialog.setCanceledOnTouchOutside(false);
        //progressDialog.show();

        mAuth.signInWithEmailAndPassword(mail,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //progressDialog.dismiss();//progress dialog ko dismiss kr dega
                progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }//end of function userlogin
}//end of class mainactivity