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

package me.wobblyyyy.pathfinder.math.functional;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.stream.DoubleStream;

/**
 * Math utility to get the average of a set of numbers.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class Average {
    /**
     * Get the average of a set of values.
     *
     * @param values the values to get the average of.
     * @return the average of the provided values.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static double of(double... values) {
        if (values.length < 1) return 0;
        else return DoubleStream.of(values).average().getAsDouble();
    }

    /**
     * Get the average of an array of points.
     *
     * @param points the points to average.
     * @return the average of the points.
     */
    public static Point of(Point... points) {
        double sumX = 0;
        double sumY = 0;
        final int l = points.length;

        for (Point point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }

        return new Point(
                sumX / l,
                sumY / l
        );
    }

    /**
     * Get the average of an array of points.
     *
     * @param points the points to average.
     * @return the average of the points.
     */
    public static HeadingPoint of(HeadingPoint... points) {
        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;
        final int l = points.length;

        for (HeadingPoint point : points) {
            sumX += point.getX();
            sumY += point.getY();
            sumZ += point.getHeading();
        }

        return new HeadingPoint(
                sumX / l,
                sumY / l,
                sumZ / l
        );
    }
}
