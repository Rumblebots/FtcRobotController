package me.wobblyyyy.intra.ftc2.utils.math;

/**
 * A class used in representing a proportional-integral-derivative
 * virtual controller.
 *
 * <p>
 * The code for this {@code PidController} is based off code from the wpilib
 * project, which I did not contribute to. None of the code here is copied
 * line-for-line, rather, the general structure and functionality of this
 * class generally mimics that of the wpilib {@code PidController}.
 * </p>
 *
 * <p>
 * PID controllers can sometimes be rather difficult to understand - I know
 * for a fact I didn't understand a thing about them when I first starting
 * using them in code. In order to (hopefully) help make this process at least
 * a bit easier for you, if you're not acquainted with PID controllers, here's
 * a brief collection of resources that might be helpful.
 * <ul>
 *     <li>
 *         <a href="https://en.wikipedia.org/wiki/PID_controller">
 *             Wikipedia
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://www.omega.com/en-us/resources/tuning-a-pid-controller">
 *             Omega Engineering: Tuning a PID Controller
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://www.sciencedirect.com/science/article/abs/pii/S0098135404001097">
 *             Science Direct: Tuning PID controllers for FOPTD systems
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://digital-library.theiet.org/content/journals/10.1049/ip-cta_19971435">
 *             IET Digital Library: Tuning PID controllers
 *         </a>
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * The balance of these effects is achieved by loop tuning to produce the
 * optimal control function. The tuning constants are shown below as "K" and
 * must be derived for each control application, as they depend on the response
 * characteristics of the complete loop external to the controller. These are
 * dependent on the behavior of the measuring sensor, the final control element
 * (such as a control valve), any control signal delays and the process itself.
 * Approximate values of constants can usually be initially entered knowing the
 * type of application, but they are normally refined, or tuned, by "bumping"
 * the process in practice by introducing a setpoint change and observing
 * the system response.
 * </p>
 *
 * <p>
 * The use of the PID algorithm does not guarantee optimal control of the
 * system or its control stability. Situations may occur where there are
 * excessive delays: the measurement of the process value is delayed, or the
 * control action does not apply quickly enough. In these cases lead–lag
 * compensation is required to be effective. The response of the controller
 * can be described in terms of its responsiveness to an error, the degree to
 * which the system overshoots a setpoint, and the degree of any system
 * oscillation. But the PID controller is broadly applicable since it relies
 * only on the response of the measured process variable, not on knowledge
 * or a model of the underlying process.
 * </p>
 *
 * <p>
 * Many PID loops control a mechanical device (for example, a valve). Mechanical
 * maintenance can be a major cost and wear leads to control degradation in the
 * form of either stiction or backlash in the mechanical response to an input
 * signal. The rate of mechanical wear is mainly a function of how often a
 * device is activated to make a change. Where wear is a significant concern,
 * the PID loop may have an output deadband to reduce the frequency of
 * activation of the output (valve). This is accomplished by modifying the
 * controller to hold its output steady if the change would be small (within
 * the defined deadband range). The calculated output must leave the deadband
 * before the actual output will change.
 * </p>
 *
 * @author Colin Robertson
 */
@SuppressWarnings("unused")
public class PidController {
    /**
     * The default time value.
     *
     * <p>
     * Assuming no time value is inputted as a constructor parameter, this
     * one will be used. A value of 0.02 is generally pretty okay, although
     * it might be wise to raise or lower that value depending on how
     * responsive your control system is.
     * </p>
     */
    public static final double TIME = 0.02;

    /**
     * The default minimum integral.
     *
     * <p>
     * If no minimum integral is provided, this one will be used.
     * </p>
     */
    public static final double INTEGRAL_MIN = -1.0;

    /**
     * The default maximum integral.
     *
     * <p>
     * If no maximum integral is provided, this one will be used.
     * </p>
     */
    public static final double INTEGRAL_MAX = 1.0;

    /**
     * The default minimum input.
     *
     * <p>
     * This value is negative infinity, meaning that there is no minimum
     * input value. If no minimum input is provided, this one will be used.
     * </p>
     */
    public static final double INPUT_MIN = Double.NEGATIVE_INFINITY;

    /**
     * The default maximum input.
     *
     * <p>
     * This value is positive infinity, meaning that there is no maximum
     * input value. If no maximum input is provided, this one will be used.
     * </p>
     */
    public static final double INPUT_MAX = Double.POSITIVE_INFINITY;

