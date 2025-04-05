package com.kedaya.springboot3mongodb.constant;

/**
 * MongoDB常量类
 */
public class MongoConstant {
    
    /**
     * 集合名称
     */
    public static final class Collection {
        // 用户集合
        public static final String USER = "user";
        // 产品集合
        public static final String PRODUCT = "product";
        // 订单集合
        public static final String ORDER = "order";
    }
    
    /**
     * 字段名称
     */
    public static final class Field {
        // ID字段
        public static final String ID = "_id";
        // 创建时间字段
        public static final String CREATE_TIME = "createTime";
        // 更新时间字段
        public static final String UPDATE_TIME = "updateTime";
        // 删除标记字段
        public static final String DELETED = "deleted";
    }
    
    private MongoConstant() {
        // 私有构造函数，防止实例化
    }
} 