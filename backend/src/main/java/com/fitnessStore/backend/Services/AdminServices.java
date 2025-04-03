package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.ExceptionHandling.InputArgumentException;
import com.fitnessStore.backend.ExceptionHandling.ResourceNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private CloudinaryService cloudinaryService;


    public ResponseEntity<?> addProduct(Map<String,String> productDetails,String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("admin")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

//        if(files.isEmpty() || title.isEmpty() || category.isEmpty() || brand.isEmpty() || price == 0 || discountPercent == 0 || description.isEmpty() || stock==0){
//            throw new InputArgumentException("Product is empty");
//        }
//
//        ProductEntity product = ProductEntity.builder()
//                .title(title)
//                .category(category)
//                .brand(brand)
//                .price(price)
//                .discountPercent(discountPercent)
//                .description(description)
//                .stock(stock)
//                .imageUrl(new ArrayList<>())
//                .build();
//
//        for(MultipartFile file: files){
//            Map uploadResult = cloudinaryService.upload(file);
//            product.getImageUrl().add((String) uploadResult.get("secure_url"));
//        }

        ProductEntity product = ProductEntity.builder().build();

        return new ResponseEntity<>(productRepo.save(product),HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteProduct(String productTitle, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        if(productTitle.isEmpty()){
            throw new InputArgumentException("Product title is empty");
        }

        ProductEntity product = productRepo.findByTitle(productTitle);

        if(product == null){
            throw new ResourceNotFoundException("product not found with the given title");
        }

        productRepo.delete(product);

        return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);

    }

    public ResponseEntity<?> getAllProducts(String category, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        if(category.isEmpty()){
            throw new InputArgumentException("Category is empty");
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
            throw new InputArgumentException("Product title is empty");
        }

        if(newProduct == null){
            throw new InputArgumentException("new product is empty");
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

        if(userName.isEmpty()){
            throw new InputArgumentException("Username is empty");
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

        boolean updated= false;

        for(CartItemClass item : items){
            if(item.getProduct().getTitle().equals(product.getTitle())){
                item.setStatus(status);
                if(status.equals("Delivered")){
                    item.setDeliveredAt(LocalDateTime.now());
                    user.getProducts().add(product);
                    userRepo.save(user);
                }
                orderRepo.save(order.get());
                updated=true;
                break;
            }
        }

        if(!updated){
            return new ResponseEntity<>("Product not found in that order",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Set the product status to "+status,HttpStatus.OK);
    }

    public ResponseEntity<?> uploadImage(MultipartFile myFile, String token) {
        UserEntity user = userByToken.userDetails(token);

        System.out.println(user);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","You are not allowed to access this page");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        Map uploadResult = cloudinaryService.upload(myFile);

        responseBody.put("success",true);
        responseBody.putAll(uploadResult);

        System.out.println(responseBody);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }
}
