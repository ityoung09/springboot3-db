package com.kedaya.springboot3mongodb.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Document(collection = "products")
@CompoundIndexes({
    @CompoundIndex(name = "category_price", def = "{'category': 1, 'price': -1}")
})
public class ProductEntity {
    
    @Id
    private String id;
    
    @NotBlank(message = "商品名称不能为空")
    @Indexed(unique = true)
    private String name;
    
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;
    
    @Indexed
    private String category;
    
    private String description;
    
    @Indexed(background = true)
    private LocalDateTime createTime;
} 