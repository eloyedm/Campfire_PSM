package com.example.android.campfire;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Signup extends AppCompatActivity {

    private static final String TAG = "Signup";
    private EditText e_name;
    private EditText e_email;
    private EditText e_password;
    private Button b_signupBtn;
    private TextView t_loginLink;

/*    EditText input_name = findViewById(R.id.input_name);
    EditText input_email = findViewById(R.id.input_email);
    EditText input_password = findViewById(R.id.input_password);
    Button btn_signup = findViewById(R.id.btn_signup);
    TextView link_login = findViewById(R.id.link_login);*/
    /*@InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button btn_signup;
    @InjectView(R.id.link_login) TextView link_login;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e_name = findViewById(R.id.input_name);
        e_email = findViewById(R.id.input_email);
        e_password = findViewById(R.id.input_password);
        b_signupBtn = findViewById(R.id.btn_signup);
        t_loginLink = findViewById(R.id.link_login);

        b_signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        t_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        b_signupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = e_name.getText().toString();
        String email = e_email.getText().toString();
        String password = e_password.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        b_signupBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        b_signupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = e_name.getText().toString();
        String email = e_email.getText().toString();
        String password = e_password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            e_name.setError("at least 3 characters");
            valid = false;
        } else {
            e_name.setError(null);
        }

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
