package com.taohdao.http;

/**
 * Created by admin on 2018/3/21.
 */

public abstract class PageRequest extends BasicsRequest {

    public PageRequest(){
        init();
    }

    protected void init() {

    }

    public int page = 1 ;
    public int pageSize = 10 ;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
