package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import javax.annotation.Nullable;

public class ActivityEdit extends AppCompatActivity {

    //public static final String MY_NAME = "userName";
  //  public static final String MY_EMAIL = "userEmail";
    private TextView myName,myEmail;
    private FirebaseAuth FBase=FirebaseAuth.getInstance();
    private FirebaseFirestore FStore=FirebaseFirestore.getInstance();
    private String userId=FBase.getCurrentUser().getUid();
    private DocumentReference proEdit = FStore.collection("users").document(userId);
    private EditText edit_name,edit_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        setTitle("Edit Profile");

        myName = findViewById(R.id.fullname);
        myEmail = findViewById(R.id.fullemail);
       // userId = FBase.getCurrentUser().getUid();

        DocumentReference documentReference = FStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                myName.setText(documentSnapshot.getString("userName"));
                myEmail.setText(documentSnapshot.getString("userEmail"));
            }
        });
    }

    public void updatebutton(View view){

        edit_name =(EditText)findViewById(R.id.editname);
        edit_email=(EditText)findViewById(R.id.editemail);

        String name_new = edit_name.getText().toString();
        String email_new = edit_email.getText().toString();

       if (name_new.isEmpty()) {
            edit_name.setError("Please Enter new Username.");
            return;
        }
        if (email_new.isEmpty()) {
            edit_email.setError("Please Enter New Email.");
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email_new).matches()) {
            edit_email.setError("Email not Valid.");
            return;
        }

        proEdit.update("userName", name_new);
        proEdit.update("userEmail", email_new);

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        this.finish();
    }

}

