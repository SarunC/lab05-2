package com.example.lab0502;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value="index2")
public class MyView2 extends HorizontalLayout {
    public MyView2() {
        TextField addWord = new TextField("Add Word");
        Button addGood = new Button("Add Good Word");
        Button addBad = new Button("Add Bad Word");
        Select goodWord = new Select();
        goodWord.setLabel("Good Words");
        Select badWord = new Select();
        badWord.setLabel("Bad Words");

        addWord.setWidthFull();
        addGood.setWidthFull();
        addBad.setWidthFull();
        goodWord.setWidthFull();
        badWord.setWidthFull();

        VerticalLayout block1 = new VerticalLayout();
        block1.add(addWord, addGood, addBad, goodWord, badWord);

        TextField addSen = new TextField("Add Sentence");
        Button addSenBtn = new Button("Add Sentence");
        TextField goodSen = new TextField("Good Sentences");
        TextField badSen  = new TextField("Bad Sentences");
        Button show = new Button("Show Sentences");

        addSen.setWidthFull();
        addSenBtn.setWidthFull();
        goodSen.setWidthFull();
        badSen.setWidthFull();
        show.setWidthFull();

        VerticalLayout block2 = new VerticalLayout();
        block2.add(addSen, addSenBtn, goodSen, badSen, show);

        add(block1, block2);

        Notification noti = new Notification();

        addGood.addClickListener(event -> {
            MultiValueMap<String, String>formData = new LinkedMultiValueMap<>();
            formData.add("word", addWord.getValue());
            ArrayList<String> out = WebClient.create()
                    .post()
                    .uri("http://127.0.0.1:8080/addGood")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            noti.show("Add Good Word");
            goodWord.setItems(out);
        });

        addBad.addClickListener(event -> {
            MultiValueMap<String, String>formData = new LinkedMultiValueMap<>();
            formData.add("word", addWord.getValue());
            ArrayList<String> out = WebClient.create()
                    .post()
                    .uri("http://127.0.0.1:8080/addBad")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ArrayList.class)

                    .block();
            badWord.setItems(out);
        });

        addSenBtn.addClickListener(event -> {
            MultiValueMap<String, String>formData = new LinkedMultiValueMap<>();
            formData.add("sentence", addSen.getValue());
            String out = WebClient.create()
                    .post()
                    .uri("http://127.0.0.1:8080/proof")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            noti.show(out);
        });

        show.addClickListener(event -> {
            Sentence out = WebClient.create()
                    .get()
                    .uri("http://127.0.0.1:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            System.out.println(out);
            goodSen.setValue(out.goodSentences.toString());
            badSen.setValue(out.badSentences.toString());
        });

    }
}
