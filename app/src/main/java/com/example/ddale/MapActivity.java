package com.example.ddale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ddale.map.DdaleInfoWindow;
import com.example.ddale.modele.Oeuvre;

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

public class MapActivity extends AppCompatActivity {
    MapView map = null;
    private MyLocationNewOverlay myLocationNewOverlay;
    private Switch switchMyLocation;
    private ArrayList<Oeuvre> oeuvres;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Oeuvre oeuvre = new Oeuvre(1, 3.14749, 50.61014, 0, "Le Radeau de la Méduse", "Théodore Géricault", 1818, 491, 716, "Peinture à l'huile", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/JEAN_LOUIS_TH%C3%89ODORE_G%C3%89RICAULT_-_La_Balsa_de_la_Medusa_%28Museo_del_Louvre%2C_1818-19%29.jpg/320px-JEAN_LOUIS_TH%C3%89ODORE_G%C3%89RICAULT_-_La_Balsa_de_la_Medusa_%28Museo_del_Louvre%2C_1818-19%29.jpg", "");
        oeuvres = new ArrayList<>(1);
        oeuvres.add(0,oeuvre);
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_map);
        switchMyLocation = findViewById(R.id.switchMyLocation);

        setUpMap();
        setUpOeuvreMarkers(oeuvres);


    }

    private void setUpMap() {
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        final List<Overlay> overlays = map.getOverlays();
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        overlays.add(mScaleBarOverlay);
        IMapController mapController = map.getController();
        mapController.setZoom(20);
        ((IMapController) mapController).setCenter(new GeoPoint(oeuvres.get(0).getLocalisation().getLatitude(), oeuvres.get(0).getLocalisation().getLongitude() - 0.0001));
        setUpMyLocationOverlay();
    }

    private void setUpOeuvreMarkers(ArrayList<Oeuvre> oeuvres) {
        ArrayList<Marker> markers = new ArrayList<>(oeuvres.size());
        for (Oeuvre oeuvre : oeuvres) {
            Marker marker = new Marker(map);
            marker.setRelatedObject(oeuvre);
            marker.setPosition(oeuvre.getLocalisation());
            marker.setInfoWindow(new DdaleInfoWindow(map, oeuvre));
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

}



