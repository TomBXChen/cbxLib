package com.taohdao.library.common.widget.fragmentnavigator;

import android.support.v4.app.Fragment;

/**
 * Created by aspsine on 16/3/30.
 */
public interface FragmentNavigatorAdapter {

     Fragment onCreateFragment(int position);

     String getTag(int position);

     int getCount();

     FragmentAnim getAnim(int position);
}
