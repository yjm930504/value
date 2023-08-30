package riskcapital.marketrisk.sba;

import org.yjm.instruments.Instruments;


public class GIRR extends RiskClass{

    private GIRRFactor factor;

    public GIRR(GIRRFactor factor){
        this.factor = factor;
    }

    private Instruments instrument;

    public enum GIRRFactor{
        /** 无风险利率曲线 */
        RISK_FREE_CURVE,
        /** 隐含通胀率曲线 */
        Implied_Inflation_CURVE,
        /** 交叉货币基差曲线 */
        Cross_Currency_Basis_Curve,
    }

    public double riskFreeCurveTerm[] = {0.25,0.5,1,2,3,5,10,15,20,30};


}
