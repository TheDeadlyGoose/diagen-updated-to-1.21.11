package misterx.diamondgen.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import misterx.diamondgen.DiamondGen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RenderMain {
    public static RenderMain get() {
        return INSTANCE;
    }

    private static final RenderMain INSTANCE = new RenderMain();

    public void renderFinders(PoseStack poseStack) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.disableDepthTest();
        
        DiamondGen.gen.simOreGen.render();
        
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}