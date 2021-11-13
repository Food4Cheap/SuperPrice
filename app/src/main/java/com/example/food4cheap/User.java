package com.example.food4cheap;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import bolts.Task;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String KEY_SHOPPING_CART = "shoppingCart";

    public void setShoppingCart(){
        ShoppingCart userShoppingCart = new ShoppingCart();
        userShoppingCart.clearCart();
        userShoppingCart.saveInBackground();
        put(KEY_SHOPPING_CART, userShoppingCart);
    }
}
