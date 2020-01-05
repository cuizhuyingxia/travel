package cn.itcast.travel.domain;

import java.util.List;

public class PageBean<T> {
    // 自定义泛型T，方便以后重复使用
    // 因为以后做商品列表，订单列表等都需要用到分页，所以我们可以自定义泛型T
    // 做商品列表时，就传入商品对象作为泛型，做订单列表时，就传入订单对象作为泛型
    private int totalCount;         // 总记录数
    private int totalPage;          // 总页数
    // 根据自定义的泛型，来将泛型对应的对象存储到list集合中
    private List<T> list;           // 每页显示的数据（List集合）
    private int currentPage;        // 当前页码
    private int rows;               // 每页显示的条目数

    public PageBean() {
    }

    public PageBean(int totalCount, int totalPage, List<T> list, int currentPage, int rows) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.list = list;
        this.currentPage = currentPage;
        this.rows = rows;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                ", list=" + list +
                ", currentPage=" + currentPage +
                ", rows=" + rows +
                '}';
    }
}
