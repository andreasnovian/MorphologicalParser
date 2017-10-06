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

    private String rootWord;

    private final ArrayList<String> tempResult;

    public Parser() throws IOException {
        this.lexicon = new Lexicon();
        this.rootWord = "";
        this.tempResult = new ArrayList<>();
    }

    /**
     * Method to search whether a word is a valid root word in lexicon tree or
     * not
     *
     * @param word a word to check
     * @return true if and only if word is a valid root word in lexicon tree
     */
    private boolean isRootWord(String word) {
        return lexicon.searchInTree(word.toLowerCase());
    }

    /**
     * Method to do parsing process a line of text by divide them into each word
     * separated by space character, then parse each word
     *
     * @param text line of text to parse
     * @return a list of all the possible parse of each word in text
     */
    public String process(String text) {
        String result = "";
        String word[] = text.split(" ");

        for (String word1 : word) {
            this.tempResult.clear();
            parse(word1.toLowerCase());
            System.out.println(word1.toUpperCase() + ": ");
            for (int i = 0; i < this.tempResult.size(); i++) {
                System.out.println(this.tempResult.get(i) + ";");
            }
            System.out.println("");
        }

        return result;
    }

    /**
     * Method to perform parse operation of a word
     *
     * @param word word to parse
     */
    private void parse(String word) {
        String temp;
        boolean isAWord = true;
        boolean haveResult = false;
        for (int i = 0; i < word.length(); i++) {
            int c = (int) word.charAt(i);
            if (c < 97 || c > 122) {
                isAWord = false;
            }
        }

        if (isAWord) {
            //if word is found in lexicon
            if (isRootWord(word)) {
                this.rootWord = word;
                this.tempResult.add("Bentuk Dasar [" + word + "]");
                haveResult = true;
            }

            //afixed word must be 3 or more letters
            if (word.length() > 2) {
                String c2 = word.substring(0, 2);
                String c3 = word.substring(0, 3);

                String w2 = word.substring(2);
                String w3 = word.substring(3);

                //prefix check
                if (c2.equalsIgnoreCase("be")) {
                    haveResult = prefiksBer(w2);
                } else if (c2.equalsIgnoreCase("me")) {
                    haveResult = prefiksMe(w2);
                } else if (c2.equalsIgnoreCase("di")) {
                    haveResult = prefiksDi(w2);
                } else if (c2.equalsIgnoreCase("ke")) {
                    haveResult = prefiksKe(w2);
                } else if (c2.equalsIgnoreCase("ku")) {
                    haveResult = prefiksKu(w2);
                } else if (c2.equalsIgnoreCase("se")) {
                    haveResult = prefiksSe(w2);
                } else if (c2.equalsIgnoreCase("pe")) {
                    haveResult = prefiksPe(w2);
                } else if (c2.equalsIgnoreCase("te")) {
                    haveResult = prefiksTer(w2);
                } else if (c3.equalsIgnoreCase("per")) {
                    haveResult = prefiksPer(w3);
                } else if (c3.equalsIgnoreCase("kau")) {
                    haveResult = prefiksKau(w3);
                }

                //sufix check
                if (!haveResult) {
                    if (word.substring(word.length() - 3).equalsIgnoreCase("kan")) {
                        temp = word.substring(0, word.length() - 3);
                        if (sufiksKan(temp)) {
                            this.tempResult.add("Bentuk Dasar [" + temp + "]" + " + Sufiks [kan]");
                            haveResult = true;
                        }
                    }
                    if (word.substring(word.length() - 2).equalsIgnoreCase("an")) {
                        temp = word.substring(0, word.length() - 2);
                        if (sufiksAn(temp)) {
                            this.tempResult.add("Bentuk Dasar [" + temp + "]" + " + Sufiks [an]");
                            haveResult = true;
                        }
                    }
                    if (word.substring(word.length() - 1).equalsIgnoreCase("i")) {
                        temp = word.substring(0, word.length() - 1);
                        if (sufiksI(temp)) {
                            this.tempResult.add("Bentuk Dasar [" + temp + "]" + " + Sufiks [i]");
                            haveResult = true;
                        }
                    }
                }

                //if word is a reduplication
                if (word.contains("-")) {

                }
            }
        }
        if (!haveResult) {
            this.tempResult.add("Bentuk Asing [" + word + "]");
        }
    }

    private boolean prefiksBer(String word) {
        String temp;
        boolean haveResult = false;

        //ex. beragam
        if (isRootWord(word)) {
            this.rootWord = word;
            this.tempResult.add("Prefiks [ber] + Bentuk Dasar [" + word + "]");
            haveResult = true;
        }

        temp = word.substring(1);
        //ex. beranak, belajar
        if (isRootWord(temp)) {
            this.rootWord = temp;
            this.tempResult.add("Prefiks [ber] + Bentuk Dasar [" + temp + "]");
            haveResult = true;
        }

        boolean sufiksStatus = false;

        if (word.substring(word.length() - 3).equalsIgnoreCase("kan")) {
            temp = word.substring(0, word.length() - 3);
            sufiksStatus = sufiksKan(temp);
            if (sufiksStatus) {
                this.tempResult.add("Prefiks [ber] + Bentuk Dasar [" + temp + "]" + " + Sufiks [kan]");
                haveResult = sufiksStatus;
            }
        }
        if (word.substring(word.length() - 2).equalsIgnoreCase("an")) {
            temp = word.substring(0, word.length() - 2);
            sufiksStatus = sufiksAn(temp);
            if (sufiksStatus) {
                this.tempResult.add("Prefiks [ber] + Bentuk Dasar [" + temp + "]" + " + Sufiks [an]");
                haveResult = sufiksStatus;
            }
        }
        if (word.substring(word.length() - 1).equalsIgnoreCase("i")) {
            temp = word.substring(0, word.length() - 1);
            sufiksStatus = sufiksI(temp);
            if (sufiksStatus) {
                this.tempResult.add("Prefiks [ber] + Bentuk Dasar [" + temp + "]" + " + Sufiks [i]");
                haveResult = sufiksStatus;
            }
        }

        if (sufiksStatus) {
            //check konfiks or klofiks
        }

        return haveResult;
    }

    private boolean prefiksMe(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksDi(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksKe(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksKu(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksSe(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksPe(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksPer(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksTer(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    private boolean prefiksKau(String word) {
        boolean haveResult = false;

        return haveResult;
    }

    //true if valid sufiks appear
    private boolean sufiksKan(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }

    private boolean sufiksAn(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }

    private boolean sufiksI(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }

    private boolean sufiksKu(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }

    private boolean sufiksMu(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }

    private boolean sufiksNya(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }

    private boolean sufiksLah(String word) {
        boolean haveResult = false;

        if (isRootWord(word)) {
            haveResult = true;
        }

        return haveResult;
    }
}
