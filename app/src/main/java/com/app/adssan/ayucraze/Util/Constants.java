package com.app.adssan.ayucraze.Util;

public class Constants {

    public static final String SERVER_PATH = "http://ayucraze.com/admin/public/";
    public static final String SUCCESS_PATH = "http://ayucraze.com/success.php";
    public static final String FAILURE_PATH = "http://ayucraze.com/failure.php";
    //public static final String SERVER_PATH = "http://192.168.2.13/bootic-admin/public/";

    public static final String PATH_TO_SERVER = SERVER_PATH + "braintree_payment.php";

    public static final String PRODUCT_GRID_URL = SERVER_PATH + "json/recent_products.php";
    public static final String RECENT_PRODUCT_IMAGE_URL = SERVER_PATH + "uploads/recent-products/";

    public static final String CATEGORY_IMAGE_URL = SERVER_PATH + "uploads/categories/";
    public static final String CATEGORY_GRID_URL = SERVER_PATH + "json/categories.php";

    public static final String CATEGORY_GRID_URL1 = "http://ayucraze.com/categoryapi.php";

    public static final String FILTER_GRID_URL = "http://ayucraze.com/api1.php";
    public static final String PRODUCT_GRID_URL1 = "http://ayucraze.com/api2.php";

    public static final String PRODUCT_IMAGES_URL = SERVER_PATH + "json/product_images.php";
    public static final String UPDATE_ADDRESS = SERVER_PATH + "json/update_address_by_id.php";
    public static final String INSERT_ADDRESS = SERVER_PATH + "json/insert_address.php";

    public static final String PATH_TO_PAYMENT = SERVER_PATH + "new_order.php";
    public static final String PATH_TO_PAYMENT_COMPLETE = SERVER_PATH + "new_ordered_product.php";

    public static final String PRODUCT_INVENTORY = SERVER_PATH + "json/get_product_inventory.php";
    public static final String INVENTORY_BY_ID = SERVER_PATH + "/json/get_inventory_by_ids.php";


    public static final String USER_REGISTRATION_URL = SERVER_PATH + "user_registration.php";
    public static final String USER_LOGIN_URL = SERVER_PATH + "user_login.php";

    public static final String INTENT_CATEGORY_ID = "intentCategoryId";

    public static final String FAVOURITE_ACTIVITY = "favouriteActivity";

    public static final String LOGIN_PREV_ACTIVITY = "loginPrevActivity";
    public static final String LOGIN_PREV_MAIN_ACTIVITY = "loginPrevMainActivity";
    public static final String LOGIN_PREV_CATEGORY = "loginPrevCategory";
    public static final String LOGIN_PREV_PRODUCT_DETAIL = "loginPrevProductActivity";
    public static final String LOGIN_PREV_CHECKOUT = "loginPrevCheckout";

    public static final String SHARED_PREF_FAVOURITE = "sharedPreferencesFavourites";
    public static final String SHARED_PREF_FAVOURITE_PRODUCTS = "sharedPreferencesFavouriteProducts";
    public static final String SHARED_PREF_USER = "sharedPreferencesUser";
    public static final String SHARED_PREF_LOGGEDIN_USER = "sharedPreferencesLoggedInUser";

    public static final String PRODUCT_DETAIL_INTENT = "productDetailIntent";
    public static final String CONFIRM_PAYMENT_INTENT = "confirmPaymentIntent";

    public static final String TOTAL_PRICE = "totalPrice";

    public static final String LOGIN_WITH_GOOGLE = "login_with_google";
    public static final String LOGIN_WITH_FACEBOOK = "login_with_facebook";


    public static final String TO_REGISTER = "toRegisterActivity";
    public static final String FROM_CART = "fromCartActivty";
    public static final String FROM_LOGIN = "fromLoginActivty";

}
