import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class VigenereMain {

    public static void main(String[] args) throws IOException {

        String filePath = "Task3/src/text_2";
        String text = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        String input = new String(Base64.getDecoder().decode(text));


        findKeyLength(input);


        String [] arr = devideSubstr(input, 3 );
        for (String s:
             arr) {
            //System.out.println("New line");
            //System.out.println(s);
        }

        for (int j = 0; j < 3; j++) {

            char[] textChar = getChar(arr[j]);
            HashMap<char[], Integer> set = new HashMap<>();

            int max = 0;
            for (int i = 0; i < 256; i++) {

                char[] res = singleByteXor(textChar,(char)i);
                int n = checkASCII(res);
                if (n> max){
                    set.clear();
                    set.put(res, i);
                    max = n;
                } else if (n == max){
                    set.put(res, i);
                }

            }



            System.out.println("************NEW LINE " + j + "************");
            for (char[] c:set.keySet()) {
                System.out.println("Maybe KEY = " + (char)set.get(c).byteValue());
                    System.out.println(c);
                    System.out.println();
            }
        }

        System.out.println(totalXOR(input, "L0l"));

    }

    public static void findKeyLength(String text){
        String output = text;

        HashMap<Integer, Integer> tries = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            output = move(output);
            tries.put(i+1, indexOfCoincidance(text, output));
            System.out.println("Key size = "+ (i+1)+ " Result = " + indexOfCoincidance(text, output));
        }

    }


    public static String totalXOR(String encoded, String key){
        String decoded = "";
        for (int i = 0; i < encoded.length(); i++) {
            decoded += (char)(encoded.charAt(i) ^ key.charAt(i%3));
        }

        return  decoded;
    }
    public static char[] singleByteXor(char[] text, char key){

        char[] output = new char[text.length];
        for (int i = 0; i < text.length; i++) {
            output[i] = (char)(text[i]^key);
        }

        return output;

    }
    public static char[] getChar (String text){
        char[] chars = new char[text.length()];
        for (int i = 0; i < text.length(); i++) {
            chars[i] = text.charAt(i);
        }
        return chars;
    }



    public static String[] devideSubstr (String text, int length){
        String [] substrings = new String[length];
        for (int i = 0; i < length; i++) {
            String substr = "";
            for (int j = 0; j < text.length()/length; j++) {
                substr = substr + text.charAt(j*length+i);
            }
            substrings[i]=substr;
        }

        return  substrings;
    }

    public static String move(String input){
        String output = "";

        for (int i = 0; i < input.length(); i++) {
            if (i!=0){
                output = output + input.charAt(i-1);
            } else output = output + input.charAt(input.length()-1);

        }

        return output;
    }

    public static int indexOfCoincidance(String input, String output){
        int n = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i)==output.charAt(i)) n++;
        }
        return n;
    }

    public static int checkASCII (char[] line){
        int count = 0;
        for (char c:line) {
            if ((((int)c>64)&&((int)c<91))||(((int)c>96)&&((int)c<123)))
                count++;
        }

        return count;
    }

}
