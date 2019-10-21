package com.taohdao.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.jakewharton.rxbinding2.view.RxView;
import com.jess.arms.base.delegate.IActivity;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.ActivityLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.taohdao.library.common.widget.dialog.THDViewHelper;
import com.taohdao.library.common.widget.topbar.THDTopBar;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static com.jess.arms.utils.ThirdViewUtil.convertAutoView;
import static com.taohdao.http.utils.HttpsUtils.checkNull;


/**
 * Created by admin on 2018/3/13.
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleable,ISupportActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;
    @Inject
    protected P mPresenter;


    protected ImmersionBar mImmersionBar;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = convertAutoView(name, context, attrs);
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    protected void onCreateBefore(@Nullable Bundle savedInstanceState){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateBefore(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        initImmersionBar();
        if(useARouterInject()){
            ARouter.getInstance().inject(this);
        }
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    protected boolean useARouterInject() {
        return false;
    }



    @Override
    protected void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;

        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }


    public P getPresenter() {
        return mPresenter;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        if (useDarkStatus()) {
            mImmersionBar.statusBarDarkFont(true, 0.2f);
        }
        initBeforeBar(mImmersionBar);
        mImmersionBar.init();
    }

    protected void initBeforeBar(ImmersionBar immersionBar) {

    }

    protected boolean useDarkStatus() {
        return true;
    }

    public void addRxClick(final View view) {
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .map(new Function<Object, View>() {
                    @Override
                    public View apply(Object o) throws Exception {
                        return view;
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(this))
                .subscribe(new Consumer<View>() {
                    @Override
                    public void accept(View view) throws Exception {
                        onRxClick(view);
                    }
                });
    }

    public void addRxClick(final View view,long windowDuration) {
        RxView.clicks(view)
                .throttleFirst(windowDuration, TimeUnit.SECONDS)
                .map(new Function<Object, View>() {
                    @Override
                    public View apply(Object o) throws Exception {
                        return view;
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(this))
                .subscribe(new Consumer<View>() {
                    @Override
                    public void accept(View view) throws Exception {
                        onRxClick(view);
                    }
                });
    }

    public void onRxClick(View view) {

    }

    public void initTopBar(THDTopBar topBar, String title) {
        initTopBar(topBar, title, null);
    }

    public void initTopBarNotLeftButton(THDTopBar topBar, String title) {
        if (topBar != null) {
            if (mImmersionBar != null) ImmersionBar.setTitleBar(this, topBar);
            topBar.setTitle(checkNull(title));
            topBar.setTitleGravity(Gravity.CENTER);
        }
    }

    public void initTopBar(THDTopBar topBar, String title, String suString) {
        if (topBar != null) {
            if (mImmersionBar != null) ImmersionBar.setTitleBar(this, topBar);
            topBar.setTitle(checkNull(title));
            if (!TextUtils.isEmpty(suString)) {
                topBar.setSubTitle(checkNull(suString));
            } else {
                topBar.setTitleGravity(Gravity.CENTER);
            }
            topBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTitleFinishClick(v);
                    finish();
                }
            });
        }
    }

    public Button addRightTextButton(THDTopBar topBar, String text) {
        final Button button = topBar.addRightTextButton(text, THDViewHelper.generateViewId());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleRigthClick(v);
            }
        });
        return button;
    }

    public void addRightSingleImageButton(THDTopBar topBar, int drawableResId) {
        topBar.addRightImageButton(drawableResId, THDViewHelper.generateViewId()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleRigthClick(v);
            }
        });
    }

    public void onTitleRigthClick(View v) {

    }

    public void onTitleFinishClick(View v) {

    }

    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return mDelegate;
    }

    @Override
    public ExtraTransaction extraTransaction() {
        return  mDelegate.extraTransaction();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    // 选择性拓展其他方法

    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadMultipleRootFragment(int containerId, int showPosition, ISupportFragment... toFragments) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition,toFragments);
    }

    public void showHideFragment(ISupportFragment showFragment) {
        mDelegate.showHideFragment(showFragment);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Same as Activity's LaunchMode.
     */
    public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     *
     * @see #popTo(Class, boolean)
     * +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    /**
     * Pop the fragment.
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }
}
