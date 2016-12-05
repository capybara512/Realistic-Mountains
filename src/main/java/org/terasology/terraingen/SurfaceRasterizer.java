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

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.widgets.treeView.Tree;
import org.terasology.terraingen.trees.TreeFacet;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

public class SurfaceRasterizer implements WorldRasterizer{
    public static int WATER_HEIGHT = 0, SAND_HEIGHT = 3;
    private Block dirt, grass, water, sand;

    @Override
    public void initialize(){
        dirt = CoreRegistry.get(BlockManager.class).getBlock("Core:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("Core:Grass");
        water = CoreRegistry.get(BlockManager.class).getBlock("Core:Water");
        sand = CoreRegistry.get(BlockManager.class).getBlock("Core:Sand");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion){
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);

        for(Vector3i location : chunkRegion.getRegion()){
            int height = Math.round(surfaceHeightFacet.getWorld(location.x, location.z));

            if(location.y > height){
                if(location.y < WATER_HEIGHT){
                    chunk.setBlock(ChunkMath.calcBlockPos(location), water);
                }
            }else{
                if(height < SAND_HEIGHT){
                    chunk.setBlock(ChunkMath.calcBlockPos(location), sand);
                }else{
                    if(location.y == height){
                        chunk.setBlock(ChunkMath.calcBlockPos(location), grass);
                    }else{
                        chunk.setBlock(ChunkMath.calcBlockPos(location), dirt);
                    }
                }
            }
        }
    }

}
