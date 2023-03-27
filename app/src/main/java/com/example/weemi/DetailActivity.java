package com.example.weemi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class DetailActivity extends AppCompatActivity {

    EditText titleEditText,ContentImage,Commentaire;
    ImageButton saveArticleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleEditText = findViewById(R.id.articleTitle);
        ContentImage = findViewById(R.id.articleImage);
        Commentaire = findViewById(R.id.articleCom);
        saveArticleBtn = findViewById(R.id.saveBtn);

        saveArticleBtn.setOnClickListener((v ->saveNote() ));


    }

    void saveNote(){
        String titreArticle = titleEditText.getText().toString();
        String imageContent = ContentImage.getText().toString();
        String com = Commentaire.getText().toString();
        if (titreArticle == null|| titreArticle.isEmpty()){
            titleEditText.setError("Titre manquant");
            return;
        }

        Article article = new Article();
        article.setTitre(titreArticle);
        article.setImageContent(imageContent);
        article.setAnnonce(com);
        article.setTimestamp(Timestamp.now());

        saveArticleToFirebase(article);

    }

    void saveArticleToFirebase(Article article){
        DocumentReference documentReference;
        documentReference = SaveToFirebase.getCollectionReferenceForArticle().document();

        documentReference.set(article).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                        // article ajouté
                        SaveToFirebase.showToast(DetailActivity.this,"Article ajouté");
                        finish();

                }else {

                    SaveToFirebase.showToast(DetailActivity.this,"Echec ajout de l'article");

                }
            }
        });
    }


}