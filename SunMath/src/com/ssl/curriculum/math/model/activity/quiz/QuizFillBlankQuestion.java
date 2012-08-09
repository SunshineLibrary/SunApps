package com.ssl.curriculum.math.model.activity.quiz;

public class QuizFillBlankQuestion extends QuizQuestion{
    private String quizContent;


    public QuizFillBlankQuestion() {
        super(QuizQuestion.TYPE_FILLBLANKS);
    }

    public String getQuizContent() {
        return quizContent;
    }

    public void setQuizContent(String quizContent) {
        this.quizContent = quizContent;
    }


}
