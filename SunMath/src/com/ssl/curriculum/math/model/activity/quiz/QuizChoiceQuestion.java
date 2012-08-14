package com.ssl.curriculum.math.model.activity.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizChoiceQuestion extends QuizQuestion {

    public QuizChoiceQuestion(QuizQuestion quizQuestion) {
        super(quizQuestion);
    }

	private ArrayList<Choice> choices = new ArrayList<Choice>();

    public void addChoice(Choice choice){
		choices.add(choice);
	}

    @Override
    public String toString() {
        return super.toString() + "--choices:" + choices.toString();
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public static class Choice{
        public String choice;
        public String body;

        public Choice(String choice, String body) {
            this.choice = choice;
            this.body = body;
        }

        @Override
        public String toString() {
            return "(" + choice + "," + body + ")";
        }
    }
}
