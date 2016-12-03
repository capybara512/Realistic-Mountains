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

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

public class TreeRasterizer implements WorldRasterizer{
    private Block trunk, leaf;

    @Override
    public void initialize(){
        trunk = CoreRegistry.get(BlockManager.class).getBlock("Core:OakTrunk");
        leaf = CoreRegistry.get(BlockManager.class).getBlock("Core:GreenLeaf");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion){
        TreeFacet treeFacet = chunkRegion.getFacet(TreeFacet.class);

        for(Vector3i location : treeFacet.getWorldRegion()){
            if(treeFacet.getWorld(location)){
                int trunkHeight = (location.x * location.y * location.z) % 5 + 8; //very basic random number generator that maintains similarity for each location to make seeds constant
                int leavesRadius = ((location.x * location.y * location.z) % 3) + 4; //very basic random number generator that maintains similarity for each location to make seeds constant
                int leavesRadiusSquared = square(leavesRadius);

                int ttx = location.x; //ttx = trunk top x
                int tty = location.y + trunkHeight;
                int ttz = location.z;

                for(int x=-leavesRadius;x<=leavesRadius;++x){
                    for(int y=-leavesRadius;y<=leavesRadius;++y){
                        for(int z=-leavesRadius;z<=leavesRadius;++z){
                            if(chunkRegion.getRegion().encompasses(ttx + x, tty + y, ttz + z) && distSquared(x + ttx, y + tty, z + ttz, ttx, tty, ttz) < leavesRadiusSquared){
                                chunk.setBlock(ChunkMath.calcBlockPos(ttx + x, tty + y, ttz + z), leaf);
                            }
                        }
                    }
                }

                for(int y=location.y;y<tty;++y){
                    if(!chunkRegion.getRegion().encompasses(location.x, y, location.z)) continue;
                    chunk.setBlock(ChunkMath.calcBlockPos(location.x, y, location.z), trunk);
                }
            }
        }
    }

    private int distSquared(int xa, int ya, int za, int xb, int yb, int zb){
        return square(xa - xb) + square(ya - yb) + square(za - zb);
    }

    private int square(int a){
        return a * a;
    }

}
