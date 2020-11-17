package com.example.android14.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android14.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText editText;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editText = findViewById(R.id.email);
        auth = FirebaseAuth.getInstance();
    }

    public void recoverPassword(View view) {
        String email = editText.getText().toString();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "We've sent a message to your email", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Wrong email", Toast.LENGTH_LONG).show();
                }
            }
        });
        SendMail sendMail = new SendMail(getApplicationContext(), email, "Forget password", "Change your password here");
        sendMail.execute();
    }
}