package com.app.adssan.ayucraze.Adapter;

import com.app.adssan.ayucraze.R;

 public class DataParser {

     String _order_id;
     String _orderMehtod;
     String _orderAmount;
     String _productId;
     String _productTitle;
     String _productPrice;

     public DataParser(String _order_id, String _orderMehtod, String _orderAmount, String _productId, String _productTitle, String _productPrice) {
         this._order_id = _order_id;
         this._orderMehtod = _orderMehtod;
         this._orderAmount = _orderAmount;
         this._productId = _productId;
         this._productTitle = _productTitle;
         this._productPrice = _productPrice;
     }

     public String get_order_id() {
         return _order_id;
     }

     public void set_order_id(String _order_id) {
         this._order_id = _order_id;
     }

     public String get_orderMehtod() {
         return _orderMehtod;
     }

     public void set_orderMehtod(String _orderMehtod) {
         this._orderMehtod = _orderMehtod;
     }

     public String get_orderAmount() {
         return _orderAmount;
     }

     public void set_orderAmount(String _orderAmount) {
         this._orderAmount = _orderAmount;
     }

     public String get_productId() {
         return _productId;
     }

     public void set_productId(String _productId) {
         this._productId = _productId;
     }

     public String get_productTitle() {
         return _productTitle;
     }

     public void set_productTitle(String _productTitle) {
         this._productTitle = _productTitle;
     }

     public String get_productPrice() {
         return _productPrice;
     }

     public void set_productPrice(String _productPrice) {
         this._productPrice = _productPrice;
     }
 }

