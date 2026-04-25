package com.example.aiassistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText messageInput;
    private Button sendButton;
    private ScrollView scrollView;
    private TextView thinkingView;
    private String apiKey;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiKey = getString(R.string.openai_api_key);

        chatContainer = findViewById(R.id.chatContainer);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        scrollView = findViewById(R.id.scrollView);

        addMessage("👋 Γεια! Είμαι ο AI Tutor σου για την Πληροφορική. Ρώτησέ με ό,τι θέλεις!", false);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                addMessage(userMessage, true);
                messageInput.setText("");
                sendButton.setEnabled(false);
                addThinkingMessage();
                getAIResponse(userMessage);
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void addThinkingMessage() {
        runOnUiThread(() -> {
            thinkingView = new TextView(this);
            thinkingView.setText("🤔 Σκέφτομαι...");
            thinkingView.setPadding(24, 16, 24, 16);
            thinkingView.setTextSize(15);
            thinkingView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            thinkingView.setBackground(getResources().getDrawable(R.drawable.bubble_ai));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 16, 0, 4);
            params.gravity = android.view.Gravity.START;
            thinkingView.setLayoutParams(params);
            chatContainer.addView(thinkingView);
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        });
    }

    private void addMessage(String message, boolean isUser) {
        runOnUiThread(() -> {
            if (thinkingView != null && !isUser) {
                chatContainer.removeView(thinkingView);
                thinkingView = null;
            }

            // Message bubble
            TextView textView = new TextView(this);
            textView.setText(message);
            textView.setPadding(24, 16, 24, 16);
            textView.setTextSize(15);
            textView.setTextColor(getResources().getColor(android.R.color.black));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 16, 0, 4);

            if (isUser) {
                textView.setBackground(getResources().getDrawable(R.drawable.bubble_user));
                params.gravity = android.view.Gravity.END;
            } else {
                textView.setBackground(getResources().getDrawable(R.drawable.bubble_ai));
                params.gravity = android.view.Gravity.START;
            }

            textView.setLayoutParams(params);
            chatContainer.addView(textView);

            // Timestamp
            TextView timeView = new TextView(this);
            timeView.setText(getCurrentTime());
            timeView.setTextSize(11);
            timeView.setTextColor(getResources().getColor(android.R.color.darker_gray));

            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            timeParams.setMargins(8, 0, 8, 8);
            timeParams.gravity = isUser ? android.view.Gravity.END : android.view.Gravity.START;
            timeView.setLayoutParams(timeParams);
            chatContainer.addView(timeView);

            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        });
    }

    private void getAIResponse(String userMessage) {
        try {
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "Είσαι αποκλειστικός εκπαιδευτικός βοηθός πληροφορικής. Απαντάς ΜΟΝΟ σε ερωτήσεις που αφορούν πληροφορική, προγραμματισμό, αλγόριθμους, βάσεις δεδομένων και δίκτυα. Αν σε ρωτήσουν κάτι άσχετο, εξηγείς ευγενικά ότι μπορείς να βοηθήσεις μόνο με θέματα πληροφορικής. Απαντάς πάντα στα ελληνικά.");

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            JSONArray messages = new JSONArray();
            messages.put(systemMessage);
            messages.put(userMsg);

            JSONObject body = new JSONObject();
            body.put("model", "gpt-3.5-turbo");
            body.put("messages", messages);

            Request request = new Request.Builder()
                    .url(API_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    addMessage("Σφάλμα σύνδεσης. Δοκίμασε ξανά.", false);
                    runOnUiThread(() -> sendButton.setEnabled(true));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);
                        String reply = json.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        addMessage(reply, false);
                    } catch (Exception e) {
                        addMessage("Κάτι πήγε στραβά. Δοκίμασε ξανά.", false);
                    }
                    runOnUiThread(() -> sendButton.setEnabled(true));
                }
            });
        } catch (Exception e) {
            addMessage("Σφάλμα. Δοκίμασε ξανά.", false);
            sendButton.setEnabled(true);
        }
    }
}