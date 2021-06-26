package com.example.reachusadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    View repairingCardView,maidCardView,cleaningCardView,carCardView,tutorCardView,pcontrolCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repairingCardView=findViewById(R.id.repairingCardView);
        maidCardView=findViewById(R.id.maidCardView);
        cleaningCardView=findViewById(R.id.cleaningCardView);
        carCardView=findViewById(R.id.carCardView);
        tutorCardView=findViewById(R.id.tutorCardView);
        pcontrolCardView=findViewById(R.id.pasteCardView);

        repairingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                intent.putExtra("Job", "Repairing");
                startActivity(intent);
            }
        });
        maidCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                intent.putExtra("Job", "Maid");
                startActivity(intent);
            }
        });
        carCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                intent.putExtra("Job", "Car");
                startActivity(intent);
            }
        });
        cleaningCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                intent.putExtra("Job", "Sanitizing");
                startActivity(intent);
            }
        });
        tutorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                intent.putExtra("Job", "Tutor");
                startActivity(intent);
            }
        });
        pcontrolCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Services.class);
                intent.putExtra("Job", "Paste");
                startActivity(intent);
            }
        });
    }
}