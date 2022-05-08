package com.example.inspigram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    public static final String TAG = SignInActivity.class.getName();
    public static final String PREF_KEY = SignInActivity.class.getPackage().toString();

    private FirebaseAuth firebaseAuth;

    EditText userName;
    EditText email;
    EditText password;
    EditText passwordConfirm;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);

        SharedPreferences preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        this.email.setText(email);
        this.password.setText(password);
        this.firebaseAuth = FirebaseAuth.getInstance();

    }

    public void singIn(View view) {
        String userName = this.userName.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();

        if (!passwordConfirm.equals(password)) {
            Toast.makeText(SignInActivity.this, "A jelszók nem egyeznek!", Toast.LENGTH_SHORT).show();

        } else {
            Log.i(TAG, "The registration was successful!\n Your username is " + userName + " and your email is " + email);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "The registration was successful!");
                            goHome();
                        } else {
                            Toast.makeText(SignInActivity.this, "A regisztráció sikertelen volt!\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    public void cancel(View view) {
        finish();
    }

}