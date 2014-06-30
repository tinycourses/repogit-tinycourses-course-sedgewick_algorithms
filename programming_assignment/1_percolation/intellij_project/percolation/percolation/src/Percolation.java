public class Percolation {

    private boolean[][] openQ;
    private WeightedQuickUnionUF percolationPath;
    private WeightedQuickUnionUF isFullSite;
    private int size;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException(
                    String.format("size %s out of bounds invalid", N));
        }
        openQ = new boolean[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                openQ[i][j] = false;
            }
        }

        size = N;

        percolationPath = new WeightedQuickUnionUF(N * N + 2);
        isFullSite = new WeightedQuickUnionUF(N * N + 1);

    }

    public void open(int x, int y) {

        if (x < 1 || x > size || y < 1 || y > size) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }

        openQ[x - 1][y - 1] = true;

        int ufIdx = coordinateToIndex(x, y);

        if (x == 1) {
            percolationPath.union(0, coordinateToIndex(x, y));
            isFullSite.union(0, coordinateToIndex(x, y));
        }
        if (x > 1 && isOpen(x - 1, y)) {
            percolationPath.union(ufIdx, coordinateToIndex(x - 1, y));
            isFullSite.union(ufIdx, coordinateToIndex(x - 1, y));
        }
        if (x < size && isOpen(x + 1, y)) {
            percolationPath.union(ufIdx, coordinateToIndex(x + 1, y));
            isFullSite.union(ufIdx, coordinateToIndex(x + 1, y));
        }
        if (y > 1 && isOpen(x, y - 1)) {
            percolationPath.union(ufIdx, coordinateToIndex(x, y - 1));
            isFullSite.union(ufIdx, coordinateToIndex(x, y - 1));
        }
        if (y < size && isOpen(x, y + 1)) {
            percolationPath.union(ufIdx, coordinateToIndex(x, y + 1));
            isFullSite.union(ufIdx, coordinateToIndex(x, y + 1));
        }

        if (x == size) {
            percolationPath.union(ufIdx, size * size + 1);
        }

    }

    public boolean isOpen(int x, int y) {

        if (x < 1 || x > size || y < 1 || y > size) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }

        return openQ[x - 1][y - 1];

    }


    public boolean isFull(int x, int y) {

        if (x < 1 || x > size || y < 1 || y > size) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }

        if (x == 1) {
            return isOpen(x, y);
        }

        if (!isOpen(x, y)) {
            return false;
        }

        return isFullSite.connected(0, coordinateToIndex(x, y));

    }

    public boolean percolates() {

        if (percolationPath.count() > size*size  - size + 1) {
            return false;
        }

        return percolationPath.connected(0, size*size+1);
    }

    private int coordinateToIndex(int x, int y) {
        return (x - 1) * size + y;
    }

}
