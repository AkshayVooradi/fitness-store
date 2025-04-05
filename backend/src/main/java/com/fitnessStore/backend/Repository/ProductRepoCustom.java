package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<ProductEntity> filterProducts(List<String> category,List<String> brand, String sortBy){
        Query query = new Query();

        if(category!=null && !category.isEmpty()){
            query.addCriteria(Criteria.where("category").in(category));
        }

        if(brand!=null && !brand.isEmpty()){
            query.addCriteria(Criteria.where("brand").in(brand));
        }

        Sort sort = switch (sortBy){
            case "price-hightolow" -> Sort.by(Sort.Direction.DESC,"price");
            case "title-atoz" -> Sort.by(Sort.Direction.ASC,"title");
            case "title-ztoa" -> Sort.by(Sort.Direction.DESC,"title");
            default -> Sort.by(Sort.Direction.ASC,"price");
        };

        query.with(sort);

        return mongoTemplate.find(query,ProductEntity.class);
    }
}
