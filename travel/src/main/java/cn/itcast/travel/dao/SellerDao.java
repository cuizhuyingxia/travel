package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     * 根据商家id查询商家信息
     * @param sid
     * @return
     */
    Seller findSeller(int sid);
}
