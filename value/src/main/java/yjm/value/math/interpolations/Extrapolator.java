package yjm.value.math.interpolations;

public interface Extrapolator {

    public void enableExtrapolation();

    public void disableExtrapolation();

    public boolean allowsExtrapolation();

}
