package com.example.example;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textview.MaterialTextView;

public class create_acc extends AppCompatActivity {

    private MaterialTextView datePickerText;
    private MaterialTextView listViewText;
    private String[] listItems = {"Male", "Female", "Attack Helicopter"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        ImageButton backButton = findViewById(R.id.back_button);
        datePickerText = findViewById(R.id.date_picker_text);
        listViewText = findViewById(R.id.list_view_text);

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
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    datePickerText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showListViewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option");

        builder.setItems(listItems, (dialog, which) -> {
            String selectedOption = listItems[which];
            listViewText.setText(selectedOption);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}