package com.example.ddale;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddale.modele.Parcours;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Parcours> mListeParcours;
    private OnParcoursListener mOnParcoursListener;

    public RecyclerViewAdapter(ArrayList<Parcours> mListeParcours, OnParcoursListener mOnParcoursListener) {
        this.mListeParcours = mListeParcours;
        this.mOnParcoursListener = mOnParcoursListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_liste_parcours, parent, false);
        return new ViewHolder(view,mOnParcoursListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Parcours parcoursAffiche = mListeParcours.get(position);
        holder.bind(parcoursAffiche);

    }

    @Override
    public int getItemCount() {
        return mListeParcours.size();
    }

    public void show(ArrayList<Parcours> listeParcours) {
        mListeParcours = listeParcours;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nomParcours;
        TextView duree;
        CardView parentLayout;
        ImageView imageView;
        OnParcoursListener mOnParcoursListener;

        public ViewHolder(@NonNull View itemView, OnParcoursListener onParcoursListener) {
            super(itemView);
            duree=itemView.findViewById(R.id.duree);
            nomParcours=itemView.findViewById(R.id.parcours);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            imageView=itemView.findViewById(R.id.image_oeuvre);

            this.mOnParcoursListener = onParcoursListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnParcoursListener.onParcoursClick(getAdapterPosition());
        }

        public void bind(Parcours parcoursAffiche) {
            nomParcours.setText(parcoursAffiche.getNom());
            String texteAffiche = "Durée : " + parcoursAffiche.getDuree() + " min";
            duree.setText(texteAffiche);
            if (!parcoursAffiche.getOeuvres().isEmpty()){
                Picasso.get().load("https://ddale.rezoleo.fr/" + parcoursAffiche.getOeuvres().get(0).getUrlImageCible()).into(imageView);
            }
        }
    }


    public interface OnParcoursListener{
        void onParcoursClick(int position);
    }
}
