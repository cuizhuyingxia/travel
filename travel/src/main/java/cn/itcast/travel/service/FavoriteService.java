package cn.itcast.travel.service;

public interface FavoriteService {

    /**
     * 判断用户是否收藏该旅游线路
     * @param uid
     * @param rid
     * @return
     */
    boolean isFavorite(int uid, String rid);

    /**
     * 添加收藏
     * @param uid
     * @param rid
     */
    void addFavorite(int uid, String rid);
}
