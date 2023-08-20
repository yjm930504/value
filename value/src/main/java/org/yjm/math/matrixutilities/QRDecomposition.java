package org.yjm.math.matrixutilities;

import java.util.Arrays;

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

        System.out.println("mT (BEFORE) = " + mT);
        Minpack.qrfac(
                m, n,
                mT,                      // input/output parameter (sorry for that :~ )
                pivot,
                ipvt, rdiag, rdiag, wa  // output parameters (sorry for that :~ )
        );

        System.out.println("mT (AFTER)  = " + mT);
        System.out.println("Array ipvt = " + Arrays.toString(ipvt));
        System.out.println("Array rdiag = " + rdiag);
        System.out.println("Array wa = " + wa);

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

        System.out.println("Matrix Q = "+Q.toString());
        System.out.println("Matrix R = "+R.toString());
        System.out.println("Matrix P = "+P.toString());
        System.out.println("Matrix mT = "+mT.toString());
    }
}
