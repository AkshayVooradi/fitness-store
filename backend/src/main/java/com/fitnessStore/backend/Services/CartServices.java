package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.CartRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public ResponseEntity<?> addProduct(String productTitle,int quantity,String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        CartEntity cart = cartRepo.findByUser(user);

        if(cart == null){
            cart = CartEntity.builder()
                    .user(user)
                    .products(new ArrayList<>())
                    .build();
            user.setCart(cart);
            cartRepo.save(cart);
            userRepo.save(user);
        }

        ProductEntity product = productRepo.findByTitle(productTitle);

        if(product == null){
            return new ResponseEntity<>("No such product found with name "+productTitle,HttpStatus.NOT_FOUND);
        }

        CartItemClass item = CartItemClass.builder()
                .product(product)
                .quantity(quantity)
                .build();

        cart.getProducts().add(item);

        return new ResponseEntity<>(cartRepo.save(cart), HttpStatus.OK);
    }

    public ResponseEntity<?> getProducts(String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        CartEntity cart = user.getCart();

        if(cart == null){
            return new ResponseEntity<>("Cart is Empty",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cart.getProducts(),HttpStatus.OK);
    }

    public ResponseEntity<?> deleteProduct(String title, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        CartEntity cart = user.getCart();

        ProductEntity product = productRepo.findByTitle(title);

        if(product == null){
            return new ResponseEntity<>("No such product found with name "+title,HttpStatus.NOT_FOUND);
        }

        cart.getProducts().removeIf(item -> item.getProduct().equals(product));

        cartRepo.save(cart);

        return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);

    }

    public ResponseEntity<?> updateProduct(String title,int quantity, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        CartEntity cart = user.getCart();

        ProductEntity product = productRepo.findByTitle(title);

        if(product == null){
            return new ResponseEntity<>("No such product found with name "+title,HttpStatus.NOT_FOUND);
        }

        boolean updated = false;

        for(CartItemClass item : cart.getProducts()){
            if(item.getProduct().equals(product)){
                item.setQuantity(quantity);
                updated = true;
                break;
            }
        }

        if(!updated){
            return new ResponseEntity<>("Product not found or updated",HttpStatus.NOT_FOUND);
        }

        cartRepo.save(cart);

        return new ResponseEntity<>("Updated Successfully",HttpStatus.NO_CONTENT);
    }
}
