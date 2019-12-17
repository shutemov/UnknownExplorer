package com.example.unknownexplorer.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.fragments.AllRoutesFragment;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllRoutesAdapter extends RecyclerView.Adapter<AllRoutesAdapter.RoutesViewHolder> {


    private OnAllRoutesClickListener onAllRoutesClickListener;

    public interface OnAllRoutesClickListener {
        void onRouteClick(Route route);
    }

    public AllRoutesAdapter(OnAllRoutesClickListener _onMyROutesClickListener, AllRoutesFragment _contex) {
        this.onAllRoutesClickListener = _onMyROutesClickListener;
    }


    @NonNull
    @Override
    public RoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_all_routes, parent, false);
        return new RoutesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RoutesViewHolder holder, int position) {
        holder.bind(routeList.get(position));
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }


    private List<Route> routeList = new ArrayList<>();

    public void setItems(Collection<Route> routes) {
        clearItems();
        routeList.addAll(routes);
        this.notifyDataSetChanged();
    }

    public void clearItems() {
        routeList.clear();
        this.notifyDataSetChanged();
    }


    class RoutesViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView interestTextView;
        private TextView typeTextView;
        private TextView timeTextView;
        private TextView ratingTextView;


        public RoutesViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.text_title_router_all_routes);
            descriptionTextView = itemView.findViewById(R.id.text_description_router_all_routes);
            interestTextView = itemView.findViewById(R.id.text_interest_router_my_routes);
            typeTextView = itemView.findViewById(R.id.text_type_router_all_routes);
            timeTextView = itemView.findViewById(R.id.text_time_router_all_routes);
            ratingTextView = itemView.findViewById(R.id.text_rating_router_all_routes);


            itemView.findViewById(R.id.info_about_route_element).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route route = routeList.get(getLayoutPosition());
                    onAllRoutesClickListener.onRouteClick(route);
                }
            });

        }

        public void bind(Route route) {
            titleTextView.setText(route.getTitle());
            descriptionTextView.setText(route.getDescription());
            interestTextView.setText(route.getInterest());
            typeTextView.setText(route.getType());
            timeTextView.setText(route.getTime());
            ratingTextView.setText(route.getRating());
        }
    }
}
