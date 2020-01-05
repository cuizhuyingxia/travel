package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 判断用户是否收藏该旅游线路
     * @param uid
     * @param rid
     * @return
     */
    @Override
    public boolean isFavorite(int uid, String rid) {
        // 1. 查询Favorite对象
        Favorite favorite = favoriteDao.findByRidAndUid(uid, Integer.parseInt(rid));

        // 2. 判断Favorite对象是否为空
        if (favorite != null) {
            // 已收藏
            return true;
        } else {
            // 未收藏
            return false;
        }
    }

    @Override
    public void addFavorite(int uid, String rid) {
        // 1. 调用Dao添加收藏
        favoriteDao.addFavorite(uid, Integer.parseInt(rid));
    }
}
