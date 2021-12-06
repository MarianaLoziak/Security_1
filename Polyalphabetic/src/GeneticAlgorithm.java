import java.util.*;

public class GeneticAlgorithm {

    public List<Gene> currentGeneration;
    public HashMap<String, Double> trigrams;

    public String task;
    public int generationSize;
    public int populationSize;
    public double mutationRate;
    public int alphabet;

    private Random random = new Random();

    public GeneticAlgorithm(HashMap<String, Double> trigrams, String task, int generationSize, int populationSize, double mutationRate, int alphabet) {
        this.trigrams = trigrams;
        this.task = task;
        this.generationSize = generationSize;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.alphabet = alphabet;
        this.currentGeneration = new ArrayList<>(populationSize);
    }

    public String algorithm(){
        createPopulation();
        evaluateAndSort();
        for (int i = 0; i < generationSize; i++) {
            nextGeneration();
            evaluateAndSort();
            System.out.println(currentGeneration.get(0).fitness);
        }
        System.out.println(currentGeneration.get(0).fitness);
        for (int i = 0; i < alphabet; i++) {
            System.out.println(currentGeneration.get(0).gene[i]);
        }

        return currentGeneration.get(0).decrypt(task);
    }


    public int rouletteSelection(){
/*        double totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            totalFitness+= currentGeneration.get(i).fitness;
        }
        double rand = random.nextDouble(0,totalFitness);
        double partialSum = 0;
        for (int i = 0; i < populationSize; i++) {
            partialSum+= currentGeneration.get(i).fitness;
            if(partialSum >= rand)
                return i;
        }
        return -1;*/

        int index = random.nextInt(populationSize/3);
        return index;
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

        return ngrams;
    }

    public double fitness( HashMap<String, Double> text_trigrams, HashMap <String, Double> trigrams ){
        double freq;
        double freqEng;
        double score = 0;
        for (String key: text_trigrams.keySet()) {
            freq = text_trigrams.get(key);
            freqEng =trigrams.containsKey(key)? trigrams.get(key):0;
            score+=freq - freqEng;
        }

        return score;
    }

    private String[] devideIntoSubstrings(String line, int key){
        String[] output = new String[key];
        for (int i = 0; i < key; i++) {
            output[i] = new String();
        }
        for (int i = 0; i < line.length(); i++) {
            output[i%key] += line.charAt(i);
        }

        return output;
    }

    private double fitnessForSubline(HashMap<String, Double> monograms, String subline){
        HashMap<String, Double> frequency = countFrequencies(subline);
        double freq;
        double freqEng;
        double score = 0;
        for (String key: frequency.keySet()){
            freq = frequency.get(key);
            freqEng = monograms.get(key);
            score += Math.abs(freq - freqEng);
        }

        return score;
    }

    public HashMap<String, Double> countFrequencies(String line){

        HashMap<String, Double> output = new HashMap<>();
        for (int i = 0; i < line.length(); i++) {
            output.put(Character.toString(line.charAt(i)), output.containsKey(line.charAt(i)) ? output.get(line.charAt(i)) + 1 : 1);
        }

        for (String key: output.keySet()) {
            output.put(key, output.get(key)/line.length());
        }

        return output;
    }

    public void createPopulation(){
        currentGeneration.clear();

        for (int i = 0; i < populationSize; i++) {
            Gene gene = new Gene(alphabet, true);
            currentGeneration.add(gene);
        }
    }

    public void evaluateAndSort(){
        for (int i = 0; i < populationSize; i++) {
            Gene gene = currentGeneration.get(i);
            String decrypt = gene.decrypt(task);
            HashMap<String, Double> decryptMap = getNgramsFreq(3,decrypt);

            currentGeneration.get(i).fitness = fitness(decryptMap, trigrams);
        }

        Collections.sort(currentGeneration, new Gene.GeneComparer());
    }

    private double evalFitness(Gene gene){
        String decrypt = gene.decrypt(task);
        HashMap<String, Double> decryptMap = getNgramsFreq(3,decrypt);

        return fitness(decryptMap, trigrams);
    }

    public void nextGeneration(){
        List<Gene> nextGeneration = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize/10; i++) {
            nextGeneration.add(currentGeneration.get(i));
        }
        for (int i = populationSize/10; i < populationSize; i+=2 ){
            Gene firstParent;
            Gene secondParent;

            firstParent = currentGeneration.get(rouletteSelection());
            secondParent = currentGeneration.get(rouletteSelection());

            crossoverM(firstParent, secondParent, nextGeneration);
        }
        currentGeneration.clear();
        currentGeneration = nextGeneration;

    }

    public void crossoverM(Gene mother, Gene father, List<Gene> next){
        int alphabetN = mother.gene.length;
        int motherGene;
        int fatherGene;

        int end = random.nextInt(mother.gene[0].size()) + 1;
        int start = random.nextInt(end);

        Gene childFirst = new Gene(alphabetN, false);
        Gene childSecond = new Gene(alphabetN, false);


        for (int j = 0; j < alphabetN; j++) {
            motherGene= j;
            fatherGene = j;
            for (int i = start; i < end; i++) {
                char first = mother.gene[motherGene].get(i);
                char second = father.gene[fatherGene].get(i);

                childFirst.gene[motherGene].set(i, second);
                childSecond.gene[fatherGene].set(i, first);
            }

            for (int i = 0; i < mother.gene[0].size(); i++) {
                if(i==start){
                    i+=end-start;
                    if(i == mother.gene[0].size())
                        continue;
                }

                int ind = i;
                while(childFirst.gene[motherGene].contains(mother.gene[motherGene].get(ind))){
                    ind = childFirst.gene[motherGene].indexOf(mother.gene[motherGene].get(ind));
                }
                childFirst.gene[motherGene].set(i, mother.gene[motherGene].get(ind));

                ind = i;
                while(childSecond.gene[fatherGene].contains(father.gene[fatherGene].get(ind))){
                    ind = childSecond.gene[fatherGene].indexOf(father.gene[fatherGene].get(ind));
                }
                childSecond.gene[fatherGene].set(i, father.gene[fatherGene].get(ind));

            }
        }


        mutation(childFirst);
        next.add(childFirst);
        next.add(childSecond);

    }

    public void mutation(Gene gene) {
        for (int i = 0; i < gene.gene.length; i++) {
            for (int j = 0; j < gene.gene[0].size(); j++) {
                if (random.nextDouble() < mutationRate) {
                    Collections.swap(gene.gene[i], j, random.nextInt(gene.gene[0].size()));
                }
            }
        }

/*        for (int i = 0; i < alphabet; i++) {
            int point1 = random.nextInt(gene.gene[i].size());
            int point2 = random.nextInt(gene.gene[i].size());
            while(point1==point2){
                point2 = random.nextInt(gene.gene[i].size());
            }
            Collections.swap(gene.gene[i],point1, point2);
        }*/

    }

    /*public void crossover(Gene mother, Gene father, List<Gene> next) {
        Random rn = new Random();
        int end = rn.nextInt(mother.gene.size()) + 1;
        int start = rn.nextInt(end);

        Gene childFirst = new Gene();
        Gene childSecond = new Gene();


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

        mutation(childFirst);
        mutation(childSecond);
        next.add(childFirst);
        next.add(childSecond);

    }*/

}


