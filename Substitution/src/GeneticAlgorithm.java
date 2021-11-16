import java.util.*;

public class GeneticAlgorithm {

    public List<Gene> currentGeneration;
    public HashMap<String, Double> trigrams;

    public String task;
    public int generationSize;
    public int populationSize;

    public GeneticAlgorithm(HashMap<String, Double> trigrams, String task, int generationSize, int populationSize) {
        this.trigrams = trigrams;
        this.task = task;
        this.generationSize = generationSize;
        this.populationSize = populationSize;
        this.currentGeneration = new ArrayList<>(populationSize);
    }

    public void algorithm(){
        createPopulation();
        evaluateAndSort();
        for (int i = 0; i < generationSize; i++) {
            nextGeneration();
            evaluateAndSort();
            System.out.println(currentGeneration.get(0).fitness);
        }
        System.out.println(currentGeneration.get(0).fitness);
        System.out.println(currentGeneration.get(0).gene);

        /*for (int i = 0; i < populationSize; i++) {currentGeneration.get(0).
            System.out.println(currentGeneration.get(i).fitness);

        }*/
        System.out.println(currentGeneration.get(0).decrypt(task));
    }
    public static List<Character> createIndividual (){

        List<Character> individual = Arrays.asList('A', 'B', 'C', 'D','E', 'F', 'G','H','I', 'J','K', 'L', 'M', 'N', 'O', 'P' ,'Q' ,'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        Collections.shuffle(individual);
        return individual;

    }


    public int rouletteSelection(){
        double totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            totalFitness+= currentGeneration.get(i).fitness;
        }
        Random r = new Random();
        double rand = r.nextDouble(0,totalFitness);
        double partialSum = 0;
        for (int i = 0; i < populationSize; i++) {
            partialSum+= currentGeneration.get(i).fitness;
            if(partialSum >= rand)
                return i;
        }
        return -1;
    }

    public HashMap<String, Integer> getNgram(int n, String text){
        HashMap<String, Integer> ngrams = new HashMap<>();
        for (int i = 0; i < text.length()-n+1; i++) {
            String trigram = text.substring(i, i+n);
            ngrams.put(trigram, ngrams.containsKey(trigram)?ngrams.get(trigram)+1:1);
        }

        return ngrams;
    }
    
    public HashMap<String, Double> getNgramsFreq(int n, String text){
        HashMap<String, Double> ngrams = new HashMap<>();
        int count = 0;
        for (int i = 0; i < text.length()-n+1; i++) {
            String ngram = text.substring(i, i+n);
            ngrams.put(ngram, ngrams.containsKey(ngram)?ngrams.get(ngram)+1:1);
            count++;
        }

        for (String key: ngrams.keySet()) {
            ngrams.put(key, ngrams.get(key)/count);
        }
        
        //System.out.println(ngrams);
        
        return ngrams;
    }

    public double fitness( HashMap<String, Double> text_trigrams, HashMap <String, Double> trigrams ){
        double freq;
        double freqEng;
        double score = 0;
        for (String key: text_trigrams.keySet()) {
           /*freq = text_trigrams.get(key);
           freqEng =trigrams.containsKey(key)? trigrams.get(key):0;
           score+=freq*(Math.log(freqEng) / Math.log(2));*/
            freq = text_trigrams.get(key);
            freqEng =trigrams.containsKey(key)? trigrams.get(key):0;
            score+=freq - freqEng;
        }

        return score;
    }

    public void createPopulation(){
        currentGeneration.clear();

        for (int i = 0; i < populationSize; i++) {
            Gene gene = new Gene(createIndividual());
            currentGeneration.add(gene);
        }
    }
    
    public void evaluateAndSort(){
        for (int i = 0; i < populationSize; i++) {
            Gene gene = currentGeneration.get(i);
            String decrypt = gene.decrypt(task);
            HashMap<String, Double> decryptMap = getNgramsFreq(3,decrypt);
            //gene.fitness = fitness(decryptMap, trigrams);
            currentGeneration.get(i).fitness = fitness(decryptMap, trigrams);
        }

        Collections.sort(currentGeneration, new Gene.GeneComparer());
    }

    public void nextGeneration(){
        List<Gene> nextGeneration = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize/5; i++) {
            nextGeneration.add(currentGeneration.get(i));
        }
        for (int i = populationSize/5; i < populationSize; i+=2 ){
            Gene firstParent;
            Gene secondParent;

            firstParent = currentGeneration.get(rouletteSelection());
            secondParent = currentGeneration.get(rouletteSelection());

            crossoverNew(firstParent, secondParent, nextGeneration);
        }
        currentGeneration.clear();
        currentGeneration = nextGeneration;

    }

    public void crossover(Gene mother, Gene father, List<Gene> next) {
        Random rn = new Random();
        int crossOverPoint = rn.nextInt(mother.gene.size());

        Gene childFirst = new Gene(new ArrayList<>());
        Gene childSecond = new Gene(new ArrayList<>());

        for (int i = 0; i < crossOverPoint; i++) {
/*            char temp = mother.gene.get(i);
            mother.gene.set(i, father.gene.get(i));
            father.gene.set(i,temp);*/
            childFirst.gene.add(mother.gene.get(i));
            childSecond.gene.add(father.gene.get(i));
        }
        for (int i = crossOverPoint; i < mother.gene.size(); i++) {
/*            char temp = mother.gene.get(i);
            mother.gene.set(i, father.gene.get(i));
            father.gene.set(i,temp);*/
            childSecond.gene.add(mother.gene.get(i));
            childFirst.gene.add(father.gene.get(i));
        }
        next.add(childFirst);
        next.add(childSecond);

    }

    public void crossoverNew(Gene mother, Gene father, List<Gene> next) {
        Random rn = new Random();
        int end = rn.nextInt(mother.gene.size()) + 1;
        int start = rn.nextInt(end);

        Gene childFirst = new Gene();
        Gene childSecond = new Gene();

        //childFirst.gene.addAll(mother.gene);
        //childSecond.gene.addAll(father.gene);

        for (int i = start; i < end; i++) {
            char first = mother.gene.get(i);
            char second = father.gene.get(i);

            childFirst.gene.set(i, second);
            childSecond.gene.set(i, first);

        }

        for (int i = 0; i < mother.gene.size(); i++) {
            if(i==start){
                i+=end-start;
                if(i == mother.gene.size())
                    continue;
            }

            int ind = i;
            while(childFirst.gene.contains(mother.gene.get(ind))){
                ind = childFirst.gene.indexOf(mother.gene.get(ind));
            }
            childFirst.gene.set(i, mother.gene.get(ind));

            ind = i;
            while(childSecond.gene.contains(father.gene.get(ind))){
                ind = childSecond.gene.indexOf(father.gene.get(ind));
            }
            childSecond.gene.set(i, father.gene.get(ind));

        }

        next.add(childFirst);
        next.add(childSecond);

    }
}

