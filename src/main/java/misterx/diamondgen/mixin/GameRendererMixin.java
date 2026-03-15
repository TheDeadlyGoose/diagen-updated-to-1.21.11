package misterx.diamondgen.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import misterx.diamondgen.render.RenderQueue;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void renderLevelStart(float delta, long time, PoseStack poseStack, CallbackInfo ci) {
        RenderQueue.get().setTrackRender(poseStack);
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void renderLevelFinish(float delta, long time, PoseStack poseStack, CallbackInfo ci) {
        RenderQueue.get().setTrackRender(null);
    }
}