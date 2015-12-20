import java.util.HashSet;
import java.util.Set;


public class TrainingInstance
{
   private Set<Integer> activePositions;
   private int label;
   public TrainingInstance(String instanceString) {      
      String[] split = instanceString.split(" ");
      this.activePositions = new HashSet<>(split.length - 1);
      setLabel(split[0]);
      setActivePositions(split);
   }
   
   private void setLabel(String labelString) {
      char labelSign = labelString.charAt(0);
      this.label = Integer.parseInt(labelString.substring(1));
      if(labelSign == '-') {
         this.label *= -1;
      }
   }
   
   private void setActivePositions(String[] trainingInstance) {
      int noOfActivePositions = trainingInstance.length;
      String[] positionSplit;
      for(int splitCounter = 1; splitCounter<noOfActivePositions; splitCounter++) {
         positionSplit = trainingInstance[splitCounter].split(":");
         activePositions.add(Integer.parseInt(positionSplit[0]));
      }
   }
   
   public int getLabel() {
      return this.label;
   }
   
   public Set<Integer> getActivePositions() {
      return activePositions;
   }
}