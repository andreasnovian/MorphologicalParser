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

    private final ArrayList<String> tempResult;

    public Parser() throws IOException {
        this.lexicon = new Lexicon();
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
     * Check each component in tempResult, cross check with lexicon, delete from
     * tempResult if not valid based on lexicon
     *
     * @throws IOException
     */
    private void componentValidator() throws IOException {
        String rootWord;
        String component;
        String line;
        boolean valid;
        String[] words;

        for (int i = 0; i < this.tempResult.size(); i++) {
            component = "";
            line = this.tempResult.get(i);
            words = line.split("\\+");
            rootWord = words[0];
            for (int j = 1; j < words.length; j++) {
                if (words[j].charAt(0) != '$') {
                    component += words[j] + "+";
                }
            }

            if (!component.equalsIgnoreCase("")) {
                component = component.substring(0, component.length() - 1);
                valid = this.lexicon.searchInFile(rootWord, component);
                if (!valid) {
                    this.tempResult.remove(line);
                    i--;
                }
            }
        }
    }

    /**
     * To convert from String "malu+#per-kan+[me+$mu" to "Prefiks [me] + Bentuk
     * Dasar [malu] + Konfiks [per-kan] + Klitika [mu]"
     */
    private void convertToWord() {
        String rootWord;
        String prefiks, sufiks, konfiks, klitika;
        String line, result;
        String[] words;
        String[] temp;

        for (int i = 0; i < this.tempResult.size(); i++) {
            prefiks = "";
            sufiks = "";
            konfiks = "";
            klitika = "";
            result = "";
            line = this.tempResult.get(i);
            words = line.split("\\+");
            rootWord = words[0];
            for (String word : words) {
                switch (word.charAt(0)) {
                    case '[':
                        prefiks += word.substring(1) + "+";
                        break;
                    case ']':
                        sufiks += word.substring(1) + "+";
                        break;
                    case '#':
                        konfiks += word.substring(1) + "+";
                        break;
                    case '$':
                        klitika += word.substring(1) + "+";
                        break;
                    default:
                        break;
                }
            }
            if (!prefiks.equalsIgnoreCase("")) {
                prefiks = prefiks.substring(0, prefiks.length() - 1);
                temp = prefiks.split("\\+");
                for (String word : temp) {
                    result = "Prefiks [" + word + "] + " + result;
                }
            }

            if (rootWord.charAt(0) == '!') {
                result += "Bentuk Asing [" + rootWord.substring(1) + "] + ";
            } else {
                result += "Bentuk Dasar [" + rootWord + "] + ";
            }

            if (!sufiks.equalsIgnoreCase("")) {
                sufiks = sufiks.substring(0, sufiks.length() - 1);
                temp = sufiks.split("\\+");
                for (String word : temp) {
                    result += "Sufiks [" + word + "] + ";
                }
            }
            if (!konfiks.equalsIgnoreCase("")) {
                konfiks = konfiks.substring(0, konfiks.length() - 1);
                temp = konfiks.split("\\+");
                for (String word : temp) {
                    result += "Konfiks [" + word + "] + ";
                }
            }
            if (!klitika.equalsIgnoreCase("")) {
                klitika = klitika.substring(0, klitika.length() - 1);
                temp = klitika.split("\\+");
                for (String word : temp) {
                    result += "Klitika [" + word + "] + ";
                }
            }

            result = result.substring(0, result.length() - 3);
            this.tempResult.remove(i);
            this.tempResult.add(i, result);
        }
    }

    /**
     * Method to do parsing process a line of text by divide them into each word
     * separated by space character, then parse each word
     *
     * @param text line of text to parse
     * @return a list of all the possible parse of each word in text
     *
     * @throws java.io.IOException
     */
    public String process(String text) throws IOException {
        String result = "";
        String word[] = text.split(" ");

        for (String word1 : word) {
            this.tempResult.clear();
            parse(word1.toLowerCase());
            //this.componentValidator();
            this.convertToWord();
            System.out.println(word1.toUpperCase() + ": ");
            if (this.tempResult.isEmpty()) {
                System.out.println("Bentuk Asing [" + word1 + "];");
            } else {
                for (int i = 0; i < this.tempResult.size(); i++) {
                    System.out.println(this.tempResult.get(i) + ";");
                }
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
        boolean isAWord = true;
        for (int i = 0; i < word.length(); i++) {
            int c = (int) word.charAt(i);
            if (c < 97 || c > 122) {
                isAWord = false;
            }
        }

        if (isAWord) {
            //if word is found in lexicon
            if (isRootWord(word)) {
                this.tempResult.add(word);
            }

            //afixed word must be 3 or more letters
            if (word.length() > 2) {
                prefiksCheck(word);

                //sufix only check
                if (this.tempResult.isEmpty()) {
                    sufiksCheck(word, "");
                }

                //if word is a reduplication
                if (word.contains("-")) {

                }
            }
        }

        if (this.tempResult.isEmpty()) {
            this.tempResult.add("!" + word);
        }
    }

    private void prefiksCheck(String word) {
        String c2 = word.substring(0, 2);
        String c3 = word.substring(0, 3);

        String w2 = word.substring(2);
        String w3 = word.substring(3);

        //prefix check
        if (c2.equalsIgnoreCase("be")) {
            prefiksBer(w2);
        } else if (c2.equalsIgnoreCase("me")) {
            prefiksMe(w2);
        } else if (c2.equalsIgnoreCase("di")) {
            prefiksDi(w2);
        } else if (c2.equalsIgnoreCase("ke")) {
            prefiksKe(w2);
        } else if (c2.equalsIgnoreCase("ku")) {
            prefiksKu(w2);
        } else if (c2.equalsIgnoreCase("se")) {
            prefiksSe(w2);
        } else if (c2.equalsIgnoreCase("pe")) {
            prefiksPe(w2);
        } else if (c2.equalsIgnoreCase("te")) {
            prefiksTer(w2);
        } else if (c3.equalsIgnoreCase("per")) {
            prefiksPer(w3);
        } else if (c3.equalsIgnoreCase("kau")) {
            prefiksKau(w3);
        }
    }

    private void sufiksCheck(String word, String prevResult) {
        String temp;

        if (word.length() > 2) {
            String c3 = word.substring(word.length() - 3);
            String w3 = word.substring(0, word.length() - 3);

            if (c3.equalsIgnoreCase("kan")) {
                temp = sufiksKan(w3);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w3 + temp + prevResult;
                    this.tempResult.add(temp);
                }
            }
            if (c3.equalsIgnoreCase("nya")) {
                temp = sufiksNya(w3);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w3 + temp;
                    this.tempResult.add(temp);
                }
                sufiksCheck(w3, "+$nya");
            }
            if (c3.equalsIgnoreCase("lah")) {
                temp = sufiksLah(w3);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w3 + temp;
                    this.tempResult.add(temp);
                }
                sufiksCheck(w3, "+$lah");
            }
        }
        if (word.length() > 1) {
            String c2 = word.substring(word.length() - 2);
            String w2 = word.substring(0, word.length() - 2);

            if (c2.equalsIgnoreCase("an")) {
                temp = sufiksAn(w2);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w2 + temp + prevResult;
                    this.tempResult.add(temp);
                }
            }
            if (c2.equalsIgnoreCase("ku")) {
                temp = sufiksKu(w2);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w2 + temp;
                    this.tempResult.add(temp);
                }
                sufiksCheck(w2, "+$ku");
            }
            if (c2.equalsIgnoreCase("mu")) {
                temp = sufiksMu(w2);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w2 + temp;
                    this.tempResult.add(temp);
                }
                sufiksCheck(w2, "+$mu");
            }
        }
        if (word.length() > 0) {
            String c1 = word.substring(word.length() - 1);
            String w1 = word.substring(0, word.length() - 1);

            if (c1.equalsIgnoreCase("i")) {
                temp = sufiksI(w1);
                if (!temp.equalsIgnoreCase("")) {
                    temp = w1 + temp + prevResult;
                    this.tempResult.add(temp);
                }
            }
        }
    }

    private void prefiksBer(String word) {
        String temp;

        //ex. beragam
        if (isRootWord(word)) {
            this.tempResult.add(word + "+[ber");
        }

        temp = word.substring(1);
        //ex. beranak, belajar
        if (isRootWord(temp)) {
            this.tempResult.add(temp + "+[ber");
        }

        if (word.substring(word.length() - 3).equalsIgnoreCase("kan")) {
            //ex. be + rootWord + kan
            temp = sufiksKan(word.substring(0, word.length() - 3));
            if (!temp.equalsIgnoreCase("")) {
                //check lexicon component for either prefiks only and sufiks only, if either valid, it's klofiks, otherwise it's konfiks
                this.tempResult.add(word.substring(0, word.length() - 3) + "+[ber" + temp);
            }
            //ex. ber + rootWord + kan
            temp = sufiksKan(word.substring(1, word.length() - 3));
            if (!temp.equalsIgnoreCase("")) {
                //check lexicon component for either prefiks only and sufiks only, if either valid, it's klofiks, otherwise it's konfiks
                this.tempResult.add(word.substring(1, word.length() - 3) + "+[ber" + temp);
            }

        }
        if (word.substring(word.length() - 2).equalsIgnoreCase("an")) {
            //ex. be + rootWord + an
            temp = sufiksAn(word.substring(0, word.length() - 2));
            if (!temp.equalsIgnoreCase("")) {
                //check lexicon component for either prefiks only and sufiks only, if either valid, it's klofiks, otherwise it's konfiks
                this.tempResult.add(word.substring(0, word.length() - 2) + "+[ber" + temp);
            }
            //ex. ber + rootWord + an
            temp = sufiksAn(word.substring(1, word.length() - 2));
            if (!temp.equalsIgnoreCase("")) {
                //check lexicon component for either prefiks only and sufiks only, if either valid, it's klofiks, otherwise it's konfiks
                this.tempResult.add(word.substring(1, word.length() - 2) + "+[ber" + temp);
            }

        }
        if (word.substring(word.length() - 1).equalsIgnoreCase("i")) {
            //ex. be + rootWord + i
            temp = sufiksI(word.substring(0, word.length() - 1));
            if (!temp.equalsIgnoreCase("")) {
                //check lexicon component for either prefiks only and sufiks only, if either valid, it's klofiks, otherwise it's konfiks
                this.tempResult.add(word.substring(0, word.length() - 1) + "+[ber" + temp);
            }
            //ex. ber + rootWord + i
            temp = sufiksI(word.substring(1, word.length() - 1));
            if (!temp.equalsIgnoreCase("")) {
                //check lexicon component for either prefiks only and sufiks only, if either valid, it's klofiks, otherwise it's konfiks
                this.tempResult.add(word.substring(1, word.length() - 1) + "+[ber" + temp);
            }
        }
    }

    private void prefiksMe(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[me";
        }
    }

    private void prefiksDi(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[di";
        }
    }

    private void prefiksKe(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[ke";
        }
    }

    private void prefiksKu(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+$ku";
        }
    }

    private void prefiksSe(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[se";
        }
    }

    private void prefiksPe(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[pe";
        }
    }

    private void prefiksPer(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[per";
        }
    }

    private void prefiksTer(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+[ter";
        }
    }

    private void prefiksKau(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+$kau";
        }
    }

    private String sufiksKan(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+]kan";
        }

        return result;
    }

    private String sufiksAn(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+]an";
        }

        return result;
    }

    private String sufiksI(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+]i";
        }

        return result;
    }

    private String sufiksKu(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+$ku";
        }

        return result;
    }

    private String sufiksMu(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+$mu";
        }

        return result;
    }

    private String sufiksNya(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+$nya";
        }

        return result;
    }

    private String sufiksLah(String word) {
        String result = "";

        if (isRootWord(word)) {
            result = "+$lah";
        }

        return result;
    }
}
