package com.taohdao.library.utils;

import org.simple.eventbus.EventBus;

/**
 * Created by admin on 2018/3/19.
 */

public class EventBusUtils {
    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }
    public static void post(Object event,String tag) {
        EventBus.getDefault().post(event,tag);
    }
}
