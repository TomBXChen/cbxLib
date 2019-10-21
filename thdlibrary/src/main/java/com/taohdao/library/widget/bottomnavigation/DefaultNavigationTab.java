package com.taohdao.library.widget.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.taohdao.library.R;

public class DefaultNavigationTab extends BottomNavigationTab{
    float labelScale;

    public DefaultNavigationTab(Context context) {
        super(context);
    }

    public DefaultNavigationTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultNavigationTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DefaultNavigationTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.default_bottom_navigation_item, this, true);
        containerView = view.findViewById(R.id.fixed_bottom_navigation_container);
        labelView = view.findViewById(R.id.fixed_bottom_navigation_title);
        iconView = view.findViewById(R.id.fixed_bottom_navigation_icon);
        iconContainerView = view.findViewById(R.id.fixed_bottom_navigation_icon_container);
        badgeView = view.findViewById(R.id.fixed_bottom_navigation_badge);

        super.init();
    }

    @Override
    protected void setNoTitleIconContainerParams(LayoutParams layoutParams) {
        layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.fixed_no_title_icon_container_height);
        layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.fixed_no_title_icon_container_width);
    }

    @Override
    protected void setNoTitleIconParams(LayoutParams layoutParams) {
        layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.fixed_no_title_icon_height);
        layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.fixed_no_title_icon_width);
    }
}
