import java.io.IOException;

class Plotter {

    public static void plotRUMap(double dr, double il) {
        double[] drs = { dr };
        double[] ils = { il }; 

        Plotter.plotRUMap(drs, ils);
    }

    public static void plotRUMap(double[] drs, double[] ils) {
        if (drs.length != ils.length) {
            System.err.println("The number of information losses does not match the number of disclosure risks!");
            System.exit(-1);
        }

        try {
            Runtime rt = Runtime.getRuntime();

            String cmd = "python plot_ru_map.py";
            for (int i = 0; i < drs.length; i++) {
                cmd += " " + drs[i] + " " + ils[i];
            }

            rt.exec(cmd);
        } catch (IOException e) {
            System.err.println("An I/O exception occured: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }

}