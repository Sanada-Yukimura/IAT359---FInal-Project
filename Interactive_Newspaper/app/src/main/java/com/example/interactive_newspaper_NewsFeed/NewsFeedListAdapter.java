package com.example.interactive_newspaper_NewsFeed;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.interactive_newspaper.LocalNews;
import com.example.interactive_newspaper.R;
import com.example.interactive_newspaper_SQL.SQLDatabaseHelper;
import com.example.interactive_newspaper.Sports;
import com.example.interactive_newspaper.WorldNews;
import com.example.interactive_newspaper_SQL.SQLMyDatabase;

import java.util.List;

public class NewsFeedListAdapter extends RecyclerView.Adapter<NewsFeedListAdapter.FeedModelViewHolder> implements SensorEventListener  {
    Context context;
    private List<NewsFeedModel> myNewsFeedModels;
    private Sports sports = new Sports();
    private WorldNews worldNews = new WorldNews();
    private LocalNews localNews = new LocalNews();

    public NewsFeedListAdapter(List<NewsFeedModel> newsFeedModels) {
        myNewsFeedModels = newsFeedModels;

    }





    @Override
    public NewsFeedListAdapter.FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position)

    {

        final NewsFeedModel newsFeedModel = myNewsFeedModels.get(position);
        ((TextView)holder.newsFeedView.findViewById(R.id.titleText)).setText(newsFeedModel.title);
        ((TextView)holder.newsFeedView.findViewById(R.id.titleText)).setTextColor(ContextCompat.getColor(context, R.color.darkSecondaryFont));
        ((TextView)holder.newsFeedView.findViewById(R.id.descriptionText)).setText(newsFeedModel.descrip);
        ((TextView)holder.newsFeedView.findViewById(R.id.descriptionText)).setTextColor(ContextCompat.getColor(context, R.color.darkSecondaryFont));
        ((TextView)holder.newsFeedView.findViewById(R.id.linkText)).setText(newsFeedModel.url);

    }

    @Override
    public int getItemCount() {
        return myNewsFeedModels.size();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class FeedModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private View newsFeedView;


        public FeedModelViewHolder(View v) {
            super(v);
            newsFeedView = v;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            context = v.getContext();
        }
        // Clicking on any recyclerView card will open up the browser for the full news article.
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            NewsFeedModel thisNews = myNewsFeedModels.get(position);
            String url = thisNews.url;
            String website = url.substring(url.indexOf("w"), url.length());
            website = "http://"+website;
            Uri webpage = Uri.parse(website);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

            context.startActivity(intent);
        }
        // on long click adds link to the sql database for bookmarks
        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            NewsFeedModel thisNews = myNewsFeedModels.get(position);
            String title = thisNews.title;
            String url = thisNews.url;
            SQLMyDatabase thisDatabase = new SQLMyDatabase(context);
            thisDatabase.addData(title);
            Toast.makeText(context, "Link has been bookmarked", Toast.LENGTH_SHORT).show();

            return true;
        }
    }

}
