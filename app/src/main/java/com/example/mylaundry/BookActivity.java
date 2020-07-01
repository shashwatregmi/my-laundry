package com.example.mylaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();
        MachineItemList machine = intent.getParcelableExtra("Machine");
        String title = machine.getTitle();

        TextView txtview = findViewById(R.id.textView);
        txtview.setText(title);
    }
}