package com.example.unknownexplorer.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder> {



    @NonNull
    @Override
    public RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("test","onCreateViewHolder from RoutersAdapter");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new RoutesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RoutesViewHolder holder, int position) {
        Log.d("test","onBindViewHolder from RoutersAdapter");
        holder.bind(routerList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("test","getItemCount() from RoutersAdapter");
        return routerList.size();
    }



    private List<Router> routerList = new ArrayList<>();

    public void setItems(Collection<Router> routers) {
//        clearItems();
        Log.d("test","setItems from RoutersAdapter");
        routerList.addAll(routers);
        notifyDataSetChanged();
    }

    public void clearItems() {
        routerList.clear();
        notifyDataSetChanged();
    }



    class RoutesViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;

        public RoutesViewHolder(View itemView){
            super(itemView);
            Log.d("test","RoutesViewHolder from RoutersAdapter");
            titleTextView = itemView.findViewById(R.id.text_title_router);
            Log.d("test", "RoutesViewHolder: titleText "+titleTextView );
            descriptionTextView = itemView.findViewById(R.id.text_desctiption_router);
            Log.d("test", "RoutesViewHolder: descText "+descriptionTextView );
        }

        public void bind(Router router) {
            Log.d("test","bind from RoutersAdapter");

            titleTextView.setText(router.getTitle());
            descriptionTextView.setText(router.getDescription());
        }
    }
}
