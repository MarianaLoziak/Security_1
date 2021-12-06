import java.util.*;

public class Gene {
    public List<Character>[] gene;
    public double fitness;

   /* public Gene(List<Character> gene) {
        this.gene = gene;
    }*/



    public Gene(int alphabetNumber, boolean flag){
        this.gene = new List[alphabetNumber];
        if(flag) {
            for (int i = 0; i < alphabetNumber; i++) {
                gene[i] = createIndividual();
            }
        } else{
            for (int j = 0; j < alphabetNumber; j++) {
                gene[j] = new ArrayList<>();
                for (int i = 0; i < 26; i++) {
                    gene[j].add('!');
                }
            }
        }
    }

    public static List<Character> createIndividual (){

        List<Character> individual = Arrays.asList('A', 'B', 'C', 'D','E', 'F', 'G','H','I', 'J','K', 'L', 'M', 'N', 'O', 'P' ,'Q' ,'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        Collections.shuffle(individual);
        return individual;

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
            output[i] = gene[i % gene.length].get(input[i]-'A');
        }
        String text = new String(output);

        return text;

    }
}

