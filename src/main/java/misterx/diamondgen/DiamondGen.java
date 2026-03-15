package misterx.diamondgen;

import misterx.diamondgen.render.RenderMain;
import misterx.diamondgen.render.RenderQueue;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;

public class DiamondGen implements ClientModInitializer {

    public static int range = 100;
    public static boolean active = true;
    public static String ver = "1.21";

    private static boolean opaque = false;

    public static StartGen gen = new StartGen(0);

    public static boolean isOpaque() {
        return opaque;
    }

    public static void setOpaque(boolean opaque) {
        DiamondGen.opaque = opaque;
    }

    @Override
    public void onInitializeClient() {
        clear(0);
        RenderQueue.get().add("hand", RenderMain.get()::renderFinders);
    }

    public static void clear(long seed) {
        gen = new StartGen(seed);

        if (MinecraftClient.getInstance().player == null) {
            return;
        }

        Util.reload();
    }
}
