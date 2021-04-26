package com.example.pesuapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventNotif extends AppCompatActivity {
    private FirestoreRecyclerAdapter<EventModel, EventViewHolder> adapter;
    private String usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event);


        //usd="PES12018001";
        Intent intent5= getIntent();
        usr= intent5.getStringExtra("usd");

        Date temp=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String temp1= formatter.format(temp);
        temp1=temp1.replaceAll("[/]","");
        String i5= temp1.substring(4,8)+temp1.substring(2,4)+temp1.substring(0,2);
        Integer temp2 = Integer.parseInt(i5);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Events")
                .whereGreaterThanOrEqualTo("exp",temp2)
                .orderBy("exp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<EventModel> options = new FirestoreRecyclerOptions.Builder<EventModel>()
                .setQuery(query, EventModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<EventModel, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull EventModel model) {
                String temp51 = model.getUsr();
                boolean temp5=usr.contains(temp51);
                holder.setEventName(model.getDescription(),model.getDate(),model.getEvent(),model.getTime(),temp5);
            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleevent, parent, false);
                return new EventViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private class EventViewHolder extends RecyclerView.ViewHolder{
        private View view;

        EventViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setEventName(String Dis,String date, String event,String time,boolean a) {
            TextView textView = view.findViewById(R.id.text_view);
            TextView textView1 = view.findViewById(R.id.text_view1);
            TextView dateView = view.findViewById(R.id.date_view);
            TextView timeView = view.findViewById(R.id.time_view);
            textView1.setText(event);
            textView.setText(Dis);
            dateView.setText(date);
            timeView.setText(time);
            if(a){
                TextView YourView = view.findViewById(R.id.foryou);
                YourView.setText("For you");
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
