package com.taohdao.http.mvp.persenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.taohdao.http.BasicsRequest;
import com.taohdao.http.JsonHandleSubscriber;
import com.taohdao.http.JsonResponse;
import com.taohdao.http.OnExecuteEvent;
import com.taohdao.http.mvp.interfaces.IBasics;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import okhttp3.ResponseBody;

/**
 * Created by admin on 2018/3/15.
 */
@ActivityScope
public class BasicsPresenterImpl extends BasePresenter<IBasics.Model, IBasics.View> {
    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;


    @Inject
    public BasicsPresenterImpl(IBasics.Model model, IBasics.View rootView, RxErrorHandler handler, RxPermissions permissions) {
        super(model, rootView);
        mErrorHandler = handler;
        mRxPermissions = permissions;
    }

    public <T extends BasicsRequest> void get(T t, boolean showLoading, int what) {
        get(t, showLoading, what, null);
    }

    public <T extends BasicsRequest> void get(T t, boolean showLoading, int what, final Object o) {
        if (t == null) {
            mRootView.callback(null, what, o);
            return;
        }
        mModel.get(t)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (showLoading) mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {//不管成功失败，我最后执行
                    if (showLoading) mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new JsonHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onSucceed(JsonResponse response) {
                        mRootView.callback(response, what, o);
                    }

                    /*
                     * 单独处理则重写该方法，去掉super.onError
                     * */
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.callback(null, what, o);
                    }

                    @Override
                    public void onDefinedError(int code) {
                        mRootView.callback(null, what, o);
                    }
                });
    }


    public <T extends BasicsRequest> void requestJson(T t, boolean showLoading, int what) {
        requestJson(t, showLoading, what, null);
    }

    public <T extends BasicsRequest> void requestJson(final T t, final boolean showLoading, int what, Object o) {
        if (t == null) {
            mRootView.callback(null, what, o);
            return;
        }
        mModel.postJson(t)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (showLoading) mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {//不管成功失败，我最后执行
                    if (showLoading) mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new JsonHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onSucceed(JsonResponse response) {
                        mRootView.callback(response, what, o);
                    }

                    /*
                     * 单独处理则重写该方法，去掉super.onError
                     * */
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.callback(null, what, o);
                    }

                    @Override
                    public void onDefinedError(int code) {
                        mRootView.callback(null, what, o);
                    }
                });
    }

    public <T extends BasicsRequest> void request(T t, boolean showLoading, int what) {
        request(t, showLoading, what, null);
    }

    public <T extends BasicsRequest> void request(final T t, final boolean showLoading, int what, Object o) {
        if (t == null) {
            mRootView.callback(null, what, o);
            return;
        }
        mModel.post(t)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (showLoading) mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {//不管成功失败，我最后执行
                    if (showLoading) mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new JsonHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onSucceed(JsonResponse response) {
                        mRootView.callback(response, what, o);
                    }

                    /*
                     * 单独处理则重写该方法，去掉super.onError
                     * */
                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.callback(null, what, o);
                    }

                    @Override
                    public void onDefinedError(int code) {
                        mRootView.callback(null, what, o);
                    }
                });
    }


    /**
     * 上传单张图片
     *
     * @param imageType
     * @param filePath
     * @param showLoading
     * @param what
     * @param o
     */
    public void upload(String imageType, final String filePath, final boolean showLoading, int what, Object o) {
        mModel.upSingleFile(imageType, new File(filePath), null)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (showLoading) mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {//不管成功失败，我最后执行
                    if (showLoading) mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {

                    @Override
                    public void onNext(ResponseBody body) {
                        try {
                            final String path = body.string();
                            final JsonResponse jsonResponse = new JsonResponse();
                            jsonResponse.setData(path);
                            mRootView.callback(jsonResponse, what, o);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mRootView.callback(null, what, o);
                        }
                    }
                });
    }

    public void uploads(String imageType, final List<String> filePath, final boolean showLoading, int what, Object o) {
        List<File> files = new ArrayList<>();
        for (String path : filePath) {
            files.add(new File(path));
        }
        mModel.upLoadFiles(imageType, files, null)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(1, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (showLoading) mRootView.showLoading();
                })
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {//不管成功失败，我最后执行
                    if (showLoading) mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {

                    @Override
                    public void onNext(ResponseBody body) {
                        try {
                            final String path = body.string();
                            final JsonResponse jsonResponse = new JsonResponse();
                            jsonResponse.setData(path);
                            mRootView.callback(jsonResponse, what, o);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mRootView.callback(null, what, o);
                        }
                    }
                });
    }


    public interface OnCodeListener {
        String applyString();
    }

    public void execute(final boolean showLoading, OnExecuteEvent mOnExecuteEvent) {
        Observable.just(1)
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (showLoading) mRootView.showLoading();
                })
                .doFinally(() -> {//不管成功失败，我最后执行
                    if (showLoading) mRootView.hideLoading();
                })
                .observeOn(Schedulers.io())
                .map(new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer) throws Exception {

                        return mOnExecuteEvent != null ? mOnExecuteEvent.onExecute() : false;
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (mOnExecuteEvent != null) mOnExecuteEvent.accept();
                    }
                });

    }

    public void execute(long milliseconds, OnExecuteEvent mOnExecuteEvent) {
        Observable.just(1)
                //延时两秒，第一个参数是数值，第二个参数是事件单位
                .delay(milliseconds, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())//主线程执行
                .doOnSubscribe(disposable -> {//什么都不用管，我先执行
                    if (mOnExecuteEvent != null) mOnExecuteEvent.onExecute();
                })
                .subscribeOn(Schedulers.io())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer s) throws Exception {
                        if (mOnExecuteEvent != null) mOnExecuteEvent.accept();
                    }
                });
    }

    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }
}
