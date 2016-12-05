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

import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.In;
import org.terasology.terraingen.trees.TreeProvider;
import org.terasology.terraingen.trees.TreeRasterizer;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

@SuppressWarnings("unused")
@RegisterWorldGenerator(id="realisticmountains", displayName = "Realistic Mountains")
public class TerrainGenerator extends BaseFacetedWorldGenerator{
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public TerrainGenerator(SimpleUri uri){
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld(){
        return new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new SurfaceProvider())
                .addProvider(new SeaLevelProvider(0))
                .addProvider(new TreeProvider())
                .addRasterizer(new SurfaceRasterizer())
                .addRasterizer(new TreeRasterizer())
            ;
    }

}
