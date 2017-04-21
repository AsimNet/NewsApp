package asim.net.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asimaltwijry on 4/21/17.
 */

public class listAdapter extends RecyclerView.Adapter<listAdapter.ArticleViewHolder> {

    private List<Article> articles;
    Context context;

    public listAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    public void reloadList(ArrayList<Article> mArticls) {
        this.articles.addAll(mArticls);
    }
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article currentArticle = this.articles.get(position);
        holder.currentArticle = currentArticle;
        holder.context = context;
        holder.title.setText(currentArticle.getTitle());
        holder.body.setText(currentArticle.getSection());
        holder.author.setText(currentArticle.getAuthor());

    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }


    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView)
        CardView cv;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.section)
        TextView body;
        @BindView(R.id.author)
        TextView author;

        Article currentArticle;

        Context context;

        ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri earthquakeUri = Uri.parse(currentArticle.getUrl());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // Send the intent to launch a new activity
                    context.startActivity(websiteIntent);
                }
            });
        }
    }

}
