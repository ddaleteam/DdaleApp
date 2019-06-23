package com.example.ddale;

import android.graphics.Bitmap;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nomParcours;
        CardView parentLayout;
        ImageView imageView;
        OnParcoursListener onParcoursListener;

        public ViewHolder(@NonNull View itemView, OnParcoursListener onParcoursListener) {
            super(itemView);
            nomParcours=itemView.findViewById(R.id.parcours);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            imageView=itemView.findViewById(R.id.image_oeuvre);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnParcoursListener.onParcoursClick(getAdapterPosition());
        }

        public void bind(Parcours parcoursAffiche) {
            nomParcours.setText(parcoursAffiche.getNom());
            Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/JEAN_LOUIS_TH%C3%89ODORE_G%C3%89RICAULT_-_La_Balsa_de_la_Medusa_%28Museo_del_Louvre%2C_1818-19%29.jpg/320px-JEAN_LOUIS_TH%C3%89ODORE_G%C3%89RICAULT_-_La_Balsa_de_la_Medusa_%28Museo_del_Louvre%2C_1818-19%29.jpg").into(imageView);

        }
    }

    public interface OnParcoursListener{
        void onParcoursClick(int position);
    }
}
