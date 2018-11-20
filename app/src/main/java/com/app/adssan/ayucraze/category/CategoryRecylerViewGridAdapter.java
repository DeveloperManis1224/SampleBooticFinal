package com.app.adssan.ayucraze.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.prductGrid.ProductGridActivity;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.model.ProductCategory;

import java.util.ArrayList;
import java.util.Map;

public class CategoryRecylerViewGridAdapter extends RecyclerView.Adapter<CategoryRecylerViewGridAdapter.ViewHolder>{

    private ArrayList<ProductCategory> categoryList;
    private Context context;

    public CategoryRecylerViewGridAdapter(ArrayList<ProductCategory> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_grid_category, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCategory productCategory = categoryList.get(position);
//        if(CategoryActivity.getTypeCategeory.equals("filter"))
//        {
//            if(!productCategory.getSubtitle().equals("Dal"))
//            {
//               String categoryImageUrl = Constants.CATEGORY_IMAGE_URL + productCategory.getImage_name();
//              Picasso.get().load(categoryImageUrl).into(holder.ivCategoryImage);
//              holder.tvCategoryName.setText(productCategory.getSubtitle());
//            }
//            else
//            {
//                holder.lytGrid.setVisibility(View.GONE);
//            }
//        }
//        else
//        {
            String categoryImageUrl = Constants.CATEGORY_IMAGE_URL + productCategory.getImage_name();
            Picasso.get().load(categoryImageUrl).into(holder.ivCategoryImage);
            holder.tvCategoryName.setText(productCategory.getSubtitle());
       // }


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        ImageView ivCategoryImage;
        TextView tvCategoryName;
        LinearLayout lytGrid;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            lytGrid= itemView.findViewById(R.id.lyt_grid);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ProductCategory productCategory = categoryList.get(position);
            Intent intent = new Intent(context, ProductGridActivity.class);
            intent.putExtra(Constants.INTENT_CATEGORY_ID, productCategory.getId());
            context.startActivity(intent);
        }
    }
}
