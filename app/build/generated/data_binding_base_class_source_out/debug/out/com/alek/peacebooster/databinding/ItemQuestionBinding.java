// Generated by view binder compiler. Do not edit!
package com.alek.peacebooster.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.alek.peacebooster.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemQuestionBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RadioGroup rgAnswers;

  @NonNull
  public final TextView tvQuestion;

  private ItemQuestionBinding(@NonNull ConstraintLayout rootView, @NonNull RadioGroup rgAnswers,
      @NonNull TextView tvQuestion) {
    this.rootView = rootView;
    this.rgAnswers = rgAnswers;
    this.tvQuestion = tvQuestion;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemQuestionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemQuestionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_question, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemQuestionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rgAnswers;
      RadioGroup rgAnswers = ViewBindings.findChildViewById(rootView, id);
      if (rgAnswers == null) {
        break missingId;
      }

      id = R.id.tvQuestion;
      TextView tvQuestion = ViewBindings.findChildViewById(rootView, id);
      if (tvQuestion == null) {
        break missingId;
      }

      return new ItemQuestionBinding((ConstraintLayout) rootView, rgAnswers, tvQuestion);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
