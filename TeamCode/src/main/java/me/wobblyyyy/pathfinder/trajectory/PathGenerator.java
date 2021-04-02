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

package me.wobblyyyy.pathfinder.trajectory;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.intra.ftc2.utils.math.Range;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

/**
 * Convert a trajectory into a path.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class PathGenerator {
    /**
     * Convert a trajectory into a path. The path is generated by sampling the
     * trajectory 50 times per segment, and then adding all of the sampled
     * points into an array which is returned to you.
     *
     * @param trajectory the trajectory that should be converted.
     * @param samples    how many times the trajectory should be sampled.
     * @return a {@code DynamicArray} of points generated from the trajectory's
     * component segments.
     */
    public static DynamicArray<HeadingPoint> toPath(Trajectory trajectory,
                                                    int samples) {
        DynamicArray<Segment> segments = trajectory.getSegments();
        DynamicArray<HeadingPoint> points = new DynamicArray<>(
                segments.size() * samples
        );

        /*
         * For each segment in the trajectory...
         */
        segments.itr().forEach(segment -> {
            /*
             * Define a range of acceptable X values for the segment.
             */
            Range xRange = new Range(
                    Math.min(segment.start().getX(), segment.end().getX()),
                    Math.max(segment.start().getX(), segment.end().getX())
            );

            /*
             * This is used if we need to invert the order that the points
             * are added to the actual array of points.
             */
            DynamicArray<HeadingPoint> localPoints = new DynamicArray<>();

            /*
             * Each path should be interpolated 50 times.
             *
             * i = minimum
             * while i < max
             *  i += range / 50
             */
            for (double i = xRange.getMin();
                 i < xRange.getMax();
                 i += ((xRange.getMax() - xRange.getMin()) / samples)) {
                if (segment.start().getX() < segment.end().getX()) {
                    /*
                     * If the segment's start point is greater than the
                     * segment's end point, we know the segment isn't inverted.
                     */
                    points.add(Point.withHeading(
                            segment.interpolateFromX(i),
                            segment.angleAt(segment
                                    .interpolateFromX(i))
                                    .getDegrees()
                    ));
                } else {
                    /*
                     * If that's not the case, however, the segment is inverted.
                     * Add the points to the local points, which will then
                     * be added to the main thing of points in a minute.
                     */
                    localPoints.add(Point.withHeading(
                            segment.interpolateFromX(i),
                            segment.angleAt(segment
                                    .interpolateFromX(i))
                                    .getDegrees()
                    ));
                }
            }

            /*
             * For each of the local points, add it and effectively invert
             * the order to the main points array.
             */
            final int maxIndex = points.size() - 1;
            localPoints.itr().forEach(point -> points.add(maxIndex, point));
        });

        return points;
    }

    /**
     * Convert a trajectory into a path. The path is generated by sampling the
     * trajectory 50 times per segment, and then adding all of the sampled
     * points into an array which is returned to you.
     *
     * @param trajectory the trajectory that should be converted.
     * @return a {@code DynamicArray} of points generated from the trajectory's
     * component segments.
     */
    public static DynamicArray<HeadingPoint> toPath(Trajectory trajectory) {
        return toPath(trajectory, 50);
    }
}
