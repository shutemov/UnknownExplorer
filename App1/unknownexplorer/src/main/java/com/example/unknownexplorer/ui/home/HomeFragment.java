package com.example.unknownexplorer.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapter.Router;
import com.example.unknownexplorer.adapter.RoutesAdapter;

import java.util.Arrays;
import java.util.Collection;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RoutesAdapter routesAdapter;

    private void loadRouters() {
        Log.d("test","loadRouters  from home fragment");
        Collection<Router> routers = getRouters();
        routesAdapter.setItems(routers);
    }

    @org.jetbrains.annotations.NotNull
    private Collection<Router> getRouters() {
        Log.d("test","getRouters  from home fragment");
        return Arrays.asList(
                new Router("титле1", "описани1"),
                new Router("титле2", "описани2"),
                new Router("титле3", "описани3"),
                new Router("титле4", "описани4"),
                new Router("титле5", "описани5"),
                new Router("титле6", "описани6")
        );
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("test","onCreateView1 from home fragment");
        Log.d("test", "onCreateView: "+R.layout.fragment_home);

        View root = inflater.inflate(R.layout.fragment_home,container,false);
        Log.d("test","onCreateView2 from home fragment");
        Log.d("test", "VIEW ID?: " +root);
        recyclerView = root.findViewById(R.id.routes_recycler_view);


        Log.d("test", "onCreateView: " +recyclerView);
        Log.d("test","onCreateView3 from home fragment");


        View recycler_item = inflater.inflate(R.layout.recycler_item,null);
        recyclerView.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));
        routesAdapter = new RoutesAdapter();
        recyclerView.setAdapter(routesAdapter);


        Log.d("test","onCreateView4 from home fragment");

        loadRouters();

        return root;
    }

}