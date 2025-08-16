package com.example.pronotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteDetailsActivity extends AppCompatActivity {

    ImageButton save_note_btn;
    EditText notes_title_text;
    EditText notes_content_text;
    TextView pageTitleTextView;
    TextView deleteNoteTextViewBtn;

    String title , content, docId;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        notes_title_text = findViewById(R.id.notes_title_text);
        notes_content_text = findViewById(R.id.notes_content_text);
        save_note_btn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title_text_view);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");


        if (docId!= null && !docId.isEmpty()){
            isEditMode = true;
        }

        notes_title_text.setText(title);
        notes_content_text.setText(content);
        if (isEditMode){
            pageTitleTextView.setText("Edit Your Note");
            // DELETE NOTE BTN SHOULD BE VISIBLE IN EDIT MODE
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }



        save_note_btn.setOnClickListener(View->saveNote());
        deleteNoteTextViewBtn.setOnClickListener(View->deleteNoteFromFirebase());
    }


    void saveNote(){
        String noteTitle = notes_title_text.getText().toString();
        String noteContent = notes_content_text.getText().toString();
        if (noteTitle==null || noteTitle.isEmpty()){
            notes_title_text.setError("Title is Required");
            return;
        }
        finish();

        // OBJECT OF NOTE CLASS

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteInFirebase(note);
    }
    void saveNoteInFirebase(Note note){
        DocumentReference documentReference;

        if (isEditMode){
            // OPDATE NOTE
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else{
            // CREATE NOTE
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // NOTE ADDED TO FIREBASE
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }
                else{
                    // NOTE NOT ADDED
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");

                }
            }
        });

    }
    void deleteNoteFromFirebase(){

        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // NOTE DELETE TO FIREBASE
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                    finish();
                }
                else{
                    // NOTE NOT DELETED
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");

                }
            }
        });

    }
}

