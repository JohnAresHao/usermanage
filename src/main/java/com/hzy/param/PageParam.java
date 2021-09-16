package com.hzy.param;
/*
 * Created by IntelliJ IDEA.
 * User: haozhaoyin
 * Time: 2020/1/29 21:13
 * Description: 分页类
 */

public class PageParam {
    private int beginLine=0;//起始行
    private Integer pageSize=6;//每页多少条数据
    private Integer currentPage=1;//当前页码

    public int getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "PageParam{" +
                "beginLine=" + beginLine +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                '}';
    }
}
