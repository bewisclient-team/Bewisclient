package bewis09.bewisclient.mixin;

import bewis09.bewisclient.Bewisclient;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotRecorderMixin {
    @Shadow
    @Final
    public static String SCREENSHOTS_DIRECTORY;

    @Shadow
    private static File getScreenshotFilename(File directory) {
        return null;
    }

    @Shadow
    public static NativeImage takeScreenshot(Framebuffer framebuffer) {
        return null;
    }

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "saveScreenshotInner", at = @At("HEAD"), cancellable = true)
    private static void inject(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
        if(SettingsLoader.INSTANCE.get("general", Settings.Companion.getSettings().getSCREENSHOT_OPEN_FOLDER())) {
            NativeImage nativeImage = takeScreenshot(framebuffer);
            File file = new File(gameDirectory, SCREENSHOTS_DIRECTORY);
            file.mkdir();
            File file2 = fileName == null ? getScreenshotFilename(file) : new File(file, fileName);
            Util.getIoWorkerExecutor().execute(() -> {
                try {
                    assert nativeImage != null;
                    assert file2 != null;
                    nativeImage.writeTo(file2);
                    MutableText text = Text.literal(file2.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath())));
                    MutableText folder = Bewisclient.INSTANCE.getTranslationText("screenshot.folder").styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getParentFile().getAbsolutePath()))).formatted(Formatting.YELLOW).formatted(Formatting.UNDERLINE);
                    messageReceiver.accept(Text.translatable("screenshot.success", text).append(" ").append(folder));
                } catch (Exception exception) {
                    LOGGER.warn("Couldn't save screenshot", exception);
                    messageReceiver.accept(Text.translatable("screenshot.failure", exception.getMessage()));
                } finally {
                    assert nativeImage != null;
                    nativeImage.close();
                }
            });
            ci.cancel();
        }
    }
}
