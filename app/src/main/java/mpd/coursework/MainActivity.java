package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewClass.ItemClickListener {

    private Button plannedRoadworksButton;
    private Button currentRoadworksButton;
    private Button currentIncidentsButton;
    private RecyclerView recyclerView;
    private ProgressBar progBar;
    private RecyclerViewClass recyclerViewClass;
    private RSSModelClass _rssModelClass;
    private Boolean buttonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerViewClass = new RecyclerViewClass(this, new ArrayList<RSSModelClass>());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        this.recyclerView.setAdapter(recyclerViewClass);
        this.recyclerViewClass.setClickListener(this);

        plannedRoadworksButton = (Button) findViewById(R.id.plannedRoadworksButton);
        plannedRoadworksButton.setOnClickListener(this);

        currentRoadworksButton = (Button) findViewById(R.id.currentRoadworksButton);
        currentRoadworksButton.setOnClickListener(this);

        currentIncidentsButton = (Button) findViewById(R.id.currentIncidentsButton);
        currentIncidentsButton.setOnClickListener(this);

        progBar = findViewById(R.id.progress_bar);

        if (savedInstanceState != null) {
            Parcelable mListState = savedInstanceState.getParcelable("rssModelClass_state");
            ArrayList<RSSModelClass> rssModelClass = savedInstanceState.getParcelableArrayList("rssModelClass");
            }
        }

    public void onClick(View aview) {
        switch (aview.getId()) {

            case R.id.plannedRoadworksButton:
                new AsyncTaskClass(recyclerViewClass, progBar ,BuildConfig.PLANNED_ROADWORKS).execute();
                buttonPressed = true;
                break;

            case R.id.currentRoadworksButton:
                new AsyncTaskClass(recyclerViewClass, progBar , BuildConfig.CURRENT_ROADWORKS).execute();
                buttonPressed = true;
                break;

            case R.id.currentIncidentsButton:
                new AsyncTaskClass(recyclerViewClass, progBar , BuildConfig.CURRENT_INCIDENTS).execute();
                buttonPressed = true;
                break;

            default:
                new AsyncTaskClass(recyclerViewClass, progBar ,  BuildConfig.PLANNED_ROADWORKS).execute();
                break;


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(buttonPressed == true) {
                    recyclerViewClass.getFilter().filter(newText);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        Double[] coordinates = _rssModelClass.getLatLng();
        Double lat = coordinates[0];
        Double lng = coordinates[1];
        Intent mpd = new Intent(getApplicationContext(), MapActivity.class);
        mpd.putExtra("lat", lat);
        mpd.putExtra("geolong", lng);
        mpd.putExtra("rssModelClass", recyclerViewClass.getItem((position)));
        startActivity(mpd);
    }

}