package com.example.inspigram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>
        {
    public static final String TAG = MainActivity.class.getName();
    public static final String PREF_KEY = MainActivity.class.getPackage().toString();
    public static final int INT_KEY = 69;

    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;
    private GoogleSignInClient googleSignInClient;

    EditText email;
    EditText password;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.img2);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Button button = findViewById(R.id.logInButton);
        //new LogInAsyncTask(button).execute();

        getSupportLoaderManager().restartLoader(0, null, this);
    }

    public void logIn(View view) {
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                goHome();
            } else {
                Toast.makeText(MainActivity.this, "A bejelentkezés sikertelen volt!\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    private void goHome() {
        animation();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        preferences.edit()
                .putString("email", email.getText().toString())
                .putString("password", password.getText().toString())
                .apply();

        /*SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("password", password.getText().toString());

        editor.apply();*/

    }

    public void googleLogIn(View view) {
        animation();
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, INT_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INT_KEY) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(googleSignInAccount.getIdToken());
            }
            catch (ApiException apiException) {
                Log.w(TAG, apiException.getMessage(), apiException);
            }
        }
    }

    private void firebaseAuthWithGoogle(String token) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(token, null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "The log in with google account was successful!");
                goHome();
            } else {
                Toast.makeText(MainActivity.this, "A bejelentkezés sikertelen volt!\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new LogInAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Button button = findViewById(R.id.logInButton);
        button.setText(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}

    public void animation() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_sun);
        imageView.startAnimation(animation);
    }
}