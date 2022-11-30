package com.example.proyecto4to;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Modelo> userList;

    public Adapter(List<Modelo>userList) {
        this.userList=userList;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        int imagen = userList.get(position).getImageView();
        String nombre=userList.get(position).getTextview();

        holder.setData(imagen,nombre);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //here use xml ids
            //give different name not like constructor
            imageView=itemView.findViewById(R.id.imageview);
            textView=itemView.findViewById(R.id.textview);

        }

        public void setData(int resource, String name) {

            imageView.setImageResource(resource);
            textView.setText(name);


        }
    }
}
