package com.company;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        String filePath = "src/com/company/text_1";
        String text = new String ( Files.readAllBytes( Paths.get(filePath) ) );

        char[] textChar = getChar(hexToAscii(text));
        for (int i = 0; i < 256; i++) {
            System.out.println("Key = " + (char)i);
            singleByteXor(textChar,(char)i);
            System.out.println(" ");
        }
    }

    public static String hexToAscii(String hexText){
        StringBuilder newStr = new StringBuilder("");

        for (int i = 0; i < hexText.length(); i+=2) {
            String temp = hexText.substring(i, i+2);
            newStr.append((char)Integer.parseInt(temp,16));
        }

        return  newStr.toString();
    }

    public static void singleByteXor(char[] text, char key){

        char[] output = new char[text.length];
        for (int i = 0; i < text.length; i++) {
            output[i] = (char)(text[i]^key);
            System.out.print(output[i]);
        }

    }
     public static char[] getChar (String text){
         char[] chars = new char[text.length()];
         for (int i = 0; i < text.length(); i++) {
             chars[i] = text.charAt(i);
         }
         return chars;
     }



}
