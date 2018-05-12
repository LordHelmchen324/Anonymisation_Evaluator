import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

class Dataset {

    private List<Trajectory> trajectories = new LinkedList<Trajectory>();

    public Dataset() { }

    public Dataset(Dataset original) {
        for (Trajectory r : original.trajectories) this.add(new Trajectory(r));
    }

    public static Dataset geoLife() {
        File datasetFile = new File("../Geolife Trajectories 1.3/translated.json");
        try (BufferedReader r = new BufferedReader(new FileReader(datasetFile))) {
            System.out.print("Reading data set from JSON file ... ");

            Gson gson = new Gson();
            Dataset d = gson.fromJson(r, Dataset.class);

            System.out.print("done!\n");
            System.out.println(" -> Size of the data set = " + d.size() + "\n");

            return d;
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file at path \"" + datasetFile.getName() + "\".");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
            System.exit(1);
        }
        return null;
    }

    public static Dataset randomPerfect(int size, long timespan, long maxX, long maxY) {
        Random rand = new Random();

        Dataset d = new Dataset();
        
        for (int i = 0; i < size; i++) {
            Trajectory r = new Trajectory();
            for (long t = 0; t < timespan; t++) {
                long randomX = Math.abs(rand.nextLong() % maxX);
                long randomY = Math.abs(rand.nextLong() % maxY);
                r.add(t, new Place(randomX, randomY));
            }
            d.add(r);
        }

        return d;
    }

    public static Dataset smallDummy() {
        Dataset d = new Dataset();

        Trajectory t1 = new Trajectory();
        t1.add(1, new Place(1, 0));
        t1.add(2, new Place(3, 2));
        t1.add(3, new Place(4, 2));
        t1.add(4, new Place(5, 2));
        t1.add(5, new Place(5, 2));
        t1.add(6, new Place(9, 7));
        d.add(t1);

        Trajectory t11 = new Trajectory();
        t11.add(1, new Place(1, 0));
        t11.add(2, new Place(3, 2));
        t11.add(3, new Place(4, 2));
        t11.add(4, new Place(5, 2));
        t11.add(5, new Place(5, 2));
        t11.add(6, new Place(9, 7));
        d.add(t11);

        Trajectory t2 = new Trajectory();
        t2.add(1, new Place(1, 0));
        t2.add(2, new Place(3, 2));
        t2.add(3, new Place(4, 2));
        t2.add(4, new Place(5, 2));
        t2.add(5, new Place(5, 2));
        t2.add(6, new Place(9, 7));
        d.add(t2);
        d.add(t2);

        Trajectory t22 = new Trajectory();
        t22.add(1, new Place(1, 0));
        t22.add(2, new Place(3, 6));
        t22.add(3, new Place(5, 3));
        t22.add(4, new Place(9, 7));
        t22.add(5, new Place(2, 4));
        t22.add(6, new Place(4, 2));
        d.add(t22);

        return d;
    }

    public static Dataset largeDummy() {
        Dataset d = new Dataset();
        
        // club 1

        Trajectory t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 0));
        t.add(3, new Place(2, 0));
        t.add(4, new Place(3, 0));
        t.add(5, new Place(4, 0));
        t.add(6, new Place(5, 0));
        t.add(7, new Place(6, 0));
        t.add(8, new Place(7, 0));
        t.add(9, new Place(8, 0));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(1, 2));
        t.add(2, new Place(1, 0));
        t.add(3, new Place(2, 0));
        t.add(4, new Place(3, 0));
        t.add(5, new Place(4, 1));
        t.add(6, new Place(5, 0));
        t.add(7, new Place(6, 0));
        t.add(8, new Place(7, 0));
        t.add(9, new Place(8, 0));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 0));
        t.add(3, new Place(2, 1));
        t.add(4, new Place(3, 1));
        t.add(5, new Place(4, 0));
        t.add(6, new Place(5, 0));
        t.add(7, new Place(6, 0));
        t.add(8, new Place(7, 0));
        t.add(9, new Place(6, 1));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 0));
        t.add(3, new Place(2, 0));
        t.add(4, new Place(3, 0));
        t.add(5, new Place(4, 0));
        t.add(6, new Place(5, 0));
        t.add(7, new Place(6, 0));
        t.add(8, new Place(7, 0));
        t.add(9, new Place(9, 0));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 1));
        t.add(2, new Place(1, 1));
        t.add(3, new Place(2, 1));
        t.add(4, new Place(3, 0));
        t.add(5, new Place(4, 0));
        t.add(6, new Place(5, 0));
        t.add(7, new Place(6, 0));
        t.add(8, new Place(7, 0));
        t.add(9, new Place(8, 0));
        d.add(t);

        // club 2
        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 1));
        t.add(3, new Place(2, 2));
        t.add(4, new Place(3, 3));
        t.add(5, new Place(4, 4));
        t.add(6, new Place(5, 5));
        t.add(7, new Place(6, 6));
        t.add(8, new Place(7, 7));
        t.add(9, new Place(8, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 1));
        t.add(3, new Place(2, 3));
        t.add(4, new Place(3, 4));
        t.add(5, new Place(4, 5));
        t.add(6, new Place(5, 6));
        t.add(7, new Place(6, 7));
        t.add(8, new Place(7, 8));
        t.add(9, new Place(8, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 2));
        t.add(2, new Place(1, 2));
        t.add(3, new Place(2, 2));
        t.add(4, new Place(3, 3));
        t.add(5, new Place(4, 4));
        t.add(6, new Place(5, 5));
        t.add(7, new Place(6, 6));
        t.add(8, new Place(7, 7));
        t.add(9, new Place(8, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 1));
        t.add(3, new Place(2, 2));
        t.add(4, new Place(3, 3));
        t.add(5, new Place(4, 4));
        t.add(6, new Place(5, 5));
        t.add(7, new Place(6, 7));
        t.add(8, new Place(8, 8));
        t.add(9, new Place(9, 9));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(1, 1));
        t.add(3, new Place(1, 2));
        t.add(4, new Place(2, 3));
        t.add(5, new Place(4, 4));
        t.add(6, new Place(5, 5));
        t.add(7, new Place(6, 6));
        t.add(8, new Place(7, 7));
        t.add(9, new Place(8, 8));
        d.add(t);

        // club 3

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(0, 1));
        t.add(3, new Place(0, 2));
        t.add(4, new Place(0, 3));
        t.add(5, new Place(0, 4));
        t.add(6, new Place(0, 5));
        t.add(7, new Place(0, 6));
        t.add(8, new Place(0, 7));
        t.add(9, new Place(0, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(1, 0));
        t.add(2, new Place(1, 1));
        t.add(3, new Place(0, 2));
        t.add(4, new Place(0, 3));
        t.add(5, new Place(0, 4));
        t.add(6, new Place(0, 5));
        t.add(7, new Place(0, 6));
        t.add(8, new Place(0, 7));
        t.add(9, new Place(1, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(0, 1));
        t.add(3, new Place(0, 2));
        t.add(4, new Place(0, 3));
        t.add(5, new Place(1, 4));
        t.add(6, new Place(1, 5));
        t.add(7, new Place(2, 6));
        t.add(8, new Place(1, 7));
        t.add(9, new Place(0, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 2));
        t.add(2, new Place(0, 2));
        t.add(3, new Place(0, 2));
        t.add(4, new Place(0, 3));
        t.add(5, new Place(0, 4));
        t.add(6, new Place(0, 5));
        t.add(7, new Place(0, 6));
        t.add(8, new Place(0, 7));
        t.add(9, new Place(0, 8));
        d.add(t);

        t = new Trajectory();
        t.add(1, new Place(0, 0));
        t.add(2, new Place(0, 1));
        t.add(3, new Place(0, 2));
        t.add(4, new Place(0, 3));
        t.add(5, new Place(0, 4));
        t.add(6, new Place(0, 5));
        t.add(7, new Place(0, 7));
        t.add(8, new Place(0, 8));
        t.add(9, new Place(0, 9));
        d.add(t);

        return d;
    }

    @Override
    public String toString() {
        final String lineSeperator = "--------------";
        String s = "" + lineSeperator;
        for (Trajectory r : this.trajectories) s += "\n" + r.toString();
        s += "\n" + lineSeperator;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Dataset) {
            Dataset d = (Dataset)o;
            if (this.size() != d.size()) return false;

            List<Trajectory> trajectoriesCopy = new LinkedList<Trajectory>(this.trajectories);
            for (int i = 0; i < d.size(); i++) {
                Trajectory r = d.getTrajectories().get(i);
                boolean found = false;
                for (int j = 0; j < trajectoriesCopy.size(); j++) {
                    if (trajectoriesCopy.get(j).equals(r)) {
                        trajectoriesCopy.remove(j);
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void add(Trajectory r) {
        this.trajectories.add(r);
    }

    public void remove(Trajectory r) {
        this.trajectories.remove(r);
    }

    public int size() {
        return this.trajectories.size();
    }

    public Trajectory getTrajectoryById(int id) {
        for (Trajectory r : this.trajectories) {
            if (r.id == id) return r;
        }
        return null;
    }

    public List<Trajectory> getTrajectories() {
        return this.trajectories;
    }

    // MDAV

    public Trajectory closestTrajectoryTo(Trajectory r, DistanceMeasure dM) {
        if (this.trajectories.isEmpty()) {
            System.err.println("Cannot return clostest Trajectory to t = " + r + " from within empty Dataset!");
            System.exit(1);
        }

        System.out.print("      > Finding closest trajectory ... ");

        double minDistance = Double.MAX_VALUE;
        Trajectory closest = null;
        for (Trajectory s : this.trajectories) {
            double distance = dM.computeDistance(r, s);
            if (distance < minDistance || closest == null) {
                minDistance = distance;
                closest = s;
            }
        }

        System.out.print("done!\n");

        return closest;
    }

    public Trajectory furthestTrajectoryTo(Trajectory r, DistanceMeasure dM) {
        if (this.trajectories.isEmpty()) {
            System.err.println("Cannot return furthest Trajectory to t = " + r + " from within empty Dataset!");
            System.exit(1);
        }

        System.out.print("      > Finding furthest trajectory ... ");

        double maxDistance = 0.0;
        Trajectory furthest = null;
        for (Trajectory s : this.trajectories) {
            double distance = dM.computeDistance(r, s);
            if (distance > maxDistance || furthest == null) {
                maxDistance = distance;
                furthest = s;
            }
        }

        System.out.print("done!\n");

        return furthest;
    }

    public List<Trajectory> removeClusterAround(Trajectory r, int size, DistanceMeasure dM) {
        List<Trajectory> cluster = new LinkedList<Trajectory>();
        cluster.add(r);
        this.remove(r);

        for (int i = 0; i < size - 1; i++) {
            Trajectory closest = this.closestTrajectoryTo(r, dM);
            cluster.add(closest);
            this.remove(closest);
        }

        System.out.println("    > Made cluster of size " + size);

        return cluster;
    }

    public void fillUpToEqualLength() {
        if (this.trajectories.isEmpty()) {
            System.err.println("Tried to fill up empty Dataset!");
            return;
        }

        // find longest
        Iterator<Trajectory> i = this.trajectories.iterator();
        Trajectory longest = i.next();
        while (i.hasNext()) {
            Trajectory r = i.next();
            if (r.length() > longest.length()) longest = r;
        }

        // fill up all the trajectories to the length of the longest
        for (Trajectory t : this.trajectories) t.lengthenToEqualLengthAs(longest);
    }

    public Dataset protectedByMDAV(int k, DistanceMeasure dM, MedianStrategy mS) {
        System.out.print(" -> Creating a temporary copy of the dataset ... ");

        Dataset temp = new Dataset(this);

        System.out.print("done!\n");

        List<List<Trajectory>> clusters = new LinkedList<List<Trajectory>>();

        System.out.println(" -> Clustering ...");

        while (temp.size() > k) {
            Trajectory avrg = mS.computeMedian(temp.trajectories);
            Trajectory furthest = temp.furthestTrajectoryTo(avrg, dM);
            clusters.add(temp.removeClusterAround(furthest, k, dM));

            System.out.println("    > " + temp.size() + " trajecotires remaining");

            if (temp.size() > k) {
                Trajectory furthest2 = temp.furthestTrajectoryTo(avrg, dM);
                clusters.add(temp.removeClusterAround(furthest2, k, dM));

                System.out.println("    > " + temp.size() + " trajecotires remaining");
            }
        }
        
        List<Trajectory> lastCluster = clusters.get(clusters.size() - 1);
        Iterator<Trajectory> it = temp.trajectories.iterator();
        while (it.hasNext()) {
            Trajectory r = it.next();
            lastCluster.add(r);
            it.remove();
        }

        System.out.println(" -> Setting clusters to their median ...");

        Dataset result = new Dataset();
        for (List<Trajectory> c : clusters) {
            Trajectory clusterMedian = mS.computeMedian(c);
            for (Trajectory ro : c) {
                Trajectory rp = new Trajectory(clusterMedian);
                rp.id = ro.id;
                result.add(rp);
            }
        }

        return result;
    }

}