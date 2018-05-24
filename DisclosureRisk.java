import java.util.LinkedList;
import java.util.List;

class DisclosureRisk {

    public static double computeViaRecordLinkage(Dataset o, Dataset p) {
        Dataset tempo = new Dataset(o);
        Dataset tempp = new Dataset(p);

        for (Trajectory r : tempo.getTrajectories()) r.id *= 2;
        for (Trajectory r : tempp.getTrajectories()) r.id = r.id * 2 + 1;

        DistanceMeasure dM = new SynchronisedDistance();
        List<Dataset> ds = new LinkedList<Dataset>(); ds.add(tempo); ds.add(tempp);
        dM.createSupportData(ds);

        // Link trajectories
        Trajectory[][] lps = new Trajectory[tempo.size()][2];
        int i = 0;
        for (Trajectory ro : tempo.getTrajectories()) {
            Trajectory closest = null;
            double closestDistance = Double.POSITIVE_INFINITY;
            for (Trajectory rp : tempp.getTrajectories()) {
                double distance = dM.computeDistance(ro, rp);

                if (closest == null || distance < closestDistance) {
                    closest = rp;
                    closestDistance = distance;
                }
            }

            lps[i][0] = ro;
            lps[i][1] = closest;

            i++;
        }

        double sum = 0.0;
        for (Trajectory[] pair : lps) {
            sum += dM.computeDistance(pair[0], pair[1]);
        }

        return sum / lps.length;
    }

}