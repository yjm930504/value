package yjm.value.math.matrixutilities;

import yjm.value.QL;
import yjm.value.math.optimization.Minpack;

import java.util.Arrays;


/**
 * @Author  Jiaming Yan
 * @Description QR分解
 */
public class QRDecomposition {

    private final int m;
    private final int n;
    private final Matrix A;
    private final Matrix Q;
    private final Matrix R;
    private final Matrix P;
    private final int ipvt[];
    private final boolean isNonSingular;


    /**
     * @Author  Jiaming Yan
     * @Description QR分解
     */
    public QRDecomposition(final Matrix matrix) {
        this(matrix, false);
    }

    /**
     * @Author  Jiaming Yan
     * @Description QR分解
     */
    public QRDecomposition(final Matrix A, final boolean pivot) {

        this.A = A;
        this.m = A.rows();
        this.n = A.cols();
        this.ipvt = new int[n];

        final Matrix mT = A.clone().toJava().transpose();
        final Array rdiag = new Array(n);
        final Array wa = new Array(n);

        Minpack.qrfac(
                m, n,
                mT,                      // input/output parameter (sorry for that :~ )
                pivot,
                ipvt, rdiag, rdiag, wa  // output parameters (sorry for that :~ )
        );

        // obtain R
        final double[][] r = new double[n][n];
        for (int i=0; i < n; i++) {
            // r[i][i] = rdiag[i];
            r[i][i] = rdiag.get(i);
            if (i < m) {
                // std::copy(mT.column_begin(i)+i+1, mT.column_end(i), r.row_begin(i)+i+1);
                for (int k = i + 1; k < n; k++) {
                    // r[i][k] = mT[k][i];
                    r[i][k] = mT.get(k, i);
                }
            }
        }

        // obtain Q
        final double[][] q = new double[m][n];
        final double w[] = new double[m];
        for (int k=0; k < m; k++) {
            Arrays.fill(w, 0.0);
            w[k] = 1.0;
            for (int j=0; j < Math.min(n, m); j++) {
                final double t3 = mT.get(j, j);
                if (t3 != 0.0) {
                    // final double t = std::inner_product(mT.row_begin(j)+j, mT.row_end(j), w.begin()+j, 0.0)/t3;
                    double t = 0.0;
                    for (int p = j; p < m; p++) {
                        t += mT.get(j, p) * w[p];
                    }
                    t /= t3;

                    for (int i=j; i<m; i++) {
                        w[i] -= mT.get(j, i) * t;
                    }
                }
                q[k][j] = w[j];
            }
        }

        // reverse column pivoting
        final double[][] p = new double[n][n];
        if (pivot) {
            for (int i=0; i < n; ++i) {
                p[ipvt[i]][i] = 1.0;
            }
        } else {
            for (int i=0; i < n; ++i) {
                p[i][i] = 1.0;
            }
        }

        this.isNonSingular = isNonSingular(rdiag.$);

        final boolean fortran = this.A.addr.isFortran();
        this.R = fortran ? new Matrix(r).toFortran() : new Matrix(r);
        this.Q = fortran ? new Matrix(q).toFortran() : new Matrix(q);
        this.P = fortran ? new Matrix(p).toFortran() : new Matrix(p);

    }

    public Matrix Q() {
        return Q;
    }

    public Matrix R() {
        return R;
    }

    public Matrix P() {
        return P;
    }

    public boolean isNonSingular() {
        return isNonSingular;
    }

    public Array solve (
            final Array b,
            final boolean pivot,
            final Array d) {

        QL.require(b.size() == m, "dimensions of A and b don't match");
        QL.require(d != null && !d.empty() && d.size() == n, "dimensions of A and d don't match");

        final Matrix aT = A.transpose();
        final Matrix rT = R.transpose();

        final Array sdiag = new Array(n);
        final Array wa = new Array(n);

        final Array ld = new Array(n);
        if (d!=null && !d.empty()) {
            ld.fill(d);
        }
        final Array x = new Array(n);
        final Array qtb = Q.transpose().mul(b);

        throw new UnsupportedOperationException();
    }

    private boolean isNonSingular(final double[] rdiag) {
        for (final double diag : rdiag) {
            if (diag == 0)
                return false;
        }
        return true;
    }

}
