import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Utilities
{
   public static List<TrainingInstance> readTrainingFile(File trainingInstances) {
      List<TrainingInstance> instances = new ArrayList<>();
      try {
         FileReader fileReader = new FileReader(trainingInstances);
         BufferedReader bufferedReader = new BufferedReader(fileReader);
         String currentInstanceString;
         TrainingInstance currentInstance;
         while((currentInstanceString = bufferedReader.readLine()) != null) {
            currentInstance = new TrainingInstance(currentInstanceString);
            instances.add(currentInstance);
         }
         bufferedReader.close();
      } catch (Exception e) {
         System.err.println("Unable to read training File!!");
         e.printStackTrace();
         System.exit(1);
      }
      return instances;
   }
   
   public static double getDotProduct(double[] weightVector, Set<Integer> featureVector) {
      double dotProduct = 0;
      int noOfWeights = weightVector.length;
      for(int weightCounter = 0; weightCounter<noOfWeights; weightCounter++) {
         if(weightVector[weightCounter] != 0 &&  featureVector.contains(weightCounter + 1)) {
            dotProduct += weightVector[weightCounter];
         }
      }
      return dotProduct;
   }
   
   public static void initializeArrayWithValue(double[] array, double value) {
      int arrayLength = array.length;
      for(int i = 0; i<arrayLength; i++) {
         array[i] = value;
      }
   }
}
