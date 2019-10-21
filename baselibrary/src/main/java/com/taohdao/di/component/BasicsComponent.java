package com.taohdao.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.taohdao.base.BasicsImplActivity;
import com.taohdao.base.BasicsImplFragment;
import com.taohdao.di.module.BasicsModule;

import dagger.Component;

/**
 * Created by admin on 2018/3/15.
 */
@ActivityScope
@Component(modules = {BasicsModule.class},dependencies = AppComponent.class)
public interface BasicsComponent  {
    void inject(BasicsImplActivity activity);
    void inject(BasicsImplFragment fragment);
}
