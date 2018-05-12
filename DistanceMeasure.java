public interface DistanceMeasure {

    void createSupportData(Dataset d);

    void removeImpossibleTrajectoriesFromDataset(Dataset d);

    double computeDistance(final Trajectory r, final Trajectory s);

}