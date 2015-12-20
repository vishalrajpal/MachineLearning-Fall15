import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class SGDClassifier
{
   /*
    * main : String[] -> void
    * Input : The arguments to the program
    * Output : Learns the parameters W and bias, processes the testing file and 
    *          writes the predictions to 'predictions.pl'
    */
   public static void main(String[] args) {
      LogisticRegression lr = new LogisticRegression("features.lexicon", "train.libsvm");
      lr.learn();
      FileWriter writer;
      try
      {
         writer = new FileWriter("predictions.lr");
         List<Double> predictions = lr.decide("test.libsvm");
         for(Double d : predictions) {
            writer.write(d+"\n");
         }
         writer.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
