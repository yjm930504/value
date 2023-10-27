package yjm.value;


import yjm.value.daycounters.Actual365Fixed;
import yjm.value.daycounters.DayCounter;
import yjm.value.math.Rounding;
import yjm.value.termstructures.InterestRate;

public class Main {


    public static void main(String[] args) {
//        double r = 2.0;
//
//        InterestRate interestRate = new InterestRate(r, new Actual365Fixed());
//        InterestRate[] couponRates_ = new InterestRate[]{interestRate};

        Rounding r = new Rounding(2, Rounding.Type.Closest,5);
        double ar = r.operator(100.21245);

    }


}
