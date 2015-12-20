import java.util.HashMap;
import java.util.Map;


public class ClassLabel
{
   private int label;
   private Map<Integer, Double> positionProbability;
   private Map<Integer, Integer> positionCounts;
   private double prior;
   private int trainingCount;
   private int totalPositionCount;
   public ClassLabel(int label) {
      this.label = label;
      trainingCount = 0;
      positionCounts = new HashMap<>();
      positionProbability = new HashMap<>();
      this.totalPositionCount = 0;
      
   }
   
   public int getLabel() {
      return label;
   }
   
   public void addToTrainingCount(int toAdd) {
      this.trainingCount += toAdd;
   }
   
   public void setPrior(int totalCount) {
      this.prior = ((double)this.trainingCount)/totalCount;
   }

   public double getPrior() {
      return prior;
   }
   
   public void setPositionProbability(Integer position, double probability) {
      positionProbability.put(position, probability);
    }
    
    public double getProbability(Integer position) {
       double prob = 0;
       if(positionProbability.containsKey(position)){
          prob = positionProbability.get(position);
       }
       return prob;
    }    
    
    public int getPositionCount(Integer position) {
       int positionCount = 0;
       if(positionCounts.containsKey(position)) {
          positionCount = positionCounts.get(position);
       }
       return positionCount;
    }
    
    public int getTotalPositionCount() {
       return this.totalPositionCount;
    }
    
    public void processPosition(Integer position) {
       int currentCount = 0;
       if(positionCounts.containsKey(position)) {
          currentCount = positionCounts.get(position);
       }
       positionCounts.put(position, currentCount + 1);
       totalPositionCount++;
    }
}
