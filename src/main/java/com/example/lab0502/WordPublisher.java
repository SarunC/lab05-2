package com.example.lab0502;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;

@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbit;
    protected Word words = new Word();

    public WordPublisher() {
        words.goodWords.add("happy");
        words.goodWords.add("enjoy");
        words.goodWords.add("life");
        words.badWords.add("fuck");
        words.badWords.add("olo");
    }

    @RequestMapping(value="/addBad", method= RequestMethod.POST)
    public ArrayList<String> addBadWord(@RequestParam("word") String s) {
        words.badWords.add(s);
        return words.badWords;
    }
    @RequestMapping(value="/delBad/{word}", method= RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s) {
        words.badWords.remove(s);
        return words.badWords;
    }
    @RequestMapping(value="/addGood", method= RequestMethod.POST)
    public ArrayList<String> addGoodWord(@RequestParam("word") String s) {
        words.goodWords.add(s);
        return words.goodWords;
    }
    @RequestMapping(value="/delGood/{word}", method= RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s) {
        words.goodWords.remove(s);
        return words.goodWords;
    }
    @RequestMapping(value="/proof", method= RequestMethod.POST)
    public String proofSentence(@RequestParam("sentence") String s) {
        boolean haveGood = false;
        boolean haveBad = false;
        for (String word : words.goodWords) {
            if (s.contains(word)) {
                haveGood = true;
            }
        }
        for (String word : words.badWords) {
            if (s.contains(word)) {
                haveBad = true;
            }
        }
        if (haveGood && haveBad) {
            rabbit.convertAndSend("Fanout", "", s);
            return "Found good and bad word";
        } else if (haveBad) {
            rabbit.convertAndSend("Direct", "bad", s);
            return "Found bad word";
        } else if (haveGood) {
            rabbit.convertAndSend("Direct", "good", s);
            return "Found good word";
        }
        return null;

    }

    @RequestMapping(value="/getSentence", method=RequestMethod.GET)
    public Sentence getSentence(){
        Object str = rabbit
                .convertSendAndReceive("Direct", "get","");
        return (Sentence) str;
    }
}
