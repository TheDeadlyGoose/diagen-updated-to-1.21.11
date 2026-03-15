package misterx.diamondgen.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.*;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class RenderQueue {

    private static final RenderQueue INSTANCE = new RenderQueue();

    private final Map<String, List<Consumer<PoseStack>>> typeRunnableMap = new HashMap<>();
    private PoseStack poseStack = null;

    public static RenderQueue get() {
        return INSTANCE;
    }

    public void add(String type, Consumer<PoseStack> runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        if(!this.typeRunnableMap.containsKey(type)) {
            this.typeRunnableMap.put(type, new ArrayList<>());
        }

        List<Consumer<PoseStack>> runnableList = this.typeRunnableMap.get(type);
        runnableList.add(runnable);
    }

    public void remove(String type, Consumer<PoseStack> runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        if(!this.typeRunnableMap.containsKey(type)) {
            return;
        }

        List<Consumer<PoseStack>> runnableList = this.typeRunnableMap.get(type);
        runnableList.remove(runnable);
    }

    public void setTrackRender(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public void onRender(String type) {
        if(this.poseStack == null || !this.typeRunnableMap.containsKey(type))return;
        this.typeRunnableMap.get(type).forEach(r -> r.accept(this.poseStack));
    }
}