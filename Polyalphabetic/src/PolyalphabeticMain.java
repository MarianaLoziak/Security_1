import java.io.*;
import java.util.HashMap;
import java.util.TreeMap;

public class PolyalphabeticMain {

    public static void main(String[] args) throws IOException {
        String word = "UMUPLYRXOYRCKTYYPDYZTOUYDZHYJYUNTOMYTOLTKAOHOKZCMKAVZDYBRORPTHQLSERUOERMKZGQJOIDJUDNDZATUVOTTLMQBOWNMERQTDTUFKZCMTAZMEOJJJOXMERKJHACMTAZATIZOEPPJKIJJNOCFEPLFBUNQHHPPKYYKQAZKTOTIKZNXPGQZQAZKTOTIZYNIUISZIAELMKSJOYUYYTHNEIEOESULOXLUEYGBEUGJLHAJTGGOEOSMJHNFJALFBOHOKAGPTIHKNMKTOUUUMUQUDATUEIRBKYUQTWKJKZNLDRZBLTJJJIDJYSULJARKHKUKBISBLTOJRATIOITHYULFBITOVHRZIAXFDRNIORLZEYUUJGEBEYLNMYCZDITKUXSJEJCFEUGJJOTQEZNORPNUDPNQIAYPEDYPDYTJAIGJYUZBLTJJYYNTMSEJYFNKHOTJARNLHHRXDUPZIALZEDUYAOSBBITKKYLXKZNQEYKKZTOKHWCOLKURTXSKKAGZEPLSYHTMKRKJIIQZDTNHDYXMEIRMROGJYUMHMDNZIOTQEKURTXSKKAGZEPLSYHTMKRKJIIQZDTNROAUYLOTIMDQJYQXZDPUMYMYPYRQNYFNUYUJJEBEOMDNIYUOHYYYJHAOQDRKKZRRJEPCFNRKJUHSJOIRQYDZBKZURKDNNEOYBTKYPEJCMKOAJORKTKJLFIOQHYPNBTAVZEUOBTKKBOWSBKOSKZUOZIHQSLIJJMSURHYZJJZUKOAYKNIYKKZNHMITBTRKBOPNUYPNTTPOKKZNKKZNLKZCFNYTKKQNUYGQJKZNXYDNJYYMEZRJJJOXMERKJVOSJIOSIQAGTZYNZIOYSMOHQDTHMEDWJKIULNOTBCALFBJNTOGSJKZNEEYYKUIXLEUNLNHNMYUOMWHHOOQNUYGQJKZLZJZLOLATSEHQKTAYPYRZJYDNQDTHBTKYKYFGJRRUFEWNTHAXFAHHODUPZMXUMKXUFEOTIMUNQIHGPAACFKATIKIZBTOTIKZNKKZNLORUKMLLFBUUQKZNLEOHIEOHEDRHXOTLMIRKLEAHUYXCZYTGUYXCZYTIUYXCZYTCVJOEBKOHE";

        //findKeyLength(word);

        HashMap<String, Double> corpus = readNGrams("Polyalphabetic/src/Trigrams");
        GeneticAlgorithm ga = new GeneticAlgorithm(corpus, word, 600,1000, 0.2, 4);
        String result = ga.algorithm();
        printFormatted(result, 4);

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

    public static HashMap<String, Double> readNGrams(String filename) throws IOException {
        HashMap<String, Double> ngrams = new HashMap<>();
        FileInputStream fstream = new FileInputStream(filename);

        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;

        while ((strLine = br.readLine()) != null)   {

            String ngram = strLine.substring(0, strLine.indexOf(" "));
            double freq = Double.parseDouble(strLine.substring(strLine.indexOf(" ")+1));
            ngrams.put(ngram, freq);
        }

        in.close();

        return ngrams;
    }


    public static void printFormatted(String line, int n){
        for (int i = 0; i < line.length(); i++) {
            if((i % n) != (n-1)){
                System.out.print(line.charAt(i) + "\t");
            } else{
                System.out.print(line.charAt(i) + "\n");
            }
        }
    }
}
