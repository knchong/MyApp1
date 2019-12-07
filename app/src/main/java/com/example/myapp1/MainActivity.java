package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {


    private TextView name,email;
    private FirebaseAuth FBase;
    private  FirebaseFirestore FStore;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.fullname);
        email =findViewById(R.id.fullemail);

        FStore = FirebaseFirestore.getInstance();
        FBase = FirebaseAuth.getInstance();

        userId=FBase.getCurrentUser().getUid();

        DocumentReference documentReference=FStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("userName"));
                email.setText(documentSnapshot.getString("userEmail"));
            }
        });
    }

    public void editprofile(View view) {
        startActivity(new Intent(getApplicationContext(),ActivityEdit.class));
    }

    public void notebtn(View view) {
        startActivity(new Intent(getApplicationContext(),Activity2.class));
    }

    public void startbtn(View view) {
        startActivity(new Intent(getApplicationContext(),Timer1.class));

        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

}
