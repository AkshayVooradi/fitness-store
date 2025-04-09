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
import java.util.stream.Collectors;

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


    public ResponseEntity<?> addProduct(String title,String category,String brand,String price,String salePrice,String description,String totalStock,String image, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        if(title.isEmpty() || category.isEmpty() || brand.isEmpty() || price.isEmpty() || salePrice.isEmpty() || description.isEmpty() || totalStock.isEmpty() ){
            throw new InputArgumentException("Product is empty");
        }

        ProductEntity product = ProductEntity.builder()
                .title(title)
                .category(category)
                .brand(brand)
                .price(Double.parseDouble(price))
                .salePrice(Double.parseDouble(salePrice))
                .description(description)
                .totalStock(Integer.parseInt(totalStock))
                .image(image)
                .build();


        productRepo.save(product);

        responseBody.put("success",true);
        responseBody.put("data",product);

        return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteProduct(String id, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        if(id.isEmpty()){
            throw new InputArgumentException("Id is empty");
        }

        Optional<ProductEntity> product = productRepo.findById(id);

        if(product.isEmpty()){
            responseBody.put("success",false);
            responseBody.put("message","Product not found");
            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        productRepo.delete(product.get());
        responseBody.put("success",true);
        responseBody.put("message","Product delete successfully");

        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }

    public ResponseEntity<?> getAllProducts(String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        Map<String,Object> responseBody= new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        responseBody.put("success",true);
        responseBody.put("data",productRepo.findAll());

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> updateProduct(String id,String title,String category,String brand,String price,String salePrice,String description,String totalStock, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        Optional<ProductEntity> product = productRepo.findById(id);

        if(product.isEmpty()){
            throw new InputArgumentException("Product title is empty");
        }

        product.get().setTitle(title.isEmpty()?product.get().getTitle():title);
        product.get().setCategory(category.isEmpty()?product.get().getCategory():category);
        product.get().setBrand(brand.isEmpty()?product.get().getBrand():brand);
        product.get().setPrice(price.isEmpty()?product.get().getPrice():Double.parseDouble(price));
        product.get().setSalePrice(salePrice.isEmpty()?product.get().getSalePrice():Double.parseDouble(salePrice));
        product.get().setDescription(description.isEmpty()?product.get().getDescription():description);
        product.get().setTotalStock(description.isEmpty()?product.get().getTotalStock():Integer.parseInt(totalStock));

        productRepo.save(product.get());

        responseBody.put("success",true);
        responseBody.put("data",product);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrderById(String id,String authHeader, String status) {
        UserEntity AdminUser = userByToken.userDetails(authHeader);

        Map<String,Object> responseBody = new HashMap<>();

        if(!AdminUser.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        Optional<OrderEntity> order = orderRepo.findById(id);

        if(order.isEmpty()){
            responseBody.put("success",false);
            responseBody.put("message","Order not found");
            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        order.get().setOrderStatus(status);
        order.get().setOrderUpdateDate(LocalDateTime.now());

        orderRepo.save(order.get());

        responseBody.put("success",true);
        responseBody.put("message","Order status is updated successfully!");

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> uploadImage(MultipartFile myFile, String token) {
        UserEntity user = userByToken.userDetails(token);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","You are not allowed to access this page");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }


        Map uploadResult = cloudinaryService.upload(myFile);

        responseBody.put("success",true);
        responseBody.put("result",uploadResult.get("secure_url"));


        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> getAllOrders(String token) {
        UserEntity user = userByToken.userDetails(token);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        List<OrderEntity> orders = orderRepo.findAll();

//        List<OrderEntity> validOrders = orders.stream().filter(orderEntity -> !orderEntity.isCancelled()).collect(Collectors.toList());

        responseBody.put("success",true);
        responseBody.put("data",orders);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> getOrderById(String id, String token) {
        UserEntity user = userByToken.userDetails(token);

        Map<String,Object> responseBody = new HashMap<>();

        if(!user.getRole().equals("admin")){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        if(id.isEmpty()){
            throw new InputArgumentException("Product title is empty");
        }

        Optional<OrderEntity> order = orderRepo.findById(id);

        if(order.isEmpty()){
            responseBody.put("success",false);
            responseBody.put("message","Order not found");
            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        responseBody.put("success",true);
        responseBody.put("data",order);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }
}
