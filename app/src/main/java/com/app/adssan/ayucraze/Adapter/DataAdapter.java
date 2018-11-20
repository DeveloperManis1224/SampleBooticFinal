package com.app.adssan.ayucraze.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.app.adssan.ayucraze.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DataAdapter  extends RecyclerView.Adapter<DataAdapter.MyViewHolder>{
    public static ArrayList<DataParser> objs_arr=new ArrayList<>();
    public DataAdapter(ArrayList<DataParser> objs) {
        this.objs_arr = objs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View rental_view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cart_product,parent,false);
        rental_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int rent_position;
////                try {
////                    rent_position= Admin_HomePage.recycleView.getChildAdapterPosition(view);
////                }catch (Exception e)
////                {
////                     rent_position = HomePage.recycleView.getChildAdapterPosition(view);
////                }
////                Intent in = new Intent(view.getContext(), BusDetailsActivity.class);
////                in.putExtra("bus_id", objs_arr.get(rent_position).get_busId());
////                view.getContext().startActivity(in);
            }
        });
        return new MyViewHolder (rental_view);

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            holder.productTitle.setText(objs_arr.get(position).get_productTitle());
            holder.productAmount.setText(nf.format(Double.valueOf(objs_arr.get(position).get_orderAmount())));
            holder.orderMethod.setText(objs_arr.get(position).get_orderMehtod());


            Picasso.get().load(objs_arr.get(position).get_productPrice()).into(holder.productImg);
//            holder.BusName.setText(objs_arr.get(position).get_busName());
//            holder.BusNumber.setText(objs_arr.get(position).get_busNumber());
//            holder.BusRoute.setText(objs_arr.get(position).get_busRoute());
//            holder.BusTime.setText(objs_arr.get(position).get_busTime());
//            holder.BusDelay.setText("Delay "+objs_arr.get(position).get_busDelay());
//            if (objs_arr.get(position).get_busDelay().equalsIgnoreCase("") || objs_arr.get(position).get_busDelay().equalsIgnoreCase("null")) {
//                holder.BusDelay.setVisibility(View.GONE);
//            }else{
//                holder.BusDelay.setVisibility(View.VISIBLE);
//            }
//            Glide.with(holder.ImgBus.getContext()).load("http://buslocatorapp.rahmantraders.com/images/" + objs_arr.get(position).get_busImage()).into(holder.ImgBus);
        }catch (Exception e)
        {
            Log.e("ERROR",""+e);
        }
    }
    @Override
    public int getItemCount() {
        return objs_arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView productImg;
        TextView productTitle;
        TextView productAmount;
        TextView orderMethod;

        public MyViewHolder(View itemView) {
            super(itemView);

            productImg=itemView.findViewById(R.id.iv_cart_product_image);
            productTitle=itemView.findViewById(R.id.tv_cart_product_heading);
            productAmount= itemView.findViewById(R.id.tv_cart_product_price);
            orderMethod= itemView.findViewById(R.id.tv_cart_product_color);

        }
    }

}
