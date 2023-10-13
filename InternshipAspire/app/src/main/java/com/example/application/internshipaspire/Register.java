package com.example.application.internshipaspire;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editPasswordAgain;
    Button buttonReg, buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView warningTxtName, warningTxtAge;

    EditText edtTxtFirstName, edtTxtLastName, edtAge;
    CheckBox agreeCheckBox;
    RadioGroup radioGroupGender;

    View parentLayout;

    private static final String TAG = "RegisterActivity";

    @Override
    public void onStart() {
        super.onStart();
        // Check if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        buttonLogin = findViewById(R.id.loginNow);
        editPasswordAgain = findViewById(R.id.passwordAgain);
        parentLayout = findViewById(R.id.rootLayout);

        initViews();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String email, password, password_again;

                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                password_again = String.valueOf(editPasswordAgain.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(password_again)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (validateData()) {
                    if (agreeCheckBox.isChecked()) {
                        warningTxtName.setVisibility(View.GONE);
                        warningTxtAge.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(Register.this, "You need to agree to the license agreement", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign-in fails, display a message to the user.
                                    Snackbar.make(parentLayout, "Registration failed. Please try again.", Snackbar.LENGTH_INDEFINITE)
                                            .setAction("Retry", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    // Retry action
                                                }
                                            }).show();
                                }
                            }
                        });
            }
        });
    }

    private boolean validateData() {
        Log.d(TAG, "validateData: started");
        boolean isValid = true;

        if(edtTxtFirstName.getText().toString().isEmpty() || edtTxtLastName.getText().toString().isEmpty()){
            warningTxtName.setVisibility(View.VISIBLE);
            warningTxtName.setText("Enter your full name");
            isValid = false;

        }

        else {
            warningTxtName.setVisibility(View.GONE);
        }

        if (edtAge.getText().toString().isEmpty()) {
            warningTxtAge.setVisibility(View.VISIBLE);
            warningTxtAge.setText("Enter your age");
            isValid = false;
        } else {
            warningTxtAge.setVisibility(View.GONE);
        }

        return isValid;
    }

    private void initViews() {
        Log.d(TAG, "initViews: Started");
        edtTxtFirstName = findViewById(R.id.edtTxtFirstName);
        edtTxtLastName = findViewById(R.id.edtTxtLastName);
        edtAge = findViewById(R.id.edtAge);

        warningTxtName = findViewById(R.id.warningTxtName);
        warningTxtAge = findViewById(R.id.warningTxtAge);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
    }
}

