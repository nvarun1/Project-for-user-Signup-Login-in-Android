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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    EditText email,password;
    Button register,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        register = (Button)findViewById(R.id.register);
        back = (Button)findViewById(R.id.back);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//jo bhi stack me activity khula hai usko band kr rha hai
                startActivity(intent);
                finish();//saare khule hue activity ko destroy kr dega jo bhi stack me hai
            }
        });

    }
    private void registerUser(){

        String mail = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if(TextUtils.isEmpty(mail)){
            //Email is empty
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Enter Valid Email Id");
            email.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            //password is empty
            Toast.makeText(this,"Enter Password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if(password.length()<6){
            password.setError("Min Length of password should be 6 characters");
            password.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(mail,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();//progress dialog ko dismiss kr dega
                    Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }else{
                    progressDialog.dismiss();//progress dialog ko dismiss kr dega}
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "This Email is Taken... Try Another", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}