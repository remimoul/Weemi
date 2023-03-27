package com.example.weemi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ArticleAdapter extends FirestoreRecyclerAdapter<Article, ArticleAdapter.ArticleViewHolder> {
Context context;

    public ArticleAdapter(@NonNull FirestoreRecyclerOptions<Article> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ArticleViewHolder holder, int position, @NonNull Article article) {
        holder.titleArticle.setText(article.titre);
        holder.imageContent.setText(article.imageContent);
        holder.annonceTextview.setText(article.annonce);
        holder.timestampTextView.setText(SaveToFirebase.timestampToString(article.timestamp));
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_article_item,parent,false);
        return new ArticleViewHolder(view);
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView titleArticle,imageContent,annonceTextview,timestampTextView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleArticle = itemView.findViewById(R.id.titre_article_text_view);
            imageContent = itemView.findViewById(R.id.image_article_view);
            annonceTextview = itemView.findViewById(R.id.annonce_article_text_view);
            timestampTextView =itemView.findViewById(R.id.article_timestamp_text_view);

        }
    }
}
