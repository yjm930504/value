package yjm.value;


import yjm.value.daycounters.Actual365Fixed;
import yjm.value.daycounters.DayCounter;
import yjm.value.termstructures.InterestRate;

public class Main {


    public static void main(String[] args) {
        double r = 2.0;

        InterestRate interestRate = new InterestRate(r, new Actual365Fixed());
        InterestRate[] couponRates_ = new InterestRate[]{interestRate};


    }


}
