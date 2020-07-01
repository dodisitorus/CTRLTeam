// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AllContactFragment_ViewBinding implements Unbinder {
  private AllContactFragment target;

  @UiThread
  public AllContactFragment_ViewBinding(AllContactFragment target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.list_item, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_list, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.shimmerFrameLayout = Utils.findRequiredViewAsType(source, R.id.shimmer_view_container, "field 'shimmerFrameLayout'", ShimmerFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AllContactFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.shimmerFrameLayout = null;
  }
}
