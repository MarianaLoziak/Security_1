import java.io.*;
import java.util.*;

public class SubstitutionMain {

            public static void main(String[] args) throws IOException {
                String word = "EFFPQLEKVTVPCPYFLMVHQLUEWCNVWFYGHYTCETHQEKLPVMSAKSPVPAPVYWMVHQLUSPQLYWLASLFVWPQLMVHQLUPLRPSQLULQESPBLWPCSVRVWFLHLWFLWPUEWFYOTCMQYSLWOYWYETHQEKLPVMSAKSPVPAPVYWHEPPLUWSGYULEMQTLPPLUGUYOLWDTVSQETHQEKLPVPVSMTLEUPQEPCYAMEWWYTYWDLUULTCYWPQLSEOLSVOHTLUYAPVWLYGDALSSVWDPQLNLCKCLRQEASPVILSLEUMQBQVMQCYAHUYKEKTCASLFPYFLMVHQLUPQLHULIVYASHEUEDUEHQBVTTPQLVWFLRYGMYVWMVFLWMLSPVTTBYUNESESADDLSPVYWCYAMEWPUCPYFVIVFLPQLOLSSEDLVWHEUPSKCPQLWAOKLUYGMQEUEMPLUSVWENLCEWFEHHTCGULXALWMCEWETCSVSPYLEMQYGPQLOMEWCYAGVWFEBECPYASLQVDQLUYUFLUGULXALWMCSPEPVSPVMSBVPQPQVSPCHLYGMVHQLUPQLWLRPOEDVMETBYUFBVTTPENLPYPQLWLRPTEKLWZYCKVPTCSTESQPBYMEHVPETCMEHVPETZMEHVPETKTMEHVPETCMEHVPETT";
                analyze(word);
                String right = "ADDTHEABILITYTODECIPHERANYKINDOFPOLYALPHABETICSUBSTITUTIONCIPHERSTHEONEUSEDINTHECIPHERTEXTSHEREHASTWENTYSIXINDEPENDENTRANDOMLYCHOSENMONOALPHABETICSUBSTITUTIONPATTERNSFOREACHLETTERFROMENGLISHALPHABETITISCLEARTHATYOUCANNOLONGERRELYONTHESAMESIMPLEROUTINEOFGUESSINGTHEKEYBYEXHAUSTIVESEARCHWHICHYOUPROBABLYUSEDTODECIPHERTHEPREVIOUSPARAGRAPHWILLTHEINDEXOFCOINCIDENCESTILLWORKASASUGGESTIONYOUCANTRYTODIVIDETHEMESSAGEINPARTSBYTHENUMBEROFCHARACTERSINAKEYANDAPPLYFREJUENCYANALYSISTOEACHOFTHEMCANYOUFINDAWAYTOUSEHIGHERORDERFREJUENCYSTATISTICSWITHTHISTYPEOFCIPHERTHENEXTMAGICALWORDWILLTAKETOTHENEXTLABENQOYBITLYSLASHTWOCAPITALYCAPITALQCAPITALBLCAPITALYCAPITALL";


               /* String word1 = "EFGTR";
                System.out.println(word1);
                Gene gene = new Gene(ga.createIndividual());
                String word2 = gene.gene.toString().substring(1,3*gene.gene.size()-1).replaceAll(", ", "");
                System.out.println(word2);
                String word3 = gene.decrypt(word1);
                System.out.println(word3);*/



                // Creating an empty ArrayList of string type
/*                GeneticAlgorithm ga =new GeneticAlgorithm();
                HashMap<String, Double> corpus = readNGrams("C:\\Users\\user\\Downloads\\Security_1\\Substitution\\src\\Trigrams");
                HashMap<String, Double> r = ga.getNgramsFreq(3,right);
                for (int i = 0; i <100; i++) {
                    Gene gene = new Gene(ga.createIndividual());
                    String d = gene.decrypt(word);
                    HashMap<String, Double> decrypt = ga.getNgramsFreq(3,d);

                    System.out.println(ga.fitness(decrypt, corpus));
                }
                System.out.println(ga.fitness(r, corpus));*/

                HashMap<String, Double> corpus = readNGrams("C:\\Users\\user\\Downloads\\Security_1\\Substitution\\src\\Trigrams");
                GeneticAlgorithm ga = new GeneticAlgorithm(corpus,word, 500,500);
                ga.algorithm();
            }
            
            public static void analyze(String word){
                char[] letters = word.toCharArray();
                HashMap<Character,Integer> statistic = new HashMap();
                for (char c :
                        letters) {
                    statistic.put(c,statistic.containsKey(c)?statistic.get(c)+1:1);
                }
                TreeMap<Character, Integer> sorted = new TreeMap<Character, Integer>();
                sorted.putAll(statistic);
                for (var key : sorted.keySet()) {
                    System.out.println(key + ":" + sorted.get(key));
                }
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
