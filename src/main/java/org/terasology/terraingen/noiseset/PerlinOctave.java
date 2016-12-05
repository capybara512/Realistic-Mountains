/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.terraingen.noiseset;

import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.random.FastRandom;

public class PerlinOctave implements Noise{
    private int a, b, c, d;

    public PerlinOctave(){
        a = findPrime(10000, 99999);
        b = findPrime(100000, 999999);
        c = findPrime(1000000000, 2000000000);
        d = findPrime(1000000000, 2000000000);
    }

    @Override
    public float noise(float x, float y) {
        return 0;
    }

    @Override
    public float noise(float x, float y, float z) {
        return 0;
    }

    private double noise(int x){
        x = (x<<13)^x;
        return ( 1.0 - ( (x * (x * x * a + b) + c) & 0x7fffffff) / (double)d);
    }

    /*
     * Prime number searching code
     */

    private static FastRandom random = new FastRandom(System.currentTimeMillis());

    /**
     * Finds a random prime between the given numbers.
     * @param min The minimum value for prime number selection (inclusive).
     * @param max The maximum value for prime number selection (inclusive).
     * @return
     */
    private int findPrime(int min, int max){
        int n = random.nextInt(max - min) + min;

        while(!isPrime(n)){
            if(++n > max){
                n = min;
            }
        }

        return n;
    }

    /**
     * Returns whether or not the given int is prime.
     * @param n The int to check for primality
     * @return whether or not the given int is prime.
     */
    private boolean isPrime(int n){
        int sqrtn = ((int) Math.sqrt(n)) + 1;
        for(int i=2;i<sqrtn;++i){
            if(n % i == 0) return false;
        }
        return true;
    }

    /*
     * Noise Overloads
     */

    @Override
    public float noise(int x, int y){
        return noise((float)x, (float)y);
    }

    @Override
    public float noise(int x, int y, int z) {
        return noise((float)x, (float)y, (float)z);
    }

}
