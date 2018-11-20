package com.app.adssan.ayucraze.productDetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.Util.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class SliderProductDetailFullItemFragment extends Fragment {


    public static final String IMAGE_KEY = "IMAGE_KEY";

    public SliderProductDetailFullItemFragment() {
        // Required empty public constructor
    }

    public static SliderProductDetailFullItemFragment newInstance(String imgName) {

        Bundle args = new Bundle();
        args.putString(IMAGE_KEY, imgName);

        SliderProductDetailFullItemFragment fragment = new SliderProductDetailFullItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item_slider_fragment_product_detail_full, container, false);

        Bundle args = getArguments();
        if(args == null) throw  new AssertionError();
        String imgName = args.getString(IMAGE_KEY);
        if(imgName == null) throw  new AssertionError();

        ImageView ivProductDetailImage = rootView.findViewById(R.id.iv_product_detail_image);

        String imageUrl = Constants.RECENT_PRODUCT_IMAGE_URL + imgName;
        Picasso.get().load(imageUrl).into(ivProductDetailImage);

        return rootView;
    }



}
