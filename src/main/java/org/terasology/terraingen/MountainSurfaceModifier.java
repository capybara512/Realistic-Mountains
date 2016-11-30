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
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Updates(@Facet(SurfaceHeightFacet.class))
public class MountainSurfaceModifier implements FacetProvider{
    private static final int MOUNTAIN_HEIGHT = 256;
    private Noise heightMap;

    @Override
    public void setSeed(long seed){
        heightMap = new SubSampledNoise(new SimplexNoise(seed), new Vector2f(0.01f, 0.01f), 1);

    }

    @Override
    public void process(GeneratingRegion region){
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        float noise;

        for(BaseVector2i position : surfaceHeightFacet.getWorldRegion().contents()){
            noise = heightMap.noise(position.x(), position.y());

            if(noise > 0){
                noise *= MOUNTAIN_HEIGHT;
            }

            surfaceHeightFacet.setWorld(position, Math.round(surfaceHeightFacet.getWorld(position) + noise));
        }
    }

}
