package org.yjm.math.matrixutilities;

import org.yjm.QL;

/**
 * @Author  Jiaming Yan
 * @Description Schur分解
 */
public class SymmetricSchurDecomposition {

    private static final double epsPrec = 1e-15;
    private static final int maxIterations = 100;
    private final int size;
    private final Matrix A;
    private final Array diag;

    public SymmetricSchurDecomposition(final Matrix m) {
        QL.require(m.rows() == m.cols(), Matrix.MATRIX_MUST_BE_SQUARE);

        this.size = m.rows();
        this.A = new Matrix(m.rows(), m.cols(), m.flags());
        this.diag = new Array(size, m.flags());

        final double tmpDiag[] = new double[size];
        final double tmpSum[] = new double[size];

        final Matrix s = m.clone();
        final int offset = s.offset();

        for (int q = offset; q < size+offset; q++) {
            diag.$[diag.addr.op(q)] = s.$[s.addr.op(q, q)];
            A.$[A.addr.op(q, q)] = 1.0;
        }
        for (int j = 0; j < size; j++) {
            tmpDiag[j] = diag.$[diag.addr.op(j+offset)];
        }

        boolean keeplooping = true;
        int ite = 1;
        double threshold;
        do {
            // main loop
            double sum = 0;
            for (int a = offset; a < size - 1 + offset; a++) {
                for (int b = a + 1; b < size + offset; b++) {
                    sum += Math.abs(s.$[s.addr.op(a, b)]);
                }
            }

            if (sum == 0) {
                keeplooping = false;
            } else {
                /*
                 * To speed up computation a threshold is introduced to make sure it is worthy to perform the Jacobi rotation
                 */
                if (ite < 5) {
                    threshold = 0.2 * sum / (size * size);
                } else {
                    threshold = 0.0;
                }

                int j, k, l;
                for (j = offset; j < size - 1 + offset; j++) {
                    for (k = j + 1; k < size + offset; k++) {
                        double sine, rho, cosin, heig, tang, beta;
                        final double smll = Math.abs(s.$[s.addr.op(j, k)]);
                        if (ite > 5 && smll < epsPrec * Math.abs(diag.$[diag.addr.op(j)])
                                && smll < epsPrec * Math.abs(diag.$[diag.addr.op(k)])) {
                            s.$[s.addr.op(j, k)] = 0;
                        } else if (Math.abs(s.$[s.addr.op(j, k)]) > threshold) {
                            heig = diag.$[diag.addr.op(k)] - diag.$[diag.addr.op(j)];
                            if (smll < epsPrec * Math.abs(heig)) {
                                tang = s.$[s.addr.op(j, k)] / heig;
                            } else {
                                beta = 0.5 * heig / s.$[s.addr.op(j, k)];
                                tang = 1.0 / (Math.abs(beta) + Math.sqrt(1 + beta * beta));
                                if (beta < 0) {
                                    tang = -tang;
                                }
                            }
                            cosin = 1 / Math.sqrt(1 + tang * tang);
                            sine = tang * cosin;
                            rho = sine / (1 + cosin);
                            heig = tang * s.$[s.addr.op(j, k)];
                            tmpSum[j-offset] -= heig;
                            tmpSum[k-offset] += heig;
                            diag.$[diag.addr.op(j)] -= heig;
                            diag.$[diag.addr.op(k)] += heig;
                            s.$[s.addr.op(j, k)] = 0.0;
                            for (l = offset; l + 1 <= j; l++) {
                                jacobiRotate(s, rho, sine, l, j, l, k);
                            }
                            for (l = j + 1; l <= k - 1; l++) {
                                jacobiRotate(s, rho, sine, j, l, l, k);
                            }
                            for (l = k + 1; l < size + offset; l++) {
                                jacobiRotate(s, rho, sine, j, l, k, l);
                            }
                            for (l = offset; l < size + offset; l++) {
                                jacobiRotate(A, rho, sine, l, j, l, k);
                            }
                        }
                    }
                }
                for (k = 0; k < size; k++) {
                    tmpDiag[k] += tmpSum[k];
                    diag.$[diag.addr.op(k+offset)] = tmpDiag[k];
                    tmpSum[k] = 0.0;
                }
            }
        } while (++ite <= maxIterations && keeplooping);

        QL.ensure(ite <= maxIterations, "迭代次数过多");
    }

    /**
     * @Author  Jiaming Yan
     * @Description jacobi旋转
     */
    private void jacobiRotate(
            final Matrix m,
            final double rot,
            final double dil,
            final int j1,
            final int k1,
            final int j2,
            final int k2) {
        double x1, x2;
        x1 = m.$[m.addr.op(j1, k1)];
        x2 = m.$[m.addr.op(j2, k2)];
        m.$[m.addr.op(j1, k1)] = x1 - dil * (x2 + x1 * rot);
        m.$[m.addr.op(j2, k2)] = x2 + dil * (x1 - x2 * rot);
    }
}
