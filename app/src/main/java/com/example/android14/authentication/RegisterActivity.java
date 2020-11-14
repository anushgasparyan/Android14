package com.example.android14.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android14.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText name, surname, email, password, age;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        age = findViewById(R.id.age);
        auth = FirebaseAuth.getInstance();
    }

    public void go_to_signin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "There is a problem", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();
                        databaseReference = FirebaseDatabase.getInstance().getReference("users").push();
                        Model model = new Model();
                        model.setId(databaseReference.getKey());
                        model.setName(name.getText().toString());
                        model.setSurname(surname.getText().toString());
                        model.setAge(Integer.parseInt(age.getText().toString()));
                        model.setEmail(email.getText().toString());
                        databaseReference.setValue(model);
                    }
                }
            });
        }
    }
}