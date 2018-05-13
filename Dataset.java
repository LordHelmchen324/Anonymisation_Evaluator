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

    public static fromJSON(String jsonFilePath) {
        File datasetFile = new File(jsonFilePath);
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

}