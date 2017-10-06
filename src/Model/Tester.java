/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andreas Novian
 */
public class Tester {

    public static void main(String[] args) throws IOException {
        Parser p = new Parser();
        
//        System.out.println(p.convertToWord("punya", "#me-i"));

        System.out.print(p.process("berakitan"));
        System.out.print(p.process("berakitkan"));
        System.out.print(p.process("berakiti"));

//        MorphologicalParser mp = new MorphologicalParser();
//        String text = "Andreas sedang melakukan tari-menari";
//        String word[] = text.split(" ");
//        ArrayList<String> parseResult;
//
//        for (String i : word) {
//            parseResult = mp.getMorphologic(i);
//            System.out.println(i + ": "+ parseResult.toString());
//            System.out.println("");
//        }
    }
}
