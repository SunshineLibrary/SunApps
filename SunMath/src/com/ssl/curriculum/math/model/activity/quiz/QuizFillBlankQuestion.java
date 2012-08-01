package com.ssl.curriculum.math.model.activity.quiz;

public class QuizFillBlankQuestion extends QuizQuestion{
    private String quizContent;
    private String answer;

    public QuizFillBlankQuestion() {
        super(QuizQuestion.TYPE_FILLBLANKS);
    }

    public String getQuizContent() {
        return quizContent;
    }

    public void setQuizContent(String quizContent) {
        this.quizContent = quizContent;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
