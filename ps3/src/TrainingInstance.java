import java.util.ArrayList;
import java.util.List;


public class TrainingInstance
{
   private List<Integer> featureVector;
   private double label;
   
   public TrainingInstance(String label) {
      featureVector = new ArrayList<Integer>();
      if(label.equals("2.0")) {
         this.label = 0.0;
      } else {
         this.label = 1.0;
      }
   }
   
   /*
    * addFeature : int -> void
    * input : the word number present in the training instance
    * process : adds the feature to the feature vector of this training instance
    */
   public void addFeature(int wordNumber) {
      featureVector.add(wordNumber - 1);
   }
   
   /*
    * getFeatureVector : -> List<Integer>
    * output : the feature vector of this training instance
    */
   public List<Integer> getFeatureVector() {
      return featureVector;
   }
   
   /*
    * getLabel : -> double
    * output : returns the label of this training instance
    */
   public double getLabel() {
      return this.label;
   }
   
}
