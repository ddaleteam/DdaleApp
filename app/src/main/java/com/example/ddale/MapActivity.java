package com.example.ddale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.ddale.API.APIClient;
import com.example.ddale.API.APIInterface;
import com.example.ddale.map.DdaleInfoWindow;
import com.example.ddale.modele.Oeuvre;
import com.example.ddale.modele.Parcours;
import com.squareup.picasso.Picasso;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements DdaleInfoWindow.onARButtonClickListener {
    MapView map = null;
    private MyLocationNewOverlay myLocationNewOverlay;
    private Switch switchMyLocation;
    private List<Oeuvre> oeuvres;
    private Parcours parcours;
    private final String TAG = "PMR";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        switchMyLocation = findViewById(R.id.switchMyLocation);
        setUpMap();

        int parcoursId = getIntent().getIntExtra("parcoursId", -1);
        if (parcoursId < 0) {
            finish();
        }

        getOeuvres(parcoursId);

    }

    private void getOeuvres(int parcoursId) {

        Log.d(TAG, "ParcoursId: " + parcoursId);
        APIInterface api = APIClient.createService(APIInterface.class);
        Call<Parcours> parcoursCall = api.appelAPIParcoursID(parcoursId);
        parcoursCall.enqueue(new Callback<Parcours>() {
            @Override
            public void onResponse(Call<Parcours> call, Response<Parcours> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");
                    parcours = response.body();
                    oeuvres = parcours.getOeuvres();
                    //warmUpCache(oeuvres);
                    setUpOeuvreMarkers(oeuvres);
                    IMapController mapController = map.getController();
                    mapController.setZoom(20);
                    ((IMapController) mapController).setCenter(new GeoPoint(oeuvres.get(0).getLocalisation().getLatitude(), oeuvres.get(0).getLocalisation().getLongitude() - 0.0001));

                } else {
                    Log.d(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Parcours> call, Throwable t) {

            }
        });

    }

    private void warmUpCache(List<Oeuvre> oeuvres) {
        for (Oeuvre oeuvre : oeuvres) {
            Picasso.get().load(oeuvre.getUrlImageCible()).fetch();
        }
    }

    private void setUpMap() {
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        final List<Overlay> overlays = map.getOverlays();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        overlays.add(mScaleBarOverlay);
        setUpMyLocationOverlay();
    }

    private void setUpOeuvreMarkers(List<Oeuvre> oeuvres) {
        ArrayList<Marker> markers = new ArrayList<>(oeuvres.size());
        for (Oeuvre oeuvre : oeuvres) {
            Marker marker = new Marker(map);
            marker.setRelatedObject(oeuvre);
            marker.setPosition(oeuvre.getLocalisation());
            marker.setInfoWindow(new DdaleInfoWindow(map, oeuvre, this));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getDrawable(R.drawable.ic_place_32dp));
            marker.setTitle(oeuvre.getTitre());
            marker.setSnippet(oeuvre.getAuteur());
            markers.add(marker);
        }
        Log.i("PMR", "oeuvres lenght : " + oeuvres.size() + " /markers : " + markers.size());
        map.getOverlays().addAll(markers);
    }

    private void setUpMyLocationOverlay() {
        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationNewOverlay.setPersonIcon(getBitmap(R.drawable.ic_person_pin_32dp));
        myLocationNewOverlay.setPersonHotspot(40, 40);
        myLocationNewOverlay.disableMyLocation();
        map.getOverlays().add(myLocationNewOverlay);
        switchMyLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myLocationNewOverlay.enableMyLocation();
                } else {
                    myLocationNewOverlay.disableMyLocation();
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void goToARActivity(Oeuvre oeuvre) {
        Intent arIntent = new Intent(MapActivity.this, ARActivity.class);
        arIntent.putExtra("idOeuvre", oeuvre.getId());
        startActivity(arIntent);
    }

    @Override
    public void onARButtonClick(int oeuvreId) {
        Intent arIntent = new Intent(MapActivity.this, ARActivity.class);
        arIntent.putExtra("idOeuvre", oeuvreId);
        startActivity(arIntent);
    }
}



