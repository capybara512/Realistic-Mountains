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
package org.terasology.terraingen;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.terraingen.noiseset.NoiseSet;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(SurfaceHeightFacet.class)
public class SurfaceProvider implements FacetProvider{
    private NoiseSet heightMap;

    @Override
    public void setSeed(long seed){
        FastRandom random = new FastRandom(seed); //using a random for each octave's seed to ensure that they are completely different -- provides slightly better results at a negligible performance impact of a new FastRandom object

        heightMap = createFractalGradientMap(random);
    }

    private NoiseSet createFractalGradientMap(FastRandom random){
        return new NoiseSet(
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0008f, 128f),
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0016f, 64f),
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0032f, 32f),
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0128f, 16f),
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0256f, 8f),
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0512f, 4f),
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.1024f, 2f)
        );
    }

    @Override
    public void process(GeneratingRegion region){
        Border3D border = region.getBorderForFacet(SurfaceHeightFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = new SurfaceHeightFacet(region.getRegion(), border);

        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();
        for(BaseVector2i position : processRegion.contents()){
            float value = heightMap.noise(position.x(), position.y());

            surfaceHeightFacet.setWorld(position,
                    Math.round( //rounding to allow == height grass to work in SurfaceRasterizer
                            value *
                            scaleClimateMap(value * 256f) *
                            256f
                    )
            );
        }

        region.setRegionFacet(SurfaceHeightFacet.class, surfaceHeightFacet);
    }

    private float scaleClimateMap(float climateInput){
        if(climateInput > 0f && climateInput < 16f){
            return 0.01171875f; //make sure exactly 3 blocks are present where there normally would be 16
        }else if(climateInput > 16f){
            return max((climateInput - 16f) / 32f, 1f);
        }else{
            return 1f;
        }
    }

    private float max(float n, float m){
        return n < m ? n : m;
    }

}
