/*
 * ======================================================================
 * || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 * ||                                                                  ||
 * || This file is part of the "Pathfinder" project, which is licensed ||
 * || and distributed under the GPU General Public License V3.         ||
 * ||                                                                  ||
 * || Pathfinder is available on GitHub:                               ||
 * || https://github.com/Wobblyyyy/Pathfinder                          ||
 * ||                                                                  ||
 * || Pathfinder's license is available:                               ||
 * || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 * ||                                                                  ||
 * || Re-distribution of this, or any other files, is allowed so long  ||
 * || as this same copyright notice is included and made evident.      ||
 * ||                                                                  ||
 * || Unless required by applicable law or agreed to in writing, any   ||
 * || software distributed under the license is distributed on an "AS  ||
 * || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 * || express or implied. See the license for specific language        ||
 * || governing permissions and limitations under the license.         ||
 * ||                                                                  ||
 * || Along with this file, you should have received a license file,   ||
 * || containing a copy of the GNU General Public License V3. If you   ||
 * || did not receive a copy of the license, you may find it online.   ||
 * ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.kinematics;

import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.AngleUtils;

import java.util.function.Function;

/**
 * Kinematics designed for using relative rather than absolute transformations.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class RelativeMeccanumKinematics {
//    private static final double SIN_45 = Math.sin(Math.toDegrees(45));
//    private static final double SIN_315 = Math.sin(Math.toDegrees(315));
//    private static final double COS_45 = Math.cos(Math.toDegrees(45));
//    private static final double COS_315 = Math.cos(Math.toDegrees(315));

    private static final double[] ANGLES = new double[]{
            45,  // FRONT LEFT
            315, // FRONT RIGHT
            315, // BACK LEFT
            45   // BACK RIGHT
    };

    public RelativeMeccanumKinematics() {

    }

    /**
     * Calculate a single component transformation based on a trig function
     * and transformation parameters.
     *
     * @param trig          the trig function that's applied to the angles.
     *                      This should really only ever be sin/cos.
     * @param movementAngle the angle at which the wheel is going to move at.
     * @param wheelAngle    the angle at which the wheel applies power.
     * @param magnitude     the magnitude at which the wheel should move at.
     * @return a single transformation (often x or y).
     */
    private static double transform(Function<Double, Double> trig,
                                    double movementAngle,
                                    double wheelAngle,
                                    double magnitude) {
        double trigMovementAngle = trig.apply(movementAngle);
        double trigWheelAngle = trig.apply(wheelAngle);

        return trigMovementAngle * trigWheelAngle * magnitude;
    }

    /**
     * Calculate power for a single wheel.
     *
     * @param movementAngle the angle at which the wheel should be moving at.
     *                      This angle should be measured in degrees.
     * @param wheelAngle    the angle at which the wheel will apply power.
     *                      Typically, on a meccanum robot, the angles will
     *                      be 45 and 315, but any variants work.
     * @param magnitude     the magnitude at which the wheel's power should be
     *                      applied.
     * @return a power value calculated based on the movement angle, the wheel
     * angle, and the magnitude.
     */
    private static double calculatePower(double movementAngle,
                                         double wheelAngle,
                                         double magnitude) {
        // X transform uses sine
        double xTransform = transform(
                Math::sin,
                Math.toRadians(movementAngle),
                Math.toRadians(wheelAngle),
                magnitude
        );

        // Y transform uses cosine
        double yTransform = transform(
                Math::cos,
                Math.toRadians(movementAngle),
                Math.toRadians(wheelAngle),
                magnitude
        );

        // A wheel's power is equal to the sum of its component
        // X and Y translations.
        return xTransform + yTransform;
    }

    /**
     * Convert a robot transformation to a meccanum state that can be applied
     * to your ever-so-lovely meccanum robot.
     *
     * <p>
     * This transformation should often be generated with
     * {@link RTransform#fromGyro(double, double, double, Angle)} instead of
     * creating a new transformation directly. This transformation is ALWAYS
     * relative to the robot, so the transformation should be generated to
     * account for the robot's current positioning.
     * </p>
     *
     * @param transform the transformation that's used in generating the
     *                  meccanum state. This transformation's X and Y values
     *                  should fit within the range (-1, 1) and should, when
     *                  added, not have an absolute value exceeding 2. This
     *                  transformation's turn value does not use a specific
     *                  unit - rather, higher turn values make the robot
     *                  turn faster, and lower ones make it turn slower. The
     *                  turn value is added to each of the wheel's powers
     *                  after determining the wheel's power based on movement
     *                  angle, so using a turn value that's too high will
     *                  cause your robot to spin in circles and go haywire.
     * @return a meccanum state for the desired transformation.
     */
    public MeccanumState toMeccanumState(RTransform transform) {
        // The angle at which the robot will move.
        // This angle is determined by looking at the component X and Y
        // transform values, very epic.
        double movementAngle = AngleUtils.fixDeg(Math.toDegrees(Math.atan2(
                transform.getY(),
                transform.getX()
        )) + 0);

        // The magnitude at which the robot should move. We clip the magnitude
        // at 1 so the robot doesn't go at mach ten.
        double magnitude = Math.min(Math.hypot(transform.getX(), transform.getY()), 1);

        // Create power values for each of the individual wheels.
        // See the ANGLES double[] at the top of this class to see the
        // angles each of the wheels moves at.
        double fl = calculatePower(movementAngle, ANGLES[0], magnitude);
        double fr = calculatePower(movementAngle, ANGLES[1], magnitude);
        double bl = calculatePower(movementAngle, ANGLES[2], magnitude);
        double br = calculatePower(movementAngle, ANGLES[3], magnitude);

        // Add turn values to each of these bad boys. These turn values can
        // sometimes cause the robot's power to go haywire - if you're
        // turning too fast (turn values near 1ish) your robot won't do
        // anything other than turn.
        fl += transform.getTurn();
        fr -= transform.getTurn();
        bl += transform.getTurn();
        br -= transform.getTurn();

        // Create a new state and normalize the power values.
        MeccanumState state = new MeccanumState(fl, fr, bl, br);
        state.normalizeFromMaxUnderOne();

        // Redefine the state after multiplying each of the normalized
        // power values by the total magnitude.
        state = new MeccanumState(
                state.flPower() * magnitude,
                state.frPower() * magnitude,
                state.blPower() * magnitude,
                state.brPower() * magnitude
        );

        // And we're done!
        return state;
    }
}
