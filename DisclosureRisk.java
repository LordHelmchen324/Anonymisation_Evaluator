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

    /*public static double computeViaRecordLinkage(Dataset o, Dataset p) {
        Trajectory[][] lp = DisclosureRisk.linkTrajectories(o, p);

        DistanceMeasure eu = new EuclideanDistance();
        DistanceMeasure sts = new ShortTimeSeriesDistance();

        double euSum = 0.0;
        double stsSum = 0.0;
        int n = 0;
        for (Trajectory[] pair : lp) {
            euSum += eu.computeDistance(pair[0], pair[1]);
            stsSum += sts.computeDistance(pair[0], pair[1]);
            n = pair[0].length();
        }

        return Math.max(euSum, stsSum) / (lp.length * n);
    }

    private static Trajectory[][] linkTrajectories(Dataset o, Dataset p) {
        Trajectory[][] lp = new Trajectory[o.size()][2];

        DistanceMeasure eu = new EuclideanDistance();
        DistanceMeasure sts = new ShortTimeSeriesDistance();

        int i = 0;
        for (Trajectory ro : o.getTrajectories()) {
            Trajectory closest = null;
            double closestDistance = Double.POSITIVE_INFINITY;
            for (Trajectory rp : p.getTrajectories()) {
                double distance = Math.max(eu.computeDistance(ro, rp), sts.computeDistance(ro, rp));

                if (closest == null || distance < closestDistance) {
                    closest = rp;
                    closestDistance = distance;
                }
            }

            lp[i][0] = ro;
            lp[i][1] = closest;

            i++;
        }

        return lp;
    }*/

}