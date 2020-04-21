package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewClass extends RecyclerView.Adapter<RecyclerViewClass.ViewHolder> implements Filterable {

    private List<RSSModelClass> trafficData;
    private List<RSSModelClass> trafficDataFull;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;

    public RecyclerViewClass(Context context, List<RSSModelClass> data) {
        mInflater = LayoutInflater.from(context);
        this.trafficData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_class_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final RSSModelClass rssModelClass = trafficData.get(position);
        holder.title.setText(rssModelClass.getTitle());
        holder.description.setText(rssModelClass.getDescription());
        holder.startDate.setText(rssModelClass.getStartDate());
        holder.endDate.setText(rssModelClass.getEndDate());
        holder.geoPoint.setText(rssModelClass.getGeoPoint());
        holder.title.setTextColor(Color.parseColor(rssModelClass.calcColour(rssModelClass.getDuration())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String title = rssModelClass.getTitle();
                String desc = rssModelClass.getDescription();
                Double[] coordinates = rssModelClass.getLatLng();
                Double lat = coordinates[0];
                Double lng = coordinates[1];
                Intent mpd = new Intent(context.getApplicationContext(), MapActivity.class);
                mpd.putExtra("title", title);
                mpd.putExtra("desc", desc);
                mpd.putExtra("lat", lat);
                mpd.putExtra("geolong", lng);
                mpd.putExtra("rssModelClass", getItem((position)));
                context.startActivity(mpd);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trafficData.size();
    }

    void setRecyclerViewData(List<RSSModelClass> trafficFeedModels) {
        this.trafficData = trafficFeedModels;
        trafficDataFull = new ArrayList<>(trafficData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, description, geoPoint, startDate, endDate;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            geoPoint = itemView.findViewById(R.id.geoPoint);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }


    }

    String getItem(int id) {
        return trafficData.get(id).getTitle();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public Filter getFilter(){
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RSSModelClass> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0 )
            {
                filteredList.addAll(trafficDataFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (RSSModelClass item : trafficDataFull){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    if (item.getStartDate().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    if (item.getEndDate().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    if (item.getDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            trafficData.clear();

            trafficData.addAll((List)results.values);

            notifyDataSetChanged();
        }
    };
}
