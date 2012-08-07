package com.ssl.curriculum.math.model.activity.quiz;

public class QuizQuestion {
    public static final int TYPE_CHOICE = 0;
    public static final int TYPE_FILLBLANKS = 1;

    private int type;
    private int id = 0;
    private String answer;

    public QuizQuestion(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
