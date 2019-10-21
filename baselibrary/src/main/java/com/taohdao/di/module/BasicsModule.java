package com.taohdao.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.taohdao.http.mvp.interfaces.IBasics;
import com.taohdao.http.mvp.model.BasicsModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * Created by admin on 2018/3/14.
 */
@Module
public class BasicsModule {
    private IBasics.View view;
    /**
     * 构建 UserModule 时,将 View 的实现类传进来,这样就可以提供 View 的实现类给 Presenter
     *
     * @param view
     */
    public BasicsModule(IBasics.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    IBasics.View provideUserView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    IBasics.Model provideUserModel(BasicsModel model) {
        return model;
    }
    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }
}
