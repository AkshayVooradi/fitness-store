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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public ResponseEntity<?> createOrder(AddressClass addressData, String authorization) {

        UserEntity user = getUserByToken.userDetails(authorization);

        CartEntity cart = user.getCart();

        if(cart == null || cart.getProducts().isEmpty()){
            return new ResponseEntity<>("Cart is Empty", HttpStatus.NOT_FOUND);
        }

        OrderEntity orderEntity = OrderEntity.builder()
                .createdAt(LocalDateTime.now())
                .address(addressData)
                .products(new ArrayList<>())
                .user(user)
                .totalCost(0)
                .build();

        List<OrderEntity> orders = user.getOrders();

        if(orders.isEmpty()){
            orders = new ArrayList<>();
        }

        cart.getProducts().forEach(item -> orderEntity.getProducts().add(item));

        double totalCost=0;

        List<CartItemClass> items = cart.getProducts();

        for(CartItemClass item : items){
            if(item.getQuantity()>item.getProduct().getStock()){
                return new ResponseEntity<>("Currently the stock for product "+item.getProduct().getTitle()+" is "+item.getProduct().getStock()+". Your quantity "+item.getQuantity()+" is much more",HttpStatus.BAD_REQUEST);
            }
            totalCost+=item.getCost();
        }

        //to make sure stocks are getting updated correctly
        for(CartItemClass item : items){
            ProductEntity product = item.getProduct();
            product.setStock(product.getStock()- item.getQuantity());
            productRepo.save(product);
        }

        orderEntity.setTotalCost(totalCost);

        orderEntity.getProducts().forEach(item-> item.setStatus("Under Packaging"));

        user.getOrders().add(orderEntity);
        user.getAddress().add(addressData);
        cart.setProducts(new ArrayList<>());

        orderRepo.save(orderEntity);
        userRepo.save(user);
        cartRepo.save(cart);


        return new ResponseEntity<>(orderEntity,HttpStatus.CREATED);
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
            if(order.getId().equals(objectId)){
                List<CartItemClass> products = order.getProducts();
                for(CartItemClass item : products){
                    if(item.getProduct().equals(product) &&
                            (item.getStatus().equals("Under Packaging") || item.getStatus().equals("Out For Delivery"))){
                        item.setStatus("Cancelled");
                        orderRepo.save(order);
                        updated= true;
                        break;
                    }
                }
            }
        }

        if(!updated){
            return new ResponseEntity<>("Item has already been delivered, cannot cancel now!",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Cancelled successfully",HttpStatus.OK);
    }
}
