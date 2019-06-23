package com.example.ddale;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddale.modele.Parcours;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Parcours> mListeParcours;
    private OnParcoursListener mOnParcoursListener;

    /**
     * Le constructeur de l'adapter
     * @param mListeParcours
     * @param mOnParcoursListener
     */
    public RecyclerViewAdapter(ArrayList<Parcours> mListeParcours, OnParcoursListener mOnParcoursListener) {
        this.mListeParcours = mListeParcours;
        this.mOnParcoursListener = mOnParcoursListener;
    }

    /**
     * @param parent
     * @param viewType
     * @return le viewholder qui va afficher les cardviews des parcours
     */
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_liste_parcours, parent, false);
        return new ViewHolder(view,mOnParcoursListener);
    }

    /**
     * @param holder le holder utilisé
     * @param position l'index de la liste de l'élément à afficher
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Parcours parcoursAffiche = mListeParcours.get(position);
        holder.bind(parcoursAffiche);

    }

    /**
     * @return la taille de la liste à afficher
     */
    @Override
    public int getItemCount() {
        return mListeParcours.size();
    }

    /**
     * @param listeParcours
     */
    public void show(ArrayList<Parcours> listeParcours) {
        mListeParcours = listeParcours;
        notifyDataSetChanged(); // actualise le recyclerview
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nomParcours;
        TextView duree;
        CardView parentLayout;
        ImageView imageView;
        OnParcoursListener mOnParcoursListener;

        /**
         * @param itemView la cardview à afficher
         * @param onParcoursListener le listener à affecter à la cardview
         */
        public ViewHolder(@NonNull View itemView, OnParcoursListener onParcoursListener) {
            super(itemView);
            duree=itemView.findViewById(R.id.duree);
            nomParcours=itemView.findViewById(R.id.parcours);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            imageView=itemView.findViewById(R.id.image_oeuvre);

            this.mOnParcoursListener = onParcoursListener;
            itemView.setOnClickListener(this);
        }

        /**
         * @param view la cardview
         */
        @Override
        public void onClick(View view) {
            mOnParcoursListener.onParcoursClick(getAdapterPosition()); // permet de faire appel à l'interface onParcoursClick
        }

        /**
         * bind() permet de relier le contenu à la vue
         * @param parcoursAffiche le parcours à afficher
         */
        public void bind(Parcours parcoursAffiche) {
            nomParcours.setText(parcoursAffiche.getNom());
            String texteAffiche = "Durée : " + parcoursAffiche.getDuree() + " min";
            duree.setText(texteAffiche);
            // On vérifie que le parcours contient bien des oeuvres.
            if (!parcoursAffiche.getOeuvres().isEmpty()){
                // Picasso est la dépendance qui permet de charger l'image.
                Picasso.get().load("https://ddale.rezoleo.fr/" + parcoursAffiche.getOeuvres().get(0).getUrlImageCible()).into(imageView);
            }
        }
    }


    public interface OnParcoursListener{
        void onParcoursClick(int position);
    }
}
