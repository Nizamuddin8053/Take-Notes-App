package com.example.pronotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {
    private final String toString = Utility.getCollectionReferenceForNotes().getId().toString();
    ImageButton backBtn, searchBtn;
    EditText searchInput;
    RecyclerView searchRecyclerView;
    NoteAdapter noteAdapter;
    private CollectionReference collectionReferenceForNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        backBtn = findViewById(R.id.back_btn);
        searchBtn = findViewById(R.id.search_note_btn);
        searchInput = findViewById(R.id.note_input);
        searchInput.requestFocus();
        searchRecyclerView = findViewById(R.id.search_note_recycler_view);
        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });
        searchBtn.setOnClickListener((v)->{
            String searchTerm = searchInput.getText().toString();
            if (searchTerm.isEmpty() || searchTerm.length()<3){
                searchInput.setError("Invalid input");
                return;
            }
            setUpSearchRecyclerView(searchTerm);

        });
    }
    void setUpSearchRecyclerView(String searchTerm){
        Query query = Utility.getCollectionReferenceForNotes().orderBy("title").whereLessThanOrEqualTo("title", searchTerm);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        noteAdapter = new NoteAdapter(options,this);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(noteAdapter);

    }

}