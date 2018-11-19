package com.w3engineers.ecommerce.bootic.prductGrid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.Constants;
import com.w3engineers.ecommerce.bootic.Util.CustomSharedPrefs;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.database.DataSource;
import com.w3engineers.ecommerce.bootic.main.MainActivity;
import com.w3engineers.ecommerce.bootic.model.Product;
import com.w3engineers.ecommerce.bootic.productDetail.ProductDetailActivity;
import com.w3engineers.ecommerce.bootic.userLogin.LoginAttemptActivity;

import java.util.ArrayList;

public class ProductRecylerViewAdapter extends RecyclerView.Adapter<ProductRecylerViewAdapter.ViewHolder> {

    private ArrayList<Product> productList;
    public Context context;


    private DataSource dataSource;

    public ProductRecylerViewAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        dataSource = new DataSource(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = null;
        try {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_product, parent, false);
        }catch(Exception e)
        {
            Log.e("LOOOOOOOO",""+e.getMessage());
            e.printStackTrace();
        }
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = productList.get(position);

        String pId = String.valueOf(product.getId());
        String[] favProductIds = CustomSharedPrefs.getFavProductsInPref(context, dataSource);
        for (int i = 0; i < favProductIds.length; i++) {
            if(favProductIds[i].equals(pId)){
                holder.btnFavourite.setLiked(true);
            }
        }

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + product.getImage_name();
        Picasso.get().load(imageUrl).into(holder.ivProductImage);

        holder.tvProductHeading.setText(product.getTitle());

        Log.e("PRICE",""+product.getPrice());
        holder.tvProductPrice.setText(UtilityClass.getNumberFormat(Double.valueOf(product.getPrice())));

        if (product.getPrevious_price() != 0) {
            holder.tvGridProductPrevPrice.setText(UtilityClass.getNumberFormat(product.getPrevious_price()));
        } else {
            holder.tvGridProductPrevPrice.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, OnLikeListener {

        public ImageView ivProductImage;
        public TextView tvProductHeading;
        public TextView tvProductPrice;
        public TextView tvGridProductPrevPrice;

        public LikeButton btnFavourite;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivProductImage = itemView.findViewById(R.id.iv_grid_product_image);
            tvProductHeading = itemView.findViewById(R.id.tv_grid_product_heading);
            tvProductPrice = itemView.findViewById(R.id.tv_grid_product_price);
            tvGridProductPrevPrice = itemView.findViewById(R.id.tv_grid_product_Previous_price);

            btnFavourite = itemView.findViewById(R.id.btn_favourite);

            btnFavourite.setOnLikeListener(this);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Product product = productList.get(position);
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra(Constants.PRODUCT_DETAIL_INTENT, UtilityClass.productToJson(product));

            context.startActivity(intent);

        }

        @Override
        public void liked(LikeButton likeButton) {

            if(CustomSharedPrefs.getLoggedInUser(context) != null) {

                int position = getAdapterPosition();
                Product currentProduct = productList.get(position);
                currentProduct.setUser_id(CustomSharedPrefs.getLoggedInUserId(context));
                dataSource.addFavProduct(currentProduct);
                CustomSharedPrefs.setFavProductsInPref(context, dataSource);

            }else{

                Intent intentLogin = new Intent(context, LoginAttemptActivity.class);
                intentLogin.putExtra(Constants.LOGIN_PREV_ACTIVITY, Constants.LOGIN_PREV_MAIN_ACTIVITY);
                context.startActivity(intentLogin);
                likeButton.setLiked(false);
                CustomSharedPrefs.setFavProductsInPref(context, dataSource);

            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            int position = getAdapterPosition();
            Product currentProduct = productList.get(position);
            dataSource.removeFavProductById(currentProduct.getId());
        }
    }

}


