package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.OrderRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServices {


    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private GetUserByToken userByToken;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;


    public ResponseEntity<?> addProduct(ProductEntity product, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(productRepo.save(product),HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteProduct(String productTitle, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = productRepo.findByTitle(productTitle);

        if(product == null){
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }

        productRepo.delete(product);

        return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);

    }

    public ResponseEntity<?> getAllProducts(String category, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(productRepo.findByCategory(category),HttpStatus.OK);
    }

    public ResponseEntity<?> updateProduct(String productTitle, ProductEntity newProduct, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = productRepo.findByTitle(productTitle);

        if(product == null){
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }

        if(newProduct.getPrice() != 0){
            product.setPrice(newProduct.getPrice());
        }

        if(newProduct.getDiscountPercent()!=0){
            product.setDiscountPercent(newProduct.getDiscountPercent());
        }

        if(newProduct.getStock()!=0){
            product.setStock(newProduct.getStock());
        }

        productRepo.save(product);

        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrder(String id,String title, String userName,String status, String authHeader) {
        UserEntity AdminUser = userByToken.userDetails(authHeader);

        if(!AdminUser.getRole().equals("ADMIN")){
            return new ResponseEntity<>("You are not allowed to modify",HttpStatus.UNAUTHORIZED);
        }

        UserEntity user = userRepo.findByUsername(userName);

        if(user == null){
            return new ResponseEntity<>("User not found with name "+userName,HttpStatus.NOT_FOUND);
        }

        ProductEntity product = productRepo.findByTitle(title);

        if(product == null){
            return new ResponseEntity<>("Product Not found with name "+title,HttpStatus.NOT_FOUND);
        }

        ObjectId objectId = new ObjectId(id);

        Optional<OrderEntity> order = orderRepo.findById(objectId);

        if(order.isEmpty()){
            return new ResponseEntity<>("Order Not Found with the given id "+objectId,HttpStatus.NOT_FOUND);
        }

        List<CartItemClass> items = order.get().getProducts();

        for(CartItemClass item : items){
            if(item.getProduct().equals(product)){
                item.setStatus(status);
                if(status.equals("Delivered")){
                    item.setDeliveredAt(LocalDateTime.now());
                }
                orderRepo.save(order.get());
                break;
            }
        }

        return new ResponseEntity<>("Set the product status to "+status,HttpStatus.OK);
    }
}