    /**
     * The default setpoint tolerance value.
     *
     * <p>
     * Generally speaking, tolerance values should be as low as possible
     * without causing issues with the functionality of the controlled
     * system that you're operating.
     * </p>
     */
    public static final double SETPOINT_TOLERANCE = 0.05;

    /**
     * The default velocity tolerance value.
     *
     * <p>
     * By default, this value is set to positive infinity, meaning that there
     * is no velocity tolerance.
     * </p>
     */
    public static final double VELOCITY_TOLERANCE = Double.POSITIVE_INFINITY;

    /**
     * The "proportional" component of the PID.
     *
     * <p>
     * The obvious method is proportional control: the motor current is set in
     * proportion to the existing error. However, this method fails if, for
     * instance, the arm has to lift different weights: a greater weight
     * needs a greater force applied for the same error on the down side,
     * but a smaller force if the error is on the upside. That's where the
     * integral and derivative terms play their part.
     * </p>
     *
     * <p>
     * Term P is proportional to the current value of the SP-PV error e(t). For
     * example, if the error is large and positive, the control output will be
     * proportionately large and positive, taking into account the gain factor
     * "K". Using proportional control alone will result in an error between
     * the setpoint and the actual process value because it requires an error
     * to generate the proportional response. If there is no error, there
     * is no corrective response.
     * </p>
     *
     * <p>
     * A high proportional gain results in a large change in the output for a
     * given change in the error. If the proportional gain is too high, the
     * system can become unstable (see the section on loop tuning). In contrast,
     * a small gain results in a small output response to a large input error,
     * and a less responsive or less sensitive controller. If the proportional
     * gain is too low, the control action may be too small when responding to
     * system disturbances. Tuning theory and industrial practice indicate that
     * the proportional term should contribute the bulk of the output change.
     * </p>
     */
    @SuppressWarnings("UnusedAssignment")
    private double kP = 0;

    /**
     * The "integral" component of the PID.
     *
     * <p>
     * An integral term increases action in relation not only to the error but
     * also the time for which it has persisted. So, if the applied force is
     * not enough to bring the error to zero, this force will be increased as
     * time passes. A pure "I" controller could bring the error to zero, but
     * it would be both slow reacting at the start (because the action would be
     * small at the beginning, needing time to get significant) and brutal (the
     * action increases as long as the error is positive, even if the error has
     * started to approach zero).
     * </p>
     *
     * <p>
     * Term I accounts for past values of the SP-PV error and integrates them
     * over time to produce the I term. For example, if there is a residual
     * SP-PV error after the application of proportional control, the integral
     * term seeks to eliminate the residual error by adding a control effect
     * due to the historic cumulative value of the error. When the error is
     * eliminated, the integral term will cease to grow. This will result
     * in the proportional effect diminishing as the error decreases,
     * but this is compensated for by the growing integral effect.
     * </p>
     *
     * <p>
     * The integral term accelerates the movement of the process towards
     * setpoint and eliminates the residual steady-state error that occurs
     * with a pure proportional controller. However, since the integral term
     * responds to accumulated errors from the past, it can cause the present
     * value to overshoot the setpoint value (see the section on loop tuning).
     * </p>
     */
    @SuppressWarnings("UnusedAssignment")
    private double kI = 0;

    /**
     * The "derivative" component of the PID.
     *
     * <p>
     * A derivative term does not consider the error (meaning it cannot bring
     * it to zero: a pure D controller cannot bring the system to its setpoint),
     * but the rate of change of error, trying to bring this rate to zero. It
     * aims at flattening the error trajectory into a horizontal line, damping
     * the force applied, and so reduces overshoot (error on the other side
     * because of too great applied force). Applying too much impetus when the
     * error is small and decreasing will lead to overshoot. After overshooting,
     * if the controller were to apply a large correction in the opposite
     * direction and repeatedly overshoot the desired position, the output
     * would oscillate around the setpoint in either a constant, growing, or
     * decaying sinusoid. If the amplitude of the oscillations increases with
     * time, the system is unstable. If they decrease, the system is stable.
     * If the oscillations remain at a constant magnitude, the system is
     * considered marginally stable.
     * </p>
     *
     * <p>
     * Term D is a best estimate of the future trend of the SP-PV error, based
     * on its current rate of change. It is sometimes called "anticipatory
     * control", as it is effectively seeking to reduce the effect of the SP-PV
     * error by exerting a control influence generated by the rate of error
     * change. The more rapid the change, the greater the controlling or
     * damping effect.
     * </p>
     *
     * <p>
     * Derivative action predicts system behavior and thus improves settling
     * time and stability of the system. An ideal derivative is not causal, so
     * that implementations of PID controllers include an additional low-pass
     * filtering for the derivative term to limit the high-frequency gain and
     * noise. Derivative action is seldom used in practice though – by one
     * estimate in only 25% of deployed controllers – because of its variable
     * impact on system stability in real-world applications.
     * </p>
     */
    @SuppressWarnings("UnusedAssignment")
    private double kD = 0;

