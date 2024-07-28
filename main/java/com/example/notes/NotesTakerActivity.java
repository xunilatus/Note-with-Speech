package com.example.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.lang.String;
import androidx.appcompat.app.AppCompatActivity;
import com.example.notes.Models.Notes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotesTakerActivity extends AppCompatActivity {
    EditText editText_title, editText_notes;
    ImageView imageView_save, imageView_say;
    Notes notes;
    boolean isOldNote = false;

    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        imageView_save = findViewById(R.id.imageView_save);
        imageView_say = findViewById(R.id.imageView_say);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);

        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            isOldNote = true;
        } catch (Exception e){
            e.printStackTrace();
        }


        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editText_title.getText().toString();
                String description = editText_notes.getText().toString();

                if (description.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "please add some notes", Toast.LENGTH_SHORT).show();
                    return;
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm: a");
                Date date = new Date();

                if (!isOldNote) {
                    notes = new Notes();
                }
                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int language = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        imageView_say.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText_notes.getText().toString();
                int speech = textToSpeech.speak(s, textToSpeech.QUEUE_FLUSH, null);
            }

        });



    }
}