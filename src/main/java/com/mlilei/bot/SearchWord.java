package com.mlilei.bot;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Author lilei
 * @Description
 * @Date 2021/2/24 16:31
 */
public class SearchWord {

    public static String randomWord() {
        List<List<String>> allWords = Lists.newArrayList();
        for (int i = 0; i < 9; i++) {
            String word = ConfigHelper.PROPERTIES.getProperty("word" + i);
            if (null == word) {
                break;
            }
            allWords.add(Lists.newArrayList(word.split("\\|")));
        }

        StringBuilder word = new StringBuilder();
        for (final List<String> words : allWords) {
            word.append(words.get(RandomUtils.nextInt(0,words.size())));
        }

        return word.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(SearchWord.randomWord());
        }
    }

}
