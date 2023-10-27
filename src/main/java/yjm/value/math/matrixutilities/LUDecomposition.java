package yjm.value.math.matrixutilities;

import yjm.value.QL;
import yjm.value.lang.exceptions.LibraryException;

public class LUDecomposition {
    private final static String MATRIX_IS_SINGULAR = "矩阵是奇异的";
    private final int m;
    private final int n;
    private final Matrix LU;
    private final int piv[];
    private int pivsign;

    /**
     * @Author  Jiaming Yan
     * @Description LU分解
     */
    public LUDecomposition(final Matrix A) {
        this.LU = A.clone().toJava();
        this.m = LU.rows();
        this.n = LU.cols();

        this.piv = new int[m];
        for (int i = 0; i < m; i++) {
            piv[i] = i;
        }
        this.pivsign = 1;

        final double[] LUcolj = new double[m];

        for (int j = 0; j < n; j++) {

            for (int i = 0; i < m; i++) {
                LUcolj[i] = LU.$[LU.addr.op(i, j)];
            }

            for (int i = 0; i < m; i++) {

                final int kmax = Math.min(i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; k++) {
                    s += LU.$[LU.addr.op(i, k)] * LUcolj[k];
                }

                LU.$[LU.addr.op(i, j)] = LUcolj[i] -= s;
            }

            int p = j;
            for (int i = j + 1; i < m; i++) {
                if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
                    p = i;
                }
            }
            if (p != j) {
                for (int k = 0; k < n; k++) {
                    final double t = LU.$[LU.addr.op(p, k)];
                    LU.$[LU.addr.op(p, k)] = LU.$[LU.addr.op(j, k)];
                    LU.$[LU.addr.op(j, k)] = t;
                }
                final int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            if (j < m && LU.$[LU.addr.op(j, j)] != 0.0) {
                for (int i = j + 1; i < m; i++) {
                    LU.$[LU.addr.op(i, j)] /= LU.$[LU.addr.op(j, j)];
                }
            }
        }
    }

    /**
     * @Author  Jiaming Yan
     * @Description 判断矩阵是否非奇异
     */
    public boolean isNonSingular() {
        for (int j = 0; j < n; j++) {
            if (LU.$[LU.addr.op(j, j)] == 0)
                return false;
        }
        return true;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回下三角因子
     */
    public Matrix L() {
        final Matrix L = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    L.$[L.addr.op(i, j)] = LU.$[LU.addr.op(i, j)];
                } else if (i == j) {
                    L.$[L.addr.op(i, j)] = 1.0;
                }
            }
        }
        return L;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回上三角因子
     */
    public Matrix U() {
        final Matrix U = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i <= j) {
                    U.$[U.addr.op(i, j)] = LU.$[LU.addr.op(i, j)];
                }
            }
        }
        return U;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 返回枢轴排列向量
     */
    public int[] getPivot() {
        final int[] p = new int[m];
        for (int i = 0; i < m; i++) {
            p[i] = piv[i];
        }
        return p;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 计算矩阵行列式
     */
    public double det() {
        QL.require(m == n, Matrix.MATRIX_MUST_BE_SQUARE);
        double d = pivsign;
        for (int j = 0; j < n; j++) {
            d *= LU.$[LU.addr.op(j, j)];
        }
        return d;
    }

    /**
     * @Author  Jiaming Yan
     * @Description 解决A*X = B
     */
    public Matrix solve(final Matrix B) {
        QL.require(B.rows() == this.m, Matrix.MATRIX_IS_INCOMPATIBLE);
        if (!this.isNonSingular())
            throw new LibraryException(MATRIX_IS_SINGULAR);

        final Matrix X = B.range(piv, 0, B.cols());

        for (int k = 0; k < n; k++) {
            for (int i = k + 1; i < n; i++) {
                for (int j = 0; j < B.cols(); j++) {
                    X.$[X.addr.op(i, j)] -= X.$[X.addr.op(k, j)] * LU.$[LU.addr.op(i, k)];
                }
            }
        }

        for (int k = n - 1; k >= 0; k--) {
            for (int j = 0; j < B.cols(); j++) {
                X.$[X.addr.op(k, j)] /= LU.$[LU.addr.op(k, k)];
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < B.cols(); j++) {
                    X.$[X.addr.op(i, j)] -= X.$[X.addr.op(k, j)] * LU.$[LU.addr.op(i, k)];
                }
            }
        }
        return X;
    }






}
