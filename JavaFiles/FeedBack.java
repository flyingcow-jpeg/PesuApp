package com.example.pesuapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedBack extends AppCompatActivity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedbackform);

        btn=findViewById(R.id.btnMainActivity);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
    }
    private void showDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("feedback form");
        dialog.setMessage("provide us your feedback");

        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.send_feedback,null);
        final TextInputEditText edtEmail = reg_layout.findViewById(R.id.edtEmail);
        final TextInputEditText edtName = reg_layout.findViewById(R.id.edtName);
        final TextInputEditText edtFeedback  = reg_layout.findViewById(R.id.edtFeedback);
        final TextInputEditText edtLecturer  = reg_layout.findViewById(R.id.edtLecturer);
        final TextInputEditText edtCourse  = reg_layout.findViewById(R.id.edtCourse);
        dialog.setView(reg_layout);

        dialog.setView(reg_layout);

        dialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(edtEmail.getText().toString())){
                    Toast.makeText(FeedBack.this,"Type email...", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if(!edtEmail.getText().toString().trim().matches(emailPattern)){
                        Toast.makeText(FeedBack.this, "email not valid bro", Toast.LENGTH_SHORT).show();
                    }
                }
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    Toast.makeText(FeedBack.this, "name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (edtName.getText().toString().length()>50) {
                    Toast.makeText(FeedBack.this, "Bro what name is this", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Pattern p = Pattern.compile("([0-9])");
                    Matcher m = p.matcher(edtName.getText().toString());

                    if(m.find()){
                        Toast.makeText(FeedBack.this,"You're robot or what", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                if (TextUtils.isEmpty(edtCourse.getText().toString())) {
                    Toast.makeText(FeedBack.this, "Course Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edtCourse.getText().toString().length()==9){
                    if(edtCourse.getText().toString().charAt(0)!='U'||
                            edtCourse.getText().toString().charAt(1)!='E' ||
                            !Character.isLetter(edtCourse.getText().toString().charAt(4)) ||
                            !Character.isLetter(edtCourse.getText().toString().charAt(5))) {
                        Toast.makeText(FeedBack.this, "You lyin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Toast.makeText(FeedBack.this, "You lyin for sho", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtFeedback.getText().toString())){
                    Toast.makeText(FeedBack.this,"Feedback", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (edtFeedback.getText().toString().length()>10) {
                    Toast.makeText(FeedBack.this, "Too long.....thats what sh....", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!edtFeedback.getText().toString().contains(" ")) {
                    Toast.makeText(FeedBack.this, "spam", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edtLecturer.getText().toString())) {
                    Toast.makeText(FeedBack.this, "Lecturer's name", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object value = snapshot.getValue();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FeedBack.this,"Failed  to read value",Toast.LENGTH_SHORT).show();

                    }
                });
                myRef.child("Users").child(edtName.getText().toString()).child("Email").setValue(edtEmail.getText().toString());
                myRef.child("Users").child(edtName.getText().toString()).child("Feedback").setValue(edtFeedback.getText().toString());
                myRef.child("Users").child(edtName.getText().toString()).child("Name").setValue(edtName.getText().toString());
                myRef.child("Users").child(edtName.getText().toString()).child("Course Code").setValue(edtCourse.getText().toString());
                Toast.makeText(FeedBack.this,"Thanks for your feedback",Toast.LENGTH_SHORT).show();

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
