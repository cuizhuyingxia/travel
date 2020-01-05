package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {

    /**
     * 根据uid和rid查询收藏信息
     * @param uid
     * @param rid
     * @return
     */
    Favorite findByRidAndUid(int uid, int rid);

    /**
     * 查询该旅游线路的收藏次数
     * @param rid
     * @return
     */
    int findFavoriteCount(int rid);

    /**
     * 添加收藏
     * @param uid
     * @param rid
     */
    void addFavorite(int uid, int rid);
}
