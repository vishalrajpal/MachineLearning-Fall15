import java.util.Comparator;
import java.util.Map;


public class ValueComparator implements Comparator<Instance>
{
   Map<Instance, Double> map;
   public ValueComparator(Map<Instance, Double> map)
   {
      this.map = map;
   }
   
   @Override
   public int compare(Instance o1, Instance o2)
   {
      if(map.get(o1)<=map.get(o2)) {
         return -1;
      } else {//if (map.get(o1)>map.get(o2)){
         return 1;
      }
      //return 0;
   }

   
}
