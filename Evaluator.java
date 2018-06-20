import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Evaluator {

	public static void main(String[] args) {
		String[] distanceMeasureStrings = { "eu", "sts", "sync" };
		String[] medianStrategyStrings = { "XmYm", "XmY" };
		int[] ks = { 2, 3, 6, 9, 12, 15, 20, 30 };

		// Artifical data set

		System.out.println("----- Starting evaluation of the artificial data set ...");

		Dataset o = Dataset.fromJSON("../Perfect Data Set/perfect_grid_1000_60_2000_2000.json");

		List<Evaluator.ResultRecord> results = new LinkedList<Evaluator.ResultRecord>();

		for (String dMString : distanceMeasureStrings) {
			for (String mSString : medianStrategyStrings) {
				for (int k : ks) {
					System.out.println("Starting evaluation for " + dMString + " / " + mSString + " / " + k + " ...");

					Dataset p = Dataset.fromJSON("../Perfect Data Set/perfect_grid_1000_60_2000_2000_PROTECTED_" + dMString + "_" + mSString + "_k" + k + ".json");

					double dr = DisclosureRisk.computeViaRecordLinkage(o, p);
					System.out.println("Disclosure risk: " + dr);

					double il = InformationLoss.compute(o, p);
					System.out.println("Information loss: " + il);

					results.add(new Evaluator.ResultRecord(dMString, mSString, k, dr, il));
				}
			}
		}

		Evaluator.ResultRecord.resultsToFile(results, "../Perfect Data Set/evaluation_results.csv");

		// Real data set

		System.out.println("----- Starting evaluation of the real data set ...");

		o = Dataset.fromJSON("../San Francisco Taxi Data/CabSpotting_pandas-downsampled.json");

		results = new LinkedList<Evaluator.ResultRecord>();

		for (String mSString : medianStrategyStrings) {
			for (int k : ks) {
				Dataset p = Dataset.fromJSON("../San Francisco Taxi Data/CabSpotting_pandas-downsampled_PROTECTED_sync_" + mSString + "_k" + k + ".json");

				double dr = DisclosureRisk.computeViaRecordLinkage(o, p);
				System.out.println("Disclosure risk: " + dr);
					
				double il = InformationLoss.compute(o, p);
				System.out.println("Information loss: " + il);

				results.add(new Evaluator.ResultRecord("sync", mSString, k, dr, il));
			}
		}

		Evaluator.ResultRecord.resultsToFile(results, "../San Francisco Taxi Data/evaluation_results.csv");
		
	}

	private static class ResultRecord {
		public String distanceMeasureString = "None";
		public String medianStrategyString = "None";
		public int k = -1;

		public double dr = -1.0;
		public double il = -1.0;

		public ResultRecord(String distanceMeasureString, String medianStrategyString, int k, double dr, double il) {
			this.distanceMeasureString = distanceMeasureString;
			this.medianStrategyString = medianStrategyString;
			this.k = k;
			this.dr = dr;
			this.il = il;
		}

		public static void resultsToFile(List<ResultRecord> results, String filePath) {
			System.out.print("\rWriting results to file ...");

			File resultsFile = new File(filePath);
			try (BufferedWriter w = new BufferedWriter(new FileWriter(resultsFile))) {
				for (Evaluator.ResultRecord result : results) {
					w.write(result.distanceMeasureString + "," + result.medianStrategyString + "," + result.k + "," + result.dr + "," + result.il + "\n");
				}
			} catch (FileNotFoundException e) {
				System.err.println("Could not find file at path \"" + resultsFile.getName() + "\".");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
				System.exit(1);
			}

			System.out.print("\rWrote results to file     \n");
		}
	}

}
