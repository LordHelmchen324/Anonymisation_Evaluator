import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SynchronisedDistance implements DistanceMeasure {

    private double[][] distanceGraph;
    private double[][] shortestDistanceMatrix;

    @Override
	public void createSupportData(Dataset d) {
        System.out.println(" -> SynchronisedDistance: Creating support data ...");

        System.out.println("    > Assigning indicies as IDs to the trajectories ...");
        int i = 0;
        for (Trajectory r : d.getTrajectories()) {
            r.id = i;
            i++;
        }

        System.out.println("    > Copying dataset ... ");
        Dataset temp = new Dataset(d);

        System.out.println("    > Synchronising trajectories ...");
        SynchronisedDistance.synchroniseTrajectories(temp);

        System.out.println("    > Building distance graph ...");
        this.distanceGraph = SynchronisedDistance.makeDistanceGraph(temp);

        System.out.println("    > Computing shortest distance matrix ...");
        this.shortestDistanceMatrix = SynchronisedDistance.computeShortestDistanceMatrix(this.distanceGraph);
    }

    private static void synchroniseTrajectories(Dataset d) {
        // Get a set of all timestamps of all existing places within the data set
        System.out.println("      > Getting set of all timestamps ...");
        Set<Long> ts = new HashSet<Long>();
        for (Trajectory tj : d.getTrajectories()) {
            ts.addAll(tj.getTimestamps());
        }

        for (Trajectory r : d.getTrajectories()) {
            System.out.println("      > Synchronising trajectory of length " + r.length() + " ...");

            long minT = Collections.min(r.getTimestamps());
            long maxT = Collections.max(r.getTimestamps());

            // For all timestamps in the data set ...
            for (long t : ts) {
                // If the timestamp is withing the time frame the Trajectory was recorded ...
                if (minT < t && t < maxT) {
                    // If the trajectory has no place for timestamp t ... add an interpolated one
                    if (!r.getTimestamps().contains(t)) {
                        long timeBefore = minT;
                        for (long rT: r.getTimestamps()) {
                            if (rT < t && (t - rT < t - timeBefore || timeBefore == -1)) timeBefore = rT;
                        }
                        Place placeBefore = r.getPlaceAtTime(timeBefore);

                        long timeAfter = maxT;
                        for (long rT: r.getTimestamps()) {
                            if (rT > t && (rT - t < timeAfter - t || timeAfter == -1)) timeAfter = rT;
                        }
                        Place placeAfter = r.getPlaceAtTime(timeAfter);

                        long xInterpolated = placeBefore.getX() + (placeAfter.getX() - placeBefore.getX()) / (timeAfter - timeBefore);
                        long yInterpolated = placeBefore.getY() + (placeAfter.getY() - placeBefore.getY()) / (timeAfter - timeBefore);
                        r.add(t, new Place(xInterpolated, yInterpolated));
                    }
                }
            }
        }
    }

    private static double[][] makeDistanceGraph(Dataset temp) {
        List<Trajectory> trajectories = temp.getTrajectories();

        double[][] distanceGraph = new double[trajectories.size()][trajectories.size()];

        for (Trajectory r : trajectories) {
            for (Trajectory s : trajectories) {
                double d;

                if (s == r) {
                    d = 0;
                } else if (SynchronisedDistance.percentContemporary(r, s) > 0) {
                    d = SynchronisedDistance.directDistance(r, s);
                } else {
                    d = Double.POSITIVE_INFINITY;
                }

                distanceGraph[r.id][s.id] = distanceGraph[s.id][r.id] = d;
            }
        }

        return distanceGraph;
    }

    private static double percentContemporary(Trajectory r, Trajectory s) {
        long rFirstT = Collections.min(r.getTimestamps());
        long rLastT = Collections.max(r.getTimestamps());
        long sFirstT = Collections.min(s.getTimestamps());
        long sLastT = Collections.max(s.getTimestamps());

        long I = Math.max(Math.min(rLastT, sLastT) - Math.max(rFirstT, sFirstT), 0);

        return 100 * Math.min((double)I / (double)(rLastT - rFirstT), (double)I / (double)(sLastT - sFirstT));
    }

    private static double directDistance(Trajectory r, Trajectory s) {
        Set<Long> ot = new HashSet<Long>(r.getTimestamps());
        ot.retainAll(s.getTimestamps());

        double sum = 0.0;
        for (long t : ot) {
            double a1 = Math.pow(r.getPlaceAtTime(t).getX() - s.getPlaceAtTime(t).getX(), 2);
            double a2 = Math.pow(r.getPlaceAtTime(t).getY() - s.getPlaceAtTime(t).getY(), 2);
            sum += (a1 + a2) / Math.pow(ot.size(), 2);
        }

        return Math.sqrt(sum) / SynchronisedDistance.percentContemporary(r, s);
    }

    private static double[][] computeShortestDistanceMatrix(double[][] distanceGraph) {
        int n = distanceGraph.length;

        double[][] shortestDistanceMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                shortestDistanceMatrix[i][j] = distanceGraph[i][j];
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distanceGraph[i][k] + distanceGraph[k][j] < distanceGraph[i][j]) {
                        distanceGraph[i][j] = distanceGraph[i][k] + distanceGraph[k][j];
                    }
                }
            }
        }

        return distanceGraph;
    }

    @Override
	public void removeImpossibleTrajectoriesFromDataset(Dataset d) {
        int[] visitedMask = new int[d.size()];
        for (int i = 0; i < visitedMask.length; i++) visitedMask[i] = -1;

        int c = 0;
        int largestC = 0;
        int largestCSize = 0;

        for (int i = 0; i < d.size(); i++) {
            if (visitedMask[i] == -1) {
                int n = this.depthFirstSearch(i, c, visitedMask);
                if (n > largestCSize) {
                    largestC = c;
                    largestCSize = n;
                }
                c++;
            }
        }

        for (int i = 0; i < d.size(); i++) {
            if (visitedMask[i] != largestC) {
                List<Trajectory> rs = d.getTrajectories();
                Iterator<Trajectory> rsIter = rs.iterator();
                while (rsIter.hasNext()) {
                    Trajectory r = rsIter.next();
                    if (r.id == i) rsIter.remove();
                }
            }
        }
    }
    
    public int depthFirstSearch(int v, int c, int[] visitedMask) {
        visitedMask[v] = c;

        double[] connections = this.distanceGraph[v];
        int n = 1;
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] < Double.POSITIVE_INFINITY && visitedMask[i] == -1) {
                int k = this.depthFirstSearch(i, c, visitedMask);
                n += k;
            }
        }

        return n;
    }

	@Override
	public double computeDistance(Trajectory r, Trajectory s) {
        return this.shortestDistanceMatrix[r.id][s.id];
	}

}