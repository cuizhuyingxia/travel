package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService{
    // 创建CategoryDao对象
    private CategoryDao categoryDaodao = new CategoryDaoImpl();
    /**
     * 查询所有旅游分类信息
     * @return
     */
    @Override
    public List<Category> findAll() {
        // 1. 从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        // 查询sotredset中的权重(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);

        List<Category> list = null;
        // 2. 判断查询到的集合是否为空
        if (categorys == null || categorys.size() == 0) {
            System.out.println("从数据库查询...");
            // 如果为空，则从数据库中进行查询
            list = categoryDaodao.findAll();
            // 然后将数据，再存入到redis中
            for (Category category : list) {
                // 使用有序集合类型sortedset存储
                jedis.zadd("category", category.getCid(), category.getCname());
            }
        } else {
            System.out.println("从redis中查询...");
            // 如果不为空，则将从redis中查询到的set集合类型的数据转换为list集合
            list = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                list.add(category);
            }
        }

        // 返回list集合
        return list;
    }
}
