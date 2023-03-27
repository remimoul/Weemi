package com.example.weemi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class HistoryActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;

    ArticleAdapter articleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        addNoteBtn = findViewById(R.id.add_objet);
        recyclerView = findViewById(R.id.recycler_View);
        menuBtn= findViewById(R.id.menuBtn);
        addNoteBtn.setOnClickListener((v -> startActivity(new Intent(HistoryActivity.this,DetailActivity.class))));
        menuBtn.setOnClickListener((v) -> showMenu() );
        setupRecyclerView();
    }

    void showMenu(){
        // Article Menu

    }

    void setupRecyclerView(){
        Query query = SaveToFirebase.getCollectionReferenceForArticle().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Article> options = new FirestoreRecyclerOptions.Builder<Article>()
                .setQuery(query,Article.class).build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new ArticleAdapter(options,this);
        recyclerView.setAdapter(articleAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        articleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        articleAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        articleAdapter.notifyDataSetChanged();
    }
}