    /**
     * The amount of time, in seconds, between each period update.
     *
     * <p>
     * This controller is designed to be updated as frequently as possible.
     * Well, no, not really - as regularly as possible would be a better term.
     * </p>
     */
    private final double periodSeconds;

    /**
     * The integral range.
     *
     * <p>
     * This range is defined by the {@link PidController#INTEGRAL_MIN} and
     * {@link PidController#INTEGRAL_MAX} values defined earlier.
     * </p>
     */
    private Range integralRange = new Range(INTEGRAL_MIN, INTEGRAL_MAX);

    /**
     * The input range.
     *
     * <p>
     * This value is, by default, entirely unrestrained. Input values can
     * range from negative to positive infinity.
     * </p>
     *
     * <p>
     * This range is defined by the {@link PidController#INPUT_MIN} and
     * {@link PidController#INPUT_MAX} values defined earlier.
     * </p>
     */
    private Range inputRange = new Range(INPUT_MIN, INPUT_MAX);

    /**
     * A comparator used in determining whether or not a setpoint value
     * is close enough to a target value.
     */
    private final Comparator c_setpoint = new Comparator(SETPOINT_TOLERANCE);

    /**
     * A comparator used in determining whether or not a velocity value is
     * close enough to a target value.
     */
    private final Comparator c_velocity = new Comparator(VELOCITY_TOLERANCE);

    /**
     * A tracker for the last-recorded position error.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private Error positionError = new Error(0, 0);

    /**
     * A tracker for the last-recorded velocity error.
     */
    @SuppressWarnings("FieldMayBeFinal")
    private Error velocityError = new Error(0, 0);

    /**
     * The PID controller's target position.
     *
     * <p>
     * This target position is defined in units. These units are entirely
     * irrelevant to the function of the PID.
     * </p>
     */
    private double setpoint;

    /**
     * The last-recorded measurement.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private double measurement;

    /**
     * Should the {@code PidController} function in a continuous manner?
     *
     * <p>
     * In essence: should the values of the PID controller be wrapped?
     * </p>
     */
    private boolean isContinuous;

    /**
     * Create a new {@code PidController} instance using the default time
     * scale - {@link PidController#TIME}.
     *
     * <p>
     * This is an overloaded constructor. Another constructor is available that
     * allows you to specify a custom time component.
     * </p>
     *
     * @param kP the {@code PidController}'s PROPORTIONAL component.
     * @param kI the {@code PidController}'s INTEGRAL component.
     * @param kD the {@code PidController}'s DERIVATIVE component.
     * @see PidController#PidController(double, double, double, double)
     * @see PidController#kP
     * @see PidController#kI
     * @see PidController#kD
     */
    public PidController(double kP,
                         double kI,
                         double kD) {
        this(
                kP,
                kI,
                kD,
                TIME
        );
    }

