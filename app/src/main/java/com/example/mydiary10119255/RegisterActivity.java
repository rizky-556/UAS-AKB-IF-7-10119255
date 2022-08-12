package com.example.mydiary10119255;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*
        NIM : 10119255
        Nama : Rizki Lail Rahman
        Kelas : IF-7
         */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView banner;
    private TextView regisUser;
    private EditText editTextNamaLengkap, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        banner = (ImageView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        regisUser = (Button) findViewById(R.id.regisUser);
        regisUser.setOnClickListener(this);

        editTextNamaLengkap = (EditText) findViewById(R.id.namaLengkap);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.regisUser:
                regisUser();
                break;
        }
    }

    private void regisUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String namaLengkap = editTextNamaLengkap.getText().toString().trim();

        if (namaLengkap.isEmpty()) {
            editTextNamaLengkap.setError("Wajib di isi!");
            editTextNamaLengkap.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Wajib di isi!");
            editTextEmail.requestFocus();
            return;
        }



         if (password.isEmpty()){
             editTextPassword.setError("Wajib di isi!");
             editTextPassword.requestFocus();
             return;
         }
         if (password.length() < 6){
             editTextPassword.setError("Password minimal 6 karakter1");
             editTextPassword.requestFocus();
             return;
         }

         progressBar.setVisibility(View.VISIBLE);
         mAuth.createUserWithEmailAndPassword(email, password)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {

                         if (task.isSuccessful()){
                             User user = new User(namaLengkap, email);

                             FirebaseDatabase.getInstance().getReference("Users")
                                     .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                     .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {

                                             if (task.isSuccessful()){
                                                 Toast.makeText(RegisterActivity.this, "Pengguna telah sukses mendaftar!", Toast.LENGTH_LONG).show();
                                                 progressBar.setVisibility(View.VISIBLE);

                                             } else {
                                                 Toast.makeText(RegisterActivity.this, "Registrasi gagal! Coba lagi.", Toast.LENGTH_LONG).show();
                                                 progressBar.setVisibility(View.GONE);
                                             }
                                         }
                                     });

                         }else {Toast.makeText(RegisterActivity.this, "Registrasi gagal! Coba lagi.", Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.GONE);
                         }
                     }
                 });
    }
}