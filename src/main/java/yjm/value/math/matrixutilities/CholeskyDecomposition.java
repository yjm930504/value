package yjm.value.math.matrixutilities;

import yjm.value.QL;
import yjm.value.lang.exceptions.LibraryException;


/**
 * @Author  Jiaming Yan
 * @Description Cholesky分解，把对称正定的矩阵分解为下三角矩阵L和其转置的乘积
 */
public class CholeskyDecomposition {

    private final static String MATRIX_IS_NOT_SIMMETRIC_POSITIVE = "Matrix is not symmetric positive definite.";

    /**
     * @Author  Jiaming Yan
     * @Description 方矩阵的维度
     */
    private final int n;

    /**
     * @Author  Jiaming Yan
     * @Description 分解的矩阵
     */
    private final Matrix L;

    /**
     * @Author  Jiaming Yan
     * @Description 正定标记
     */
    private boolean isspd;

    /**
     * @Author  Jiaming Yan
     * @Description Cholesky分解构造函数，
     */
    public CholeskyDecomposition(final Matrix A) {

        QL.require(A.rows() == A.cols(), Matrix.MATRIX_MUST_BE_SQUARE);

        this.n = A.rows();
        this.L = new Matrix(n, n);
        this.isspd = (A.rows() == A.cols());

        for (int j = 0; j < n; j++) {
            double d = 0.0;
            for (int k = 0; k < j; k++) {
                double s = 0.0;
                for (int i = 0; i < k; i++) {
                    s += L.$[L.addr.op(k, i)] * L.$[L.addr.op(j, i)];
                }
                L.$[L.addr.op(j, k)] = s = (A.$[A.addr.op(j, k)] - s) / L.$[L.addr.op(k, k)];
                d = d + s * s;
                isspd = isspd & (A.$[A.addr.op(k, j)] == A.$[A.addr.op(j, k)]);
            }
            d = A.$[A.addr.op(j, j)] - d;
            isspd = isspd && (d > 0.0); //FINDBUGS:: NS_DANGEROUS_NON_SHORT_CIRCUIT (solved)
            L.$[L.addr.op(j, j)] = Math.sqrt(Math.max(d, 0.0));
            for (int k = j + 1; k < n; k++) {
                L.$[L.addr.op(j, k)] = 0.0;
            }
        }
    }

    public boolean isSPD() {
        return isspd;
    }

    public Matrix L() {
        return L.clone();
    }

    /**
     * @Author  Jiaming Yan
     * @Description Solve A*X = B
     */
    public Matrix solve(final Matrix B) {
        QL.require(B.rows() == this.n, Matrix.MATRIX_IS_INCOMPATIBLE); // QA:[RG]::verified
        if (!this.isSPD())
            throw new LibraryException(MATRIX_IS_NOT_SIMMETRIC_POSITIVE);

        // Copy right hand side.
        final int nx = B.cols();
        final Matrix X = B.clone();

        // Solve L*Y = B;
        for (int k = 0; k < n; k++) {
            for (int j = 0; j < nx; j++) {
                for (int i = 0; i < k; i++) {
                    X.$[X.addr.op(k, j)] -= X.$[X.addr.op(i, j)] * L.$[L.addr.op(k, i)];
                }
                X.$[X.addr.op(k, j)] /= L.$[L.addr.op(k, k)];
            }
        }

        // Solve L'*X = Y;
        for (int k = n - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                for (int i = k + 1; i < n; i++) {
                    X.$[X.addr.op(k, j)] -= X.$[X.addr.op(i, j)] * L.$[L.addr.op(i, k)];
                }
                X.$[X.addr.op(k, j)] /= L.$[L.addr.op(k, k)];
            }
        }

        return X;
    }

}
