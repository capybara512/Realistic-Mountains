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
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(SurfaceHeightFacet.class)
public class SurfaceProvider implements FacetProvider{

    @Override
    public void setSeed(long seed){

    }

    @Override
    public void process(GeneratingRegion region){
        Border3D border = region.getBorderForFacet(SurfaceHeightFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = new SurfaceHeightFacet(region.getRegion(), border);

        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();
        for(BaseVector2i position : processRegion.contents()){
            surfaceHeightFacet.setWorld(position, 64f);
        }

        region.setRegionFacet(SurfaceHeightFacet.class, surfaceHeightFacet);
    }

}
