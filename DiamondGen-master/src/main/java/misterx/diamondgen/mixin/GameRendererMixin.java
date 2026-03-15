package misterx.diamondgen.mixin;

import misterx.diamondgen.render.RenderQueue;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void diamondgen$renderLevelStart(
            float tickDelta,
            long startTime,
            MatrixStack matrices,
            Camera camera,
            GameRenderer gameRenderer,
            LightmapTextureManager lightmap,
            Matrix4f projectionMatrix,
            CallbackInfo ci
    ) {
        RenderQueue.get().setTrackRender(matrices);
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void diamondgen$renderLevelEnd(
            float tickDelta,
            long startTime,
            MatrixStack matrices,
            Camera camera,
            GameRenderer gameRenderer,
            LightmapTextureManager lightmap,
            Matrix4f projectionMatrix,
            CallbackInfo ci
    ) {
        RenderQueue.get().setTrackRender(null);
    }
}