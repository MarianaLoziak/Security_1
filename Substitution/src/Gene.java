import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Gene {
    public List<Character> gene;
    public double fitness;

    public Gene(List<Character> gene) {
        this.gene = gene;
    }

    public Gene(){
        this.gene = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            gene.add('!');
        }

    }

    public static class GeneComparer implements Comparator<Gene> {

        @Override
        public int compare(Gene arg0, Gene arg1) {
            if (arg0.fitness > arg1.fitness) {
                return 1;
            } else {
                if (arg0.fitness < arg1.fitness) {
                    return -1;
                }
            }

            return 0;
        }

    }
    public String decrypt(String line){
        char[] input = line.toCharArray();
        char[] output = new char[input.length];
        for (int i = 0; i < input.length; i++){
            output[i] = gene.get(input[i]-'A');
        }
        String text = new String(output);

        return text;

    }
}
