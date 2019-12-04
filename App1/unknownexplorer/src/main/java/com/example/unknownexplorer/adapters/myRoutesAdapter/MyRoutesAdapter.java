package com.example.unknownexplorer.adapters.myRoutesAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.models.Route;
import com.example.unknownexplorer.ui.myRoutes.myRoutesFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyRoutesAdapter extends RecyclerView.Adapter<MyRoutesAdapter.MyRoutesViewHolder> {

    private static List<Route> routeList = new ArrayList<>();
    private static OnMyRoutesClickListener onMyRoutesClickListener;
    private myRoutesFragment context;


    public interface OnMyRoutesClickListener {
        void onRouteClick(Route route);
        void onEditClick(Route route);
        void onDeleteClick(Route route);
    }


    public MyRoutesAdapter(OnMyRoutesClickListener _onMyROutesClickListener, myRoutesFragment _contex) {
        this.onMyRoutesClickListener = _onMyROutesClickListener;
        this.context = _contex;
    }

    @NonNull
    @Override
    public MyRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("test", "onCreateViewHolder from RoutersAdapter");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_my_routes, parent, false);
        return new MyRoutesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyRoutesViewHolder holder, int position) {
        Log.d("test", "onBindViewHolder from RoutersAdapter");
        holder.bind(routeList.get(position));
        holder.descriptionTextView.setTag(position);
    }

    @Override
    public int getItemCount() {
        Log.d("test", "getItemCount() from RoutersAdapter");
        return routeList.size();
    }

    public void setItems(Collection<Route> routes) {
        clearItems();
        Log.d("test", "setItems from RoutersAdapter");
        routeList.addAll(routes);
        notifyDataSetChanged();
    }

    public void clearItems() {
        routeList.clear();
        notifyDataSetChanged();
    }

    static class MyRoutesViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView interestTextView;
        private TextView typeTextView;
        private TextView timeTextView;
        private TextView ratingTextView;
        private Button editButton;
        private Button deleteButton;


        public MyRoutesViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title_router_my_routes);
            descriptionTextView = itemView.findViewById(R.id.text_description_router_my_routes);
            interestTextView = itemView.findViewById(R.id.text_interest_router_my_routes);
            typeTextView = itemView.findViewById(R.id.text_type_router_my_routes);
            timeTextView = itemView.findViewById(R.id.text_time_router_my_routes);
            ratingTextView = itemView.findViewById(R.id.text_rating_router_my_routes);


            //по клику на элемент отправляем в myRoutesFragment наш Route
            itemView.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route route = routeList.get(getLayoutPosition());
//                    onMyRoutesClickListener.onRouteClick(route);
                    onMyRoutesClickListener.onDeleteClick(route);
                }
            });

            itemView.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Route route = routeList.get(getLayoutPosition());
                    onMyRoutesClickListener.onEditClick(route);
                }
            });
        }

        public void bind(Route route) {
            Log.d("test", "bind from RoutersAdapter");
            titleTextView.setText(route.getTitle());
            descriptionTextView.setText(route.getDescription());
            interestTextView.setText(route.getInterest());
            typeTextView.setText(route.getType());
            timeTextView.setText(route.getTime());
            ratingTextView.setText(route.getRating());
            Log.d("test", "bind!: " + route.getPic());
        }
    }
}
