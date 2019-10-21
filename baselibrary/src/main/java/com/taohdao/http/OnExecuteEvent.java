package com.taohdao.http;

/**
 * Created by admin on 2018/3/27.
 */

public interface OnExecuteEvent {
    boolean onExecute();

    void accept();
}
