package org.yjm.math.matrixutilities;

import org.yjm.math.matrixutilities.internal.Address;

import java.util.EnumSet;
import java.util.Set;

/**
 * @Author  Jiaming Yan
 * @Description 单位矩阵类
 */
public class Identity extends Matrix {

    /**
     * @Author  Jiaming Yan
     * @Description 构建单位矩阵
     */
    public Identity(final int dim) {
        this(dim, EnumSet.noneOf(Address.Flags.class));
    }

    /**
     * @Author  Jiaming Yan
     * @Description 构建单位矩阵
     */
    public Identity(final int dim, final Set<Address.Flags> flags) {
        super(dim, dim, flags);
        final int offset = addr.isFortran() ? 1 : 0;
        for (int i = offset; i < dim+offset; i++) {
            this.set(i, i, 1.0);
        }
    }

}
