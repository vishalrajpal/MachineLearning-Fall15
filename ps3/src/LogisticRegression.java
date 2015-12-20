import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LogisticRegression
{
   private static final int NO_OF_ITERATIONS = 10;
   private List<Double> weights;
   private List<TrainingInstance> trainingInstances;
   private double bias;
   private int noOfWords;
   private double learningRate;
   
   /*
    * Constructor : String, String -> LogisticRegression
    * Input : the path for the vocab file and the path for the training file
    */
   public LogisticRegression(String vocabPath, String trainingPath) {
      try {
         trainingInstances = new ArrayList<TrainingInstance>();
         readTrainingInstances(trainingPath);
         noOfWords = readVocabularySize(vocabPath);
         bias = 0.0;         
         weights = new ArrayList<Double>(Collections.nCopies(noOfWords, (double)0.0));
         learningRate = 0.5;
      } catch (Exception e) {
         
      }
   }
      
   /*
    * readVocabularySize : String -> int
    * input : the path of the vocabulary file
    * output : the number of workds in the vocabulary
    */
   private int readVocabularySize(String vocabPath) {
      int noOfLines = 0;
      try {
         List<String> lines = Files.readAllLines(Paths.get(vocabPath), Charset.defaultCharset());
         noOfLines = lines.size();
      } catch (Exception e) {
         System.out.print("Couldn't read training instances");
         System.exit(1);
      }
      return noOfLines;
   }
   
   /*
    * readTrainingInstances : String -> void
    * input : the path of the training examples file
    * process : reads the training file and creates 'TrainingInstance' object
    *           for every training example  
    */
   private void readTrainingInstances(String trainingPath) {
      try {
         FileReader reader = new FileReader(trainingPath);
         FileWriter writer = new FileWriter("labels.txt");
         BufferedReader bufferedReader = new BufferedReader(reader);
         String line;
         String[] split;
         String[] featureWeightSplit;
         TrainingInstance currentInstance;
         int noOfWords;
         while((line = bufferedReader.readLine()) != null) {
            split = line.split(" ");
            noOfWords = split.length;
            currentInstance = new TrainingInstance(split[0]);
            writer.write(split[0]+"\n");
            for(int i = 1; i<noOfWords; i++) {
               featureWeightSplit = split[i].split(":");
               currentInstance.addFeature(Integer.parseInt(featureWeightSplit[0]));
            }
            trainingInstances.add(currentInstance);
         }
         writer.close();
         bufferedReader.close();
      } catch (Exception e) {
         System.out.print("Couldn't read training instances");
         System.exit(1);
      }
   }
   
   /*
    * learn : -> void 
    * process : picks a random instance 'noOfTrainingInstances' times and calculates the sigmoid function value.
    *           Based on the sigmoid function value calculates a delta
    *           Based on the delta calculated updates the weight vector and the bias;
    *           Does this process for 10 times.
    */
   public void learn() {
      int randomTrainingIndex;
      int upperLimit = trainingInstances.size();
      TrainingInstance randomInstance;
      List<Integer> currentFeatureVector;
      double sigmoidFunctionValue;
      double delta;
      int noOfTrainingInstances = trainingInstances.size();
      for(int iterationCounter = 1; iterationCounter<=NO_OF_ITERATIONS; iterationCounter++) {
         for(int count = 0; count <  noOfTrainingInstances; count++) {
            randomTrainingIndex = (int)(Math.random() * upperLimit);
            randomInstance = trainingInstances.get(randomTrainingIndex);
            currentFeatureVector = randomInstance.getFeatureVector();
            sigmoidFunctionValue = getSigmoidFunctionValue(currentFeatureVector);
            delta = randomInstance.getLabel() - sigmoidFunctionValue;
            addDeltaToWeightsVector(delta, currentFeatureVector);
         }
      }
   }

   /*
    * getSigmoidFunctionValue : List<Integer> -> double
    * input : the feature vector for a training instance
    * output : the value of the sigmoid function
    */
   private double getSigmoidFunctionValue(List<Integer> featureVector) {
      double e = Math.exp(-getWeightTX(featureVector) - bias);
      double t = 1.0;
      double res1 = t + e;
      double res = 1.0/res1;
      return res;
   }
   
   /*
    * getWeightTX : List<Integer> -> double
    * input : the feature vector for a training instance
    * output : the value of [WeightTranspose].X 
    */
   private double getWeightTX(List<Integer> featureVector) {
      double result = 0;
      double currentWeight;
      for(Integer wordIndex: featureVector) {
         currentWeight = weights.get(wordIndex);
         result = result + currentWeight;
      }
      return result;
   }
   
   /*
    * addDeltaToWeightsVector : double, List<Integer> -> void
    * input : delta and feature vector
    * process : adds (delta * learningRate) to every weight of the feature present in feature vector
    */
   private void addDeltaToWeightsVector(double delta, List<Integer> featureVector) {
      double prod = (learningRate * delta);
      bias = bias + prod;
      for(Integer feature: featureVector) {
         weights.set(feature, weights.get(feature) + prod);
      }
   }
   
   /*
    * decide : String -> List<Double> 
    * input : the path of file containing training instances
    * output : The predictions based on the learned parameters by calculating
    *          the sigmoid value for every testing instance
    */
   public List<Double> decide(String testingPath) {
      List<Double> predictions = new ArrayList<Double>();
      try {
         Reader reader = new FileReader(testingPath);
         BufferedReader bufferedReader = new BufferedReader(reader);
         String line;
         String[] split;
         String[] featureWeightSplit;
         List<Integer> featureVector;
         double sigmoidValue;
         while((line = bufferedReader.readLine()) != null) {
            featureVector = new ArrayList<Integer>();
            split = line.split(" ");
            noOfWords = split.length;
            for(int i = 1; i<noOfWords; i++) {
               featureWeightSplit = split[i].split(":");
               featureVector.add(Integer.parseInt(featureWeightSplit[0]) - 1);
            }
            sigmoidValue = getSigmoidFunctionValue(featureVector);
            if(sigmoidValue >=0.5) {
               predictions.add(6.0);
            } else {
               predictions.add(2.0);
            }
         }
         bufferedReader.close();
      } catch (Exception e) {
         System.out.println("Cant read testing data!");
         System.exit(1);
      }
      return predictions;
   }
   
   /*
    * printWeightsAndBias : -> void
    * process: Prints the weight vector and bias
    * note : Used for debugging
    */
   public void printWeightsAndBias() {
      for(Double d : weights) {
         System.out.println(d);
      }
      System.out.println(bias);
   }
}
