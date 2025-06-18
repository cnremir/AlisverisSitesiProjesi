package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.Product;
import com.alisveris.AlisverisSitesi.models.User;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServices {

private final ProductServices productServices;
private final UserServices userServices;


    public PurchaseServices(ProductServices productServices, UserServices userServices) {
        this.productServices = productServices;
        this.userServices = userServices;
    }

    public void PurchaseProduct(Integer userId, Integer productId){
        User user = userServices.getUser(userId);
        Product product = productServices.getProduct(productId);

        if(user.getBalance()<product.getProductPrice()){
            throw new RuntimeException("Bakiye Yetersiz");
        }
        else{
            user.setBalance(user.getBalance()-product.getProductPrice());
        }
        userServices.updateUser(user);


    }



}
