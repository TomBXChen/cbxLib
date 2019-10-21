/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.taohdao.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * ================================================
 * 展示 {@link Application.ActivityLifecycleCallbacks} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        UKLogger.w("activity - onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        UKLogger.w(activity + " - onActivityStarted");
        if (!activity.getIntent().getBooleanExtra("isInitToolbar", false)) {
            //由于加强框架的兼容性,故将 setContentView 放到 onActivityCreated 之后,onActivityStarted 之前执行
            //而 findViewById 必须在 Activity setContentView() 后才有效,所以将以下代码从之前的 onActivityCreated 中移动到 onActivityStarted 中执行
            activity.getIntent().putExtra("isInitToolbar", true);
            final String className = activity.getComponentName().getClassName();
            if(className.endsWith("HomeActivity")){
//                AppManager.post(APP_BIND_PUSH);
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        UKLogger.w(activity + " - onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        UKLogger.w(activity + " - onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
//        UKLogger.w(activity + " - onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        UKLogger.w(activity + " - onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        UKLogger.w(activity + " - onActivityDestroyed");
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        activity.getIntent().removeExtra("isInitToolbar");
    }
}
