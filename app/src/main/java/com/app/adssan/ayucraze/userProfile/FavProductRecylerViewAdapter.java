package com.app.adssan.ayucraze.userProfile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.Util.CustomSharedPrefs;
import com.app.adssan.ayucraze.Util.UtilityClass;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.Product;
import com.app.adssan.ayucraze.productDetail.ProductDetailActivity;
import com.app.adssan.ayucraze.userLogin.LoginAttemptActivity;

import java.util.ArrayList;
import java.util.Map;

public class FavProductRecylerViewAdapter extends RecyclerView.Adapter<FavProductRecylerViewAdapter.ViewHolder> {

    private ArrayList<Product> productList;
    private Context context;
    private Product product;
    private DataSource dataSource;

    public FavProductRecylerViewAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
        dataSource = new DataSource(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_product, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       product = productList.get(position);


        String[] favProductIds = CustomSharedPrefs.getFavProductsInPref(context, dataSource);
        for (int i = 0; i < favProductIds.length; i++) {
            if(favProductIds[i].equals(product.getId())){
                holder.btnFavourite.setLiked(true);
            }
        }

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + product.getImage_name();
        Picasso.get().load(imageUrl).into(holder.ivProductImage);

        holder.tvProductHeading.setText(product.getTitle());
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
    implements View.OnClickListener, OnLikeListener{

        public ImageView ivProductImage;
        public TextView tvProductHeading;
        public TextView tvProductPrice;
        public TextView tvGridProductPrevPrice;

        public  LikeButton btnFavourite;

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
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getTitle());
            intent.putExtra("price", product.getPrice());
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


