public class Evaluator {

	public static void main(String[] args) {
		Dataset o = Dataset.fromJSON("../Anonymiser/randomPerfect1000.json");
		Dataset p = Dataset.fromJSON("../Anonymiser/randomPerfect1000_Result.json");

		double dr = DisclosureRisk.computeViaRecordLinkage(o, p);
		double il = InformationLoss.compute(o, p);
		
		Plotter.plotRUMap(dr, il);
	}

}
