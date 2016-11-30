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

import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;

/**
 * This class takes several NoiseBackend objects, designed to form octaves of fractal gradient noise, and produces
 * a single unified noise function equal to the output from each noise function multiplied by its multipler added together.
 */
public class NoiseSet implements Noise{
    private NoiseBackend[] backends;

    /**
     * Creates a <code>NoiseBackend</code> using <code>org.terasology.utilities.procedulal.SimplexNoise</code>
     * and <code>org.terasology.utilities.procedural.SubSampledNoise</code> to create a Simplex/Perlin noise octave.
     * @param seed The seed for the <code>SimplexNoise</code>
     * @param roughness Smaller values provide larger/smoother curves, larger values provide rougher/smaller curves. This argument does not affect amplitude.
     * @param multiplier What value should the noise in this octave be multiplied by?
     * @return
     */
    public static NoiseBackend createSimplexOctaveBackend(long seed, float roughness, float multiplier){
        return new NoiseBackend(
                new SubSampledNoise(new SimplexNoise(seed), new Vector2f(roughness, roughness), 1),
                multiplier
        );
    }

    /**
     * Initializes a NoiseSet with the provided backends
     * @param backends The backends with which to initialize this NoiseSet
     */
    public NoiseSet(NoiseBackend... backends){
        this.backends = backends;
    }

    @Override
    public float noise(int x, int y){
        return buildNoise((Noise noise) -> noise.noise(x, y));
    }

    @Override
    public float noise(float x, float y){
        return buildNoise((Noise noise) -> noise.noise(x, y));
    }

    @Override
    public float noise(int x, int y, int z){
        return buildNoise((Noise noise) -> noise.noise(x, y, z));
    }

    @Override
    public float noise(float x, float y, float z){
        return buildNoise((Noise noise) -> noise.noise(x, y, z));
    }

    /**
     * Calls <code>NoiseCaller.process()</code> with each of the noise backend objects and performs other necessary calculations.
     * A system like this is used to prevent code duplication since each of the noise overloads needs its own
     * parameter passing (partially to ensure the correct int vs. float method is called on the backend noises)
     * @param noiseCaller The <code>NoiseCaller</code> that will correctly pass paramaters to the backend noise function
     * @return Backend noise results from <code>NoiseCaller</code> multiplied by backend multiplier and added
     */
    private float buildNoise(NoiseCaller noiseCaller){
        float total = 0.0f;

        for(NoiseBackend backend : backends){
            total += noiseCaller.process(backend.getNoise()) * backend.getMultiplier();
        }

        return total;
    }

    private interface NoiseCaller{
        float process(Noise noise);
    }

    /**
     * Provides a backend noise method and multiplier for that noise method
     */
    public static class NoiseBackend{
        private final Noise noise;
        private final float multiplier;

        public NoiseBackend(Noise noise, float multiplier){
            this.noise = noise;
            this.multiplier = multiplier;
        }

        public Noise getNoise() {
            return noise;
        }

        public float getMultiplier() {
            return multiplier;
        }
    }

}
