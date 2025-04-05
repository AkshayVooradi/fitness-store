package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.CartRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.StorageClasses.CartItemResponse;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServices {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private GetUserByToken userByToken;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartItemClass cartItemClass;

    public ResponseEntity<?> addProduct(String productId,String quantity,String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        Map<String,Object> responseBody = new HashMap<>();

        if(productId.isEmpty() || quantity.isEmpty()){
            responseBody.put("success",false);
            responseBody.put("message","Invalid data provided!");
            return new ResponseEntity<>(responseBody,HttpStatus.BAD_REQUEST);
        }

        CartEntity cart = cartRepo.findByUser(user);

        if(cart == null){
            cart = CartEntity.builder()
                    .user(user)
                    .products(new ArrayList<>())
                    .build();
            cartRepo.save(cart);
            user.setCart(cart);
            userRepo.save(user);
        }

        Optional<ProductEntity> product = productRepo.findById(productId);

        if(product.isEmpty()){
            responseBody.put("success",false);
            responseBody.put("message","Product not found");
            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        List<CartItemClass> products = cart.getProducts();

        boolean present=false;

        CartItemClass newItem = null;

        //if product is already present inside the cart
        for(CartItemClass prod: products){
            if(prod.getProduct().getId().equals(product.get().getId())){
                prod.setQuantity(prod.getQuantity()+Integer.parseInt(quantity));
                present=true;
                newItem=prod;
                break;
            }
        }

        if(!present) {
            CartItemClass item = CartItemClass.builder()
                    .product(product.get())
                    .productId(productId)
                    .quantity(Integer.parseInt(quantity))
                    .title(product.get().getTitle())
                    .image(product.get().getImage())
                    .price(product.get().getPrice())
                    .salePrice(product.get().getSalePrice())
                    .build();
            cart.getProducts().add(item);
            newItem=item;
        }

        cartRepo.save(cart);

        responseBody.put("success",true);
        responseBody.put("data",cart);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public ResponseEntity<?> getProducts(String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        CartEntity cart = user.getCart();

        Map<String,Object> responseBody = new HashMap<>();

        if(cart == null){
            responseBody.put("success",false);
            responseBody.put("message","Cart not found");
            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        CartItemResponse response = CartItemResponse.builder()
                .items(cart.getProducts())
                .build();

        responseBody.put("success",true);
        responseBody.put("data",response);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> deleteProduct(String id, String token) {
        UserEntity user = userByToken.userDetails(token);

        CartEntity cart = user.getCart();

        Optional<ProductEntity> product = productRepo.findById(id);

        Map<String,Object> responseBody = new HashMap<>();

        if(product.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","Invalid data provided!");

            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        cart.getProducts().removeIf(item -> item.getProduct().getId().equals(product.get().getId()));

        cartRepo.save(cart);

        CartItemResponse response = CartItemResponse.builder()
                .items(cart.getProducts())
                .build();

        responseBody.put("success",true);
        responseBody.put("data",response);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }

    public ResponseEntity<?> updateProduct(String id,String quantity, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        CartEntity cart = user.getCart();

        Optional<ProductEntity> product = productRepo.findById(id);

        Map<String,Object> responseBody = new HashMap<>();

        if(product.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","Invalid data provided!");

            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        boolean updated = false;

        for(CartItemClass item : cart.getProducts()){
            if(item.getProduct().getId().equals(product.get().getId())){
                item.setQuantity(Integer.parseInt(quantity));
                updated = true;
                break;
            }
        }

        if(!updated){

            responseBody.put("success",false);
            responseBody.put("message","Invalid data provided!");

            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        cartRepo.save(cart);

        CartItemResponse response = CartItemResponse.builder()
                .items(cart.getProducts())
                .build();

        responseBody.put("success",true);
        responseBody.put("data",response);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }
}
