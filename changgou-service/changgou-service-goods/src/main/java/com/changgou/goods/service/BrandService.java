package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;

import java.util.List;

public interface BrandService {

    /***
     * 查询所有品牌
     * @return
     */
    List<Brand> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Brand findById(Integer id);

    /**
     * 修改商品信息
     * @param brand
     * @return
     */
    void update(Brand brand);

    /***
     * 新增品牌
     * @param brand
     */
    void add(Brand brand);

    /***
     * 删除品牌
     * @param id
     */
    void delete(Integer id);
}