package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncTaskClass extends AsyncTask<Void, Integer, List<RSSModelClass>> {
    private RecyclerViewClass recyclerViewClass;
    private String link;
    private ProgressBar progBar;

    public AsyncTaskClass(RecyclerViewClass recyclerViewClass, ProgressBar progBar, String link) {
        super();
        this.link = link;
        this.progBar = progBar;
        this.recyclerViewClass = recyclerViewClass;
    }

    @Override
    protected List<RSSModelClass> doInBackground(Void... args0) {
        List<RSSModelClass> rssModelClassList = new ArrayList<>();
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream stream = connection.getInputStream();

            RSSParserClass rssParserClass = new RSSParserClass();
            rssModelClassList = rssParserClass.parseXML(stream);
        }catch (IOException e) {
            Log.e("AsyncTask", "async task failed");
        }
        return rssModelClassList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<RSSModelClass> result) {
        super.onPostExecute(result);
        this.recyclerViewClass.setRecyclerViewData(result);
        this.recyclerViewClass.notifyDataSetChanged();
        progBar.setVisibility(View.GONE);
    }
}
