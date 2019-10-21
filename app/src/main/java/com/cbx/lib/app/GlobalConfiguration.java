package com.cbx.lib.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.http.log.RequestInterceptor;
import com.jess.arms.integration.ConfigModule;
import com.taohdao.app.ActivityLifecycleCallbacksImpl;
import com.taohdao.app.GlobalHttpHandlerImpl;
import com.taohdao.app.ResponseErrorListenerImpl;
import com.taohdao.base.BuildConfig;
import com.taohdao.library.GlobalConfig;
import com.taohdao.library.http.cookie.CookieJarImpl;
import com.taohdao.library.http.cookie.store.PersistentCookieStore;
import com.taohdao.library.https.HttpsUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

import static com.taohdao.library.GlobalConfig.API_UPLOAD;
import static com.taohdao.library.GlobalConfig.API_WECHAT;


/**
 * Created by admin on 2018/3/13.
 */

public class GlobalConfiguration implements ConfigModule {


    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        if (!BuildConfig.LOG_DEBUG) { //Release 时,让框架不再打印 Http 请求和响应的信息
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }

        builder.baseurl(GlobalConfig.API)
                .printHttpLogLevel(RequestInterceptor.Level.ALL)
                // 这里提供一个全局处理 Http 请求和响应结果的处理类,可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                // 用来处理 rxjava 中发生的所有错误,rxjava 中发生的每个错误都会回调此接口
                // rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                .responseErrorListener(new ResponseErrorListenerImpl())
                .gsonConfiguration((context1, gsonBuilder) -> {//这里可以自己自定义配置Gson的参数
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                })
                .retrofitConfiguration((context1, retrofitBuilder) -> {//这里可以自己自定义配置Retrofit的参数,甚至你可以替换系统配置好的okhttp对象
//                    retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());//比如使用fastjson替代gson
                })
                .okhttpConfiguration((context1, okhttpBuilder) -> {//这里可以自己自定义配置Okhttp的参数
                    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
                    okhttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager); //支持 Https,详情请百度
                    okhttpBuilder.cookieJar(new CookieJarImpl(new PersistentCookieStore(context)));
                    okhttpBuilder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS);
                    //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                    ProgressManager.getInstance().with(okhttpBuilder);
                    //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                    RetrofitUrlManager.getInstance().with(okhttpBuilder);
                    RetrofitUrlManager.getInstance().putDomain("wechat",API_WECHAT);
                    RetrofitUrlManager.getInstance().putDomain("upload",API_UPLOAD);
                })
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置 RxCache 的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    // 想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                    // 否则请 return null;
                    return null;
                });


        AutoSize.initCompatMultiProcess(context);
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)

                //当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时
                //因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
                //而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
                //但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
                //所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 可以通过 UnitsManager#setDesignSize() 方法配置
                //如果副单位的设计图尺寸与主单位的设计图尺寸相同, 则不需要调用 UnitsManager#setDesignSize(), 框架会自动使用 AndroidManifest 中填写的设计图尺寸
//                .setDesignSize(2160, 3840)

                //AndroidAutoSize 默认开启对 sp 的支持, 调用 UnitsManager.setSupportSP(false); 可以关闭对 sp 的支持
                //如果关闭对 sp 的支持, 在布局时就应该使用副单位填写字体的尺寸
                //如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持, 这里我就继续开启 sp, 请自行斟酌自己的项目是否需要关闭对 sp 的支持
                .setSupportSP(false)

                //AndroidAutoSize 默认不支持副单位, 调用 UnitsManager#setSupportSubunits() 可选择一个自己心仪的副单位, 并开启对副单位的支持
                //只能在 pt、in、mm 这三个冷门单位中选择一个作为副单位, 三个单位的适配效果其实都是一样的, 您觉的哪个单位看起顺眼就用哪个
                //您选择什么单位就在 layout 文件中用什么单位进行布局, 我选择用 mm 为单位进行布局, 因为 mm 翻译为中文是妹妹的意思
                //如果大家生活中没有妹妹, 那我们就让项目中最不缺的就是妹妹!
                .setSupportSubunits(Subunits.MM);

    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifeCyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
//                ((RefWatcher) ArmsUtils
//                        .obtainAppComponentFromContext(f.getActivity())
//                        .extras()
//                        .get(RefWatcher.class.getName()))
//                        .watch(f);
            }
        });
    }
}
