package com.app.adssan.ayucraze.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.picasso.Picasso;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;
import com.app.adssan.ayucraze.database.DataSource;
import com.app.adssan.ayucraze.model.CartProduct;
import com.app.adssan.ayucraze.model.CustomProductInventory;
import com.app.adssan.ayucraze.model.SelectedProduct;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartListAdapter extends ArrayAdapter<SelectedProduct> {

    List<SelectedProduct> selectedProductList;
    Context context;
    CustomViewHolder customViewHolder;
    TextView tvTotalWriting;
    DataSource dataSource;
    ArrayList<CustomProductInventory> customProductInventories;

    int avilableQty;

    public CartListAdapter(@NonNull Context context, int resource, @NonNull List<SelectedProduct> objects,
                           TextView tvTotalWriting, ArrayList<CustomProductInventory> customProductInventories) {
        super(context, resource, objects);
        selectedProductList = objects;
        this.context = context;
        this.tvTotalWriting = tvTotalWriting;
        this.customProductInventories = customProductInventories;
        dataSource = new DataSource(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (context.getClass().getSimpleName().equals("CartActivity")) {

            /*START CART ADAPTER*/
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_list_cart_product, parent, false);
                customViewHolder = new CustomViewHolder(convertView);

            }

            final SelectedProduct selectedProduct = selectedProductList.get(position);
            for (CustomProductInventory customProductInventory: customProductInventories) {
                if(customProductInventory.getInventory_id() == selectedProduct.getInvetory_id()){
                    avilableQty = customProductInventory.getAvailable_qty();
                }
            }

            ImageView ivCartImage = convertView.findViewById(R.id.iv_cart_product_image);
            String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + selectedProduct.getImage_name();
            Picasso.get().load(imageUrl).into(ivCartImage);

            TextView tvCartProductHeading = convertView.findViewById(R.id.tv_cart_product_heading);
            tvCartProductHeading.setText(selectedProduct.getTitle());

            final TextView tvCartProductPrice = convertView.findViewById(R.id.tv_cart_product_price);

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            tvCartProductPrice.setText(String.valueOf(nf.format(selectedProduct.getPrice())));

            final Button btnQuantity = convertView.findViewById(R.id.btn_cart_product_quantity);
            btnQuantity.setText(String.valueOf(selectedProduct.getQunatity()));

            TextView tvCartProductSize = convertView.findViewById(R.id.tv_cart_product_size);
            tvCartProductSize.setText("SIZE: " + selectedProduct.getProductSize().getSizeName());
            tvCartProductSize.setVisibility(View.GONE);

            TextView tvCartProductColor = convertView.findViewById(R.id.tv_cart_product_color);
            tvCartProductColor.setText(", COLOR: " + selectedProduct.getProductColor().getColorName());
            tvCartProductColor.setVisibility(View.GONE);


            /*START OF PLUS BTN CLICK LISTENER*/
            LinearLayout btnPlus = convertView.findViewById(R.id.btn_cart_product_plus_container);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int addedQuantuity = selectedProduct.getQunatity() + 1;
                    if(addedQuantuity > avilableQty){
                        Toast.makeText(context, "Quantity exceeds inventory.", Toast.LENGTH_SHORT).show();
                    }else{

                        dataSource.updateQuantityCart(addedQuantuity, selectedProduct.getCart_id());
                        selectedProduct.setQunatity(addedQuantuity);
                        btnQuantity.setText(String.valueOf(addedQuantuity));

                        CartActivity.totalPrice = CartActivity.totalPrice + selectedProduct.getPrice();
                        tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));
                    }


                }
            });
            /*END OF PLUS BTN CLICK LISTENER*/


            /*START OF MINUS BTN CLICK LISTENER*/
            LinearLayout btnMinus = convertView.findViewById(R.id.btn_cart_product_minus_conatiner);
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int removedQuantuity = selectedProduct.getQunatity() - 1;
                    if(removedQuantuity > avilableQty){
                        Toast.makeText(context, "Quantity exceeds inventory.", Toast.LENGTH_SHORT).show();
                    }else{
                        if (removedQuantuity > 0) {

                            dataSource.updateQuantityCart(removedQuantuity, selectedProduct.getCart_id());
                            selectedProduct.setQunatity(removedQuantuity);
                            btnQuantity.setText(String.valueOf(removedQuantuity));

                            CartActivity.totalPrice = CartActivity.totalPrice - selectedProduct.getPrice();
                            tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));

                        }
                    }

                }
            });
            /*END OF PLUS BTN CLICK LISTENER*/


            /*END OF CART ADAPTER*/

        } else {

            /*START OF ORDERED ADAPTER*/
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_list_ordered_products, parent, false);

                customViewHolder = (CustomViewHolder) convertView.getTag();
            }
            final SelectedProduct selectedProduct = selectedProductList.get(position);

            ImageView ivCartImage = convertView.findViewById(R.id.iv_ordered_product_image);
            String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + selectedProduct.getImage_name();
            Picasso.get().load(imageUrl).into(ivCartImage);


            TextView tvOrderedProductHeading = convertView.findViewById(R.id.tv_ordered_product_heading);
            tvOrderedProductHeading.setText(selectedProduct.getTitle());

            TextView tvOrderedProductPrice = convertView.findViewById(R.id.tv_ordered_product_price);

            NumberFormat nf = NumberFormat.getCurrencyInstance();
            tvOrderedProductPrice.setText(String.valueOf(nf.format(selectedProduct.getPrice())));

            /*END OF OREDED ADAPTER*/
        }

        //customViewHolder.btnDelete.setOnClickListener(onDeleteListener(position, customViewHolder));

        customViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //dari kiri
        customViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, customViewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        customViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        customViewHolder.btnDelete.setOnClickListener(onDeleteListener(position, customViewHolder));
        return convertView;

    }


    private View.OnClickListener onDeleteListener(final int position, final CustomViewHolder customViewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectedProduct sp = selectedProductList.get(position);

                dataSource.removeCartProductById(sp.getCart_id());

                double currentprice = sp.getQunatity() * sp.getPrice();
                CartActivity.totalPrice = CartActivity.totalPrice - currentprice;
                tvTotalWriting.setText(String.valueOf(CartActivity.totalPrice));

                selectedProductList.remove(position);
                customViewHolder.swipeLayout.close();
                notifyDataSetChanged();

            }
        };

    }

    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private class CustomViewHolder {
        // private TextView name;
        private ImageButton btnDelete;
        // private View btnEdit;
        private SwipeLayout swipeLayout;

        public CustomViewHolder(View v) {
            swipeLayout = (SwipeLayout) v.findViewById(R.id.swipe);
            btnDelete = (ImageButton) v.findViewById(R.id.btndelete);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }
}
