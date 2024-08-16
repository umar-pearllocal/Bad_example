package com.example.example;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    private TextView selectedOptionTextView;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.logo);
        TextView textView = findViewById(R.id.resources);
        selectedOptionTextView = findViewById(R.id.show_list_button);
        TextView showListButton = findViewById(R.id.show_list_button);
        AppCompatButton createAccButton = findViewById(R.id.button2);
        ImageButton backButton = findViewById(R.id.back_button);

        items = new String[]{"English", "Hindi", "Marathi", "Lassan", "Pyaj"};

        // Set default value to the first item
        selectedOptionTextView.setText(items[0]+" ▼");

        showListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        String bottomtext = "About | Help | More";
        SpannableString spannableString = new SpannableString(bottomtext);

        // Define clickable spans
        ClickableSpan aboutSpan = new ClickableSpan() {
            public void onClick(@NonNull View widget) {
                // Handle "About" click
            }
        };

        ClickableSpan helpSpan = new ClickableSpan() {
            public void onClick(@NonNull View widget) {
                // Handle "Help" click
            }
        };



        ClickableSpan moreSpan = new ClickableSpan() {
            public void onClick(@NonNull View widget) {
                // Handle "More" click
            }
        };

        backButton.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            finish();
        });

        createAccButton.setOnClickListener(v -> {
            // Handle "Create Account" click
            Intent intent = new Intent(getApplicationContext(), create_acc.class);
            startActivity(intent);
        });

        spannableString.setSpan(aboutSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(helpSpan, 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(moreSpan, 16, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new NoUnderlineSpan(), 0,
                bottomtext.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static class NoUnderlineSpan extends CharacterStyle implements UpdateAppearance {
        public void updateDrawState(TextPaint tp) {
            tp.setColor(Color.BLACK);
            tp.setUnderlineText(false);
        }
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.listview_botton, null);
        bottomSheetDialog.setContentView(sheetView);

        ListView listView = sheetView.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items[position]+" ▼";
            selectedOptionTextView.setText(selectedItem);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
