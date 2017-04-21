package asim.net.newsapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity  implements LoaderCallbacks<List<Article>> {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.loading_indicator)
    View loadingIndicator;

    @BindView(R.id.noArticleFound)
    TextView noArticleFoundView;

    @BindView(R.id.noArticleFoundFrame)
    FrameLayout noArticleFoundFrame;


    listAdapter adapter;

    public ArrayList<Article> articles = new ArrayList<Article>();

    static final String API_URL = "http://content.guardianapis.com/search?q=";


    static final String TAG = "MainActivity";

    private void connectToAPI(){
        noArticleFoundFrame.setVisibility(View.GONE);



        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.

            loadingIndicator.setVisibility(View.VISIBLE);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            //  loaderManager.initLoader(1, null, this);

                getLoaderManager().initLoader(1,null,this);



        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            noArticleFoundFrame.setVisibility(View.VISIBLE);
            // Update empty state with no connection error message
            noArticleFoundView.setText(R.string.no_internet);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

         adapter = new listAdapter(articles, this);
        recyclerView.setAdapter(adapter);


        connectToAPI();



    }



    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        Log.i("API", "onCreateLoader: "+ API_URL);


        DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");//foramt date
        String date =df1.format(Calendar.getInstance().getTime());

        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("from-date", date);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");

        Log.i(TAG, "onCreateLoader: " +  uriBuilder.toString());
        return new ArticleLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> Articles) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        noArticleFoundView.setText(R.string.no_article_found);


        Log.i(this.getClass().getName(), "onLoadFinished: "+Articles.toString());

        this.articles = (ArrayList<Article>) Articles;

        for (int i = 0 ; i< articles.size() ; i++){
            Log.i(TAG, "onLoadFinished: "+articles.get(i).toString());
        }
        if (articles != null && !articles.isEmpty()) {
            //tell the adapter that we have news!.
            adapter.reloadList(this.articles);
            adapter.notifyItemInserted(articles.size() - 1);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        recyclerView.removeAllViewsInLayout();
        adapter.notifyDataSetChanged();
    }
}
