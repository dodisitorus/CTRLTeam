// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewProjectFragment_ViewBinding implements Unbinder {
  private NewProjectFragment target;

  @UiThread
  public NewProjectFragment_ViewBinding(NewProjectFragment target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewpager, "field 'viewPager'", ViewPager.class);
    target.tabLayout = Utils.findRequiredViewAsType(source, R.id.tabs, "field 'tabLayout'", TabLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.floatingActionCreateNew = Utils.findRequiredViewAsType(source, R.id.floatingActionButton, "field 'floatingActionCreateNew'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NewProjectFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.tabLayout = null;
    target.toolbar = null;
    target.floatingActionCreateNew = null;
  }
}
