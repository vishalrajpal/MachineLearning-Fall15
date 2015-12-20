import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;


public class EmailTypePrediction
{
   public static void main(String[] args) {
      NaiveBayes nb = new NaiveBayes("processed/vocabulary.txt");
      nb.addLabel("articles", 1.0, "processed/articles.train.txt");
      nb.addLabel("corporate", 2.0, "processed/corporate.train.txt");
      nb.addLabel("enron_t_s", 3.0, "processed/enron_t_s.train.txt");
      nb.addLabel("enron_travel_club", 4.0, "processed/enron_travel_club.train.txt");
      nb.addLabel("hea_nesa", 5.0, "processed/hea_nesa.train.txt");
      nb.addLabel("personal", 6.0, "processed/personal.train.txt");
      nb.addLabel("systems", 7.0, "processed/systems.train.txt");
      nb.addLabel("tw_commercial_group", 8.0, "processed/tw_commercial_group.train.txt");
      nb.setTrainingDataSet("processed/train-files.txt");
      nb.learn();
      nb.setTestingDataSet("processed/test.txt");
      nb.decide();
      try {
         FileWriter writer = new FileWriter("predictions.nb");
         BufferedWriter bufferedWriter = new BufferedWriter(writer);
         List<Double> predicted = nb.getPredictedSet();
         for(Double d : predicted) {
            bufferedWriter.write(d + "\n");
         }
         bufferedWriter.close();
      } catch (Exception e) {
         
      }
   }
}
