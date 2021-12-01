import java.io.*;
import java.util.*;

public class SubstitutionMain {

            public static void main(String[] args) throws IOException {
                String word = "EFFPQLEKVTVPCPYFLMVHQLUEWCNVWFYGHYTCETHQEKLPVMSAKSPVPAPVYWMVHQLUSPQLYWLASLFVWPQLMVHQLUPLRPSQLULQESPBLWPCSVRVWFLHLWFLWPUEWFYOTCMQYSLWOYWYETHQEKLPVMSAKSPVPAPVYWHEPPLUWSGYULEMQTLPPLUGUYOLWDTVSQETHQEKLPVPVSMTLEUPQEPCYAMEWWYTYWDLUULTCYWPQLSEOLSVOHTLUYAPVWLYGDALSSVWDPQLNLCKCLRQEASPVILSLEUMQBQVMQCYAHUYKEKTCASLFPYFLMVHQLUPQLHULIVYASHEUEDUEHQBVTTPQLVWFLRYGMYVWMVFLWMLSPVTTBYUNESESADDLSPVYWCYAMEWPUCPYFVIVFLPQLOLSSEDLVWHEUPSKCPQLWAOKLUYGMQEUEMPLUSVWENLCEWFEHHTCGULXALWMCEWETCSVSPYLEMQYGPQLOMEWCYAGVWFEBECPYASLQVDQLUYUFLUGULXALWMCSPEPVSPVMSBVPQPQVSPCHLYGMVHQLUPQLWLRPOEDVMETBYUFBVTTPENLPYPQLWLRPTEKLWZYCKVPTCSTESQPBYMEHVPETCMEHVPETZMEHVPETKTMEHVPETCMEHVPETT";

                HashMap<String, Double> corpus = readNGrams("Substitution/src/Trigrams");
                GeneticAlgorithm ga = new GeneticAlgorithm(corpus,word, 500,500);
                ga.algorithm();
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


}
