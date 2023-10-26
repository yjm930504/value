package yjm.value.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target( { ElementType.TYPE })
public @interface QualityAssurance {

    Version version();
    Quality quality();
    String[] reviewers();

    /**
     * 版本枚举类
     */
    public enum Version {
        /**
         * 任何版本未验证
         */
        NONE,


        /**
         * 不在Quantlib中
         */
        OTHER,

        /**
         * QuantLib v0.8.1
         */
        V081,

        /**
         * QuantLib v0.9.7
         */
        V097

    }

    /**
     * 质量管理类
     */
    public enum Quality {
        /**
         * 翻译未完成
         */
        Q0_UNFINISHED,

        /**
         * 翻译完成
         */
        Q1_TRANSLATION,

        /**
         * JQuantLib API与QuantLib API非常相似，包括运算符重载（如果有的话）
         */
        Q2_RESEMBLANCE,

        /**
         * 许可证，标题注释，类注释，@Override，访问修饰符
         */
        Q3_DOCUMENTATION,

        /**
         * 传递单元测试（JUnit4）。计算精度在公差范围内.
         */
        Q4_UNIT,

        /**
         * 传递集成测试，例如执行功能的示例代码
         */
        Q5_INTEGRATION,

        /**
         * 准备发布的代码
         */
        Q6_RELEASED,

        /**
         * 传递代码分析
         */
        Q7_PROFILE,

        /**
         * 性能测试被认为是可接受的
         */
        Q8_PERFORMANCE
    }

}
