package com.hzy.model;

import com.hzy.param.PageParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time:  2019-11-10 14:22
 * Description: 分页封装数据
 */

/**
 *
 * @param <E>
 */
public class Page<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int currentPage=1;//当前页数
    private long totalPages;//总页数
    private long totalNumber;//总记录数
    private List<E> list;//数据集

    public static Page NULL = new Page(0, 0, 6, new ArrayList());

    public Page() {//无参构造方法
        super();
    }

    /**
     *
     * @param beginLine
     * @param totalNumber
     * @param pageSize
     * @param list
     */
    public Page(int beginLine,long totalNumber,int pageSize,List<E> list){//有参构造方法
        super();
        this.currentPage=beginLine/pageSize+1;
        this.totalNumber=totalNumber;
        this.list=list;
        this.totalPages = totalNumber % pageSize == 0 ? totalNumber
                / pageSize : totalNumber / pageSize + 1;
    }

    /**
     *
     * @param pageParam
     * @param totalNumber
     * @param list
     */
    public Page(PageParam pageParam,long totalNumber,List<E> list){//有参构造方法
        super();
        this.currentPage=pageParam.getCurrentPage();
        this.totalNumber=totalNumber;
        this.list=list;
        this.totalPages = totalNumber % pageParam.getPageSize() == 0 ? totalNumber
                / pageParam.getPageSize() : totalNumber / pageParam.getPageSize() + 1;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalNumber=" + totalNumber +
                ", list=" + list +
                '}';
    }
}
