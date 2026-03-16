package misterx.diamondgen;

import misterx.diamondgen.render.Color;
import misterx.diamondgen.render.Cube;
import misterx.diamondgen.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class SimOreGen {

    public final List<Renderer> renderers = new ArrayList<>();

    /* ================= RENDER ================= */

    public void render(PoseStack matrices) {
        if (!DiamondGen.active) return;

        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) return;

        try {
            if (isOverworld(world)) {
                for (Renderer renderer : this.renderers) {
                    if (Util.distanceToPlayer(renderer.getPos()) < DiamondGen.range) {
                        if (!DiamondGen.isOpaque() || Util.isOpaque(renderer.getPos())) {
                            renderer.render(matrices);
                        }
                    }
                }
            } else if (isNether(world)) {
                for (Renderer renderer : DiamondGen.gen.simDebrisGen.renderers) {
                    if (Util.distanceToPlayer(renderer.getPos()) < DiamondGen.range) {
                        if (!DiamondGen.isOpaque() || Util.isOpaque(renderer.getPos())) {
                            renderer.render(matrices);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= GENERATION ================= */

    public boolean generate(Random random, BlockPos blockPos, ClientWorld world, int size) {
        float f = random.nextFloat() * (float) Math.PI;
        float g = size / 8.0F;
        int i = MathHelper.ceil((size / 16.0F * 2.0F + 1.0F) / 2.0F);

        double d = blockPos.getX() + Math.sin(f) * g;
        double e = blockPos.getX() - Math.sin(f) * g;
        double h = blockPos.getZ() + Math.cos(f) * g;
        double j = blockPos.getZ() - Math.cos(f) * g;

        double l = blockPos.getY() + random.nextInt(3) - 2;
        double m = blockPos.getY() + random.nextInt(3) - 2;

        int n = blockPos.getX() - MathHelper.ceil(g) - i;
        int o = blockPos.getY() - 2 - i;
        int p = blockPos.getZ() - MathHelper.ceil(g) - i;

        int q = 2 * (MathHelper.ceil(g) + i);
        int r = 2 * (2 + i);

        return generateVeinPart(
                random, d, e, h, j, l, m,
                n, o, p, q, r, world, size
        );
    }

    protected boolean generateVeinPart(
            Random random,
            double startX, double endX,
            double startZ, double endZ,
            double startY, double endY,
            int x, int y, int z,
            int size, int height,
            ClientWorld world,
            int veinSize
    ) {
        int placed = 0;
        BitSet bitSet = new BitSet(size * height * size);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        double[] points = new double[veinSize * 4];

        for (int i = 0; i < veinSize; i++) {
            float t = (float) i / veinSize;
            double px = MathHelper.lerp(t, startX, endX);
            double py = MathHelper.lerp(t, startY, endY);
            double pz = MathHelper.lerp(t, startZ, endZ);

            double radius = random.nextDouble() * veinSize / 16.0D;
            double sizeFactor = ((MathHelper.sin((float) Math.PI * t) + 1.0F) * radius + 1.0D) / 2.0D;

            int idx = i * 4;
            points[idx]     = px;
            points[idx + 1] = py;
            points[idx + 2] = pz;
            points[idx + 3] = sizeFactor;
        }

        for (int i = 0; i < veinSize; i++) {
            double radius = points[i * 4 + 3];
            if (radius < 0) continue;

            double cx = points[i * 4];
            double cy = points[i * 4 + 1];
            double cz = points[i * 4 + 2];

            int minX = MathHelper.floor(cx - radius);
            int minY = MathHelper.floor(cy - radius);
            int minZ = MathHelper.floor(cz - radius);
            int maxX = MathHelper.floor(cx + radius);
            int maxY = MathHelper.floor(cy + radius);
            int maxZ = MathHelper.floor(cz + radius);

            for (int bx = minX; bx <= maxX; bx++) {
                double dx = (bx + 0.5D - cx) / radius;
                if (dx * dx >= 1.0D) continue;

                for (int by = minY; by <= maxY; by++) {
                    double dy = (by + 0.5D - cy) / radius;
                    if (dx * dx + dy * dy >= 1.0D) continue;

                    for (int bz = minZ; bz <= maxZ; bz++) {
                        double dz = (bz + 0.5D - cz) / radius;
                        if (dx * dx + dy * dy + dz * dz >= 1.0D) continue;

                        int index = (bx - x) + (by - y) * size + (bz - z) * size * height;
                        if (bitSet.get(index)) continue;

                        bitSet.set(index);
                        mutable.set(bx, by, bz);

                        if (by > 0) {
                            this.renderers.add(
                                    new Cube(mutable, new Color(255, 0, 0))
                            );
                            placed++;
                        }
                    }
                }
            }
        }

        return placed > 0;
    }

    /* ================= DIMENSION HELPERS ================= */

    private boolean isOverworld(World world) {
        return world.getRegistryKey() == World.OVERWORLD;
    }

    private boolean isNether(World world) {
        return world.getRegistryKey() == World.NETHER;
    }
}
