package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {

    /**
     * 根据旅游线路id查询图片信息
     * @param rid
     * @return
     */
    List<RouteImg> findImgs(int rid);
}
