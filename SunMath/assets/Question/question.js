window.onload = function() {
    var choicesDiv = document.getElementsByClassName("choices")[0];
    var inputElements = choicesDiv.getElementsByTagName("input");
    for (var i = 0; i < inputElements.length; i++) {
        (function (inputElement) {
			if (inputElement.checked) {
				Question.showConfirm();
			}
            inputElement.onchange = function() {
                if (inputElement.checked) {
					Question.setAnswer(inputElement.getAttribute("value"));
                } else {
                    Question.unsetAnswer(inputElement.getAttribute("value"));
                }
            }
        }) (inputElements[i]);
    };

    window.onSubmitAnswer = function () {
        choicesDiv.className = "choices answered";

        var labels = document.getElementsByTagName("label");
        for (var i = 0; i < labels.length; i++) {
            (function (label) {
                var id = label.getAttribute("for");
                var element = document.getElementById(id)
                    if (element != null) {
                        answer = element.getAttribute("value");
                        if (Question.isCorrectAnswer(answer)) {
                            if(element.checked){
								label.className = "correct"
							}else{
								label.className = "wrong"
							}
                        } else if (element.checked) {
                            label.className = "incorrect"
                        }
                        element.setAttribute("disabled", "true");
                    }
            }) (labels[i]);
        }
    };
};
