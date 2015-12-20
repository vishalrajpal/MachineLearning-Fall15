
public class Attribute<T>
{
   private T value;
   private boolean isMissingValue;
   public Attribute(T value, boolean isMissingValue) {
      this.value = value;
      this.isMissingValue = isMissingValue;
   }
   
   public boolean isMissingValue() {
      return isMissingValue;
   }

   public T getValue()
   {
      return value;
   }
   
   public void setValue(T value) {
      if(value != null) {
         this.value = value;
         isMissingValue = false;
      }
   }
}
