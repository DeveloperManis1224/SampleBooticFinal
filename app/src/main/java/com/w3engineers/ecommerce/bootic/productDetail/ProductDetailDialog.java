package com.w3engineers.ecommerce.bootic.productDetail;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.w3engineers.ecommerce.bootic.R;
import com.w3engineers.ecommerce.bootic.Util.UtilityClass;
import com.w3engineers.ecommerce.bootic.model.AvailableProduct;
import com.w3engineers.ecommerce.bootic.model.CustomProductInventory;
import com.w3engineers.ecommerce.bootic.model.ProductColor;
import com.w3engineers.ecommerce.bootic.model.ProductSize;
import com.w3engineers.ecommerce.bootic.model.SelectedProduct;

import java.util.ArrayList;

import okhttp3.internal.Util;

public class ProductDetailDialog extends DialogFragment {

    /*START OF RADIOGROUP SIZE VARIABLE*/
    private boolean isCheckingSize = true;
    private int radioChekedSizeId = -1;
    /*END OF RADIOGROUP SIZE VARIABLE*/

    /*START OF RADIOGROUP COLOR VARIABLE*/
    private boolean isCheckingColor = true;
    private int radioChekedColorId = -1;

    /*END OF RADIOGROUP COLOR VARIABLE*/

    /*START OF QUANTITY PLUS/MINUS */
    LinearLayout btnDialogQtyPlus;
    LinearLayout btnDialogQtyMinus;
    Button btnDialogQty;
    /*END OF QUANTITY PLUS/MINUS */

    int productQty = 1;

