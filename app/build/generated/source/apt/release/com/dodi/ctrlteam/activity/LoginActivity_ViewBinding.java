// Generated code from Butter Knife. Do not modify!
package com.dodi.ctrlteam.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dodi.ctrlteam.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.mProgressView = Utils.findRequiredView(source, R.id.login_progress, "field 'mProgressView'");
    target.mLoginFormView = Utils.findRequiredView(source, R.id.login_form, "field 'mLoginFormView'");
    target.mEmailView = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'mEmailView'", AutoCompleteTextView.class);
    target.mPasswordView = Utils.findRequiredViewAsType(source, R.id.editTextPassword, "field 'mPasswordView'", EditText.class);
    target.mEmailSignInButton = Utils.findRequiredViewAsType(source, R.id.cirLoginButton, "field 'mEmailSignInButton'", Button.class);
    target.actionpassword = Utils.findRequiredViewAsType(source, R.id.actionpassword, "field 'actionpassword'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mProgressView = null;
    target.mLoginFormView = null;
    target.mEmailView = null;
    target.mPasswordView = null;
    target.mEmailSignInButton = null;
    target.actionpassword = null;
  }
}
