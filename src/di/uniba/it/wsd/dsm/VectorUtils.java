/**
   Copyright (c) 2014, the LESK-WSD-DSM AUTHORS.

   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:

 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided
   with the distribution.

 * Neither the name of the University of Bari nor the names
   of its contributors may be used to endorse or promote products
   derived from this software without specific prior written
   permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
    
   GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 **/

package di.uniba.it.wsd.dsm;

import java.util.Arrays;
import java.util.Random;

/**
 * Some utils to manage vectors
 * @author pierpaolo
 */
public class VectorUtils {

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float[] min(float[] v1, float[] v2) {
        float[] v = new float[v1.length];
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] <= v2[i]) {
                v[i] = v1[i];
            } else {
                v[i] = v2[i];
            }
        }
        return v;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float[] max(float[] v1, float[] v2) {
        float[] v = new float[v1.length];
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] >= v2[i]) {
                v[i] = v1[i];
            } else {
                v[i] = v2[i];
            }
        }
        return v;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double[] min(double[] v1, double[] v2) {
        double[] v = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] <= v2[i]) {
                v[i] = v1[i];
            } else {
                v[i] = v2[i];
            }
        }
        return v;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double[] max(double[] v1, double[] v2) {
        double[] v = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] >= v2[i]) {
                v[i] = v1[i];
            } else {
                v[i] = v2[i];
            }
        }
        return v;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double[] addVectors(double[] v1, double[] v2) {
        double[] v = new double[v1.length];
        Arrays.fill(v, 0f);
        for (int i = 0; i < v1.length; i++) {
            v[i] = v1[i] + v2[i];
        }
        return v;
    }

    /**
     *
     * @param vec
     * @return
     */
    public static double[] getNormalizedVector(double[] vec) {
        if (isZeroVector(vec)) {
            return vec;
        }
        double norm = 0;
        int i;
        double[] tmpVec = new double[vec.length];
        for (i = 0; i < vec.length; ++i) {
            tmpVec[i] = vec[i];
        }
        for (i = 0; i < tmpVec.length; ++i) {
            norm += tmpVec[i] * tmpVec[i];
        }
        norm = (double) Math.sqrt(norm);
        for (i = 0; i < tmpVec.length; ++i) {
            tmpVec[i] = tmpVec[i] / norm;
        }
        return tmpVec;
    }

    /**
     *
     * @param vec1
     * @param vec2
     * @return
     */
    public static double scalarProduct(double[] vec1, double[] vec2) {
        double result = 0;
        for (int i = 0; i < vec1.length; ++i) {
            result += vec1[i] * vec2[i];
        }
        return result;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double[] dotVectors(double[] v1, double[] v2) {
        double[] v = new double[v1.length];
        Arrays.fill(v, 0f);
        for (int i = 0; i < v1.length; i++) {
            v[i] = v1[i] * v2[i];
        }
        return v;
    }

    /**
     *
     * @param size
     * @return
     */
    public static double[] getZeroVector(int size) {
        double[] v = new double[size];
        Arrays.fill(v, 0);
        return v;
    }

    /**
     *
     * @param vector
     * @return
     */
    public static boolean isZeroVector(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param tensor
     * @return
     */
    public static boolean isZeroTensor(float[][] tensor) {
        for (float[] tensor1 : tensor) {
            for (int j = 0; j < tensor1.length; j++) {
                if (tensor1[j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the convolution of two vectors; see Plate, Holographic Reduced
     * Representation, p. 76.
     * @param vec1
     * @param vec2
     * @return 
     */
    /*
     * public static double[] getConvolutionFromVectors(double[] vec1, double[]
     * vec2) { int dim = vec1.length; double[] conv = new double[2 * dim - 1];
     * for (int i = 0; i < dim; ++i) { conv[i] = 0; conv[conv.length - 1 - i] =
     * 0; for (int j = 0; j <= i; ++j) { // Count each pair of diagonals.
     * conv[i] += vec1[i - j] * vec2[j]; if (i != dim - 1) { // Avoid counting
     * lead diagonal twice. conv[conv.length - 1 - i] = vec1[dim - 1 - i + j] *
     * vec2[dim - 1 - j]; } } } return VectorUtils.getNormalizedVector(conv); }
     */
    public static double[] getConvolutionFromVectors(double[] vec1, double[] vec2) {
        double[] conv = new double[vec1.length];
        for (int j = 0; j < vec1.length; j++) {
            conv[j] = 0;
            for (int k = 0; k < vec1.length; k++) {
                if (vec1[k] != 0) {
                    conv[j] += vec1[k] * vec2[mod((j - k), vec1.length)];
                }
            }
        }
        return conv;
        //return VectorUtils.getNormalizedVector(conv);
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static int mod(int x, int y) {
        int result = (x % y);
        if (result < 0) {
            result += y;
        }
        return result;
    }

    /**
     *
     * @param dim
     * @param element
     * @return
     */
    public static double[] getBaseVector(int dim, int element) {
        double[] v = new double[dim];
        Arrays.fill(v, 0d);
        v[element] = 1d;
        return v;
    }

    /**
     *
     * @param dim
     * @param element
     * @return
     */
    public static float[] getBaseVectorFloat(int dim, int element) {
        float[] v = new float[dim];
        Arrays.fill(v, 0f);
        v[element] = 1f;
        return v;
    }

    /**
     *
     * @param dim
     * @return
     */
    public static double[] createZeroVectors(int dim) {
        double[] v = new double[dim];
        Arrays.fill(v, 0d);
        return v;
    }

    /**
     *
     * @param dim
     * @return
     */
    public static double[] createOneVectors(int dim) {
        double[] v = new double[dim];
        Arrays.fill(v, 1d);
        return v;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float[] addVectors(float[] v1, float[] v2) {
        float[] v = new float[v1.length];
        Arrays.fill(v, 0f);
        for (int i = 0; i < v1.length; i++) {
            v[i] = v1[i] + v2[i];
        }
        return v;
    }

    /**
     *
     * @param vec
     * @return
     */
    public static float[] getNormalizedVector(float[] vec) {
        if (isZeroVector(vec)) {
            return vec;
        }
        float norm = 0;
        int i;
        float[] tmpVec = new float[vec.length];
        for (i = 0; i < vec.length; ++i) {
            tmpVec[i] = vec[i];
        }
        for (i = 0; i < tmpVec.length; ++i) {
            norm += tmpVec[i] * tmpVec[i];
        }
        norm = (float) Math.sqrt(norm);
        for (i = 0; i < tmpVec.length; ++i) {
            tmpVec[i] = tmpVec[i] / norm;
        }
        return tmpVec;
    }

    /**
     *
     * @param vec
     * @return
     */
    public static float getNorm(float[] vec) {
        if (isZeroVector(vec)) {
            return 0;
        }
        float norm = 0;

        for (int i = 0; i < vec.length; ++i) {
            norm += vec[i] * vec[i];
        }
        return (float) Math.sqrt(norm);
    }

    /**
     *
     * @param vec1
     * @param vec2
     * @return
     */
    public static float scalarProduct(float[] vec1, float[] vec2) {
        float result = 0;
        for (int i = 0; i < vec1.length; ++i) {
            result += vec1[i] * vec2[i];
        }
        return result;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float[] dotVectors(float[] v1, float[] v2) {
        float[] v = new float[v1.length];
        Arrays.fill(v, 0f);
        for (int i = 0; i < v1.length; i++) {
            v[i] = v1[i] * v2[i];
        }
        return v;
    }

    /**
     *
     * @param v1
     * @param s
     * @return
     */
    public static float[] scalarVector(float[] v1, float s) {
        float[] v = new float[v1.length];
        Arrays.fill(v, 0f);
        for (int i = 0; i < v1.length; i++) {
            v[i] = v1[i] * s;
        }
        return v;
    }

    /**
     *
     * @param size
     * @return
     */
    public static float[] getZeroVectorFloat(int size) {
        float[] v = new float[size];
        Arrays.fill(v, 0);
        return v;
    }

    /**
     *
     * @param vector
     * @return
     */
    public static boolean isZeroVector(float[] vector) {
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the convolution of two vectors; see Plate, Holographic Reduced
     * Representation, p. 76.
     * @param vec1
     * @param vec2
     * @return 
     */
    /*
     * public static float[] getConvolutionFromVectors(float[] vec1, float[]
     * vec2) { int dim = vec1.length; float[] conv = new float[2 * dim - 1]; for
     * (int i = 0; i < dim; ++i) { conv[i] = 0; conv[conv.length - 1 - i] = 0;
     * for (int j = 0; j <= i; ++j) { // Count each pair of diagonals. conv[i]
     * += vec1[i - j] * vec2[j]; if (i != dim - 1) { // Avoid counting lead
     * diagonal twice. conv[conv.length - 1 - i] = vec1[dim - 1 - i + j] *
     * vec2[dim - 1 - j]; } } } return VectorUtils.getNormalizedVector(conv); }
     */
    public static float[] getConvolutionFromVectors(float[] vec1, float[] vec2) {
        float[] conv = new float[vec1.length];
        for (int j = 0; j < vec1.length; j++) {
            conv[j] = 0;
            for (int k = 0; k < vec1.length; k++) {
                if (vec1[k] != 0) {
                    conv[j] += vec1[k] * vec2[mod((j - k), vec1.length)];
                }
            }
        }
        return conv;
        //return VectorUtils.getNormalizedVector(conv);
    }

    /**
     *
     * @param dim
     * @return
     */
    public static float[] createZeroVectorsFloat(int dim) {
        float[] v = new float[dim];
        Arrays.fill(v, 0f);
        return v;
    }

    /**
     *
     * @param dim
     * @return
     */
    public static float[] createOneVectorsFloat(int dim) {
        float[] v = new float[dim];
        Arrays.fill(v, 1f);
        return v;
    }

    /**
     *
     * @param dimension
     * @param seedLength
     * @param random
     * @return
     */
    public static float[] generateRandomVector(int dimension, int seedLength, Random random) {
        short[] generateRandomIndexes = generateRandomIndexes(dimension, seedLength, random);
        return randomIndexes2RandomVector(generateRandomIndexes, dimension);
        /*boolean[] randVector = new boolean[dimension];
         Arrays.fill(randVector, false);
         short[] randIndex = new short[seedLength];

         int testPlace, entryCount = 0;

         //put in +1 entries
         while (entryCount < seedLength / 2) {
         testPlace = random.nextInt(dimension);
         if (!randVector[testPlace]) {
         randVector[testPlace] = true;
         randIndex[entryCount] = new Integer(testPlace + 1).shortValue();
         entryCount++;
         }
         }

         // put in -1 entries
         while (entryCount < seedLength) {
         testPlace = random.nextInt(dimension);
         if (!randVector[testPlace]) {
         randVector[testPlace] = true;
         randIndex[entryCount] = new Integer((1 + testPlace) * -1).shortValue();
         entryCount++;
         }
         }
         float[] vector = new float[dimension];
         Arrays.fill(vector, 0);
         for (int i = 0; i < randIndex.length; i++) {
         if (randIndex[i] > 0) {
         vector[Math.abs(randIndex[i]) - 1] = 1f;
         } else {
         vector[Math.abs(randIndex[i]) - 1] = -1f;
         }
         }
         return vector;*/
    }

    /**
     *
     * @param dimension
     * @param seedLength
     * @param random
     * @return
     */
    public static short[] generateRandomIndexes(int dimension, int seedLength, Random random) {
        boolean[] randVector = new boolean[dimension];
        Arrays.fill(randVector, false);
        short[] randIndex = new short[seedLength];

        int testPlace, entryCount = 0;

        /*
         * put in +1 entries
         */
        while (entryCount < seedLength / 2) {
            testPlace = random.nextInt(dimension);
            if (!randVector[testPlace]) {
                randVector[testPlace] = true;
                randIndex[entryCount] = new Integer(testPlace + 1).shortValue();
                entryCount++;
            }
        }

        /*
         * put in -1 entries
         */
        while (entryCount < seedLength) {
            testPlace = random.nextInt(dimension);
            if (!randVector[testPlace]) {
                randVector[testPlace] = true;
                randIndex[entryCount] = new Integer((1 + testPlace) * -1).shortValue();
                entryCount++;
            }
        }
        return randIndex;
    }

    /**
     *
     * @param randIndex
     * @param dimension
     * @return
     */
    public static float[] randomIndexes2RandomVector(short[] randIndex, int dimension) {
        float[] vector = new float[dimension];

        Arrays.fill(vector, 0);
        for (int i = 0; i < randIndex.length; i++) {
            if (randIndex[i] > 0) {
                vector[randIndex[i] - 1] = 1f;
            } else {
                vector[Math.abs(randIndex[i]) - 1] = -1f;
            }
        }
        return vector;
    }

    /**
     * Returns the inner product of two tensors.
     * @param ten1
     * @param ten2
     * @return 
     */
    public static float getInnerProduct(float[][] ten1, float[][] ten2) {
        float result = 0;
        int dim = ten1[0].length;
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                result += ten1[i][j] * ten2[i][j];
            }
        }
        return result;
    }

    /**
     * Returns the sum of two tensors.
     * @param ten1
     * @param ten2
     * @return 
     */
    public static float[][] getSumTensor(float[][] ten1, float[][] ten2) {
        int dim = ten1[0].length;
        float[][] result = new float[dim][dim];
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                result[i][j] = ten1[i][j] + ten2[i][j];
            }
        }
        return result;
    }

    /**
     * Returns the normalized version of a 2 tensor, i.e. an array of arrays of
     * floats.
     * @param tensor
     * @return 
     */
    public static float[][] getNormalizedTensor(float[][] tensor) {
        int dim = tensor[0].length;
        float[][] normedTensor = new float[dim][dim];
        float norm = (float) Math.sqrt(getInnerProduct(tensor, tensor));
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                normedTensor[i][j] = tensor[i][j] / norm;
            }
        }
        return normedTensor;
    }

    /**
     * Returns a 2-tensor which is the outer product of 2 vectors.
     * @param vec1
     * @param vec2
     * @return 
     */
    public static float[][] getOuterProduct(float[] vec1, float[] vec2) {
        int dim = vec1.length;
        float[][] outProd = new float[dim][dim];
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                outProd[i][j] = vec1[i] * vec2[j];
            }
        }
        return outProd;
    }

    /**
     *
     * @param indexVector
     * @param rotation
     * @return
     */
    public static float[] permuteVector(float[] indexVector, int rotation) {
        // Correct for unlikely possibility that rotation specified > indexVector.length
        if (Math.abs(rotation) > indexVector.length) {
            rotation = rotation % indexVector.length;
        }
        float[] permutedVector = new float[indexVector.length];
        int max = indexVector.length;

        for (int x = 0; x < max; x++) {
            int newIndex = x + rotation;
            if (newIndex >= max) {
                newIndex = newIndex - max;
            }
            if (newIndex < 0) {
                newIndex = max + newIndex;
            }
            permutedVector[newIndex] = indexVector[x];
        }

        return permutedVector;
    }

    /**
     *
     * @param indexVector
     * @param rotation
     * @param dimension
     * @return
     */
    public static short[] permuteVector(short[] indexVector, int rotation, int dimension) {
        short[] permutedVector = new short[indexVector.length];
        for (int x = 0; x < permutedVector.length; x++) {
            int newIndex = Math.abs(indexVector[x]);
            int sign = Integer.signum(indexVector[x]);
            // rotate vector
            newIndex += rotation;
            if (newIndex > dimension) {
                newIndex = newIndex - dimension;
            }
            if (newIndex < 1) {
                newIndex = dimension + newIndex;
            }
            newIndex = newIndex * sign;
            permutedVector[x] = (short) newIndex;
        }
        return permutedVector;
    }

    /**
     *
     * @param a
     * @param u
     * @return
     */
    public static float[] getVectorProjection(float[] a, float[] u) {
        float[] v = new float[u.length];
        float s = scalarProduct(a, u) / getNorm(u);
        for (int i = 0; i < u.length; i++) {
            v[i] = s * u[i];
        }
        return v;
    }
}
