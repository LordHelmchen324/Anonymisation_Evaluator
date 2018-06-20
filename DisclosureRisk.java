import java.util.LinkedList;
import java.util.List;

class DisclosureRisk {

    public static double computeViaRecordLinkage(Dataset o, Dataset p) {
        System.out.print("\rDisclosure risk: Copying data sets ...");
        Dataset tempo = new Dataset(o);
        Dataset tempp = new Dataset(p);
        System.out.print("\rDisclosure risk: Copied data sets       \n");

        // Assign new ids so they work for synchronised distance, but can still be reidentified
        System.out.print("\rDisclosure risk: Assigning new IDs ...");
        for (Trajectory r : tempo.getTrajectories()) r.id *= 2;
        for (Trajectory r : tempp.getTrajectories()) r.id = r.id * 2 + 1;
        System.out.print("\rDisclosure risk: Assigned new IDs       \n");

        // Create and prepare the synchronised distance
        DistanceMeasure dM = new SynchronisedDistance();
        List<Dataset> ds = new LinkedList<Dataset>(); ds.add(tempo); ds.add(tempp);
        dM.createSupportData(ds);

        // Link trajectories
        Trajectory[][] lps = new Trajectory[tempo.size()][2];
        int i = 0;
        for (Trajectory ro : tempo.getTrajectories()) {
            System.out.print("\rDisclosure risk: Linking trajectories ... " + ((i + 1) * 100 / tempo.size()) + "%");

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
        System.out.print("\n");

        // Count how many were linked to a trajectory just like their protected version
        int linkedCount = 0;
        for (Trajectory[] pair : lps) {
            System.out.print("\rDisclosure risk: Correctly linked trajectories ... " + linkedCount + " / " + tempo.size());

            Trajectory actualProtected = tempp.getTrajectoryById(pair[0].id + 1);
            if (actualProtected != null && pair[1].equals(actualProtected)) linkedCount++;
        }
        System.out.print("\n");

        return (double)linkedCount / (double)lps.length;
    }

}