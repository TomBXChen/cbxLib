package com.taohdao.base;

/**
 * Created by admin on 2018/3/23.
 */

public interface BaseEvent {
    void setObject(Object obj);

    Object getObject();

    //事件定义
    enum CommonEvent implements BaseEvent {
        LOGIN, //登录
        LOGOUT, //登出
        TOKEN_TIMEOUT, //TOKEN失效
        REFRESH, //刷新
        MENU, //菜单
        DELETE, //
        CLOSE, //关闭
        REQUEST, //请求
        CHECK_AUTH; //检查身份
        private Object obj;

        @Override
        public void setObject(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object getObject() {
            return obj;
        }
    }

    enum ActionEvent implements BaseEvent {

        BLUETOOTH_CHANGE,//蓝牙状态改变
        SCAN_CHANGE,//扫码状态改变
        ADD_FRAGMENT,//新增fragment
        NUM_CHANGE,//
        CHANGE,//
        SUCCESS,//
        CHANGE_INDEX,//
        NUM_DEDUCTION,//
        INSERT_PRODUCT,//
        BINDING_COUPON,//
        REPLACE,//
        COLLECTION,//收款成功
        SCROLL_UP,
        SCROLL_DOWN,
        BINDING_QCODE,
        BINDING_MEMBER,
        CREATE_ACTIVITY,
        NEW_ACTIVITY,
        CALL_BACK,
        UPDATE_TASK_TOTAL; //更新
        private Object obj;

        @Override
        public void setObject(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object getObject() {
            return obj;
        }
    }
}
