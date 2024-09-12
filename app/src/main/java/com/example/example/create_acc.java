package com.example.example;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class create_acc extends AppCompatActivity {

    private MaterialTextView datePickerText;
    private MaterialTextView listViewText;
    private final String[] listItems = {"Male", "Female", "Attack Helicopter"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        ImageButton backButton = findViewById(R.id.back_button);
        datePickerText = findViewById(R.id.date_picker_text);
        listViewText = findViewById(R.id.list_view_text);
        FloatingActionButton chatFab = findViewById(R.id.chat_fab);
        chatFab.setOnClickListener(v -> {
            // Open the Chat Activity
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        });

        datePickerText.setOnClickListener(v -> showDatePickerDialog());
        listViewText.setOnClickListener(v -> showListViewDialog());

        backButton.setOnClickListener(v -> {
            finish();
        });
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, month1, dayOfMonth);
                    Calendar currentDate = Calendar.getInstance();
                    Calendar agelimit=Calendar.getInstance();
                    agelimit.add(Calendar.YEAR,-15);

                    if (selectedDate.after(currentDate)) {
                        Toast.makeText(this,
                                "Time travellers not allowed", Toast.LENGTH_SHORT).show();
                    } else if(!selectedDate.before(agelimit)){
                        Toast.makeText(this,
                                "Back to bed!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        @SuppressLint("DefaultLocale") String formattedDate =
                                String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                        datePickerText.setText(formattedDate);

                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showListViewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option");

        builder.setItems(listItems, (dialog, which) -> {
            String selectedOption = listItems[which];
            if (Objects.equals(listItems[which], listItems[2])) {
                Toast.makeText(this,
                        "Helicopter attack not allowed", Toast.LENGTH_SHORT).show();
            }
            listViewText.setText(selectedOption);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}