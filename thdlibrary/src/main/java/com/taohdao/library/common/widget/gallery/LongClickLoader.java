package com.taohdao.library.common.widget.gallery;


import com.taohdao.library.common.widget.gallery.loader.ILongClickLoader;

/**
 *
 * @author yangc
 * date 2017/9/4
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 图片加载管理器
 */

public class LongClickLoader {
    private volatile ILongClickLoader loader;
    public  static LongClickLoader getInstance(){
        return  Holder.holder;
    }
    private LongClickLoader(){

    }
    private  static  class  Holder{
           static LongClickLoader holder=new LongClickLoader();
    }
    /****
     * 初始化加载图片类
     * @param  loader 自定义
     * **/
    public  void init(ILongClickLoader loader){
        this.loader=loader;
    }

    public ILongClickLoader getLoader() {
        if (loader==null){
            throw  new  NullPointerException("loader no init");
        }
        return loader;
    }
}
