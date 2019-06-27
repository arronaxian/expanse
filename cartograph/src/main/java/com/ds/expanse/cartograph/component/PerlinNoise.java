package com.ds.expanse.cartograph.component;
import java.util.function.Function;

/**
 *  Generates Perlin noise which is a type of gradient noise developed by Ken Perlin.
 *
 *  Usage:
 *  PerlinNoise noise = new PerlinNoiseBuilder();
 *  noise.generate(100,100);
 *  float perlinValue = noise.perlin(50,50);
 *  int conversionValue = noise.perlin(50,50, (pv) -> { if ( pv < 0 ) return 1; else return 2; } );
 */
public interface PerlinNoise {
    /**
     * Generates Perlin noise for area width * height.
     * @param width the width of the area.
     * @param height The height of the area.
     */
    void generate(int width, int height);

    /**
     * Computes perlin noise at x and y.
     * @param x the x position
     * @param y the y position
     * @return A value between -1 and 1; if out of bounds return 1.
     */
    double perlin(int x, int y);
    double perlin2(double x, double y);

    /**
     * Compute perlin noise at coordinates x,y and covert to int.
     * @param perlinValue the Perlin value
     * @param conversion A function with Perlin to int conversions.
     * @return The converted Perlin noise value.  if out of bounds return 1 to conversion.
     */
    int perlin(double perlinValue, Function<Double, Integer> conversion);

    /**
     * Compute perlin noise at coordinates x,y and covert to int.
     * @param x the x position
     * @param y the y position
     * @param conversion A function with Perlin to int conversions.
     * @return The converted Perlin noise value.  if out of bounds return 1 to conversion.
     */
    int perlin(int x, int y, Function<Double, Integer> conversion);
}
