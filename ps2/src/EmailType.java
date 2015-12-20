import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class EmailType
{
   private double numericalValue;
   private String label;
   private double prior;
   private Map<String, Integer> vocabulary;
   private Map<String, Double> wordProbability;
   int totalNoOfWords;
   private int trainingCount;
   
   public EmailType(String label, double numericalValue, String vocabularyPath) {
      try {
         this.label = label;
         this.numericalValue = numericalValue;
         this.vocabulary = new HashMap<String, Integer>();
         this.wordProbability = new HashMap<String, Double>();
         this.trainingCount = 0;
         File vocabularyFile = new File(vocabularyPath);
         this.totalNoOfWords = readVocabulary(vocabularyFile, vocabulary);
      } catch (Exception e) {
         System.out.println("Unable to add Label");
      }
   }
   
   private static int readVocabulary(File vocabularyFile, Map<String, Integer> vocabularyPriorMap) throws Exception {
      try {
         FileReader reader = new FileReader(vocabularyFile);
         BufferedReader bufferedReader = new BufferedReader(reader);
         String line;
         String[] split;
         int totalWordCount = 0;
         int currentWordCount;
         while((line = bufferedReader.readLine()) != null) {
            split = line.split(" ");
            currentWordCount = Integer.parseInt(split[1]);
            totalWordCount += currentWordCount;
            vocabularyPriorMap.put(split[0], currentWordCount);
         }
         bufferedReader.close();
         return totalWordCount;
      } catch (Exception e) {
         throw e;
      }
   }
   
   public void addToTrainingCount(int toAdd) {
      this.trainingCount += toAdd;
   }
   
   public void setPrior(int totalCount) {
      this.prior = ((double)this.trainingCount)/totalCount;
   }
   
   public double getPrior() {
      return this.prior;
   }
   
   public String getLabel() {
      return this.label;
   }
   
   public int getWordCount(String word) {
      int wordCount = 0;
      if(vocabulary.containsKey(word)) {
         wordCount = vocabulary.get(word);
      }
      return wordCount;
   }
   
   public void setWordProbability(String word, double probability) {
     wordProbability.put(word, probability);
   }
   
   public double getProbability(String word) {
      double prob = 0;
      if(wordProbability.containsKey(word)){
         prob = wordProbability.get(word);
      }
      return prob;
   }
   public int getTotalWordCount() {
      return this.totalNoOfWords;
   }
   
   public double getNumericalValue() {
      return numericalValue;
   }
}
