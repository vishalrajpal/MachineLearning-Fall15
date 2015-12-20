import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Perceptron
{
   private List<TrainingInstance> instances;
   private double[] weightVector;
   private double bias;
   private int N;
   private int noOfRounds;

   private double[] withoutMarginBestweightVector;
   private double withoutMarginBestBias;
   private double withoutMarginBestGamma;
   private double withoutMarginBestIta;

   private double[] withMarginBestweightVector;
   private double withMarginBestBias;
   private double withMarginBestGamma;
   private double withMarginBestIta;

   private Set<Double> gammasWithMargin;
   private Set<Double> itasWithMargin;
   private Set<Double> gammasWithoutMargin;
   private Set<Double> itasWithoutMargin;

   private double[] withoutMarginBatchWeightVector;
   private double[] withMarginBatchWeightVector;
   private double withoutMarginBatchBias;
   private double withMarginBatchBias;
   public Perceptron(List<TrainingInstance> instances, int N) {
      this.instances = instances;
      this.bias = 0;
      this.N = N;
      this.noOfRounds = 20;
      this.gammasWithoutMargin = new HashSet<>(Arrays.asList(0.0));
      this.itasWithoutMargin = new HashSet<>(Arrays.asList(1.0));
      this.gammasWithMargin = new HashSet<>(Arrays.asList(1.0));      
      this.itasWithMargin = new HashSet<>(Arrays.asList(1.5,0.25,0.03,0.005,0.001));      
   }

   public void tune(Set<Integer> D1, Set<Integer> D2) {
      tunePerceptronWithoutMargin(D1, D2);
      tunePerceptronWithMargin(D1, D2);      
   }

   private void tunePerceptronWithoutMargin(Set<Integer> D1, Set<Integer> D2) {
      Integer withoutMarginMinMistakes = Integer.MAX_VALUE;      
      int mistakes;
      for(Double gamma: gammasWithoutMargin) {
         for(Double ita: itasWithoutMargin) {
            tune(D1, gamma, ita);
            mistakes = evaluateOnD2(D2, gamma, ita);
            if(mistakes<withoutMarginMinMistakes) {
               withoutMarginBestGamma = gamma;
               withoutMarginBestIta = ita;
               withoutMarginBestweightVector = weightVector;
               withoutMarginBestBias = bias;
               withoutMarginMinMistakes = mistakes;
            }
         }
      }
      double accuracy = (float)(D2.size() - withoutMarginMinMistakes)/D2.size();
      System.out.println("Perceptron Without Margin Best Parameters:Gamma"+withoutMarginBestGamma+" Ita:"+withoutMarginBestIta+" Accuracy(D2):"+accuracy);
   }

   private void tunePerceptronWithMargin(Set<Integer> D1, Set<Integer> D2) {
      Integer withMarginMinMistakes = Integer.MAX_VALUE;      
      int mistakes;
      for(Double gamma: gammasWithMargin) {
         for(Double ita: itasWithMargin) {
            tune(D1, gamma, ita);
            mistakes = evaluateOnD2(D2, gamma, ita);
            if(mistakes<withMarginMinMistakes) {
               withMarginBestGamma = gamma;
               withMarginBestIta = ita;
               withMarginBestweightVector = weightVector;
               withMarginBestBias = bias;
               withMarginMinMistakes = mistakes;
            }
         }
      }
      double accuracy = (float)(D2.size() - withMarginMinMistakes)/D2.size();
      System.out.println("Perceptron With Margin Best Parameters:Gamma"+withMarginBestGamma+" Ita:"+withMarginBestIta+" Accuracy(D2):"+accuracy);
   }

   private void tune(Set<Integer> D1, Double gamma, Double ita) {
      this.weightVector = new double[this.N];
      this.bias = 0;
    //  int totalNoOfMistakes = 0;
     // int noOfLocalMistakes;
      TrainingInstance instance;
      for(int roundCounter = 1; roundCounter<=this.noOfRounds; roundCounter++) {
        // noOfLocalMistakes = 0;
         for(Integer instancePosition: D1) {
            instance = instances.get(instancePosition-1);
            if(isAMistake(instance, gamma)) {
              // noOfLocalMistakes++;
               updateWeightVector(instance, ita);
               //b = b + (ita * y)
               bias = bias + (ita * instance.getLabel());
            }
         }
       //  totalNoOfMistakes += noOfLocalMistakes;
      }
   }

   private boolean isAMistake(TrainingInstance instance, double gamma) {
      boolean isAMistake = false;
      Set<Integer> featureVector = instance.getActivePositions();
      double dotProduct = Utilities.getDotProduct(weightVector, featureVector) + bias;
      int actualLabel = instance.getLabel();
      double predictionActivation = actualLabel * dotProduct;
      if(predictionActivation <= gamma) {
         isAMistake = true;
      }
      return isAMistake;
   }

   private boolean isAMistake(TrainingInstance instance, double[] weightVector, double gamma, double bias) {
      boolean isAMistake = false;
      Set<Integer> featureVector = instance.getActivePositions();
      double dotProduct = Utilities.getDotProduct(weightVector, featureVector) + bias;
      int actualLabel = instance.getLabel();
      double predictionActivation = actualLabel * dotProduct;
      if(predictionActivation <= gamma) {
         isAMistake = true;
      }
      return isAMistake;
   }
   
   private void updateWeightVector(TrainingInstance instance, double ita) {
      Set<Integer> featureVector = instance.getActivePositions();
      for(Integer activeFeature: featureVector) {
         //w = w + (ita * y * x) 
         weightVector[activeFeature - 1] = weightVector[activeFeature - 1] + (ita * instance.getLabel()); 
      }
   }

   private void updateWeightVector(TrainingInstance instance, double[] weightVector, double ita) {
      Set<Integer> featureVector = instance.getActivePositions();
      for(Integer activeFeature: featureVector) {
         //w = w + (ita * y * x) 
         weightVector[activeFeature - 1] = weightVector[activeFeature - 1] + (ita * instance.getLabel()); 
      }
   }

   private int evaluateOnD2(Set<Integer> D2, Double gamma, Double ita) {
      int mistakes = 0;
      TrainingInstance instance;
      for(Integer instancePosition: D2) {
         instance = this.instances.get(instancePosition - 1);
         if(isAWrongPrediction(instance, weightVector, bias)) {
            mistakes++;
         }
      }
      return mistakes;      
   }

   public void decide() {
      int noOfInstances = this.instances.size();
      String withoutMarginPlotFileName = "WithoutMarginPerceptronPlot"+this.N+".csv";
      String withMarginPlotFileName = "WithMarginPerceptronPlot"+this.N+".csv";
      int mistakesWithoutMargin = decide(withoutMarginBestweightVector, withoutMarginBestBias, withoutMarginPlotFileName);
      System.out.println("Perceptron Mistakes Without Margin:"+mistakesWithoutMargin);
      System.out.println("Perceptron Accuracy Without Margin:"+(float)(noOfInstances - mistakesWithoutMargin)/noOfInstances);
      int mistakesWithMargin = decide(withMarginBestweightVector, withMarginBestBias, withMarginPlotFileName);
      System.out.println("Perceptron Mistakes With Margin:"+mistakesWithMargin);
      System.out.println("Perceptron Accuracy With Margin:"+(float)(noOfInstances - mistakesWithMargin)/noOfInstances);
   }

   private int decide(double[] weightVector, double bias, String fileName) {
      int mistakes = 0;
      try {         
         File plotFile = new File(fileName);
         int instanceCount = 0;
         FileWriter plotWriter = new FileWriter(plotFile);
         BufferedWriter bufferedWriter = new BufferedWriter(plotWriter);
         bufferedWriter.write("NoOfInstances,NoOfMistakes\n");
         for(TrainingInstance instance : instances) {
            instanceCount++;
            if(isAWrongPrediction(instance, weightVector, bias)) {
               mistakes++;
            }
            bufferedWriter.write(instanceCount+","+mistakes+"\n");
         }
         bufferedWriter.close();
      } catch (Exception e) {
         System.out.println("Cannot write plot file Perceptron");
         e.printStackTrace();
         System.exit(1);
      }
      return mistakes;
   }

   private boolean isAWrongPrediction(TrainingInstance instance, double[] weightVector, double bias) {
      boolean isAWrongPrediction = false;
      Set<Integer> featureVector = instance.getActivePositions();
      double dotProduct = Utilities.getDotProduct(weightVector, featureVector) + bias;
      int actualLabel = instance.getLabel();
      double predictionActivation = actualLabel * dotProduct;
      if(predictionActivation < 0) {
         isAWrongPrediction = true;
      }
      return isAWrongPrediction;
   }

   public void converge(int S) {
      String withoutMarginPlotFileName = "WithoutMarginPerceptronConvergence.csv";
      String withMarginPlotFileName = "WithMarginPerceptronConvergence.csv";
      System.out.println("Perceptron Without Margin Converge");
      converge(S, withoutMarginBestGamma, withoutMarginBestIta, withoutMarginPlotFileName);
      System.out.println("Perceptron With Margin Converge");
      converge(S, withMarginBestGamma, withMarginBestIta, withMarginPlotFileName);
   }

   private void converge(int S, double gamma, double ita, String fileName) {
      try {         
         File convergenceFile = new File(fileName);
         FileWriter writer = new FileWriter(convergenceFile, true);
         BufferedWriter bufferedWriter = new BufferedWriter(writer);         
         this.weightVector = new double[this.N];
         this.bias = 0;
         int totalNoOfMistakes = 0;
         int correctPredictions = 0;
         int iterationNumber = 0;
         int instanceCount = 0;
         int bestCorrectPredictions = 0;
         while(correctPredictions<S) {
            iterationNumber++;
            bestCorrectPredictions = 0;
            totalNoOfMistakes = 0;
            instanceCount = 0;
            correctPredictions = 0;
            for(TrainingInstance instance: this.instances) {
               instanceCount++;
               if(isAMistake(instance, gamma)) {
                  totalNoOfMistakes++;
                  if(bestCorrectPredictions<correctPredictions) {
                     bestCorrectPredictions = correctPredictions;
                  }
                  correctPredictions = 0;
                  updateWeightVector(instance, ita);
                  //b = b + (ita * y)
                  bias = bias + (ita * instance.getLabel());
               } else {
                  correctPredictions++;
                  if(correctPredictions==S) {
                     break;
                  }
               }
            }
            System.out.print(" "+iterationNumber+":"+bestCorrectPredictions);
         }
         bufferedWriter.write(this.N+","+totalNoOfMistakes+"\n");
         bufferedWriter.close();
         System.out.println("\nNo Of Iterations:"+iterationNumber+":Mistakes:"+totalNoOfMistakes+" Instances:"+instanceCount);
      } catch (Exception e) {
         System.out.println("Error in converge!");
         e.printStackTrace();
      }
   }

   public void batch() {
      System.out.println("Perceptron Without Margin Batch");
      withoutMarginBatchWeightVector = new double[this.N];
      withoutMarginBatchBias = batch(withoutMarginBatchWeightVector, withoutMarginBestGamma, withoutMarginBestIta);
      System.out.println("Perceptron With Margin Batch");
      withMarginBatchWeightVector = new double[this.N];
      withMarginBatchBias = batch(withMarginBatchWeightVector, withMarginBestGamma, withMarginBestIta);
      System.out.println(withoutMarginBatchWeightVector.length+" "+withMarginBatchWeightVector.length);
   }

   private double batch(double[] weightVector, double gamma, double ita) {      
      double localBias = 0;
      for(int roundCounter = 1; roundCounter<=this.noOfRounds; roundCounter++) {
         for(TrainingInstance instance: this.instances) {
            if(isAMistake(instance, weightVector, gamma, localBias)) {
               updateWeightVector(instance, weightVector, ita);
               //b = b + (ita * y)
               localBias = localBias + (ita * instance.getLabel());
            }
         }
      }
      return localBias;
   }

   public void batchEvaluate(List<TrainingInstance> cleanInstances) {      
      int mistakes = batchEvaluate(withoutMarginBatchWeightVector, withoutMarginBatchBias, cleanInstances);
      float accuracy = (float)(cleanInstances.size() - mistakes)/cleanInstances.size();
      System.out.println("Perceptron Without Margin Batch Mistakes:"+mistakes+" Accuracy(Test):"+accuracy);
      mistakes = batchEvaluate(withMarginBatchWeightVector, withMarginBatchBias, cleanInstances);
      accuracy = (float)(cleanInstances.size() - mistakes)/cleanInstances.size();
      System.out.println("Perceptron With Margin Batch Mistakes:"+mistakes+" Accuracy(Test):"+accuracy);
   }

   private int batchEvaluate(double[] weightVector, double bias, List<TrainingInstance> testingInstances) {
      int mistakes = 0;
      //int instanceCount = 0;
      for(TrainingInstance instance : testingInstances) {
         //instanceCount++;
         if(isAWrongPrediction(instance, weightVector, bias)) {
            mistakes++;
         }
      }
      return mistakes;
   }
}
