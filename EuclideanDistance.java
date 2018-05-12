import java.util.ArrayList;
import java.util.List;

public class EuclideanDistance implements DistanceMeasure {

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

        long result = 0;

        List<Long> rTimes = new ArrayList<Long>(r.getTimestamps());
        List<Place> rPlaces = r.getPlaces();
        List<Long> sTimes = new ArrayList<Long>(s.getTimestamps());
        List<Place> sPlaces = s.getPlaces();

        for (int i = 0; i < r.length(); i++) {
            long pt = rTimes.get(i);
            Place p = rPlaces.get(i);
            long qt = sTimes.get(i);
            Place q = sPlaces.get(i);
        
            long tsqrd = (pt - qt) * (pt - qt);
            long xsqrd = (p.getX() - q.getX()) * (p.getX() - q.getX());
            long ysqrd = (p.getY() - q.getY()) * (p.getY() - q.getY());
        
            result += (1 + tsqrd) * (xsqrd + ysqrd);
        }
    
        return Math.sqrt((double)result);
    }
    
}