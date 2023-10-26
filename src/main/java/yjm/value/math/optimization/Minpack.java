package yjm.value.math.optimization;

import yjm.value.math.matrixutilities.Array;
import yjm.value.math.matrixutilities.Matrix;


/**
 * @Author  Jiaming Yan
 * @Description 解方程
 */
public class Minpack {

    public static void qrfac(
            final int m,
            final int n,
            final Matrix a,
            final boolean pivot,
            final int[] ipvt,
            final Array rdiag,
            final Array acnorm,
            final Array wa) {

        MinpackC.qrfac(m, n, a.$, pivot?1:0, ipvt, rdiag.$, acnorm.$, wa.$);
    }

    private static class MinpackC {

        private static final double MACHEP = 1.2e-16;
        private static final double DWARF = 1.0e-38;
        private static final double rdwarf = 3.834e-20;
        private static final double rgiant = 1.304e19;
        private static final double zero = 0.0;
        private static final double one = 1.0;
        private static final double p5 = 0.5;
        private static final double p05 = 0.05;
        private static final double p25 = 0.25;


        private static double enorm(final int n, final double[] x, final int offset) {
            int i;
            double agiant, floatn, s1, s2, s3;
            double xabs;
            double x1max, x3max;
            double ans, temp;

            s1 = zero;
            s2 = zero;
            s3 = zero;
            x1max = zero;
            x3max = zero;
            floatn = n;
            agiant = rgiant / floatn;

            for (i = 0; i < n; i++) {
                xabs = Math.abs(x[offset + i]);
                if ((xabs > rdwarf) && (xabs < agiant)) {
                    s2 += xabs * xabs;
                    continue;
                }

                if (xabs > rdwarf) {
                    if (xabs > x1max) {
                        temp = x1max / xabs;
                        s1 = one + s1 * temp * temp;
                        x1max = xabs;
                    } else {
                        temp = xabs / x1max;
                        s1 += temp * temp;
                    }
                    continue;
                }
                if (xabs > x3max) {
                    temp = x3max / xabs;
                    s3 = one + s3 * temp * temp;
                    x3max = xabs;
                } else {
                    if (xabs != zero) {
                        temp = xabs / x3max;
                        s3 += temp * temp;
                    }
                }
            }
            if (s1 != zero) {
                temp = s1 + (s2 / x1max) / x1max;
                ans = x1max * Math.sqrt(temp);
                return (ans);
            }
            if (s2 != zero) {
                if (s2 >= x3max) {
                    temp = s2 * (one + (x3max / s2) * (x3max * s3));
                } else {
                    temp = x3max * ((s2 / x3max) + (x3max * s3));
                }
                ans = Math.sqrt(temp);
            } else {
                ans = x3max * Math.sqrt(s3);
            }
            return (ans);
        }

        public static void qrfac(
                final int m,
                final int n,
                final double[] a,
                final int pivot,
                final int[] ipvt,
                final double[] rdiag,
                final double[] acnorm,
                final double[] wa) {
            int i, ij, jj, j, jp1, k, kmax, minmn;
            double ajnorm, sum, temp;

            ij = 0;
            for (j = 0; j < n; j++) {
                acnorm[j] = enorm(m, a, ij);
                rdiag[j] = acnorm[j];
                wa[j] = rdiag[j];
                if (pivot != 0) {
                    ipvt[j] = j;
                }
                ij += m; /* m*j */
            }

            minmn = min(m, n);
            for (j = 0; j < minmn; j++) {
                if (pivot != 0) {

                    kmax = j;
                    for (k = j; k < n; k++) {
                        if (rdiag[k] > rdiag[kmax]) {
                            kmax = k;
                        }
                    }
                    if (kmax != j) {
                        ij = m * j;
                        jj = m * kmax;
                        for (i = 0; i < m; i++) {
                            temp = a[ij]; /* [i+m*j] */
                            a[ij] = a[jj]; /* [i+m*kmax] */
                            a[jj] = temp;
                            ij += 1;
                            jj += 1;
                        }
                        rdiag[kmax] = rdiag[j];
                        wa[kmax] = wa[j];
                        k = ipvt[j];
                        ipvt[j] = ipvt[kmax];
                        ipvt[kmax] = k;
                    }
                }

                jj = j + m * j;
                ajnorm = enorm(m - j, a, jj);
                if (ajnorm != zero) {
                    if (a[jj] < zero) {
                        ajnorm = -ajnorm;
                    }
                    ij = jj;
                    for (i = j; i < m; i++) {
                        a[ij] /= ajnorm;
                        ij += 1; /* [i+m*j] */
                    }
                    a[jj] += one;
                    jp1 = j + 1;
                    if (jp1 < n) {
                        for (k = jp1; k < n; k++) {
                            sum = zero;
                            ij = j + m * k;
                            jj = j + m * j;
                            for (i = j; i < m; i++) {
                                sum += a[jj] * a[ij];
                                ij += 1; /* [i+m*k] */
                                jj += 1; /* [i+m*j] */
                            }
                            temp = sum / a[j + m * j];
                            ij = j + m * k;
                            jj = j + m * j;
                            for (i = j; i < m; i++) {
                                a[ij] -= temp * a[jj];
                                ij += 1; /* [i+m*k] */
                                jj += 1; /* [i+m*j] */
                            }
                            if ((pivot != 0) && (rdiag[k] != zero)) {
                                temp = a[j + m * k] / rdiag[k];
                                temp = max(zero, one - temp * temp);
                                rdiag[k] *= Math.sqrt(temp);
                                temp = rdiag[k] / wa[k];
                                if ((p05 * temp * temp) <= MACHEP) {
                                    rdiag[k] = enorm(m - j - 1, a, jp1 + m * k);
                                    wa[k] = rdiag[k];
                                }
                            }
                        }
                    }
                }
                rdiag[j] = -ajnorm;
            }
        }


