package br.ufrgs.inf.pet.dinoapi.model.faq;

import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;

public class FaqItemModel {
    private Long id;
    private String question;
    private String answer;

    public FaqItemModel() {}

    public FaqItemModel(FaqItem faqItem) {
        this.setId(faqItem.getId());
        this.setQuestion(faqItem.getQuestion());
        this.setAnswer(faqItem.getAnswer());
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
