package misterx.diamondgen.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

import java.util.*;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class RenderQueue {

    private static final RenderQueue INSTANCE = new RenderQueue();

    private final Map<String, List<Consumer<MatrixStack>>> typeRunnableMap = new HashMap<>();
    private MatrixStack matrixStack = null;

    public static RenderQueue get() {
        return INSTANCE;
    }

    public void add(String type, Consumer<MatrixStack> runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        this.typeRunnableMap
                .computeIfAbsent(type, k -> new ArrayList<>())
                .add(runnable);
    }

    public void remove(String type, Consumer<MatrixStack> runnable) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(runnable);

        List<Consumer<MatrixStack>> list = this.typeRunnableMap.get(type);
        if (list != null) {
            list.remove(runnable);
        }
    }

    public void setTrackRender(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public void onRender(String type) {
        if (this.matrixStack == null) return;

        List<Consumer<MatrixStack>> list = this.typeRunnableMap.get(type);
        if (list == null) return;

        list.forEach(r -> r.accept(this.matrixStack));
    }
}