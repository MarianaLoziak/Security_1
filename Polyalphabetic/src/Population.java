import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Population {
    public List<List<Character>>alphabets;

    public Population(int populationSize){
        alphabets = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            alphabets.add(createIndividual());
        }
    }

    public static List<Character> createIndividual(){

        List<Character> individual = Arrays.asList('A', 'B', 'C', 'D','E', 'F', 'G','H','I', 'J','K', 'L', 'M', 'N', 'O', 'P' ,'Q' ,'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        Collections.shuffle(individual);
        return individual;

    }
}
