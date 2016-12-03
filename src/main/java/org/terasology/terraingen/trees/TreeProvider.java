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
package org.terasology.terraingen.trees;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(TreeFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(bottom = 16, sides = 16)))
public class TreeProvider implements FacetProvider{
    private Noise noise;

    @Override
    public void setSeed(long seed){
        noise = new SimplexNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region){
        Border3D border = region.getBorderForFacet(TreeFacet.class).extendBy(16, 16, 16);
        TreeFacet treeFacet = new TreeFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        for(BaseVector2i position : surfaceHeightFacet.getWorldRegion().contents()){
            int height = Math.round(surfaceHeightFacet.getWorld(position));
            if(treeFacet.getWorldRegion().encompasses(position.x(), height, position.y()) && noise.noise(position.x(), position.y()) > 0.99){
                treeFacet.setWorld(position.x(), height, position.y(), true);
            }
        }

        region.setRegionFacet(TreeFacet.class, treeFacet);
    }

}
