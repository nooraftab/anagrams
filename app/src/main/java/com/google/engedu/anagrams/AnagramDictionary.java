/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static final String TAG = "Noor";
    private int wordLength = DEFAULT_WORD_LENGTH;

    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {

        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {

            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            String sl = sortLetters(word);
            int wl = word.length();

            if (lettersToWord.containsKey(sl)) {

                // get the array list, add
                lettersToWord.get(sl).add(word);

            } else {

                //make key, initialize arraylist
                lettersToWord.put(sl, new ArrayList<String>());
                //add word itself
                lettersToWord.get(sl).add(word);

            }

            if (sizeToWords.containsKey(wl)) {

                //get list, add
                sizeToWords.get(wl).add(word);

            } else {

                //create key with that length
                sizeToWords.put(wl, new ArrayList<String>());
                sizeToWords.get(wl).add(word);  //add word itself

            }

        }

    }

    private String sortLetters(String word) {

        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);

    }

    public boolean isGoodWord(String word, String base) {

        if (wordSet.contains(word)) {
            if (!word.contains(base)) {
                return true;
            }
        }

        return false;

    }

    public List<String> getAnagrams(String targetWord) {

        //ensure target word exists in the hashmap at all
        if (lettersToWord.containsKey(sortLetters(targetWord))) {
            Log.i( TAG, targetWord +  "");
            return lettersToWord.get(sortLetters(targetWord));
        }
        return null;

    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {

        List<String> result = new ArrayList<>();

        for(char i = 'a'; i <= 'z'; i++) {

            String extraLetter = word + i;
            List<String> anagrams = getAnagrams(extraLetter);

            //safety check
            if (anagrams != null) {
                for (String s : anagrams) {     //for each anagram in the list
                    //ensure it is a 'good' word
                    if (isGoodWord(s, word)) {
                        result.add(s);
                    }
                }
            }
        }

        return result;

    }

    public String pickGoodStarterWord() {

        String s = getAWord();

        while (getAnagramsWithOneMoreLetter(s).size() < MIN_NUM_ANAGRAMS || !sizeToWords.get(wordLength).contains(s)) {
            s = getAWord();
        }

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        return s;

    }

    private String getAWord() {

        int index = random.nextInt(wordSet.size());

        int currIndex = 0;
        String currWord = "";

        Iterator<String> t = wordSet.iterator();

        while (currIndex < index && t.hasNext()) {

            currWord = t.next();
            currIndex++;

        }

        return currWord;

    }
}
