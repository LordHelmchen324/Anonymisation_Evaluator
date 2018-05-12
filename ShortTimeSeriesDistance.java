import java.util.ArrayList;
import java.util.List;

public class ShortTimeSeriesDistance implements DistanceMeasure {

    @Override
	public void createSupportData(Dataset d) { }

	@Override
	public void removeImpossibleTrajectoriesFromDataset(Dataset d) { }

    @Override
    public double computeDistance(final Trajectory r, final Trajectory s) {
        if (r.length() != s.length()) {
            System.err.println("Trajectories have different length!");
            System.exit(-1);
        }

        List<Long> rTimestamps = new ArrayList<Long>(r.getTimestamps());
        List<Place> rPlaces = r.getPlaces();
        List<Long> sTimestamps = new ArrayList<Long>(s.getTimestamps());
        List<Place> sPlaces = s.getPlaces();

        double result = 0.0;
        for (int i = 0; i < r.length() - 1 ; i++) {
            long pt = rTimestamps.get(i);
            Place p = rPlaces.get(i);
            long ptNext = rTimestamps.get(i + 1);
            Place pNext = rPlaces.get(i + 1);
            long qt = sTimestamps.get(i);
            Place q = sPlaces.get(i);
            long qtNext = sTimestamps.get(i + 1);
            Place qNext = sPlaces.get(i + 1);
        
            double a1 = (double)(qNext.getX() - q.getX()) / (double)(qtNext - qt);
            double a2 = (double)(pNext.getX() - p.getX()) / (double)(qtNext - qt);
            double asqrd = (a1 - a2) * (a1 - a2);
        
            double b1 = (double)(qNext.getY() - q.getY()) / (double)(ptNext - pt);
            double b2 = (double)(pNext.getY() - p.getY()) / (double)(ptNext - pt);
            double bsqrd = (b1 - b2) * (b1 - b2);
        
            result += asqrd + bsqrd;
        }
        
        return Math.sqrt(result);
    }
    
}