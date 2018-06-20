import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        // Remove all duplicates from the protected data set and keep a dictionary about which representative remains
        Dataset reps = new Dataset();
        Map<Integer, Integer> clusterRepDict = new HashMap<Integer, Integer>();
        int duplDone = 1;
        for (Trajectory r : tempp.getTrajectories()) {
            System.out.print("\rDisclosure risk: Removing duplicates ... " + (duplDone * 100 / tempp.size()) + "%");

            boolean containsEqual = false;
            int equalId = -1;
            for (Trajectory s : reps.getTrajectories()) {
                if (s.equals(r)) {
                    containsEqual = true;
                    equalId = s.id;
                }
            }

            if (!containsEqual) {
                reps.add(r);
                equalId = r.id;
            }

            clusterRepDict.put(r.id, equalId);

            duplDone++;
        }
        System.out.print("\n");

        // Create and prepare the synchronised distance
        DistanceMeasure dM = new SynchronisedDistance();
        List<Dataset> ds = new LinkedList<Dataset>(); ds.add(tempo); ds.add(reps);
        dM.createSupportData(ds);

        // Link trajectories
        Trajectory[][] lps = new Trajectory[tempo.size()][2];
        int i = 0;
        for (Trajectory ro : tempo.getTrajectories()) {
            System.out.print("\rDisclosure risk: Linking trajectories ... " + ((i + 1) * 100 / tempo.size()) + "%");

            Trajectory closest = null;
            double closestDistance = Double.POSITIVE_INFINITY;
            for (Trajectory rp : reps.getTrajectories()) {
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
            System.out.print("\rDisclosure risk: Correctly linked trajectories ... " + linkedCount);

            Integer expectedClosest = clusterRepDict.get(pair[0].id + 1);
            if (expectedClosest != null && expectedClosest == pair[1].id) linkedCount++;
        }
        System.out.print("\n");

        return (double)linkedCount / (double)lps.length;
    }

}