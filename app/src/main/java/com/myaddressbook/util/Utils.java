package com.myaddressbook.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.gc.materialdesign.views.MaterialEditText;
import com.myaddressbook.R;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by K on 2014/12/8.
 */
public class Utils {
    public static int toPx(Context context, int dp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static MaterialDialog newMaterialDialog(Context context , int Rid) {
        final MaterialDialog editDialog = new MaterialDialog(context)
                .setTitle(Rid);

        final MaterialEditText editText = new MaterialEditText(context);

        editText.setHint(R.string.edit_hint_text);
        editText.setSingleLineEllipsis(true);
        editText.setMaxCharacters(10);
        editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);

        editText.setBaseColor(context.getResources().getColor(R.color.base_color));
        editText.setPrimaryColor(context.getResources().getColor(R.color.primaryColor));
        editText.setErrorColor(context.getResources().getColor(R.color.error_color));

        editText.setTextSize(18);
        editDialog.setContentView(editText);

        return editDialog;
    }
}
