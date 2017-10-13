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

    private final ArrayList<String> parseResult;

    public Parser() throws IOException {
        this.lexicon = new Lexicon();
        this.parseResult = new ArrayList<>();
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

        for (int i = 0; i < this.parseResult.size(); i++) {
            component = "";
            line = this.parseResult.get(i);
            words = line.split("\\+");
            rootWord = words[0];
            for (int j = 1; j < words.length; j++) {
                if (words[j].charAt(0) != '$' && words[j].charAt(0) != '%') {
                    component += words[j] + "+";
                }
            }

            if (!component.equalsIgnoreCase("")) {
                component = component.substring(0, component.length() - 1);
                if (isRootWord(rootWord)) {
                    valid = this.lexicon.searchInFile(rootWord, component);
                    if (!valid) {
                        this.parseResult.remove(line);
                        i--;
                    }
                } else {
                    this.parseResult.remove(line);
                    i--;
                }
            }
        }
    }

    /**
     * To convert from String "malu+#per-kan+[me+$mu" to "Prefiks [me] + Bentuk
     * Dasar [malu] + Konfiks [per-kan] + Klitika [mu]"
     */
    private void convertToWord() throws IOException {
        String rootWord;
        String preKomposisi, postKomposisi, reduplikasi, prefiks, sufiks, konfiks, proklitika, enklitika;
        String line, result;
        String[] words;
        String[] temp;

        for (int i = 0; i < this.parseResult.size(); i++) {
            preKomposisi = "";
            postKomposisi = "";
            reduplikasi = "";
            prefiks = "";
            sufiks = "";
            konfiks = "";
            proklitika = "";
            enklitika = "";
            result = "";

            line = this.parseResult.get(i);
            if (line.contains("^(")) {
                reduplikasi = line.substring(line.indexOf("^") + 1, line.indexOf(")") + 1);
                line = line.replace("+^" + reduplikasi, "");
                reduplikasi += "+";
            }

            words = line.split("\\+");
            rootWord = words[0];
            for (String word : words) {
                switch (word.charAt(0)) {
                    case '@':
                        preKomposisi += word.substring(1) + "+";
                        break;
                    case '^':
                        reduplikasi += word.substring(1) + "+";
                        break;
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
                        proklitika += word.substring(1) + "+";
                        break;
                    case '%':
                        enklitika += word.substring(1) + "+";
                        break;
                    case '&':
                        postKomposisi += word.substring(1) + "+";
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
            if (!preKomposisi.equalsIgnoreCase("")) {
                preKomposisi = preKomposisi.substring(0, preKomposisi.length() - 1);
                temp = preKomposisi.split("\\+");
                for (String word : temp) {
                    result += "Komposisi [" + word + "] + ";
                }
            }
            if (!reduplikasi.equalsIgnoreCase("")) {
                reduplikasi = reduplikasi.substring(0, reduplikasi.length() - 1);
                if (reduplikasi.charAt(0) == '(') {
                    reduplikasi = reduplikasi.substring(1, reduplikasi.length() - 1);
                }
                result += "Reduplikasi [" + reduplikasi + "] + ";
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
            if (!proklitika.equalsIgnoreCase("")) {
                proklitika = proklitika.substring(0, proklitika.length() - 1);
                temp = proklitika.split("\\+");
                for (String word : temp) {
                    result += "Proklitika [" + word + "] + ";
                }
            }
            if (!enklitika.equalsIgnoreCase("")) {
                enklitika = enklitika.substring(0, enklitika.length() - 1);
                temp = enklitika.split("\\+");
                for (String word : temp) {
                    result += "Enklitika [" + word + "] + ";
                }
            }
            if (!postKomposisi.equalsIgnoreCase("")) {
                postKomposisi = postKomposisi.substring(0, postKomposisi.length() - 1);
                result += "Komposisi [" + postKomposisi + "] + ";
            }

            result = result.substring(0, result.length() - 3);
            this.parseResult.remove(i);
            this.parseResult.add(i, result);
        }
    }

    private void removeDuplicateResult() {
        String l1, l2;

        //remove same item
        for (int i = 0; i < this.parseResult.size(); i++) {
            l1 = this.parseResult.get(i);
            for (int j = i + 1; j < this.parseResult.size(); j++) {
                l2 = this.parseResult.get(j);
                if (l1.equalsIgnoreCase(l2)) {
                    this.parseResult.remove(j);
                    if (i > 0) {
                        i--;
                    }
                    j--;
                }
            }
        }
    }

    /**
     * Normalize text before entering parse process
     *
     * @param text
     * @return String[] containing each word in text but in lowercase, and only
     * character a..z, 0..9, and - allowed
     */
    private String[] normalizeInput(String text) {
        String[] tempArray;
        String tempWord;
        String input = "";
        String[] words;
        char c;

        text = text.toLowerCase();
        tempArray = text.split("\\s");
        for (String word : tempArray) {
            if (!word.equalsIgnoreCase("")) {
                tempWord = "";
                for (int i = 0; i < word.length(); i++) {
                    c = word.charAt(i);
                    //a..z
                    if (c >= 97 && c <= 122) {
                        tempWord += (char) c;
                    }
                    //0..9
                    if (c >= 48 && c <= 57) {
                        tempWord += (char) c;
                    }
                    //symbol - 
                    if (c == 45) {
                        tempWord += (char) c;
                    }
                }
                if (!tempWord.equalsIgnoreCase("")) {
                    input += tempWord + " ";
                }
            }
        }
        input = input.trim();
        words = input.split(" ");
        return words;
    }

    /**
     * Method to do parsing process a line of text by divide them into each word
     * separated by space character, then parse each word
     *
     * @param text line of text to parse
     * @param validator
     * @param converter
     * @return a list of all the possible parse of each word in text
     *
     * @throws java.io.IOException
     */
    public String process(String text, boolean validator, boolean converter) throws IOException {
        String result = "";
        String words[] = normalizeInput(text);
        String word;
        ArrayList<String> oneWord = new ArrayList<>(), twoWord = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            oneWord.clear();
            twoWord.clear();
            this.parseResult.clear();

            word = words[i];
            parse(word.toLowerCase());
            if (i < words.length - 1) {
                this.checkKomposisi(words[i + 1]);
            }

            this.removeDuplicateResult();

            if (validator) {
                this.componentValidator();
            }

            if (this.parseResult.isEmpty()) {
                this.parseResult.add("!" + word);
            }

            for (int j = 0; j < this.parseResult.size(); j++) {
                if (this.parseResult.get(j).contains("&")) {
                    twoWord.add(this.parseResult.get(j));
                } else {
                    oneWord.add(this.parseResult.get(j));
                }
            }

            this.parseResult.clear();
            this.parseResult.addAll(oneWord);
            this.parseResult.addAll(twoWord);

            if (converter) {
                this.convertToWord();
            }

            if (!oneWord.isEmpty()) {
                result += word.toUpperCase() + ":\n";
                for (int j = 0; j < oneWord.size(); j++) {
                    result += this.parseResult.get(j) + ";\n";
                }
                result += "\n";
            }
            if (!twoWord.isEmpty()) {
                result += word.toUpperCase() + " " + words[i + 1].toUpperCase() + ":\n";
                for (int j = oneWord.size(); j < this.parseResult.size(); j++) {
                    result += this.parseResult.get(j) + ";\n";
                }
                result += "\n";
            }
        }
        result = result.trim();
        return result;
    }

    /**
     * Method to perform parse operation of a word
     *
     * @param word word to parse
     */
    private ArrayList<String> parse(String word) throws IOException {
        boolean isAWord = true;
        for (int i = 0; i < word.length(); i++) {
            int c = (int) word.charAt(i);
            if (c < 97 || c > 122) {
                isAWord = false;
            }
            if (c == 45) {
                isAWord = true;
            }
        }

        if (isAWord) {
            this.check(word, "", "", "", "");
        }

        if (this.parseResult.isEmpty()) {
            this.parseResult.add("!" + word);
        }

        ArrayList<String> result = this.parseResult;
        return result;
    }

    /**
     * Check all the possible prefiks, including klitika, even when combined
     *
     * @param word word to check
     * @param klitika any klitika found
     * @param prefiks any prefiks found previously
     */
    private void checkPrefiks(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        //two letters prefiks
        if (word.length() > 2) {
            String c2 = word.substring(0, 2);
            String w2 = word.substring(2);

            if (c2.equalsIgnoreCase("be")) {
                prefiksBer(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("me")) {
                prefiksMe(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("di")) {
                prefiksDi(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("ke")) {
                prefiksKe(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("ku")) {
                prefiksKu(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("se")) {
                prefiksSe(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("pe")) {
                prefiksPe(w2, prefiks, sufiks, klitika, duplikasi);
            } else if (c2.equalsIgnoreCase("te")) {
                prefiksTer(w2, prefiks, sufiks, klitika, duplikasi);
            }
        }

        //three letters prefiks
        if (word.length() > 3) {
            String c3 = word.substring(0, 3);
            String w3 = word.substring(3);

            if (c3.equalsIgnoreCase("per") || c3.equalsIgnoreCase("pel")) {
                prefiksPer(w3, prefiks, sufiks, klitika, duplikasi);
            } else if (c3.equalsIgnoreCase("kau")) {
                prefiksKau(w3, prefiks, sufiks, klitika, duplikasi);
            }
        }
    }

    /**
     * Check all the possible sufiks, including klitika, even when combined ex.
     * makananmu
     *
     * @param word word to check
     * @param klitika any klitika found
     * @param prefiks any prefiks found previously
     */
    private void checkSufiks(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        if (word.length() > 2) {
            String c3 = word.substring(word.length() - 3);
            String w3 = word.substring(0, word.length() - 3);

            if (c3.equalsIgnoreCase("kan")) {
                sufiksKan(w3, prefiks, sufiks, klitika, duplikasi);
            }
            if (c3.equalsIgnoreCase("nya")) {
                sufiksNya(w3, prefiks, sufiks, klitika, duplikasi);
            }
            if (c3.equalsIgnoreCase("lah")) {
                sufiksLah(w3, prefiks, sufiks, klitika, duplikasi);
            }
            if (c3.equalsIgnoreCase("pun")) {
                sufiksPun(w3, prefiks, sufiks, klitika, duplikasi);
            }
            if (c3.equalsIgnoreCase("kah")) {
                sufiksKah(w3, prefiks, sufiks, klitika, duplikasi);
            }
        }
        if (word.length() > 1) {
            String c2 = word.substring(word.length() - 2);
            String w2 = word.substring(0, word.length() - 2);

            if (c2.equalsIgnoreCase("an")) {
                sufiksAn(w2, prefiks, sufiks, klitika, duplikasi);
            }
            if (c2.equalsIgnoreCase("ku")) {
                sufiksKu(w2, prefiks, sufiks, klitika, duplikasi);
            }
            if (c2.equalsIgnoreCase("mu")) {
                sufiksMu(w2, prefiks, sufiks, klitika, duplikasi);
            }
        }
        if (word.length() > 0) {
            String c1 = word.substring(word.length() - 1);
            String w1 = word.substring(0, word.length() - 1);

            if (c1.equalsIgnoreCase("i")) {
                sufiksI(w1, prefiks, sufiks, klitika, duplikasi);
            }
        }
    }

    /**
     * Check if word is reduplication or not based on the present of "-"
     *
     * @param word word to check
     * @param klitika any klitika found
     * @param prefiks any prefiks found previously
     * @throws IOException
     */
    private void checkRedup(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (word.contains("-")) {
            String[] words = word.split("-");

            if (words[0].equalsIgnoreCase(words[1])) {
                if (isRootWord(words[0])) {
                    temp = words[0] + prefiks + sufiks + klitika + duplikasi + "+^2";
                    this.parseResult.add(temp);
                }
                this.check(words[0], prefiks, sufiks, klitika, duplikasi + "+^2");
            } else {
                if (isRootWord(words[0])) {
                    temp = words[0] + prefiks + sufiks + klitika + duplikasi + "+^" + words[1];
                    this.parseResult.add(temp);
                }

                //to do parse on the second word and check each word on the result
                ArrayList<String> list = new Parser().parse(words[1]);
                for (String w : list) {
                    if (w.charAt(0) != '!') {
                        this.check(words[0], prefiks, sufiks, klitika, duplikasi + "+^(" + w + ")");
                    }
                }
            }
        }
    }

    private void check(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        //if word is found in lexicon
        if (isRootWord(word)) {
            this.parseResult.add(word + prefiks + sufiks + klitika + duplikasi);
        }
        //afixed word must be 3 or more letters
        if (word.length() > 3) {
            //prefiks check, including sufiks check
            checkPrefiks(word, prefiks, sufiks, klitika, duplikasi);

            //only sufiks check
            checkSufiks(word, prefiks, sufiks, klitika, duplikasi);

            //reduplication check
            checkRedup(word, prefiks, sufiks, klitika, duplikasi);
        }
    }

    private void prefiksBer(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        //ex. beragam
        this.check(word, "+[ber" + prefiks, sufiks, klitika, duplikasi);

        if (word.length() > 3) {
            if (word.charAt(0) == 'r' || word.charAt(0) == 'l') {
                //ex. beranak, belajar
                word = word.substring(1);
                this.check(word, "+[ber" + prefiks, sufiks, klitika, duplikasi);
            }
        }
    }

    private void prefiksMe(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        //me..
        this.check(word, "+[me" + prefiks, sufiks, klitika, duplikasi);

        if (word.length() > 3) {
            //mem..;
            if (word.charAt(0) == 'm') {
                word = word.substring(1);
                this.check(word, "+[me" + prefiks, sufiks, klitika, duplikasi);
                this.check("p" + word, "+[me" + prefiks, sufiks, klitika, duplikasi);
            }

            //men..;
            if (word.charAt(0) == 'n') {
                word = word.substring(1);
                this.check(word, "+[me" + prefiks, sufiks, klitika, duplikasi);
                this.check("t" + word, "+[me" + prefiks, sufiks, klitika, duplikasi);

                if (word.length() > 3) {
                    //meng..;
                    if (word.charAt(0) == 'g') {
                        word = word.substring(1);
                        this.check(word, "+[me" + prefiks, sufiks, klitika, duplikasi);
                        this.check("k" + word, "+[me" + prefiks, sufiks, klitika, duplikasi);

                        if (word.length() > 3) {
                            //menge..
                            if (word.charAt(0) == 'e') {
                                word = word.substring(1);
                                this.check(word, "+[me" + prefiks, sufiks, klitika, duplikasi);
                            }
                        }
                    }
                    //meny..;
                    if (word.charAt(0) == 'y') {
                        word = word.substring(1);
                        this.check(word, "+[me" + prefiks, sufiks, klitika, duplikasi);
                        this.check("s" + word, "+[me" + prefiks, sufiks, klitika, duplikasi);
                    }
                }
            }
        }
    }

    private void prefiksDi(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, "+[di" + prefiks, sufiks, klitika, duplikasi);
    }

    private void prefiksKe(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, "+[ke" + prefiks, sufiks, klitika, duplikasi);
    }

    private void prefiksKu(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, prefiks, sufiks, "+$ku" + klitika, duplikasi);
    }

    private void prefiksSe(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, "+[se" + prefiks, sufiks, klitika, duplikasi);
    }

    private void prefiksPe(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, "+[pe" + prefiks, sufiks, klitika, duplikasi);

        if (word.length() > 3) {
            //pem..;
            if (word.charAt(0) == 'm') {
                word = word.substring(1);
                this.check(word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
                this.check("p" + word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
            }

            //pen..;
            if (word.charAt(0) == 'n') {
                word = word.substring(1);
                this.check(word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
                this.check("t" + word, "+[pe" + prefiks, sufiks, klitika, duplikasi);

                if (word.length() > 3) {
                    //peng..;
                    if (word.charAt(0) == 'g') {
                        word = word.substring(1);
                        this.check(word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
                        this.check("k" + word, "+[pe" + prefiks, sufiks, klitika, duplikasi);

                        if (word.length() > 3) {
                            //penge..
                            if (word.charAt(0) == 'e') {
                                word = word.substring(1);
                                this.check(word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
                            }
                        }
                    }
                    //peny..;
                    if (word.charAt(0) == 'y') {
                        word = word.substring(1);
                        this.check(word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
                        this.check("s" + word, "+[pe" + prefiks, sufiks, klitika, duplikasi);
                    }
                }
            }
        }
    }

    private void prefiksPer(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, "+[per" + prefiks, sufiks, klitika, duplikasi);
    }

    private void prefiksTer(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, "+[ter" + prefiks, sufiks, klitika, duplikasi);

        if (word.length() > 3) {
            if (word.charAt(0) == 'r') {
                word = word.substring(1);
                this.check(word, "+[ter" + prefiks, sufiks, klitika, duplikasi);
            }
        }
    }

    private void prefiksKau(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        this.check(word, prefiks, sufiks, "+$kau" + klitika, duplikasi);
    }

    private void sufiksKan(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + "+]kan" + klitika + duplikasi;
            this.parseResult.add(temp);
            this.checkKonfiks();
        }
        this.checkKomposisi(word, prefiks, sufiks + "+]kan", klitika, duplikasi);
        this.checkKonfiks();
        this.check(word, prefiks, sufiks + "+]kan", klitika, duplikasi);
    }

    private void sufiksAn(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + "+]an" + klitika + duplikasi;
            this.parseResult.add(temp);
            this.checkKonfiks();
        }
        this.checkKomposisi(word, prefiks, sufiks + "+]an", klitika, duplikasi);
        this.checkKonfiks();
        this.check(word, prefiks, sufiks + "+]an", klitika, duplikasi);
    }

    private void sufiksI(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + "+]i" + klitika + duplikasi;
            this.parseResult.add(temp);
            this.checkKonfiks();
        }
        this.checkKomposisi(word, prefiks, sufiks + "+]i", klitika, duplikasi);
        this.checkKonfiks();
        this.check(word, prefiks, sufiks + "+]i", klitika, duplikasi);
    }

    private void sufiksKu(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + klitika + "+%ku" + duplikasi;
            this.parseResult.add(temp);
        }
        this.check(word, prefiks, sufiks, klitika + "+%ku", duplikasi);
    }

    private void sufiksMu(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + klitika + "+%mu" + duplikasi;
            this.parseResult.add(temp);
        }
        this.check(word, prefiks, sufiks, klitika + "+%mu", duplikasi);
    }

    private void sufiksNya(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + klitika + "+%nya" + duplikasi;
            this.parseResult.add(temp);
        }
        this.check(word, prefiks, sufiks, klitika + "+%nya", duplikasi);
    }

    private void sufiksLah(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + klitika + "+%lah" + duplikasi;
            this.parseResult.add(temp);
        }
        this.check(word, prefiks, sufiks, klitika + "+%lah", duplikasi);
    }

    private void sufiksPun(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + klitika + "+%pun" + duplikasi;
            this.parseResult.add(temp);
        }
        this.check(word, prefiks, sufiks, klitika + "+%pun", duplikasi);
    }

    private void sufiksKah(String word, String prefiks, String sufiks, String klitika, String duplikasi) throws IOException {
        String temp;
        if (isRootWord(word)) {
            temp = word + prefiks + sufiks + klitika + "+%kah" + duplikasi;
            this.parseResult.add(temp);
        }
        this.check(word, prefiks, sufiks, klitika + "+%kah", duplikasi);
    }

    private void checkKonfiks() {
        String rootWord, component, line;
        for (int i = 0; i < this.parseResult.size(); i++) {
            rootWord = "";
            line = this.parseResult.get(i);
            if (line.contains("+")) {
                for (int j = 0; j < line.indexOf("+"); j++) {
                    rootWord += line.charAt(j);
                }
                component = line.substring(line.indexOf("+") + 1);

                String temp;
                if (component.contains("[ber+]an")) {
                    temp = component.replace("[ber+]an", "#ber-an");
                    this.parseResult.add(rootWord + "+" + temp);
                } else if (component.contains("[ke+]an")) {
                    temp = component.replace("[ke+]an", "#ke-an");
                    this.parseResult.add(rootWord + "+" + temp);
                } else if (component.contains("[pe+]an")) {
                    temp = component.replace("[pe+]an", "#pe-an");
                    this.parseResult.add(rootWord + "+" + temp);
                } else if (component.contains("[per+]an")) {
                    temp = component.replace("[per+]an", "#per-an");
                    this.parseResult.add(rootWord + "+" + temp);
                } else if (component.contains("[se+]nya")) {
                    temp = component.replace("[se+]nya", "#se-nya");
                    this.parseResult.add(rootWord + "+" + temp);
                }
            }
        }
    }

    /**
     * To check if a word is first komposisi then afixed ex. pertanggungjawaban
     *
     * @param word word to check
     * @param klitika any klitika found
     * @param prefiks any prefiks found previously
     */
    private void checkKomposisi(String word, String prefiks, String sufiks, String klitika, String duplikasi) {
        String rootWord = "";
        String temp;
        for (int i = 0; i < word.length() - 1; i++) {
            rootWord += word.charAt(i);
            temp = word.substring(i + 1);
            if (isRootWord(rootWord)) {
                temp = rootWord + "+@" + temp + prefiks + sufiks + klitika + duplikasi;
                if (temp.contains("[") && temp.contains("]") && !temp.contains("-")) {
                    this.parseResult.add(temp);
                }
            }
        }
    }

    /**
     * To check if a word is first afixed then komposisi
     */
    private void checkKomposisi(String nextWord) {
        //this method called in process method after parse finish
        //for all parse result, combine each of them with next word
        //insert all possibility to each parse result
        //run a validity check on lexicon

        String line, newLine;
        int size = this.parseResult.size();
        for (int i = 0; i < size; i++) {
            line = this.parseResult.get(i);
            newLine = line + "+&" + nextWord + "";
            this.parseResult.add(newLine);
        }
    }
}
