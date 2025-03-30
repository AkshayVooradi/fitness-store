package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepoCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ProductEntity> searchProducts(String keyword){
        Query query = new Query();

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("title").regex(keyword,"i"),
                Criteria.where("description").regex(keyword,"i"),
                Criteria.where("brand").regex(keyword,"i"),
                Criteria.where("category").regex(keyword,"i")
        );

        query.addCriteria(criteria);

        return mongoTemplate.find(query,ProductEntity.class);
    }

    public List<ProductEntity> filterProducts(String category, Double minPrice, Double maxPrice,Integer minDiscount, Double minRating){
        Query query = new Query();

        Criteria criteria= Criteria.where("category").is(category);

        if(minPrice!=null && maxPrice != null){
            criteria=criteria.and("price").gte(minPrice).lte(maxPrice);
        }
        if(minDiscount != null){
            criteria=criteria.and("discountPercent").gte(minDiscount);
        }
        if(minRating != null){
            criteria=criteria.and("AverageRating").gte(minRating);
        }

        query.addCriteria(criteria);

        return mongoTemplate.find(query,ProductEntity.class);
    }
}
