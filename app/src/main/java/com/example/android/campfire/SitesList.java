package com.example.android.campfire;

import android.content.Intent;
import android.support.constraint.Placeholder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class SitesList extends AppCompatActivity {
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        initializeData();
        RVAdapter rvAdapter = new RVAdapter(places);
        rv.setAdapter(rvAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_listing, menu);
        Toolbar tb = findViewById(R.id.toolbar);
        tb.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return onOptionsItemSelected(menuItem);
                    }
                }
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapItem:
                Intent mapIntent = new Intent(getApplicationContext(), Map.class);
                startActivity(mapIntent);
                break;
            case R.id.mysites:
                showToast("mis sitios");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class Place{
        String name;
        LatLng position;
        String creator;

        public Place(String name,LatLng position, String creator) {
            this.name = name;
            this.position = position;
            this.creator = creator;
        }
    }

    private List<Place> places;

    private void initializeData(){
        places = new ArrayList<>();
        places.add(new Place("Lugar 1", new LatLng(25.00, -100.01), "Yo mero"));
        places.add(new Place("Lugar 2", new LatLng(25.00, -100.01), "Yo mero"));
        places.add(new Place("Lugar 3", new LatLng(25.00, -100.01), "Yo mero"));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
