import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class DisclosureRisk {

    public static double computeViaRecordLinkage(Dataset o, Dataset p) {
        Dataset tempo = new Dataset(o);
        Dataset tempp = new Dataset(p);

        // Assign new ids so they work for synchronised distance, but can still be reidentified
        for (Trajectory r : tempo.getTrajectories()) r.id *= 2;
        for (Trajectory r : tempp.getTrajectories()) r.id = r.id * 2 + 1;

        // Remove all duplicates from the protected data set and keep a dictionary about which representative remains
        Map<Integer, Integer> clusterRepDict = new HashMap<Integer, Integer>();
        Iterator<Trajectory> repIt = tempp.getTrajectories().iterator();
        while (repIt.hasNext()) {
            Trajectory rep = repIt.next();

            Iterator<Trajectory> repedIt = tempp.getTrajectories().iterator();
            while(repedIt.hasNext()) {
                Trajectory reped = repedIt.next();

                if (reped.equals(rep)) {
                    clusterRepDict.put(reped.id, rep.id);
                    tempp.remove(reped);
                }
            }
        }

        // Create and prepare the synchronised distance
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

        // Count how many were linked to a trajectory just like their protected version
        int linkedCount = 0;
        for (Trajectory[] pair : lps) {
            if (clusterRepDict.get(pair[0].id + 1) == pair[1].id) linkedCount++;
        }

        return linkedCount / lps.length;
    }

}