    private Button btnProductDialogOk;
    private ProductDialogListener productDialogListener;

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (Context) activity;
        productDialogListener = (ProductDialogListener) activity;
    }

    public static final String AVAILABLE_PRODUCTS = "availabeProducts";
    public static final String INVENTORY_ARRAYLIST = "inventoryArrayList";

    public static ProductDetailDialog newInstance(AvailableProduct availableProduct, ArrayList<CustomProductInventory> customProductInventories) {

        Bundle args = new Bundle();
        args.putParcelable(AVAILABLE_PRODUCTS, availableProduct);
        args.putParcelableArrayList(INVENTORY_ARRAYLIST, customProductInventories);
        ProductDetailDialog fragment = new ProductDetailDialog();
        fragment.setArguments(args);
        return fragment;

    }

    RadioGroup sizeRadioGroup1;
    RadioGroup sizeRadioGroup2;
    RadioGroup colorRadioGroup1;
    RadioGroup colorRadioGroup2;

    ArrayList<CustomProductInventory> customProductInventories;
    ArrayList<ProductSize> productSizes;
    ArrayList<ProductColor> productColors;
    LinearLayout layoutSizeWrapper;
    LinearLayout layoutColorWrapper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.product_detail_enty_dialog, container, false);

        customProductInventories = getArguments().getParcelableArrayList(INVENTORY_ARRAYLIST);
        productSizes = new ArrayList<>();
        productColors = new ArrayList<>();

        for (CustomProductInventory customProductInventory : customProductInventories) {

            ProductColor productColor = new ProductColor();
            ProductSize productSize = new ProductSize();

            productColor.setColor_id(customProductInventory.getColor_id());
            productColor.setColorName(customProductInventory.getColor_name());
            productColor.setColorCode(customProductInventory.getColor_code());
            productColors.add(productColor);

            productSize.setSize_id(customProductInventory.getSize_id());
            productSize.setSizeName(customProductInventory.getSize_name());
            productSizes.add(productSize);
        }

        AvailableProduct availableProduct = getArguments().getParcelable(AVAILABLE_PRODUCTS);

        layoutSizeWrapper = rootView.findViewById(R.id.layout_size_wrapper);
        settingSizeRadio(4);

        layoutColorWrapper = rootView.findViewById(R.id.layout_color_wrapper);
        settingColorRadio(4);


        /*START OF OK BUTTON*/
        btnProductDialogOk = rootView.findViewById(R.id.btn_product_detail_dialog_cancel);
        btnProductDialogOk.setOnClickListener(btnProductDialogCencelListener);
        /*END OF OK BUTTON*/

        /*START OF OK BUTTON*/
        btnProductDialogOk = rootView.findViewById(R.id.btn_product_detail_dialog_ok);
        btnProductDialogOk.setOnClickListener(btnProductDialogOkListener);
        /*END OF OK BUTTON*/

        /*START OF QUANTITY PLUS/MINUS*/
        btnDialogQtyPlus = rootView.findViewById(R.id.btn_dialog_cart_product_plus_conatiner);
        btnDialogQtyMinus = rootView.findViewById(R.id.btn_dialog_cart_product_minus_container);
        btnDialogQty = rootView.findViewById(R.id.btn_dialog_cart_product_quantity);

        btnDialogQty.setText(String.valueOf(productQty));

        /*START OF PLUS BTN CLICK LISTENER*/
        btnDialogQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productQty++;
                btnDialogQty.setText(String.valueOf(productQty));

            }
        });
        /*END OF PLUS BTN CLICK LISTENER*/


        /*START OF MINUS BTN CLICK LISTENER*/
        btnDialogQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQty > 1) {
                    productQty--;
                    btnDialogQty.setText(String.valueOf(productQty));
                }
            }
        });

        /*END OF QUANTITY PLUS/MINUS*/

        return rootView;

    }

    private void settingColorRadio(int colorRowCount) {

        colorRadioGroup1 = new RadioGroup(context);
        colorRadioGroup1.setOrientation(LinearLayout.HORIZONTAL);

        colorRadioGroup2 = new RadioGroup(context);
        colorRadioGroup2.setOrientation(LinearLayout.HORIZONTAL);

        colorRadioGroup1.setOnCheckedChangeListener(rGroupCheckedColorListener1);
        colorRadioGroup2.setOnCheckedChangeListener(rGroupCheckedColorListener2);

        if (productColors.size() <= colorRowCount) {

            for (int i = 0; i < productColors.size(); i++) {

                RadioButton rb = settingColorRadioBtn(productColors.get(i), i);
                colorRadioGroup1.addView(rb);

            }

            layoutColorWrapper.addView(colorRadioGroup1);

        } else if (productColors.size() <= (colorRowCount * 2)) {

            for (int i = 0; i < colorRowCount; i++) {

                RadioButton rb = settingColorRadioBtn(productColors.get(i), i);
                colorRadioGroup1.addView(rb);

            }
            layoutColorWrapper.addView(colorRadioGroup1);

            for (int i = colorRowCount; i < productColors.size(); i++) {

                RadioButton rb = settingColorRadioBtn(productColors.get(i), i);
                colorRadioGroup2.addView(rb);

            }
            layoutColorWrapper.addView(colorRadioGroup2);


        }
    }

    public RadioButton settingColorRadioBtn(ProductColor productColor, int id) {
        RadioButton rdbtn = new RadioButton(context);
        rdbtn.setId(id);
        rdbtn.setButtonDrawable(R.drawable.radio_button_color_clicked);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);

        params.setMargins(0, 0, 16, 10);
        rdbtn.setLayoutParams(params);

        int radioSize = UtilityClass.dpToPx(getResources().getDimension(R.dimen.radio_color_height), context);
        rdbtn.setHeight(radioSize);
        rdbtn.setWidth(radioSize);

        rdbtn.setBackgroundResource(R.drawable.radio_color_bg);
        GradientDrawable drawable = (GradientDrawable) rdbtn.getBackground();


        Log.e("CCCCCC",""+productColor.getColorCode());
        drawable.setColor(Color.parseColor(productColor.getColorCode()));
        return rdbtn;

    }

    private void settingSizeRadio(int sizeRowCount) {

        sizeRadioGroup1 = new RadioGroup(context);
        sizeRadioGroup1.setOrientation(LinearLayout.HORIZONTAL);

        sizeRadioGroup2 = new RadioGroup(context);
        sizeRadioGroup2.setOrientation(LinearLayout.HORIZONTAL);

        sizeRadioGroup1.setOnCheckedChangeListener(rGroupCheckedSizeListener1);
        sizeRadioGroup2.setOnCheckedChangeListener(rGroupCheckedSizeListener2);

        if (productSizes.size() <= sizeRowCount) {

            for (int i = 0; i < productSizes.size(); i++) {

                RadioButton rb = settingSizeRadioBtn(productSizes.get(i), i);
                sizeRadioGroup1.addView(rb);

            }

            layoutSizeWrapper.addView(sizeRadioGroup1);

        } else if (productSizes.size() <= (sizeRowCount * 2)) {

            for (int i = 0; i < sizeRowCount; i++) {

                RadioButton rb = settingSizeRadioBtn(productSizes.get(i), i);
                sizeRadioGroup1.addView(rb);

            }
            layoutSizeWrapper.addView(sizeRadioGroup1);

            for (int i = sizeRowCount; i < productSizes.size(); i++) {

                RadioButton rb = settingSizeRadioBtn(productSizes.get(i), i);
                sizeRadioGroup2.addView(rb);

            }
            layoutSizeWrapper.addView(sizeRadioGroup2);

        }
    }


    public RadioButton settingSizeRadioBtn(ProductSize productSize, int id) {
        RadioButton rdbtn = new RadioButton(context);

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(context, null);
        params.setMargins(0, 0, 20, 10);
        rdbtn.setLayoutParams(params);

        rdbtn.setText(productSize.getSizeName());
        rdbtn.setId(id);

        Drawable img = context.getResources().getDrawable(R.drawable.radio_button_size_clicked);
        img.setBounds(0, 0, 0, 0);
        rdbtn.setCompoundDrawables(img, null, null, null);
        return rdbtn;

    }

    /*START OF DIALOG BUTTON CANCEL LISTENER*/
    private Button.OnClickListener btnProductDialogCencelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            dismiss();

        }
    };
    /*END OF DIALOG BUTTON CANCEL LISTENER*/

    /*START OF DIALOG BUTTON OK LISTENER*/
    private Button.OnClickListener btnProductDialogOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String message = "";
            boolean showToast = false;
