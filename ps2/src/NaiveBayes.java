import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes
{
   private Map<String, Integer> vocabulary;
   private LinkedHashMap<String, EmailType> emailTypes;
   private int totalTrainingCount;
   private int totalNoOfWordsInVocabulary;
   LinkedHashMap<String, String[]> testingData;
   private List<Double> predictedEmailTypes;
   public NaiveBayes(String vocabularyPath) {
      this.totalTrainingCount = 0;
      this.totalNoOfWordsInVocabulary = 0;
      try {
         File vocabularyFile = new File(vocabularyPath);
         vocabulary = readVocabulary(vocabularyFile);
         totalNoOfWordsInVocabulary = vocabulary.size();
         emailTypes = new LinkedHashMap<String, EmailType>();
         predictedEmailTypes = new ArrayList<Double>();
         testingData = new LinkedHashMap<String, String[]>();
      } catch (Exception e) {
         System.out.println("Unable to read vocabulary file!!");
         System.exit(1);
      }
   }
   
   private Map<String, Integer> readVocabulary(File vocabularyPath) throws Exception {
      Map<String, Integer> vocabulary = new HashMap<String, Integer>();
      try {
         FileReader reader = new FileReader(vocabularyPath);
         BufferedReader bufferedReader = new BufferedReader(reader);
         String line;
         String[] split;
         int currentWordCount;
         while((line = bufferedReader.readLine()) != null) {
            split = line.split(" ");
            currentWordCount = Integer.parseInt(split[1]);
            this.totalNoOfWordsInVocabulary+=currentWordCount;
            vocabulary.put(split[0], currentWordCount);
         }
         bufferedReader.close();
      } catch (Exception e) {
         throw e;
      }
      return vocabulary;
   }
   
   public void addLabel(String label, double numericalValue, String vocabularyPath) {
      EmailType newLabel = new EmailType(label, numericalValue, vocabularyPath);
      emailTypes.put(label, newLabel);
   }
   
   public void setTrainingDataSet(String trainingFilePath) {
      File trainingFile = new File(trainingFilePath);
      processTrainingFile(trainingFile);
   }
   
   private void processTrainingFile(File trainingFile) {
      try {
         FileReader reader = new FileReader(trainingFile);
         BufferedReader bufferedReader = new BufferedReader(reader);
         String line;
         String[] split;
         EmailType currentEmailType;
         while((line = bufferedReader.readLine()) != null) {
            split = line.split("/");
            if(emailTypes.containsKey(split[0])) {
               totalTrainingCount++;
               currentEmailType = emailTypes.get(split[0]);
               currentEmailType.addToTrainingCount(1);
            }
         }
         bufferedReader.close();
      } catch (Exception e) {
         System.out.print("Unable to process the training data set");
      }
   }
   
   public void learn() {
      EmailType currentEmailType;
      int currentWordCount;
      double currentWordProbability;
      for(Map.Entry<String, EmailType> entry : emailTypes.entrySet()) {
         currentEmailType = entry.getValue();
         currentEmailType.setPrior(totalTrainingCount);
         for(String vocabularyWord: vocabulary.keySet()) {
            currentWordCount = currentEmailType.getWordCount(vocabularyWord);
            currentWordProbability = (double)(currentWordCount + 1)/(currentEmailType.getTotalWordCount() + totalNoOfWordsInVocabulary);
            currentEmailType.setWordProbability(vocabularyWord, currentWordProbability);
         }
      }
   }
   
   public void setTestingDataSet(String testingFilePath) {
      File testingFile = new File(testingFilePath);
      processTestingFile(testingFile);
   }
   
   private void processTestingFile(File testingFile) {
      try {
         testingData = new LinkedHashMap<String, String[]>();
         FileReader reader = new FileReader(testingFile);
         BufferedReader br = new BufferedReader(reader);
         String line;
         while((line = br.readLine()) != null) {
            testingData.put(line, line.split(" "));
         }
         br.close();
      } catch (Exception e) {
         System.out.println("Unable to process testing File.");
      }
   }
   
   public void decide() {
      try {
         
         
         
         double currentMax = (double) Integer.MIN_VALUE;
         String[] currentLine;
         double currentConditionalProb;
         EmailType maxLabelType;
         for(String[] value : testingData.values()) {
            currentMax = (double) Integer.MIN_VALUE;
            currentLine = value;
            maxLabelType = null;
            for(EmailType currentEmailType : emailTypes.values()) {
               currentConditionalProb = 0;
               for(int i = 1; i<currentLine.length; i++) {
                     currentConditionalProb += Math.log(currentEmailType.getProbability(currentLine[i]));
               }
               currentConditionalProb += Math.log(currentEmailType.getPrior());
               if(currentConditionalProb>currentMax) {
                  currentMax = currentConditionalProb;
                  maxLabelType = currentEmailType;
               }
            }
            predictedEmailTypes.add(maxLabelType.getNumericalValue());
         }
      } catch (Exception e) {
         
      }
   }
   
   public List<Double> getPredictedSet() {
      return predictedEmailTypes;
   }
}
