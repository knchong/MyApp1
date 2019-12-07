package com.example.myapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=_])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +             //MIN 6
                    "$");
    public static final String TAG = "TAG";

    EditText mUsername, mEmail, mPassword,mPassword2;
    Button mRegister;
    TextView mlogin;
    FirebaseAuth FBase;
    FirebaseFirestore FStore;
    ProgressBar progressBar;
    FirebaseDatabase FData;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mlogin = findViewById(R.id.GoToLogin);
        mUsername = findViewById(R.id.username);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPassword2 = findViewById(R.id.password2);
        mRegister = findViewById(R.id.registerbutton);
        FStore =FirebaseFirestore.getInstance();
        FBase = FirebaseAuth.getInstance();
        FData = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);


        if (FBase.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String password2 = mPassword2.getText().toString().trim();
                final String username = mUsername.getText().toString();


                if (username.isEmpty()) {
                    mUsername.setError("This field is required.");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email required.");
                    return;

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email not Valid.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password required.");
                    return;
                }

                if(password.length()>10){
                    mPassword.setError("Max 10 characters.");
                    return;
                }

                if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    mPassword.setError("Password too weak.");
                    return;
                }

                if (TextUtils.isEmpty(password2)) {
                    mPassword2.setError("Please Re-enter password.");
                    return;
                } else if (!password.matches(password2)) {
                    mPassword2.setError("Password do not match");
                    return;
                }

                if(password.length()>10){
                    mPassword.setError("Max 10 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register user to database
                FBase.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                            userId=FBase.getCurrentUser().getUid();
                            DocumentReference documentReference = FStore.collection("users").document(userId);

                            final Map<String,Object> user = new HashMap<>();
                            user.put("userName",username);
                            user.put("userEmail",email);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "User Created!", Toast.LENGTH_SHORT).show();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Error!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG,e.toString());
                                        }
                                    });

                            startActivity(new Intent(getApplicationContext(), Login.class));
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                            progressBar.setVisibility(View.GONE);
                        } else{
                            Toast.makeText(Register.this, "Error! Please try again. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        //to login page
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                FirebaseAuth.getInstance().signOut();
            }
        });
    }
}
