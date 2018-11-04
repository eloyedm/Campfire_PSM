package com.example.android.campfire;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    private EditText e_email;
    private EditText e_password;
    private Button b_loginBtn;
    private TextView t_signupLink;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final String TAG = "Login";
    private static final int REQUEST_SIGNUP = 0;

    /*@InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ButterKnife.inject(this);
        e_email = findViewById(R.id.input_email);
        e_password = findViewById(R.id.input_password);
        b_loginBtn = findViewById(R.id.btn_login);
        t_signupLink = findViewById(R.id.link_signup);
        b_loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        t_signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        b_loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = e_email.getText().toString();
        String password = e_password.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        Intent intentStart = new Intent(getApplicationContext(), NewPLace.class);
        startActivity(intentStart);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        b_loginBtn.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        b_loginBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = e_email.getText().toString();
        String password = e_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            e_email.setError("enter a valid email address");
            valid = false;
        } else {
            e_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            e_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            e_password.setError(null);
        }

        return valid;
    }
}
