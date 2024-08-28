package com.alek.peacebooster.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.alek.peacebooster.R;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class Quiz implements Parcelable{

    @DocumentId
    private String id;
    private String title = "";
    private String description = "";
    private ArrayList<Question> questions = new ArrayList<>();
    private HashMap<String, String> results = new HashMap<>();

    public Quiz() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }


    public void setResults(HashMap<String, String> results) {
        this.results = results;
    }

    public HashMap<String, String> getResults() {
        return results;
    }

    private int getCorrectCount(){
        int correct = 0;
        for(Question question:questions){
            if(question.isAnsweredCorrectly()) correct++;
        }
        return correct;
    }

    public String getResultCountText(Context context){
        return String.format(Locale.getDefault(),context.getString(R.string.your_score_),
                getCorrectCount(),questions.size());
    }

    public int getResultColor(){
        int correct = getCorrectCount();
        int currentMax = 0;
        int size = results.size();
        int pos = 0;
        int color = Color.WHITE;
        for(String count: results.keySet()){
            int level = Integer.parseInt(count);
            if(level>=currentMax && correct>=level){
                currentMax = level;
                if(pos == size-1) color = Color.GREEN;
                else if(pos == size-2) color = Color.YELLOW;
                else color = Color.RED;
            }
            pos++;
        }
        return color;
    }

    public String getResultMessage(){
        int correct = getCorrectCount();
        int currentMax = 0;
        String result = "";
        for(String count: results.keySet()){
            int level = Integer.parseInt(count);
            if(level>=currentMax && correct>=level){
                currentMax = level;
                result = results.get(count);
            }
        }
        return result;
    }

    protected Quiz(Parcel in) {
        id = in.readString();
        description = in.readString();
        title = in.readString();
        questions = in.createTypedArrayList(Question.CREATOR);
        results = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(description);
        parcel.writeString(title);
        parcel.writeTypedList(questions);
        parcel.writeSerializable(results);
    }

    public static class Question implements Parcelable {

        private String text = "";
        private String details = "";
        private ArrayList<String> answers = new ArrayList<>();
        private int userAnswer = -1;
        private long correctAnswer = 1;

        protected Question(Parcel in) {
            text = in.readString();
            details = in.readString();
            answers = in.createStringArrayList();
            correctAnswer = in.readLong();
        }

        public Question() {
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public ArrayList<String> getAnswers() {
            return answers;
        }

        public void setAnswers(ArrayList<String> answers) {
            this.answers = answers;
        }

        public int getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(int userAnswer) {
            this.userAnswer = userAnswer;
        }

        public long getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(long correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public boolean isAnsweredCorrectly(){
            return correctAnswer == userAnswer;
        }

        public static final Creator<Question> CREATOR = new Creator<Question>() {
            @Override
            public Question createFromParcel(Parcel in) {
                return new Question(in);
            }

            @Override
            public Question[] newArray(int size) {
                return new Question[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(text);
            parcel.writeString(details);
            parcel.writeStringList(answers);
            parcel.writeLong(correctAnswer);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        if (id != null ? !id.equals(quiz.id) : quiz.id != null) return false;
        if (description != null ? !description.equals(quiz.description) : quiz.description != null)
            return false;
        if (title != null ? !title.equals(quiz.title) : quiz.title != null) return false;
        if (questions != null ? !questions.equals(quiz.questions) : quiz.questions != null)
            return false;
        return results != null ? results.equals(quiz.results) : quiz.results == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (questions != null ? questions.hashCode() : 0);
        result = 31 * result + (results != null ? results.hashCode() : 0);
        return result;
    }
}
