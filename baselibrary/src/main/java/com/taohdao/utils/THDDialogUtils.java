package com.taohdao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jess.arms.utils.ArmsUtils;
import com.taohdao.base.BaseApp;
import com.taohdao.library.GlobalConfig;
import com.taohdao.library.common.widget.dialog.THDBottomSheet;
import com.taohdao.library.common.widget.dialog.THDDialog;
import com.taohdao.library.common.widget.dialog.THDDialogAction;
import com.taohdao.library.common.widget.dialog.THDTipDialog;
import com.taohdao.library.utils.MapNaviUtils;
import com.taohdao.widget.OnCommitListener;

/**
 * Created by admin on 2018/3/20.
 */

public class THDDialogUtils {
    /**
     * 根据类型展示不同QMUIdialog
     *
     * @param iconType THDTipDialog.Builder.ICON_TYPE_LOADING 加载中
     *                 THDTipDialog.Builder.ICON_TYPE_SUCCESS 成功
     *                 THDTipDialog.Builder.ICON_TYPE_FAIL 失败
     *                 THDTipDialog.Builder.ICON_TYPE_INFO  警告 感叹号
     * @param content
     * @return
     */
    public static THDTipDialog createTipDialog(@THDTipDialog.Builder.IconType int iconType, String content) {
        return new THDTipDialog.Builder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity())
                .setIconType(iconType)
                .setTipWord(content)
                .create();
    }

    public static THDDialog createMsgNegativeDialogSingleButton(String title, String content, String actionName, final OnCommitListener onCommitListener) {
        return new THDDialog.MessageDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity())
                .setTitle(title)
                .setMessage(content)
                .addAction(0, actionName, THDDialogAction.ACTION_PROP_NEGATIVE, new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        if (onCommitListener != null) {
                            onCommitListener.onCommit(index, null);
                        }
                        dialog.dismiss();
                    }
                }).create();
    }


    /**
     * 标题 + 内容 + 自定义按钮名称
     *
     * @param title
     * @param content
     * @param actionName
     * @param onCommitListener
     * @return
     */
    public static THDDialog createMessageNegativeDialog(String title, String content, String actionName, final OnCommitListener onCommitListener) {
        return new THDDialog.MessageDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity())
                .setTitle(title)
                .setMessage(content)
                .addAction("取消", new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, actionName, THDDialogAction.ACTION_PROP_NEGATIVE, new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        if (onCommitListener != null) {
                            onCommitListener.onCommit(index, null);
                        }
                        dialog.dismiss();
                    }
                }).create();
    }

    public static THDDialog createMessageNegativeDialog(String title, String content, String inactiveActionName,String positiveActionName,final OnCommitListener inactiveListener, final OnCommitListener positiveListener) {
        return new THDDialog.MessageDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity())
                .setTitle(title)
                .setMessage(content)
                .addAction(inactiveActionName, new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        if (inactiveListener != null) {
                            inactiveListener.onCommit(index, null);
                        }
                        dialog.dismiss();
                    }
                })
                .addAction(0,positiveActionName, THDDialogAction.ACTION_PROP_NEGATIVE, new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        if (positiveListener != null) {
                            positiveListener.onCommit(index, null);
                        }
                        dialog.dismiss();
                    }
                }).create();
    }

    /**
     * 标题 + 输入框 + 确认按钮
     *
     * @param title
     * @param hintStr
     * @param content
     * @param actionName
     * @param inputType
     * @param onCommitListener
     * @return
     */
    public static THDDialog createEditDialog(String title, String hintStr, String content, String actionName, int inputType, final OnCommitListener onCommitListener) {
        final THDDialog.EditTextDialogBuilder builder = new THDDialog.EditTextDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity());
        builder.setTitle(title)
                .setPlaceholder(hintStr)
                .setContent(content)
                .setInputType(inputType)
                .addAction("取消", new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(actionName, new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        final EditText editText = builder.getEditText();
                        if (onCommitListener != null) {
                            onCommitListener.onCommit(index, editText.getText().toString());
                        }
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }


    public static THDDialog.EditTextDialogBuilder createEditDialogBuilder(String title, String hintStr, String content, String actionName, int inputType, final OnCommitListener onCommitListener) {
        THDDialog.EditTextDialogBuilder builder =   new THDDialog.EditTextDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity());
        builder.setTitle(title)
                .setPlaceholder(hintStr)
                .setContent(content)
                .setInputType(inputType)
                .addAction("取消", new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(actionName, new THDDialogAction.ActionListener() {
                    @Override
                    public void onClick(THDDialog dialog, int index) {
                        final EditText editText = builder.getEditText();
                        if (onCommitListener != null) {
                            onCommitListener.onCommit(index, editText.getText().toString());
                        }
                        dialog.dismiss();
                    }
                });

        return builder;
    }


    /**
     * 单项选择
     *
     * @param items
     * @param checkedIndex
     * @param onCommitListener
     * @return
     */
    public static THDDialog showSingleChoiceDialog(final String[] items, final int checkedIndex, final OnCommitListener onCommitListener) {
        return new THDDialog.CheckableDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity()) {
            @Override
            protected void onCreateContent(THDDialog dialog, ViewGroup parent) {
//                final THDDialogMenuItemView menuItemView = mMenuItemViews.get(checkedIndex);
//                for (int i=0;i<menuItemView.getChildCount();i++){
//                    final View childView = menuItemView.getChildAt(i);
//                    if(childView!=null&&childView instanceof ImageView){
//                        ((ImageView)childView).setImageResource(R.drawable.ic_choose_select);
//                    }
//                }
                super.onCreateContent(dialog, parent);
            }
        }
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onCommitListener != null) {
                            onCommitListener.onCommit(which, items[which]);
                        }
