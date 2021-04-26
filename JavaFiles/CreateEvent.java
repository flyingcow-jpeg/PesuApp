package com.example.pesuapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEvent extends AppCompatActivity {
    public static final String Event_KEY = "event";
    public static final String Des_KEY = "description";
    public static final String Date_KEY = "date";
    public static final String Exp_KEY = "exp";
    public static final String Usr_KEY = "usr";

    private Button saveE;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static boolean isValidDate(String d)
    {
        String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)d);
        return matcher.matches();
    }
    public static boolean isExpDate(Integer d)
    {
        Date temp=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String temp1= formatter.format(temp);
        temp1=temp1.replaceAll("[/]","");
        String it=temp1.substring(4,8)+temp1.substring(2,4)+temp1.substring(0,2);
        Integer temp2 = Integer.parseInt(it);
        if(d >= temp2)
            return false;
        else
            return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_event);

        EditText info10 = (EditText) findViewById(R.id.eventname);
        EditText info9 = (EditText) findViewById(R.id.eventdis);
        EditText info8 = (EditText) findViewById(R.id.eventexp);
        EditText info7 = (EditText) findViewById(R.id.userbase);
        saveE = (Button) findViewById(R.id.savebutton);
        saveE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info1 = info10.getText().toString();
                String info2 = info9.getText().toString();
                String info3 = info8.getText().toString();
                String info75 = info7.getText().toString();

                Integer info4 = 0;
                if( info1.isEmpty() || info2.isEmpty()){
                    if(info1.isEmpty() && info2.isEmpty()) {
                        Toast.makeText(com.example.pesuapp.CreateEvent.this, "No event created", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(info1.isEmpty()) {
                        Toast.makeText(com.example.pesuapp.CreateEvent.this, "Enter event title", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        Toast.makeText(com.example.pesuapp.CreateEvent.this, "Enter event description", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Date date = new Date();
                if(info3.isEmpty()){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String temp1= formatter.format(date);
                    temp1=temp1.replaceAll("[/]","");
                    String i5= temp1.substring(4,8)+temp1.substring(2,4)+temp1.substring(0,2);
                    info4 = Integer.parseInt(i5);
                    info4= info4 + 7;
                    Log.d("error",String.valueOf(info4).substring(6,8));
                    if(Integer.parseInt(String.valueOf(info4).substring(6,8))>30){
                        info4=info4-30+100;
                    }
                    Toast.makeText(com.example.pesuapp.CreateEvent.this, "Default expiry date", Toast.LENGTH_SHORT).show();
                } else{
                    if(!isValidDate(info3)){
                        Toast.makeText(com.example.pesuapp.CreateEvent.this, "Enter valid date of dd/mm/yyyy format", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        info3=info3.replaceAll("[/]","");
                        String i3=info3.substring(4,8)+info3.substring(2,4)+info3.substring(0,2);
                        info4=Integer.parseInt(i3);
                        if(isExpDate(info4)) {
                            Toast.makeText(com.example.pesuapp.CreateEvent.this, "Expiration date has passed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (info75.isEmpty() || info75=="PES"){
                    info75="PES";
                } else{
                    if(!info75.substring(0,3).equals("PES") || info75.length()>13){
                        Log.d("Infosub",info75.substring(0,2));
                        Toast.makeText(com.example.pesuapp.CreateEvent.this, "Not Valid Srn", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        Pattern p = Pattern.compile("([a-zA-Z])");
                        Matcher m = p.matcher(info75.substring(3,info75.length()));

                        if(m.find()){
                            Toast.makeText(com.example.pesuapp.CreateEvent.this,"Srn cannot contain a character after PES", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }


                //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                //String info3= formatter.format(date);

                Map<String,Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put(Event_KEY,info1);
                dataToSave.put(Des_KEY,info2);
                dataToSave.put(Date_KEY,date);
                dataToSave.put(Exp_KEY,info4);
                dataToSave.put(Usr_KEY,info75);
                db.collection("Events").add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(com.example.pesuapp.CreateEvent.this,"Successfully created event",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error=e.getMessage();
                        Toast.makeText(com.example.pesuapp.CreateEvent.this,"error :"+error,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
