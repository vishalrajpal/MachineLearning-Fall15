import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NaiveBayes
{
   ClassLabel positiveLabel;
   ClassLabel negativeLabel;
   Map<Integer, ClassLabel> classLabels;
   private int totalTrainingCount;
   private int dimensiononality;
   private Map<Integer, Integer> positions;
   private List<TrainingInstance> instances;
   public NaiveBayes(List<TrainingInstance> instances, int n) {
      classLabels = new HashMap<>();
      positions = new HashMap<>();
      positiveLabel = new ClassLabel(1);
      negativeLabel = new ClassLabel(-1);
      classLabels.put(1, positiveLabel);
      classLabels.put(-1, negativeLabel);
      this.instances = instances;
      this.dimensiononality = n;
      totalTrainingCount = 0;
      processTrainingInstances();
   }
   
   private void processTrainingInstances() {
      ClassLabel currentLabel;
      int currentPositionCount;
      for(TrainingInstance instance: instances) {
         currentLabel = classLabels.get(instance.getLabel());
         if(currentLabel != null) {
            for(Integer activePosition: instance.getActivePositions()) {
               currentPositionCount = 0;
               if(positions.containsKey(activePosition)) {
                  currentPositionCount = positions.get(activePosition);
               }
               positions.put(activePosition, currentPositionCount + 1);
               currentLabel.processPosition(activePosition);
            }        
            currentLabel.addToTrainingCount(1);
         }
         totalTrainingCount++;
      }      
   }
   
   public void learn() {      
      int currentWordCount;
      double currentPositionProbability;
      ClassLabel currentClassLabel;
      for(Map.Entry<Integer, ClassLabel> currentEntry : classLabels.entrySet()) {
         currentClassLabel = currentEntry.getValue();
         currentClassLabel.setPrior(totalTrainingCount);
         for(int currentPosition = 1; currentPosition <=this.dimensiononality; currentPosition++) {
            currentWordCount = currentClassLabel.getPositionCount(currentPosition);
            if(currentWordCount != 0) {
               currentPositionProbability = (double)(currentWordCount + 1)/(currentClassLabel.getTotalPositionCount() + positions.size());
               currentClassLabel.setPositionProbability(currentPosition, currentPositionProbability);
            }
         }
      }
   }
   
   public void decide(List<TrainingInstance> instances) {
      try {
         double currentMax = (double) Integer.MIN_VALUE;
         double currentConditionalProb;
         ClassLabel maxClassLabel;
         int correctPredictions = 0;
         for(TrainingInstance instance: instances) {
            currentMax = (double) Integer.MIN_VALUE;
            maxClassLabel = null;
            for(ClassLabel currentClassLabel: classLabels.values()) {
               currentConditionalProb = 0;
               for(Integer activePosition: instance.getActivePositions()) {
                  if(currentClassLabel.getPositionCount(activePosition) != 0)
                     currentConditionalProb += Math.log(currentClassLabel.getProbability(activePosition));
               }
               currentConditionalProb += Math.log(currentClassLabel.getPrior());
               if(currentConditionalProb>currentMax) {
                  currentMax = currentConditionalProb;
                  maxClassLabel = currentClassLabel;
               }
            }
            if(maxClassLabel.getLabel() == instance.getLabel()) {
               correctPredictions++;
            }
         }
         System.out.println(correctPredictions+" Accuracy:"+(double)correctPredictions/instances.size());
      } catch (Exception e) {
         
      }
   }
}