//            if ((radioChekedSizeId > 0) || (radioChekedColorId > 0)) {

                ProductSize selectedSize = productSizes.get(0);
                ProductColor selectedColor = productColors.get(0);

                for (CustomProductInventory customProductInventory : customProductInventories) {

                    if ((customProductInventory.getColor_id() == selectedColor.getColor_id())
                            && (customProductInventory.getSize_id() == selectedSize.getSize_id())) {

                        if (productQty <= customProductInventory.getAvailable_qty()) {

                            SelectedProduct selectedProduct = new SelectedProduct();
                            selectedProduct.setInvetory_id(customProductInventory.getInventory_id());
                            selectedProduct.setProductColor(selectedColor);
                            selectedProduct.setProductSize(selectedSize);
                            selectedProduct.setQunatity(productQty);

                            productDialogListener.onProductEntryComplete(selectedProduct);
                            dismiss();
                            showToast = false;
                            break;
                        } else {
                            message = "Your quantity exceeds inventory.";
                            showToast = true;
                        }
                    } else {
                        message = selectedColor.getColorName() + " is not available in " + selectedSize.getSizeName() + " size.";
                        showToast = true;
                    }
                }
//            } else {
//                showToast = true;
//                if (radioChekedColorId == -1) {
//                    message = "Please select a color.";
//
//                } else if (radioChekedSizeId == -1) {
//                    message = "Please select a size.";
//                }
//            }
            if (showToast) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };
    /*END OF DIALOG BUTTON OK LISTENER*/


    /*START OF RADIO GROUP SIZE LISTENET*/
    private RadioGroup.OnCheckedChangeListener rGroupCheckedSizeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingSize) {
                isCheckingSize = false;
                sizeRadioGroup2.clearCheck();
                radioChekedSizeId = checkedId;
            }
            isCheckingSize = true;
        }
    };
    private RadioGroup.OnCheckedChangeListener rGroupCheckedSizeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingSize) {
                isCheckingSize = false;
                sizeRadioGroup1.clearCheck();
                radioChekedSizeId = checkedId;
            }
            isCheckingSize = true;
        }
    };

    private RadioGroup.OnCheckedChangeListener rGroupCheckedColorListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingColor) {
                isCheckingColor = false;
                colorRadioGroup2.clearCheck();
                radioChekedColorId = checkedId;
            }
            isCheckingColor = true;
        }
    };
    private RadioGroup.OnCheckedChangeListener rGroupCheckedColorListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1 && isCheckingColor) {
                isCheckingColor = false;
                colorRadioGroup1.clearCheck();
                radioChekedColorId = checkedId;
            }
            isCheckingColor = true;
        }
    };
    /*END OF RADIO GROUP SIZE LISTENET*/


    public interface ProductDialogListener {
        void onProductEntryComplete(SelectedProduct selectedProduct);
    }

}
