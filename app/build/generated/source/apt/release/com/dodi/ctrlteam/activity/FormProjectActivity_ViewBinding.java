// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FormProjectActivity_ViewBinding implements Unbinder {
  private FormProjectActivity target;

  @UiThread
  public FormProjectActivity_ViewBinding(FormProjectActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FormProjectActivity_ViewBinding(FormProjectActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.ETname = Utils.findRequiredViewAsType(source, R.id.edittext_name, "field 'ETname'", EditText.class);
    target.ETdesc = Utils.findRequiredViewAsType(source, R.id.edittext_desc, "field 'ETdesc'", EditText.class);
    target.bg_modal_progress = Utils.findRequiredViewAsType(source, R.id.bg_modal_progress, "field 'bg_modal_progress'", RelativeLayout.class);
    target.progress_percent_text = Utils.findRequiredViewAsType(source, R.id.progress_percent_text, "field 'progress_percent_text'", TextView.class);
    target.label_progress_text = Utils.findRequiredViewAsType(source, R.id.label_progress_text, "field 'label_progress_text'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FormProjectActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.ETname = null;
    target.ETdesc = null;
    target.bg_modal_progress = null;
    target.progress_percent_text = null;
    target.label_progress_text = null;
  }
}
