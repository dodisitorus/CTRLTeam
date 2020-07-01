// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListChannelActivity_ViewBinding implements Unbinder {
  private ListChannelActivity target;

  @UiThread
  public ListChannelActivity_ViewBinding(ListChannelActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ListChannelActivity_ViewBinding(ListChannelActivity target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list_item, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_list, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ListChannelActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.toolbar = null;
  }
}