    /**
     * Create a new {@code PidController} instance.
     *
     * @param kP            the {@code PidController}'s PROPORTIONAL component.
     * @param kI            the {@code PidController}'s INTEGRAL component.
     * @param kD            the {@code PidController}'s DERIVATIVE component.
     * @param periodSeconds the amount of time, in seconds, between the
     *                      {@code PidController}'s regular updates. These
     *                      updates should occur a set amount of time apart.
     * @see PidController#PidController(double, double, double)
     * @see PidController#kP
     * @see PidController#kI
     * @see PidController#kD
     */
    public PidController(double kP,
                         double kI,
                         double kD,
                         double periodSeconds) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.periodSeconds = periodSeconds;
    }

    /**
     * Get the controller's proportional component.
     *
     * @return the controller's proportional component.
     * @see PidController#kP
     * @see PidController#setKp(double)
     */
    public double getKp() {
        return kP;
    }

    /**
     * Get the controller's integral component.
     *
     * @return the controller's integral component.
     * @see PidController#kI
     * @see PidController#setKi(double)
     */
    public double getKi() {
        return kI;
    }

    /**
     * Get the controller's derivative component.
     *
     * @return the controller's derivative component.
     * @see PidController#kD
     * @see PidController#setKd
     */
    public double getKd() {
        return kD;
    }

    /**
     * Get the controller's update interval, measured in seconds.
     *
     * @return the controller's update interval, measured in seconds.
     * @see PidController#periodSeconds
     */
    public double getPeriodSeconds() {
        return periodSeconds;
    }

    /**
     * Get the controller's integral range.
     *
     * @return the controller's integral range.
     * @see PidController#integralRange
     * @see PidController#setIntegralRange(Range)
     * @see PidController#setIntegralMin(double)
     * @see PidController#setIntegralMax(double)
     * @see PidController#INTEGRAL_MIN
     * @see PidController#INTEGRAL_MAX
     */
    public Range getIntegralRange() {
        return integralRange;
    }

    /**
     * Get the controller's input range.
     *
     * @return the controller's input range.
     * @see PidController#inputRange
     * @see PidController#setInputRange(Range)
     * @see PidController#setInputMin(double)
     * @see PidController#setInputMax(double)
     * @see PidController#INPUT_MIN
     * @see PidController#INPUT_MAX
     */
    public Range getInputRange() {
        return inputRange;
    }

    /**
     * Get the controller's minimum integral.
     *
     * @return the controller's minimum integral.
     * @see PidController#integralRange
     * @see PidController#setIntegralMin(double)
     * @see PidController#setIntegralMax(double)
     * @see PidController#INTEGRAL_MIN
     * @see PidController#INTEGRAL_MAX
     */
    public double getIntegralMin() {
        return getIntegralRange().getMin();
    }

    /**
     * Get the controller's maximum integral.
     *
     * @return the controller's maximum integral.
     * @see PidController#integralRange
     * @see PidController#setIntegralMin(double)
     * @see PidController#setIntegralMax(double)
     * @see PidController#INTEGRAL_MIN
     * @see PidController#INTEGRAL_MAX
     */
    public double getIntegralMax() {
        return getIntegralRange().getMax();
    }

    /**
     * Get the controller's minimum input.
     *
     * @return the controller's minimum input.
     * @see PidController#inputRange
     * @see PidController#setInputMin(double)
     * @see PidController#setInputMax(double)
     * @see PidController#INPUT_MIN
     * @see PidController#INPUT_MAX
     */
    public double getInputMin() {
        return getInputRange().getMin();
    }

    /**
     * Get the controller's maximum input.
     *
     * @return the controller's maximum input.
     * @see PidController#inputRange
     * @see PidController#setInputMin(double)
     * @see PidController#setInputMax(double)
     * @see PidController#INPUT_MIN
     * @see PidController#INPUT_MAX
     */
    public double getInputMax() {
        return getInputRange().getMax();
    }

    /**
     * Get the setpoint comparator's tolerance.
     *
     * @return the controller comparator's tolerance.
     * @see PidController#c_setpoint
     */
    public double getSetpointTolerance() {
        return c_setpoint.getTolerance();
    }

    /**
     * Get the velocity comparator's tolerance.
     *
     * @return the velocity comparator's tolerance.
     * @see PidController#c_velocity
     */
    public double getVelocityTolerance() {
        return c_velocity.getTolerance();
    }

    /**
     * Get the positional error.
     *
     * @return positional error.
     * @see PidController#positionError
     */
    public Error getPositionError() {
        return Error.copy(positionError);
    }

    /**
     * Get the velocity error.
     *
     * @return velocity error.
     * @see PidController#velocityError
     */
    public Error getVelocityError() {
        return Error.copy(velocityError);
    }

    /**
     * Get the {@code PidController}'s setpoint value.
     *
     * @return the {@code PidController}'s setpoint value.
     * @see PidController#setpoint
     * @see PidController#setSetpoint(double)
     * @see PidController#setSetpointTolerance(double)
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * Set the controller's proportional coefficient.
     *
     * @param kP the controller's proportional coefficient.
     * @see PidController#kP
     * @see PidController#getKp()
     */
    public void setKp(double kP) {
        this.kP = kP;
    }

    /**
     * Set the controller's integral component.
     *
     * @param kI the controller's integral component.
     * @see PidController#kI
     * @see PidController#getKi()
     */
    public void setKi(double kI) {
        this.kI = kI;
    }

    /**
     * Set the controller's derivative component.
     *
     * @param kD the controller's derivative component.
     * @see PidController#kD
     * @see PidController#getKd()
     */
    public void setKd(double kD) {
        this.kD = kD;
    }

    /**
     * Set the controller's integral range.
     *
     * @param integralRange the controller's integral range.
     * @see PidController#integralRange
     */
    public void setIntegralRange(Range integralRange) {
        this.integralRange = integralRange;
    }

    /**
     * Set the controller's input range.
     *
     * @param inputRange the controller's input range.
     * @see PidController#inputRange
     */
    public void setInputRange(Range inputRange) {
        this.inputRange = inputRange;
    }

    /**
     * Set the integral range's minimum.
     *
     * @param min the integral range's minimum.
     * @see PidController#integralRange
     */
    public void setIntegralMin(double min) {
        getIntegralRange().setMin(min);
    }

    /**
     * Set the integral range's maximum.
     *
     * @param max the integral range's maximum.
     * @see PidController#integralRange
     */
    public void setIntegralMax(double max) {
        getIntegralRange().setMax(max);
    }

    /**
     * Set the input range's minimum.
     *
     * @param min the input range's minimum.
     * @see PidController#inputRange
     */
    public void setInputMin(double min) {
        getInputRange().setMin(min);
    }

    /**
     * Set the input range's maximum.
     *
     * @param max the input range's maximum.
     * @see PidController#inputRange
     */
    public void setInputMax(double max) {
        getInputRange().setMax(max);
    }

    /**
     * Set the controller's setpoint comparator's tolerance.
     *
     * @param tolerance the controller's setpoint comparator's tolerance.
     * @see PidController#c_setpoint
     */
    public void setSetpointTolerance(double tolerance) {
        c_setpoint.setTolerance(tolerance);
    }

    /**
     * Set the controller's velocity comparator's tolerance.
     *
     * @param tolerance the controller's velocity comparator's tolerance.
     * @see PidController#c_velocity
     */
    public void setVelocityTolerance(double tolerance) {
        c_velocity.setTolerance(tolerance);
    }

    /**
     * Set the controller's setpoint.
     *
     * @param setpoint the controller's (new) setpoint.
     * @see PidController#setpoint
     * @see PidController#getSetpoint()
     * @see PidController#setSetpointTolerance(double)
     */
    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    /**
     * Is the {@code PidController} operating in continuous mode?
     *
     * @return whether or not the controller is operating in continuous mode.
     * @see PidController#isContinuous
     * @see PidController#isContinuous(boolean)
     */
    public boolean isContinuous() {
        return isContinuous;
    }

    /**
     * Set whether or not the controller should operate in continuous mode.
     *
     * @param shouldBeContinuous whether or not the controller should operate
     *                           in continuous mode.
     * @see PidController#isContinuous
     * @see PidController#isContinuous()
     */
    public void isContinuous(boolean shouldBeContinuous) {
        this.isContinuous = shouldBeContinuous;
    }

    /**
     * Calculate the {@code PidController}'s output after setting a new
     * setpoint. By setting a setpoint prior to calling the code you'd like
     * to call, the controller will now determine output based on the new
     * setpoint.
     *
     * <p>
     * This method should be called as regularly as possible in accordance
     * with the provided time period. Not doing so may cause issues with
     * the accuracy and functionality of the control loop.
     * </p>
     *
     * <p>
     * This is an overloaded method for the other calculate method provided
     * in this controller class. This method does more than the other method.
     * </p>
     *
     * @param measurement the input measurement that should be used in
     *                    calculating values.
     * @param setpoint    the setpoint that should be set.
     * @return a calculated value.
     * @see PidController#calculate(double)
     * @see PidController#kP
     * @see PidController#kI
     * @see PidController#kD
     */
    public double calculate(double measurement,
                            double setpoint) {
        setSetpoint(setpoint);
        return calculate(measurement);
    }

    /**
     * Calculate the {@code PidController}'s output values based on an
     * input measurement value.
     *
     * <p>
     * This method should be called as regularly as possible in accordance
     * with the provided time period. Not doing so may cause issues with
     * the accuracy and functionality of the control loop.
     * </p>
     *
     * @param measurement an inputted measurement.
     * @return a calculated value.
     * @see PidController#calculate(double, double)
     * @see PidController#kP
     * @see PidController#kI
     * @see PidController#kD
     */
    public double calculate(double measurement) {
        this.measurement = measurement;

        if (isContinuous()) {
            positionError.set(EMath.inModulus(
                    getSetpoint() - measurement,
                    getInputMin(),
                    getInputMax()
            ));
        } else {
            positionError.set(
                    getSetpoint() - measurement
            );
        }

        velocityError.set(
                (getPositionError().getCurrent() - measurement) / periodSeconds
        );

        return getKp() * getPositionError().getCurrent() +
                getKi() * getPositionError().getTotal() +
                getKd() * getVelocityError().getCurrent();
    }
}
