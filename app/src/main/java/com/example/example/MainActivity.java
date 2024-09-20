package com.example.example;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    private TextView selectedOptionTextView;
    private String[] items;
    private ObjectAnimator jumpAnimator;
    private boolean isScrolledAllTheWayUp = true; // Flag to check if user has scrolled all the way up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.resources);
        selectedOptionTextView = findViewById(R.id.show_list_button);
        TextView showListButton = findViewById(R.id.show_list_button);
        AppCompatButton createAccButton = findViewById(R.id.button2);
        ImageButton backButton = findViewById(R.id.back_button);
        TextView animationMessage = findViewById(R.id.animation_message);
        NestedScrollView scrollView = findViewById(R.id.scrollView);

        // Initialize the jumping animation
        jumpAnimator = ObjectAnimator.ofFloat(animationMessage, "translationY", 0f, -20f, 0f);
        jumpAnimator.setDuration(1000);
        jumpAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);

        // Fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(animationMessage, "alpha", 0f, 1f);
        fadeIn.setDuration(200);

        // Fade-out animation
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(animationMessage, "alpha", 1f, 0f);
        fadeOut.setDuration(200);

        // Set initial visibility and start animations
        animationMessage.setVisibility(View.VISIBLE);
        fadeIn.start();
        jumpAnimator.start();

        // Scroll to bottom on click
        animationMessage.setOnClickListener(v -> {
            if (isScrolledAllTheWayUp) {
                scrollView.smoothScrollTo(0, scrollView.getBottom());
            }
        });

        // Listen for scroll events
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check if user has scrolled all the way up
                if (scrollY == 0) {
                    isScrolledAllTheWayUp = true; // User is at the top
                    fadeIn.start(); // Fade-in animation when the user scrolls back up
                    jumpAnimator.start(); // Restart the jumping animation
                } else {
                    isScrolledAllTheWayUp = false; // User is not at the top
                    if (!v.canScrollVertically(1)) {
                        // User scrolled to the bottom
                        fadeOut.start(); // Fade-out animation when the user scrolls to the bottom
                        jumpAnimator.cancel(); // Stop the jumping animation
                    }
                }
            }
        });

        items = new String[]{"English", "Hindi", "Marathi", "Lassan", "Pyaj"};

        // Set default value to the first item
        selectedOptionTextView.setText(items[0] + " ▼");

        showListButton.setOnClickListener(v -> showBottomSheet());

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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
        });

        createAccButton.setOnClickListener(v -> {
            // Handle "Create Account" click
            Intent intent = new Intent(getApplicationContext(), create_acc.class);
            startActivity(intent);
        });

        spannableString.setSpan(aboutSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(helpSpan, 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(moreSpan, 15, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            String selectedItem = items[position] + " ▼";
            selectedOptionTextView.setText(selectedItem);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
