public class Evaluator {

	public static void main(String[] args) {
		Dataset o = Dataset.fromJSON(args[1]);
		Dataset p = Dataset.fromJSON(args[2]);

		double dr = DisclosureRisk.computeViaRecordLinkage(o, p);
		double il = InformationLoss.compute(o, p);
		
		Plotter.plotRUMap(dr, il);
	}

}
