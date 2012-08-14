package com.ssl.curriculum.math.model.activity.quiz;

public class QuizQuestion {

    private int type;
    private int id = 0;
    private String answer;
    private boolean isPass = false;
    private String quizContent;

    public QuizQuestion(String quizContent, String answer, int id, int type) {
        this.quizContent = quizContent;
        this.answer = answer;
        this.id = id;
        this.type = type;
    }

    public QuizQuestion(QuizQuestion quizQuestion) {
        this(quizQuestion.getQuizContent(), quizQuestion.getAnswer(), quizQuestion.id, quizQuestion.getType());
    }

    public int getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuizContent() {
        return quizContent;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public boolean isPass() {
        return isPass;
    }

    @Override
    public String toString() {
        return "[" + id + ", " + type + ", " + quizContent + ", " + answer + "]";
    }
}
