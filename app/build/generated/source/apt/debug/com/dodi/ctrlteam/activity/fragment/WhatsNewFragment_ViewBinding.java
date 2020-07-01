// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WhatsNewFragment_ViewBinding implements Unbinder {
  private WhatsNewFragment target;

  @UiThread
  public WhatsNewFragment_ViewBinding(WhatsNewFragment target, View source) {
    this.target = target;

    target.title_menu = Utils.findRequiredViewAsType(source, R.id.title_menu, "field 'title_menu'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.list_subs_news = Utils.findRequiredViewAsType(source, R.id.list_subs_news, "field 'list_subs_news'", RecyclerView.class);
    target.list_news = Utils.findRequiredViewAsType(source, R.id.list_news, "field 'list_news'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_list, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.bg_modal_progress = Utils.findRequiredViewAsType(source, R.id.bg_modal_progress, "field 'bg_modal_progress'", RelativeLayout.class);
    target.progress_percent_text = Utils.findRequiredViewAsType(source, R.id.progress_percent_text, "field 'progress_percent_text'", TextView.class);
    target.label_progress_text = Utils.findRequiredViewAsType(source, R.id.label_progress_text, "field 'label_progress_text'", TextView.class);
    target.see_more = Utils.findRequiredViewAsType(source, R.id.see_more, "field 'see_more'", TextView.class);
    target.label_list_news_subs = Utils.findRequiredViewAsType(source, R.id.label_list_news_subs, "field 'label_list_news_subs'", LinearLayout.class);
    target.shimmerFrameLayout = Utils.findRequiredViewAsType(source, R.id.shimmer_view_container, "field 'shimmerFrameLayout'", ShimmerFrameLayout.class);
    target.shimmerFrameLayout2 = Utils.findRequiredViewAsType(source, R.id.shimmer_view_container2, "field 'shimmerFrameLayout2'", ShimmerFrameLayout.class);
    target.LLMenuAttendance = Utils.findRequiredViewAsType(source, R.id.LLMenuAttendance, "field 'LLMenuAttendance'", LinearLayout.class);
    target.LLMenuSchedule = Utils.findRequiredViewAsType(source, R.id.LLMenuSchedule, "field 'LLMenuSchedule'", LinearLayout.class);
    target.LLMenuFiles = Utils.findRequiredViewAsType(source, R.id.LLMenuFiles, "field 'LLMenuFiles'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WhatsNewFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title_menu = null;
    target.toolbar = null;
    target.list_subs_news = null;
    target.list_news = null;
    target.swipeRefreshLayout = null;
    target.bg_modal_progress = null;
    target.progress_percent_text = null;
    target.label_progress_text = null;
    target.see_more = null;
    target.label_list_news_subs = null;
    target.shimmerFrameLayout = null;
    target.shimmerFrameLayout2 = null;
    target.LLMenuAttendance = null;
    target.LLMenuSchedule = null;
    target.LLMenuFiles = null;
  }
}
