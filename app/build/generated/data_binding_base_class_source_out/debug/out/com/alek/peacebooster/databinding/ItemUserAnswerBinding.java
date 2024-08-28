// Generated by view binder compiler. Do not edit!
package com.alek.peacebooster.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class ItemUserAnswerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView tvCorrectAnswer;

  @NonNull
  public final TextView tvDetails;

  @NonNull
  public final TextView tvQuestion;

  @NonNull
  public final TextView tvUserAnswer;

  private ItemUserAnswerBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView tvCorrectAnswer, @NonNull TextView tvDetails, @NonNull TextView tvQuestion,
      @NonNull TextView tvUserAnswer) {
    this.rootView = rootView;
    this.tvCorrectAnswer = tvCorrectAnswer;
    this.tvDetails = tvDetails;
    this.tvQuestion = tvQuestion;
    this.tvUserAnswer = tvUserAnswer;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemUserAnswerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemUserAnswerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_user_answer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemUserAnswerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.tvCorrectAnswer;
      TextView tvCorrectAnswer = ViewBindings.findChildViewById(rootView, id);
      if (tvCorrectAnswer == null) {
        break missingId;
      }

      id = R.id.tvDetails;
      TextView tvDetails = ViewBindings.findChildViewById(rootView, id);
      if (tvDetails == null) {
        break missingId;
      }

      id = R.id.tvQuestion;
      TextView tvQuestion = ViewBindings.findChildViewById(rootView, id);
      if (tvQuestion == null) {
        break missingId;
      }

      id = R.id.tvUserAnswer;
      TextView tvUserAnswer = ViewBindings.findChildViewById(rootView, id);
      if (tvUserAnswer == null) {
        break missingId;
      }

      return new ItemUserAnswerBinding((ConstraintLayout) rootView, tvCorrectAnswer, tvDetails,
          tvQuestion, tvUserAnswer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