        /**
         * @Author  Jiaming Yan
         * @Description 解非线性方程
         */
        public static void qrsolv(
                final int n,
                final double[] r,
                final int ldr,
                final int[] ipvt,
                final double[] diag,
                final double[] qtb,
                final double[] x,
                final double[] sdiag,
                final double[] wa) {

            int i, ij, ik, kk, j;
            int jp1;
            int k;
            int kp1;
            int l;
            int nsing;
            double cos;
            double cotan, qtbpj;
            double sin, sum, tan, temp;

            kk = 0;
            for (j = 0; j < n; j++) {
                ij = kk;
                ik = kk;
                for (i = j; i < n; i++) {
                    r[ij] = r[ik];
                    ij += 1;   /* [i+ldr*j] */
                    ik += ldr; /* [j+ldr*i] */
                }
                x[j] = r[kk];
                wa[j] = qtb[j];
                kk += ldr + 1; /* j+ldr*j */
            }

            for (j = 0; j < n; j++) {
                l = ipvt[j];
                if (diag[l] != zero) {
                    for (k = j; k < n; k++) {
                        sdiag[k] = zero;
                    }
                    sdiag[j] = diag[l];

                    qtbpj = zero;
                    for (k = j; k < n; k++) {
                        if (sdiag[k] == zero) {
                            continue;
                        }
                        kk = k + ldr * k;
                        if (Math.abs(r[kk]) < Math.abs(sdiag[k])) {
                            cotan = r[kk] / sdiag[k];
                            sin = p5 / Math.sqrt(p25 + p25 * cotan * cotan);
                            cos = sin * cotan;
                        } else {
                            tan = sdiag[k] / r[kk];
                            cos = p5 / Math.sqrt(p25 + p25 * tan * tan);
                            sin = cos * tan;
                        }

                        r[kk] = cos * r[kk] + sin * sdiag[k];
                        temp = cos * wa[k] + sin * qtbpj;
                        qtbpj = -sin * wa[k] + cos * qtbpj;
                        wa[k] = temp;

                        kp1 = k + 1;
                        if (n > kp1) {
                            ik = kk + 1;
                            for (i = kp1; i < n; i++) {
                                temp = cos * r[ik] + sin * sdiag[i];
                                sdiag[i] = -sin * r[ik] + cos * sdiag[i];
                                r[ik] = temp;
                                ik += 1;
                            }
                        }
                    }
                }
                kk = j + ldr * j;
                sdiag[j] = r[kk];
                r[kk] = x[j];
            }

            nsing = n;
            for (j = 0; j < n; j++) {
                if ((sdiag[j] == zero) && (nsing == n)) {
                    nsing = j;
                }
                if (nsing < n) {
                    wa[j] = zero;
                }
            }
            if (nsing >= 1) {
                for (k = 0; k < nsing; k++) {
                    j = nsing - k - 1;
                    sum = zero;
                    jp1 = j + 1;
                    if (nsing > jp1) {
                        ij = jp1 + ldr * j;
                        for (i = jp1; i < nsing; i++) {
                            sum += r[ij] * wa[i];
                            ij += 1;
                        }
                    }
                    wa[j] = (wa[j] - sum) / sdiag[j];
                }
            }

            for (j = 0; j < n; j++) {
                l = ipvt[j];
                x[l] = wa[j];
            }
        }

        /**
         * @Author  Jiaming Yan
         * @Description 返回最大值
         */
        private static <T extends Comparable<T>> T max(final T a, final T b) {
            T max = a;
            if (b.compareTo(a) > 0)
                max = b;
            return max;
        }

        /**
         * @Author  Jiaming Yan
         * @Description 返回最小值
         */
        private static <T extends Comparable<T>> T min(final T a, final T b) {
            T min = a;
            if (b.compareTo(a) < 0)
                min = b;
            return min;
        }

    }
}
