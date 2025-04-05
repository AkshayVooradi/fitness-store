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


    public ResponseEntity<?> addProduct(List<MultipartFile> files,String title,String category,String brand,Double price,Integer salePrice,String description,Integer stock, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);
        System.out.println("Logged in user role: " + user.getRole());


        if(!user.getRole().equals("admin")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }


        if(files.isEmpty() || title.isEmpty() || category.isEmpty() || brand.isEmpty() || price == 0 || salePrice == 0 || description.isEmpty() || stock==0){
            throw new InputArgumentException("Product is empty");
        }

        ProductEntity product = ProductEntity.builder()
                .title(title)
                .category(category)
                .brand(brand)
                .price(price)
                .salePrice(salePrice)
                .description(description)
                .stock(stock)
                .imageUrl(new ArrayList<>())
                .build();

        for(MultipartFile file: files){
            Map uploadResult = cloudinaryService.upload(file);
            product.getImageUrl().add((String) uploadResult.get("secure_url"));
        }

        return new ResponseEntity<>(productRepo.save(product),HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteProduct(String productTitle, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("admin")){
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


//public ResponseEntity<?> getAllProducts(String authHeader) {
//    UserEntity user = userByToken.userDetails(authHeader);
//    System.out.println("Logged in user role: " + user.getRole());
//
//    if (!user.getRole().equalsIgnoreCase("admin")) {
//        System.out.println("Unauthorized access attempt");
//        return new ResponseEntity<>("Unauthorized user", HttpStatus.UNAUTHORIZED);
//    }
//
//    List<ProductEntity> allProducts = productRepo.findAll();
//    System.out.println("Total products found: " + allProducts.size());
//
//    return new ResponseEntity<>(allProducts, HttpStatus.OK);
//}
public ResponseEntity<?> getAllProducts(String authHeader) {
    UserEntity user = userByToken.userDetails(authHeader);
    System.out.println("Logged in user role: " + user.getRole());

    if (!user.getRole().equalsIgnoreCase("admin")) {
        System.out.println("Unauthorized access attempt");
        return new ResponseEntity<>("Unauthorized user", HttpStatus.UNAUTHORIZED);
    }

    List<ProductEntity> allProducts = productRepo.findAll();
    System.out.println("Total products found: " + allProducts.size());

    List<Map<String, Object>> cleanedProducts = allProducts.stream().map(product -> {
        Object rawId = product.getId();
        String idString = "";

        if (rawId instanceof ObjectId) {
            idString = ((ObjectId) rawId).toHexString();
        } else if (rawId != null) {
            idString = rawId.toString();
        }

        Map<String, Object> cleaned = new HashMap<>();
        cleaned.put("_id", idString);
        cleaned.put("title", product.getTitle());
        cleaned.put("description", product.getDescription());
        cleaned.put("brand", product.getBrand());
        cleaned.put("category", product.getCategory());
        cleaned.put("price", product.getPrice());
        cleaned.put("salePrice", product.getSalePrice());
        cleaned.put("stock", product.getStock());
        cleaned.put("imageUrl", product.getImageUrl());
        cleaned.put("averageRating", product.getAverageRating());
        cleaned.put("sumOfRatings", product.getSumOfRatings());

        return cleaned;
    }).toList();

    return new ResponseEntity<>(cleanedProducts, HttpStatus.OK);
}







public ResponseEntity<?> updateProduct(String id, ProductEntity newProduct, String authHeader) {
    UserEntity user = userByToken.userDetails(authHeader);

    if (!user.getRole().equals("admin")) {
        return new ResponseEntity<>("Unauthorized user", HttpStatus.UNAUTHORIZED);
    }

    ProductEntity product = productRepo.findById(new ObjectId(id))
            .orElseThrow(() -> new InputArgumentException("Product not found with id: " + id));

    if (newProduct == null) {
        throw new InputArgumentException("New product is empty");
    }


    if (newProduct.getTitle() != null && !newProduct.getTitle().isEmpty()) {
        product.setTitle(newProduct.getTitle());
    }

    if (newProduct.getDescription() != null && !newProduct.getDescription().isEmpty()) {
        product.setDescription(newProduct.getDescription());
    }

    if (newProduct.getBrand() != null && !newProduct.getBrand().isEmpty()) {
        product.setBrand(newProduct.getBrand());
    }

    if (newProduct.getCategory() != null && !newProduct.getCategory().isEmpty()) {
        product.setCategory(newProduct.getCategory());
    }

    if (newProduct.getPrice() != 0) product.setPrice(newProduct.getPrice());
    if (newProduct.getSalePrice() != 0) product.setSalePrice(newProduct.getSalePrice());
    if (newProduct.getStock() != 0) product.setStock(newProduct.getStock());

    ProductEntity updatedProduct = productRepo.save(product);

    return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
}



    public ResponseEntity<?> updateOrder(String id,String title, String userName,String status, String authHeader) {
        UserEntity AdminUser = userByToken.userDetails(authHeader);

        if(!AdminUser.getRole().equals("admin")){
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
}
