package com.version.smec.wms.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smec.wms.R;

/**
 * Created by 小黑 on 2017/8/15.
 */
public class CustomePopupWindow extends Dialog {

    public CustomePopupWindow(Context context) {
        super(context);
    }

    protected CustomePopupWindow(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CustomePopupWindow(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder{
        private Context context;
        private String title;
        private String editTextHint;
        private String positiveButtonText;
        private String negativeButtonText;
        private View layout;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }




        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder seteditTextHint(String editTextHint){
            this.editTextHint = editTextHint;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public EditText getEditText(){
            return (EditText)layout.findViewById(R.id.et_input_content);
        }


        public CustomePopupWindow create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final CustomePopupWindow dialog =
                    new CustomePopupWindow(context,R.style.Dialog);
            layout = inflater.inflate(R.layout.layout_refuse_reason, null);
            dialog.addContentView(layout,
                    new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.tv_positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.tv_positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {

                layout.findViewById(R.id.tv_negativeButton).setVisibility(
                        View.GONE);
            }

            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.tv_negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(R.id.tv_negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {

                layout.findViewById(R.id.tv_negativeButton).setVisibility(
                        View.GONE);
            }

            if (title != null) {
                ((TextView) layout.findViewById(R.id.popwindow_title)).setText(title);
            }else{
                ((TextView) layout.findViewById(R.id.popwindow_title)).setVisibility(View.GONE);
            }

            if (editTextHint!=null){
                ((EditText)layout.findViewById(R.id.et_input_content)).setHint(editTextHint);
            }

            return dialog;
        }

    }

}
