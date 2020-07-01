// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DetailChannelActivity_ViewBinding implements Unbinder {
  private DetailChannelActivity target;

  @UiThread
  public DetailChannelActivity_ViewBinding(DetailChannelActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DetailChannelActivity_ViewBinding(DetailChannelActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.image_channel = Utils.findRequiredViewAsType(source, R.id.image_channel, "field 'image_channel'", ImageView.class);
    target.collapsingToolbarLayout = Utils.findRequiredViewAsType(source, R.id.collapsing_toolbar, "field 'collapsingToolbarLayout'", CollapsingToolbarLayout.class);
    target.fab = Utils.findRequiredViewAsType(source, R.id.fab, "field 'fab'", FloatingActionButton.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'appBarLayout'", AppBarLayout.class);
    target.channel_name = Utils.findRequiredViewAsType(source, R.id.channel_name, "field 'channel_name'", TextView.class);
    target.channel_topic = Utils.findRequiredViewAsType(source, R.id.channel_topic, "field 'channel_topic'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DetailChannelActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.image_channel = null;
    target.collapsingToolbarLayout = null;
    target.fab = null;
    target.appBarLayout = null;
    target.channel_name = null;
    target.channel_topic = null;
  }
}