//                        Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).create();

    }

    public static void showMapNaviDialog(MapNaviUtils.LatLng latLng,String sname,String dname){
       final Activity currentActivity = ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity();
        THDBottomSheet.BottomListSheetBuilder builder = new THDBottomSheet.BottomListSheetBuilder(currentActivity);
        builder .addItem(String.format("百度地图%s",MapNaviUtils.isBaiduMapInstalled()?"":"（未安装）"))
                .addItem(String.format("高德地图%s",MapNaviUtils.isGdMapInstalled()?"":"（未安装）"))
                .setOnSheetItemClickListener(new THDBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(THDBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position){
                            case 0:
                                if(!MapNaviUtils.isBaiduMapInstalled())return;
                                MapNaviUtils.LatLng baiduLatLng = MapNaviUtils.GCJ02ToBD09(latLng);
                                MapNaviUtils.openBaiDuNavi(dialog.getContext(),0,0,sname,baiduLatLng.latitude,baiduLatLng.longitude,dname);
                                break;
                            case 1:
                                if(!MapNaviUtils.isGdMapInstalled())return;
                                MapNaviUtils.openGaoDeNavi(dialog.getContext(),0,0,sname,latLng.latitude,latLng.longitude,dname);
                                break;
                        }
                        dialog.dismiss();
                    }
                }).build().show();
    }

    public static THDDialog createCustomDialog(int resLayout, OnCreateContentListener mOnCreateContentListener) {
        return new THDDialog.CustomDialogBuilder(ArmsUtils.obtainAppComponentFromContext(BaseApp.getInstance()).appManager().getCurrentActivity()) {
            @Override
            protected void onCreateContent(THDDialog dialog, ViewGroup parent) {
                super.onCreateContent(dialog, parent);
                if (mOnCreateContentListener != null)
                    mOnCreateContentListener.OnCreateContent(dialog, parent);
            }
        }.setLayout(resLayout).create();
    }
    public static THDDialog createCustomDialog(Context mContext, int resLayout, OnCreateContentListener mOnCreateContentListener) {
        return new THDDialog.CustomDialogBuilder(mContext) {
            @Override
            protected void onCreateContent(THDDialog dialog, ViewGroup parent) {
                super.onCreateContent(dialog, parent);
                if (mOnCreateContentListener != null)
                    mOnCreateContentListener.OnCreateContent(dialog, parent);
            }
        }.setLayout(resLayout).create();
    }

    public interface OnCreateContentListener {
        void OnCreateContent(THDDialog dialog, ViewGroup parent);
    }

}
