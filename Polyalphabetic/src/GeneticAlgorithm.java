import java.util.*;

public class GeneticAlgorithm {
    List<Population> currentGeneration;
    public HashMap<String, Double> trigrams;

    public String task;
    public int generationSize;
    public int populationSize;
    public int keyN;

    private Random random = new Random();

    public GeneticAlgorithm(HashMap<String, Double> trigrams, String task, int generationSize, int populationSize, int keyN) {
        this.trigrams = trigrams;
        this.task = task;
        this.generationSize = generationSize;
        this.populationSize = populationSize;
        this.keyN = keyN;
    }

    public void algorithm() {
        createPopulations();
        for (int i = 0; i < keyN; i++) {
            sortAlphabets(currentGeneration.get(i), i);
        }
        for (int i = 0; i < generationSize; i++) {
            for (int j = 0; j < keyN; j++) {
                nextGenerationPopulation(j, currentGeneration.get(j));
                sortAlphabets(currentGeneration.get(j), j);
            }
            //Show fitness for this generation

            List<List<Character>> keys = new ArrayList<>(keyN);
            for (int l = 0; l < keyN; l++) {
                keys.add(currentGeneration.get(l).alphabets.get(0));
            }
            String decryptedLine = decrypt(keys, task);
            if (i == (generationSize - 1))
                System.out.println(decryptedLine);

            System.out.println(fitness(getNgramsFreq(3, decryptedLine), trigrams));
        }

    }

    public void createPopulations() {
        List<Population> populations;
        populations = new ArrayList<>(keyN);
        for (int i = 0; i < keyN; i++) {
            Population population = new Population(populationSize);
            populations.add(population);
        }
        currentGeneration = populations;
    }

    public void nextGenerationPopulation(int keyIndex, Population population) {
        List<List<Character>> alphabets = population.alphabets;
        List<List<Character>> newAlphabets = new ArrayList<>(alphabets.size());

        for (int i = 0; i < populationSize / 10; i++) {
            newAlphabets.add(alphabets.get(i));
        }
        for (int i = populationSize / 10; i < populationSize; i += 2) {
            List<Character> mother = alphabets.get(selectRandom());
            List<Character> father = alphabets.get(selectRandom());
            crossoverNew(mother, father, newAlphabets);
        }

        population.alphabets.clear();
        population.alphabets = newAlphabets;
        currentGeneration.set(keyIndex, population);
    }

    public int selectRandom() {
        int index = random.nextInt((int) (populationSize * 0.7));
        return index;
    }

    public void crossover(List<Character> mother, List<Character> father, List<List<Character>> newAlphabets) {
        int end = random.nextInt(mother.size()) + 1;
        int start = random.nextInt(end);

        List<Character> childFirst = new ArrayList<>(mother.size());
        List<Character> childSecond = new ArrayList<>(mother.size());
        for (int i = 0; i < mother.size(); i++) {
            childFirst.add('!');
            childSecond.add('!');
        }

        for (int i = start; i < end; i++) {
            char first = mother.get(i);
            char second = father.get(i);

            childFirst.set(i, second);
            childSecond.set(i, first);

        }

        for (int i = 0; i < mother.size(); i++) {
            if (i == start) {
                i += end - start;
                if (i == mother.size())
                    continue;
            }

            int ind = i;
            while (childFirst.contains(mother.get(ind))) {
                ind = childFirst.indexOf(mother.get(ind));
            }
            childFirst.set(i, mother.get(ind));

            ind = i;
            while (childSecond.contains(father.get(ind))) {
                ind = childSecond.indexOf(father.get(ind));
            }
            childSecond.set(i, father.get(ind));

        }

        newAlphabets.add(childFirst);
        newAlphabets.add(childSecond);


    }


    public void sortAlphabets(Population population, int keyIndex) {
        List<List<Character>> alphabets = population.alphabets;
        alphabets.sort(Comparator.comparingDouble(alphabet -> evaluateAlphabet(alphabet, keyIndex)));
        population.alphabets = alphabets;
    }


    public double evaluateAlphabet(List<Character> alphabet, int keyIndex) {
        double fitness = 0;
        List<List<Character>> keys = new ArrayList<>(keyN);
        for (int i = 0; i < keyN; i++) {
            keys.add(currentGeneration.get(i).alphabets.get(0));
        }
        keys.set(keyIndex, alphabet);
        String decryptedLine = decrypt(keys, task);

        fitness = fitness(getNgramsFreq(3, decryptedLine), trigrams);

        return fitness;
    }

    public String decrypt(List<List<Character>> keys, String line) {
        char[] input = line.toCharArray();
        char[] output = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = keys.get(i % keyN).get(input[i] - 'A');
        }
        String text = new String(output);

        return text;
    }

    public double fitness(HashMap<String, Double> text_trigrams, HashMap<String, Double> trigrams) {
        double freq;
        double freqEng;
        double score = 0;
        for (String key : text_trigrams.keySet()) {
            freq = text_trigrams.get(key);
            freqEng = trigrams.containsKey(key) ? trigrams.get(key) : 0;
            score += freq - freqEng;
        }

        return score;
    }

    public HashMap<String, Double> getNgramsFreq(int n, String text) {
        HashMap<String, Double> ngrams = new HashMap<>();
        int count = 0;
        for (int i = 0; i < text.length() - n + 1; i++) {
            String ngram = text.substring(i, i + n);
            ngrams.put(ngram, ngrams.containsKey(ngram) ? ngrams.get(ngram) + 1 : 1);
            count++;
        }

        for (String key : ngrams.keySet()) {
            ngrams.put(key, ngrams.get(key) / count);
        }

        return ngrams;
    }


    public List<Character> getRandomChars() {
        int r = random.nextInt(26) + 1;
        List<Character> list = Population.createIndividual();
        List<Character> result = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            result.add(list.get(i));
        }
        return result;
    }

    public List<Character> getAnotherChars(List<Character> chars) {
        List<Character> alphabet = Population.createIndividual();
        List<Character> list = new ArrayList<>();
        for (char c : alphabet) {
            if (!chars.contains(c))
                list.add(c);
        }
        return list;
    }

    public List<Character> cross(List<Character> alphabet1, List<Character> alphabet2, List<Character> letters) {
        List<Character> child = new ArrayList<>(alphabet1);
        List<Character> temp = new ArrayList<>();
        for (char c : alphabet2) {
            if (!letters.contains(c))
                temp.add(c);
        }
        int counter = 0;

        for (int i = 0; i < alphabet1.size(); i++) {
            if (letters.contains(alphabet1.get(i))) continue;
            child.set(i, temp.get(counter));
            counter++;
        }

        return child;
    }

    public void crossoverNew(List<Character> mother, List<Character> father, List<List<Character>> newAlphabets) {
        List<Character> letters1 = getRandomChars();
        List<Character> letters2 = getAnotherChars(letters1);

        List<Character> childFirst = cross(mother, father, letters1);
        List<Character> childSecond = cross(mother, father, letters2);

        newAlphabets.add(mutation(childFirst));
        newAlphabets.add(mutation(childSecond));
    }

    public List<Character> mutation(List<Character> child) {
        List<Character> alphabet = child;
        if (random.nextDouble() < 0.5) {
            Collections.swap(alphabet, random.nextInt(alphabet.size()), random.nextInt(alphabet.size()));
        }

        return alphabet;
    }


}
