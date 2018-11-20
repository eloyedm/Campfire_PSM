package com.example.android.campfire;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private LocationManager locationManager;
    Geocoder geocoder;
    List<LatLng> markerPositions;
    private FloatingActionButton createPlace;
    private FusedLocationProviderClient mFusedLocationClient;
    private final static int REQUEST_NEW_PLACE = 0;
    View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        createPlace = findViewById(R.id.addPlaceBtn);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewPLace.class);
                startActivityForResult(intent, REQUEST_NEW_PLACE);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Instaciammos nuestor objeto mapa
        map = googleMap;

        // Inicializa nuestro objeto LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Listener para detectar los eventos "Click" dentro del mapa
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            // Este evento nos devuelve la cooordenada geografica donde se dio click dentro del mapa
            @Override
            public void onMapClick(LatLng latLng) {

                // Funcion extra que desarrollamos para agregar marcadores al mapa
                // ..
                addMarker("Primer lugar", latLng, true, false);
            }
        });

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 100);
        }

        // Si estamos en Android 6.0+ tenemos que pedir permisos en tiempo de ejecucion
        // Si estamos debajo de Android 6.0 solo hace falta pedir permisos desde el AndroidManifest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkPermissions();
        else
            moveMapCameraToUserLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Se requiere aceptar el permiso", Toast.LENGTH_SHORT).show();
                checkPermissions();
            } else {
                Toast.makeText(this, "Permisio concedido", Toast.LENGTH_SHORT).show();
                moveMapCameraToUserLocation();
            }
        }
    }

    private void checkPermissions() {
        // Apartir de Android 6.0+ necesitamos pedir el permiso de ubicacion
        // directamente en tiempo de ejecucion de la app
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso para la ubicacion
            // Solicitamos permiso
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            // Ya se han concedido los permisos anteriormente
            moveMapCameraToUserLocation();
        }
    }

    private void moveMapCameraToUserLocation() {
        LatLng currentLocation = null;
        // Continuamos obteniendo la ubicacion del usuario para despues mostrar esa ubicacion en el mapa por default
        // pero cuando no se encuentre la ubicacion entonces pondremos una ubicacion fija.
        // ..
        try {
            // Muestra el boton de "Mi Ubicacion" en el mapa (El tipico circulo azul de google)
            // ..
            map.setMyLocationEnabled(true);
            // Utilizamos el metodo que desarrollamos para obtener la ubicacion del usuario
            // ..
            currentLocation = getCurrentLocation();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // Si se pudo obtener la ubicacion del usuario
        if (currentLocation != null) { // .. cambiar if

            // Movemos la camara para que apunte a otra coordenada diferente e la default
            // ..
            CameraUpdateFactory.newLatLngZoom(currentLocation, 16.f);
        } else { // Si no se pudo obtener la ubicacion

            // Ponemos una ubicacion fija
            // ..
            // ..
            LatLng mtyLocation = new LatLng(25.65, -100.29);
            CameraUpdateFactory.newLatLngZoom(mtyLocation, 16.f);
        }
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (map != null) {
            map.setMyLocationEnabled(true);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_PLACE) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    private LatLng getCurrentLocation() throws SecurityException {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers){
            Location loc = locationManager.getLastKnownLocation(provider);
            if (loc == null){
                continue;

            }
            if (bestLocation == null || loc.getAccuracy() > bestLocation.getAccuracy()){
                bestLocation = loc;
            }
        }
        if (bestLocation == null){
            showToast(" no se pudo obtener la ubicacion");
            return null;
        }
        return new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude()) ;
    }

    private void addMarker(String title, LatLng position, boolean clean, boolean polys) {
        if (clean) {
            map.clear();
        }

        // De esta manera se pueden agregar marcadores al mapa
        MarkerOptions opts = new MarkerOptions();
        opts.position(position);
        opts.title(title);

        // La clase GoogleMap tiene el metodo addMarker
        map.addMarker(opts);

        if (!polys)
            return;

        if (markerPositions == null)
            markerPositions = new ArrayList<>();

        // EXTRA: Tambien se pueden poner lineas dentro del mapa
        PolylineOptions line = new PolylineOptions();
        line.width(8);
        line.color(Color.BLUE);

        if (markerPositions.size() > 0) {
            LatLng latLng = markerPositions.get(markerPositions.size() - 1);
            line.add(latLng);
        }
        line.add(position);
        markerPositions.add(position);

        // Muestra una linea en el mapa
        map.addPolyline(line);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
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
            case R.id.listing:
                Intent listIntent = new Intent(getApplicationContext(), SitesList.class);
                startActivity(listIntent);
                break;
            case R.id.mysites:
                showToast("mis sitios");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
