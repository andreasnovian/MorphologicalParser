package Model;

import Lexicon.Model.Lexicon;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andreas Novian
 */
public class Parser {

    //an object of a lexicon
    public Lexicon lexicon;

    //to save all the possible result
    public ArrayList<String> result;

    //the root word of currently parsed word
    public String rootWord;

    public Parser() throws IOException {
        this.lexicon = new Lexicon();
    }

    /**
     * Method to search whether a word is a valid root word in lexicon tree or
     * not
     *
     * @param word a word to check
     * @return true if and only if word is a valid root word in lexicon tree
     */
    public boolean isRootWord(String word) {
        return lexicon.searchInTree(word.toLowerCase());
    }

    public ArrayList<String> process(String text) {
        result = new ArrayList<>();
        String word[] = text.split(" ");
        String parseResult;

        for (String i : word) {
            parseResult = parse(i);
            result.add(parseResult);
        }

        return result;
    }

    /**
     * Method to perform parse operation of a word
     *
     * @param word word to parse
     * @return all the possible parse result of the word
     */
    private String parse(String word) {
        String parseResult = "";

        //if word is found in lexicon
        if (isRootWord(word)) {
            parseResult = word;
            this.rootWord = word;
        }

        //if word is a reduplication
        if (word.contains("-")) {

        }

        return parseResult;
    }
}
