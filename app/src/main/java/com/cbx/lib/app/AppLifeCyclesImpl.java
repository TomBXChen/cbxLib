package com.cbx.lib.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.multidex.MultiDex;


import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.cbx.lib.BuildConfig;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.ArmsUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.taohdao.base.FullImageLoader;
import com.taohdao.base.PhotoLongClickLoader;
import com.taohdao.bean.LQUser;
import com.taohdao.http.OnExecuteEvent;
import com.taohdao.library.common.widget.gallery.LongClickLoader;
import com.taohdao.library.common.widget.gallery.ZoomMediaLoader;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import timber.log.Timber;

import static com.taohdao.library.GlobalConfig.APP_LOGOUT;
import static com.taohdao.library.GlobalConfig.APP_SHOW_TOAST;
import static com.taohdao.library.GlobalConfig.APP_TIMEOUT;
import static com.taohdao.library.GlobalConfig.NEW_ACTIVITY_TIEZI_USER_LIST;


/**
 * Created by admin on 2018/3/13.
 */

public class AppLifeCyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    @Override
    public void onCreate(Application application) {
//        if (LeakCanary.isInAnalyzerProcess(application)) {
//            return;
//        }
        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            Timber.plant(new Timber.DebugTree());
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
//            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 UKLogger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    UKLogger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            UKLogger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);
        }
        //leakCanary内存泄露检查
//        ArtUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        //扩展 AppManager 的远程遥控功能
        ArmsUtils.obtainAppComponentFromContext(application).appManager().setHandleListener((appManager, message) -> {
            switch (message.what) {
                case APP_LOGOUT://退出APP
                    break;
                case APP_TIMEOUT:

                    break;
                case APP_SHOW_TOAST:

                    break;
                case NEW_ACTIVITY_TIEZI_USER_LIST:

                    break;
            }
        });


        Utils.init(application);

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(application);

        Logger.addLogAdapter(new AndroidLogAdapter());
        ZoomMediaLoader.getInstance().init(new FullImageLoader());
        LongClickLoader.getInstance().init(new PhotoLongClickLoader());

        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                // 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 建议在该回调处上传至我们的Crash监测服务器
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();


    }

    @Override
    public void onTerminate(Application application) {

    }





}
