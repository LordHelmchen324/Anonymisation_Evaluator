import java.util.List;

public interface DistanceMeasure {

    public void createSupportData(List<Dataset> ds);

    void removeImpossibleTrajectoriesFromDataset(Dataset d);

    double computeDistance(final Trajectory r, final Trajectory s);

}