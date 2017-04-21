package asim.net.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asimaltwijry on 4/19/17.
 */

public final class API {

    private API() {
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractArticleFromJson(String ArticleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(ArticleJSON)) {
            return null;
        }

        Log.i("API", "extractArticleFromJson: " + ArticleJSON);
        // Create an empty ArrayList that we can start adding Articles to
        List<Article> Articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(ArticleJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of results (or Articles).
            JSONObject baseJsonObject = baseJsonResponse.getJSONObject("response");

            JSONArray ArticlesArray = baseJsonObject.getJSONArray("results");

            // For each Article in the ArticlesArray, create an {@link Article} object
            for (int i = 0; i < ArticlesArray.length(); i++) {

                // Get a single Article at position i within the list of Articles
                JSONObject currentArticle = ArticlesArray.getJSONObject(i);


                // Extract the value for the key called "title"
                String title = currentArticle.getString("webTitle");

                String sectionName = currentArticle.getString("sectionName");
                String webUrl = currentArticle.getString("webUrl");

                String author = "UNKNOWN";

                if (!currentArticle.isNull("tags")) {
                    JSONArray tags = currentArticle.getJSONArray("tags");
                    JSONObject tagObject = tags.getJSONObject(0);
                    author = tagObject.getString("webTitle");
                }


                // Create a new {@link Article} object with the title, author.
                // from the JSON response.
                Article Article = new Article(title, sectionName, author,webUrl);

                // Add the new {@link Article} to the list of Articles.
                Articles.add(Article);
            }

        } catch (JSONException e) {

            Log.e("API", "Problem parsing the Article JSON results", e);
        }

        // Return the list of Articles
        return Articles;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        Log.i("API", "makeHttpRequest: " + url.getPath());
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("API", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("API", "Problem retrieving the Article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("API", "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Query the Article dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("API", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}s
        List<Article> Articles = extractArticleFromJson(jsonResponse);

        Log.i("API", "fetchArticleData: "+ Articles.toString());
        // Return the list of {@link Article}s
        return Articles;
    }

}
