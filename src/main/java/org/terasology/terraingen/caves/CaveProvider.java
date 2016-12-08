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
package org.terasology.terraingen.caves;

import org.terasology.math.geom.Vector2f;
import org.terasology.math.geom.Vector3i;
import org.terasology.terraingen.noiseset.NoiseSet;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(CaveFacet.class)
@Requires(@Facet(value= SurfaceHeightFacet.class))
public class CaveProvider implements FacetProvider{
    private SubSampledNoise caveMap;

    @Override
    public void setSeed(long seed){
        caveMap = new SubSampledNoise(new SimplexNoise(seed), new Vector2f(1.0f, 1.0f), 1);
    }

    private NoiseSet createFractalGradientMap(FastRandom random){
        return new NoiseSet(
                NoiseSet.createSimplexOctaveBackend(random.nextLong(), 0.0008f, 128f)
        );
    }

    @Override
    public void process(GeneratingRegion region){
        Border3D border = region.getBorderForFacet(CaveFacet.class);
        CaveFacet caveFacet = new CaveFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        for(Vector3i position : caveFacet.getWorldRegion()){
            caveFacet.setWorld(position,
                    caveMap.noise(position.x(), position.y(), position.z()) <= .08f
            );
        }

        region.setRegionFacet(CaveFacet.class, caveFacet);
    }

}
