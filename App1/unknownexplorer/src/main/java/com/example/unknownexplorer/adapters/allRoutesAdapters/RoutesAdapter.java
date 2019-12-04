package com.example.unknownexplorer.adapters.allRoutesAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RoutesViewHolder> {



    @NonNull
    @Override
    public RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("test","onCreateViewHolder from RoutersAdapter");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_all_routes, parent, false);
        return new RoutesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RoutesViewHolder holder, int position) {
        Log.d("test","onBindViewHolder from RoutersAdapter");
        holder.bind(routeList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("test","getItemCount() from RoutersAdapter");
        return routeList.size();
    }


    private List<Route> routeList = new ArrayList<>();

    public void setItems(Collection<Route> routes) {
        clearItems();
        Log.d("test","setItems from RoutersAdapter");
        routeList.addAll(routes);
        notifyDataSetChanged();
    }

    public void clearItems() {
        routeList.clear();
        notifyDataSetChanged();
    }



    class RoutesViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView interestTextView;
        private TextView typeTextView;
        private TextView timeTextView;
        private TextView ratingTextView;
        private ImageView routerPick ;


        public RoutesViewHolder(View itemView){
            super(itemView);
            Log.d("test","RoutesViewHolder from RoutersAdapter");
            titleTextView = itemView.findViewById(R.id.text_title_router_all_routes);
            Log.d("test", "RoutesViewHolder: titleText "+titleTextView );
            descriptionTextView = itemView.findViewById(R.id.text_description_router_all_routes);
            interestTextView = itemView.findViewById(R.id.text_interest_router_my_routes);
            typeTextView = itemView.findViewById(R.id.text_type_router_all_routes);
            timeTextView = itemView.findViewById(R.id.text_time_router_all_routes);
            ratingTextView = itemView.findViewById(R.id.text_rating_router_all_routes);
//            routerPick = itemView.findViewById(R.id.image_pic_router_all_routes);
            Log.d("test", "RoutesViewHolder: descText "+descriptionTextView );
        }

        public void bind(Route route) {
            Log.d("test","bind from RoutersAdapter");

            titleTextView.setText(route.getTitle());
            descriptionTextView.setText(route.getDescription());
            interestTextView.setText(route.getInterest());
            typeTextView.setText(route.getType());
            timeTextView.setText(route.getTime());
            ratingTextView.setText(route.getRating());
            Log.d("test", "bind!: " + route.getPic());
//            routerPick.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
