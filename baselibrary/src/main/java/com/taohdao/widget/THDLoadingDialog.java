package com.taohdao.widget;

import com.taohdao.library.common.widget.dialog.THDTipDialog;
import com.taohdao.utils.THDDialogUtils;

/**
 * Created by admin on 2018/3/20.
 */

public class THDLoadingDialog extends AbsDialogView {

    THDTipDialog qmuiDialog;

    @Override
    public void showDialog() {

        if(qmuiDialog == null){
            qmuiDialog = THDDialogUtils.createTipDialog(THDTipDialog.Builder.ICON_TYPE_LOADING,"正在加载");
        }
        qmuiDialog.show();
    }

    @Override
    public void dismiss() {
        if(qmuiDialog!=null&&qmuiDialog.isShowing()){
            qmuiDialog.dismiss();
        }
    }
}
