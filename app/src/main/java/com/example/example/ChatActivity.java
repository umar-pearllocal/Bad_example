package com.example.example;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.example.BuildConfig;

public class ChatActivity extends AppCompatActivity {

    private static final String API_URL = BuildConfig.API_URL;
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private RecyclerView recyclerViewChat;
    private MessageAdapter chatAdapter;
    private List<Message> messages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recycler_view_chat);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

        messages = new ArrayList<>();
        chatAdapter = new MessageAdapter(messages);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        client = new OkHttpClient();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage(String message) {
        // Disable the send button to prevent multiple messages
        buttonSend.setEnabled(false);

        // Add user message to the list
        Message userMessage = new Message(message, "user");
        messages.add(userMessage);
        chatAdapter.notifyItemInserted(messages.size() - 1);

        // Clear input field
        editTextMessage.setText("");

        // Send message to backend
        sendRequestToBackend(message);
    }

    private void sendRequestToBackend(String userMessage) {
        try {
            // Create JSON payload
            JSONObject messageObject = new JSONObject();
            messageObject.put("role", "user");

            JSONArray partsArray = new JSONArray();
            JSONObject partObject = new JSONObject();
            partObject.put("text", userMessage);
            partsArray.put(partObject);

            messageObject.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(messageObject);

            JSONObject payload = new JSONObject();
            payload.put("contents", contentsArray);

            RequestBody body = RequestBody.create(JSON, payload.toString());

            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("x-goog-api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        // Re-enable the send button if the request fails
                        buttonSend.setEnabled(true);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        handleAIResponse(responseData);
                    } else {
                        runOnUiThread(() -> {
                            // Re-enable the send button if the response is not successful
                            buttonSend.setEnabled(true);
                        });
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            buttonSend.setEnabled(true); // Re-enable the button if there's an error creating the request
        }
    }

    private void handleAIResponse(String response) {
        runOnUiThread(() -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray candidatesArray = jsonObject.getJSONArray("candidates");

                if (candidatesArray.length() > 0) {
                    JSONObject candidate = candidatesArray.getJSONObject(0);
                    JSONObject content = candidate.getJSONObject("content");
                    JSONArray partsArray = content.getJSONArray("parts");

                    if (partsArray.length() > 0) {
                        String aiMessage = partsArray.getJSONObject(0).getString("text");

                        // Convert any special characters to displayable formats
                        aiMessage = formatMessage(aiMessage);

                        // Add AI message to the list and update RecyclerView
                        Message message = new Message(aiMessage, "ai");
                        messages.add(message);
                        chatAdapter.notifyItemInserted(messages.size() - 1);

                        // Scroll to the bottom of the RecyclerView
                        recyclerViewChat.scrollToPosition(messages.size() - 1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // Re-enable the send button after receiving AI response
                buttonSend.setEnabled(true);
            }
        });
    }

    private String formatMessage(String message) {
        // Basic example of converting plain text to HTML
        message = message.replaceAll("\\*\\*(.+?)\\*\\*", "<b>$1</b>"); // Bold
        message = message.replaceAll("\\*(.+?)\\*", "<i>$1</i>"); // Italics
        message = message.replaceAll("_(.+?)_", "<u>$1</u>"); // Underline
        message = message.replaceAll("\n", "<br/>");
        message = message.replaceAll("(?m)^- ", "• ");
        return message;
    }
}
