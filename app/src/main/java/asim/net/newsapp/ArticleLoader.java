package asim.net.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by asimaltwijry on 4/21/17.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private String mUrl;

    public ArticleLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    public List<Article> loadInBackground() {
        Log.i("API", "loadInBackground: "+mUrl);

        if (mUrl == null){
            return null;
        }

        Log.i("API", "loadInBackground: "+mUrl);
        List<Article> books = API.fetchArticleData(mUrl);
        return books;    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
