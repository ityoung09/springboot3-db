package com.kedaya.springboot3mongodb.service;

import com.kedaya.springboot3mongodb.model.entity.ProductEntity;

import java.util.List;
import java.util.Map;

interface ProductAggregationService {


    /**
     * 按类别分组并计算每个类别的平均价格
     *
     * @return 类别及其平均价格的列表
     */
    List<Map> getAveragePriceByCategory();

    /**
     * 查找特定类别中价格最高的N个产品
     *
     * @param category 产品类别
     * @param limit    返回结果数量
     * @return 产品列表
     */
    List<ProductEntity> getTopPricedProductsByCategory(String category, int limit);

    /**
     * 按价格范围分组统计产品数量
     *
     * @return 价格范围及对应的产品数量
     */
    List<Map> countProductsByPriceRange();
} 