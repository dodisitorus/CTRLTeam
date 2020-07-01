// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeScreen_Activity_ViewBinding implements Unbinder {
  private HomeScreen_Activity target;

  @UiThread
  public HomeScreen_Activity_ViewBinding(HomeScreen_Activity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HomeScreen_Activity_ViewBinding(HomeScreen_Activity target, View source) {
    this.target = target;

    target.navigation = Utils.findRequiredViewAsType(source, R.id.navigation, "field 'navigation'", BottomNavigationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeScreen_Activity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.navigation = null;
  }
}
