package com.example.ddale.map;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ddale.R;
import com.example.ddale.modele.Oeuvre;
import com.squareup.picasso.Picasso;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

/**
 * Classe définissant les infos bulles associées aux markers dans la Map Activity
 */
public class DdaleInfoWindow extends MarkerInfoWindow {

    private final ImageView imageView;

    /**
     * Interface permettant de gérer la redirection vers l'activité d'AR dans la MapActivity
     */
    public interface onARButtonClickListener {
        void onARButtonClick(int oeuvreId);
    }

    /**
     * Constructeur des infos bulles
     */
    public DdaleInfoWindow(MapView mapView, final Oeuvre oeuvre, final onARButtonClickListener onARButtonClickListener) {
        super(R.layout.bubble_ddale_layout, mapView);
        Button btn = (Button) (mView.findViewById(R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("PMR", "Intent vers AR activit avec OeuvreId = " + oeuvre.getId());
                onARButtonClickListener.onARButtonClick(oeuvre.getId());
            }
        });
        imageView = new ImageView(mView.getContext());
        Picasso.get().load("https://ddale.rezoleo.fr/" + oeuvre.getUrlImageCible()).into(imageView); //Téléchargement de l'image dans un intermédiaire, ça ne marche pas si on charge directement dans la zone prévu
    }

    /**
     * Est appellée à chaque nouveau dessin de l'info bulle
     *
     * @param item : corespond ici au marker
     */
    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
        Oeuvre oeuvre = (Oeuvre) mMarkerRef.getRelatedObject();
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
        if (mMarkerRef.getImage() == null) { // Tant qu'il n'y a pas d'image on essaye de la récupérer de l'intermédiare
            mMarkerRef.setImage(imageView.getDrawable());
        }
    }

}

