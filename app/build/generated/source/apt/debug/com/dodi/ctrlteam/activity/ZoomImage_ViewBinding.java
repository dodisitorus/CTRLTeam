// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ZoomImage_ViewBinding implements Unbinder {
  private ZoomImage target;

  @UiThread
  public ZoomImage_ViewBinding(ZoomImage target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ZoomImage_ViewBinding(ZoomImage target, View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.message_image_zoomed, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ZoomImage target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
  }
}
