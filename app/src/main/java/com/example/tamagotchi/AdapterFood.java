package com.example.tamagotchi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.CustomViewHolder>{
    private ArrayList<ClassFood> foodList;
    private Context foodContext;

    public class  CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView imgFood;
        protected TextView txtName;
        protected TextView txtPrice;
        protected TextView txtEffect;

        public CustomViewHolder(View view){
            super(view);
            this.imgFood = view.findViewById(R.id.item_food_img);
            this.txtName = view.findViewById(R.id.item_food_txt_name);
            this.txtPrice = view.findViewById(R.id.item_food_txt_price);
            this.txtEffect = view.findViewById(R.id.item_food_txt_effect);

        }
    }
    public AdapterFood(Context context, ArrayList<ClassFood> list){
        foodList = list;
        foodContext = context;
    }
    @NonNull
    @Override
    public AdapterFood.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_food,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        Glide.with(viewholder.itemView.getContext())
                .load(foodList.get(position).getFigure())
                .apply(new RequestOptions().dontTransform())
                .into(viewholder.imgFood);
        viewholder.txtName.setText(foodList.get(position).getName());
        viewholder.txtEffect.setText(foodList.get(position).getEffect());
        viewholder.txtPrice.setText(Integer.toString(foodList.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return (null != foodList ? foodList.size() : 0);
    }
}
