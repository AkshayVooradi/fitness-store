package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.CartRepo;
import com.fitnessStore.backend.Repository.OrderRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class OrderServices {

    @Autowired
    private GetUserByToken getUserByToken;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    public ResponseEntity<?> createOrder(String totalCartAmount,String authorization) {

        UserEntity user = getUserByToken.userDetails(authorization);

        CartEntity cart = user.getCart();

        Map<String,Object> responseBody = new HashMap<>();

        if(cart == null || cart.getProducts().isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","Cart is Empty");

            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        OrderEntity orderEntity = OrderEntity.builder()
                .user(user)
                .userId(user.getId())
                .orderStatus("confirmed")
                .orderDate(LocalDateTime.now())
                .cartItems(new ArrayList<>())
                .totalAmount(Double.parseDouble(totalCartAmount))
                .isCancelled(false)
                .build();

        List<OrderEntity> orders = user.getOrders();

        if(orders.isEmpty()){
            orders = new ArrayList<>();
        }

        cart.getProducts().forEach(item -> orderEntity.getCartItems().add(item));

        List<CartItemClass> items = cart.getProducts();

        for(CartItemClass item : items){
            if(item.getQuantity()>item.getProduct().getTotalStock()){

                responseBody.put("success",false);
                responseBody.put("message","Invalid quantity selected");

                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }
        }

        //to make sure stocks are getting updated correctly
        for(CartItemClass item : items){
            ProductEntity product = item.getProduct();
            product.setTotalStock(product.getTotalStock()- item.getQuantity());
            productRepo.save(product);
        }

        user.getOrders().add(orderEntity);
        cart.setProducts(new ArrayList<>());

        orderRepo.save(orderEntity);
        userRepo.save(user);
        cartRepo.save(cart);

        responseBody.put("success",true);
        responseBody.put("message","order created successfully");
        responseBody.put("orderId",orderEntity.getId());

        return new ResponseEntity<>(responseBody,HttpStatus.CREATED);
    }

    public ResponseEntity<?> getOrders(String authorization) {
        UserEntity user = getUserByToken.userDetails(authorization);

        List<OrderEntity> orders = orderRepo.findByUserId(user.getId());

        Map<String,Object> responseBody = new HashMap<>();

        if(orders == null || orders.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","no orders found");

            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        responseBody.put("success",true);
        responseBody.put("data",orders);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> getOrderById(String id){

        Map<String ,Object> responseBody = new HashMap<>();

        if(id.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","id is empty");

            return new ResponseEntity<>(responseBody,HttpStatus.BAD_REQUEST);
        }

        Optional<OrderEntity> order = orderRepo.findById(id);

        if(order.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","order not found");

            return new ResponseEntity<>(responseBody,HttpStatus.NOT_FOUND);
        }

        responseBody.put("success",true);
        responseBody.put("data",order);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> cancelOrder(String id, String title, String authorization) {
        UserEntity user = getUserByToken.userDetails(authorization);

        List<OrderEntity> orders = user.getOrders();

        if(orders.isEmpty()){
            return new ResponseEntity<>("No Order found",HttpStatus.NOT_FOUND);
        }

        ProductEntity product = productRepo.findByTitle(title);

        if(product == null){
            return new ResponseEntity<>("No Product found with title "+title,HttpStatus.NOT_FOUND);
        }

        boolean updated = false;

        ObjectId objectId = new ObjectId(id);

        for(OrderEntity order: orders){
            if(updated){
                break;
            }
//            if(order.getId().equals(objectId)){
//                List<CartItemClass> products = order.getProducts();
//                for(CartItemClass item : products){
//                    if(item.getProduct().equals(product) &&
//                            (item.getStatus().equals("Under Packaging") || item.getStatus().equals("Out For Delivery"))){
//                        item.setStatus("Cancelled");
//                        orderRepo.save(order);
//                        updated= true;
//                        break;
//                    }
//                }
//            }
        }

        if(!updated){
            return new ResponseEntity<>("Item has already been delivered, cannot cancel now!",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Cancelled successfully",HttpStatus.OK);
    }
}
