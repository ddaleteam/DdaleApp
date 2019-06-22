package com.example.ddale.map;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ddale.R;
import com.example.ddale.modele.Oeuvre;
import com.squareup.picasso.Picasso;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class DdaleInfoWindow extends MarkerInfoWindow {

    private final ImageView imageView;

    /**
     * @param mapView
     */
    public DdaleInfoWindow(MapView mapView, final Oeuvre oeuvre) {
        super(R.layout.bubble_ddale_layout, mapView);
        Button btn = (Button) (mView.findViewById(R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button clicked - oeuvre : " + oeuvre.getId(), Toast.LENGTH_LONG).show();
            }
        });
        imageView = new ImageView(mView.getContext());
        Picasso.get().load(oeuvre.getUrlImageCible()).into(imageView); //Téléchargement de l'image dans l'intermédiaire
        Drawable drawableImageOeuvre = imageView.getDrawable();//Transfert de l'image depuis l'intermédiaire vers le marker

    }


    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        Oeuvre oeuvre = (Oeuvre) mMarkerRef.getRelatedObject();
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
        if (mMarkerRef.getImage() == null) {
            mMarkerRef.setImage(imageView.getDrawable());
        }


    }


    @Override
    public void onClose() {
        super.onClose();
    }
}

