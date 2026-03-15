package misterx.diamondgen.render;

import com.mojang.blaze3d.systems.RenderSystem;
import misterx.diamondgen.DiamondGen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class RenderMain {

    private static final RenderMain INSTANCE = new RenderMain();

    public static RenderMain get() {
        return INSTANCE;
    }

    public void renderFinders(MatrixStack matrices) {
        // Save render state
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        // Use a basic position-color shader (no texture)
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // Delegate actual rendering
        DiamondGen.gen.simOreGen.render(matrices);

        // Restore render state
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}