package com.ds.expanse.cartograph.component;

import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.awt.image.BufferedImage;

@Component
public class PerlinNoiseGenerator implements PerlinNoise{

    //standard perlin permutation lookup table, don't touch

    private static final int p[] =  { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };

    private static BufferedImage image;
    private double[][] buffer;

    public PerlinNoiseGenerator() {
    }

    public int[][] getImage(){
        int[][] bufferedArray = new int[image.getHeight()][image.getWidth()];

        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                int temp = image.getRGB(i, j);
                System.out.println(temp);
                bufferedArray[i][j] = temp;
            }
        }

        return bufferedArray;
    }

    public PerlinNoiseGenerator(int height, int width){
        generate(height, width);
    }

    public void generate(int width, int height){
//        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int freq = 6; //change depending on desired output

        buffer = new double[width][height];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                double dx = (double)x / height;
                double dy = (double)y / height;

                double noise = noise((dx * freq), (dy * freq));

                buffer[x][y] = noise;

//                noise = (noise - 1) / 2; //standardize noise
//                int b = (int)(noise * 0xFF); //classify noise into RGB, starting with blue channel
//                int g = b * 0x100; //add green channel
//                int r = b * 0x10000; //add red channel
//                int returnVal = r;
//                image.setRGB(x, y, returnVal);
            }
        }
    }

    private static double noise(double x, double y){
        int xi = (int)Math.floor(x) & 255; //bitwise conversion xi in RGB
        int yi = (int)Math.floor(y) & 255; //bitwise conversion yi in RGB
        int gradient1 = p[p[xi] + yi];
        int gradient2 = p[p[xi + 1] + yi]; //next in series xi
        int gradient3 = p[p[xi] + 1 + yi];
        int gradient4 = p[p[xi + 1] + 1 + yi]; //next in series yi

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double direction1 = grad(gradient1, xf, yf);
        double direction2 = grad(gradient2, xf - 1, yf);
        double direction3 = grad(gradient3, xf, yf - 1);
        double direction4 = grad(gradient4, xf - 1, yf - 1);

        double uVector = fade(xf);
        double vVector = fade(yf);

        double x1Inter = lerp(uVector, direction1, direction2); //interpolate x1, x2 from directions
        double x2Inter = lerp(uVector, direction3, direction4);
        double yInter = lerp(vVector, x1Inter, x2Inter); //interpolate y value for 2D "noise", y is only important component

        return yInter;
    }

    private static double lerp(double amount, double left, double right){
        return ((1 - amount) * left + amount * right); //interpolation formula
    }

    private static double grad(int hash, double x, double y){
        switch(hash & 3){ //possible vector directions
            case 0: return x + y;
            case 1: return -x + y;
            case 2: return x - y;
            case 3: return -x - y;
            default: return 0;
        }
    }

    private static double fade(double t){
        return t * t * t * (t * (t * 6 - 15) + 10); //No idea why this works, just does
    }

    public float perlin(int x, int y){
        if ( buffer == null ) {
            generate(100,100);
        }

        return (float)buffer[x][y];
    }

    public int perlin(float perlinValue, Function<Float, Integer> conversion){
        return conversion.apply(perlinValue);
    }

    public int perlin(int x, int y, Function<Float, Integer> conversion){
        return conversion.apply(perlin(x, y));
    }

    public static void main(String ... args) {
        PerlinNoise perlinNoise = new PerlinNoiseGenerator(50,50);
        System.out.println(perlinNoise.perlin(49,2));
    }
}