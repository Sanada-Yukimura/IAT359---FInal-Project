package com.example.interactive_newspaper_SQL;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.interactive_newspaper.R;

import java.util.ArrayList;

public class SQLDatabaseAdapter extends RecyclerView.Adapter<SQLDatabaseAdapter.MyViewHolder> {
    public ArrayList<String> mySQLDatabase = new ArrayList<>();
    Context context;


    public SQLDatabaseAdapter(ArrayList<String> myDatabase, Context context){
        mySQLDatabase.addAll(myDatabase);
        this.context = context;

    }


    public SQLDatabaseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SQLDatabaseAdapter.MyViewHolder holder, int i) {
        TextView tv = holder.myTextView;
        tv.setText(mySQLDatabase.get(i).toString());
        tv.setTextColor(ContextCompat.getColor(context, R.color.darkSecondaryFont));

    }

    @Override
    public int getItemCount() {
        return mySQLDatabase.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public TextView myTextView;

        Context context;
        public MyViewHolder(View itemView) {

            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.titleText);
            itemView.setOnLongClickListener(this);

            context = itemView.getContext();

        }

        @Override
        public boolean onLongClick(View view) {

            int position = getAdapterPosition();
            int id = view.getId();
            String title = mySQLDatabase.get(position);

            Toast.makeText(context, "Bookmark has been deleted", Toast.LENGTH_SHORT).show();
            SQLMyDatabase thisDatabase = new SQLMyDatabase(context);
            thisDatabase.deleteData(title);
            mySQLDatabase.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mySQLDatabase.size());

            return true;
        }
    }
}
