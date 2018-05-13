public class Evaluator {

	public static void main(String[] args) {
		Dataset o = Dataset.fromJSON("../Anonymiser/randomPerfect1000.json");
		Dataset p = Dataset.fromJSON("../Anonymiser/randomPerfect1000_Result.json");

		double dr = DisclosureRisk.computeViaRecordLinkage(o, p);
		double il = InformationLoss.compute(o, p);

		System.out.println("Disclosure risk: " + dr);
		System.out.println("Information loss: " + il);
		
		Plotter.plotRUMap(dr, il);
	}

}
