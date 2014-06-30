import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class PercolationStats {

    private static final Logger LOGGER = Logger.getLogger("PercolationStats");
    private static final Level LOG_LEVEL = Level.INFO;
    private double[] openSiteFraction;
    private int T;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException(
                    String.format("size %s and/or number of iterations %s invalid",
                            N, T));
        }
        Percolation[] percolations = new Percolation[T];
        openSiteFraction = new double[T];
        this.T = T;

        for (int i = 0; i < T; i++) {

            double k = 0.;
            percolations[i] = new Percolation(N);

            while (k < N || !percolations[i].percolates()) {
                int x = (int) (1 + Math.random() * N);
                int y = (int) (1 + Math.random() * N);
                percolations[i].open(x, y);
                k++;
            }

            openSiteFraction[i] = k/(N*N);
        }
    }

    public double mean() {
        double total = 0.;
        for (int i = 0; i < T; i++) {
            total += openSiteFraction[i];
        }
        return total / T;
    }


    public double stddev() {
        double total = 0.;
        double mean = mean();
        for (int i = 0; i < T; i++) {
            total += Math.pow(openSiteFraction[i] - mean, 2.);
        }
        return Math.pow(total/(T - 1.), 0.5);
    }

    public double confidenceLo() {

        return mean() - 1.96*stddev()/Math.sqrt(T);
    }

    public double confidenceHi() {

        return mean() + 1.96*stddev()/Math.sqrt(T);
    }

    public static void main(String[] args) {

        int n = 0;
        int t = 0;
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                return;
            }
        }

        if (args.length > 1) {
            try {
                t = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[1] + " must be an integer.");
                return;
            }
        }

        Date date1 = new Date();
        PercolationStats p = new PercolationStats(n, t);
        Date date2 = new Date();
        System.out.println("mean                    = " + p.mean());
        System.out.println("stddev                  = " + p.stddev());
        System.out.println("95% confidence interval = " + p.confidenceLo()
                + ", " + p.confidenceHi());
        long seconds = (date2.getTime() - date1.getTime())/1000;
        log(String.valueOf(seconds));
    }

    private static void log(String msg) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.log(LOG_LEVEL, msg);
    }
}
