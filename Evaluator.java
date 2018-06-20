public class Evaluator {

	public static void main(String[] args) {
		String[] distanceMeasureStrings = { "eu", "sts", "sync" };
		String[] medianStrategyStrings = { "XmYm", "XmY" };
		int[] ks = { 2, 3, 6, 9, 12, 15, 20, 30 };

		// Artifical data set

		Dataset o1 = Dataset.fromJSON("../Perfect Data Set/perfect_grid_1000_60_2000_2000.json");

		for (String dMString : distanceMeasureStrings) {
			for (String mSString : medianStrategyStrings) {
				for (int k : ks) {
					Dataset p = Dataset.fromJSON("../Perfect Data Set/perfect_grid_1000_60_2000_2000_PROTECTED_" + dMString + "_" + mSString + "_k" + k + ".json");

					double dr = DisclosureRisk.computeViaRecordLinkage(o1, p);
					double il = InformationLoss.compute(o1, p);

					System.out.println("Disclosure risk: " + dr);
					System.out.println("Information loss: " + il);
				}
			}
		}

		// Real data set

		Dataset o2 = Dataset.fromJSON("../San Francisco Taxi Data/CabSpotting_pandas-downsampled.json");

		for (String mSString : medianStrategyStrings) {
			for (int k : ks) {
				Dataset p = Dataset.fromJSON("../San Francisco Taxi Data/CabSpotting_pandas-downsampled_PROTECTED_sync_" + mSString + "_k" + k + ".json");

				double dr = DisclosureRisk.computeViaRecordLinkage(o2, p);
				double il = InformationLoss.compute(o2, p);

				System.out.println("Disclosure risk: " + dr);
				System.out.println("Information loss: " + il);
			}
		}
		
	}

}
