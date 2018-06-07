public class Evaluator {

	public static void main(String[] args) {
		//Dataset o = Dataset.fromJSON("../Geolife Trajectories 1.3/MS_GeoLife_pandas-downsampled.json");
		//Dataset p = Dataset.fromJSON("../Geolife Trajectories 1.3/MS_GeoLife_pandas-downsampled_PROTECTED.json");
		Dataset o = Dataset.fromJSON("../San Francisco Taxi Data/CabSpotting_pandas-downsampled.json");
		Dataset p = Dataset.fromJSON("../San Francisco Taxi Data/CabSpotting_pandas-downsampled_PROTECTED.json");
		
		double dr = DisclosureRisk.computeViaRecordLinkage(o, p);
		double il = InformationLoss.compute(o, p);

		System.out.println("Disclosure risk: " + dr);
		System.out.println("Information loss: " + il);
		
		Plotter.plotRUMap(dr, il);
	}

}
