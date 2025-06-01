package bewis09.bewisclient.mixin;

import bewis09.bewisclient.drawable.option_elements.screenshot.ScreenshotElement;
import bewis09.bewisclient.settingsLoader.Settings;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
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
    private static File getScreenshotFilename(File directory) {
        return null;
    }

    @Shadow
    public static void takeScreenshot(Framebuffer framebuffer, Consumer<NativeImage> callback) {
    }

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/client/gl/Framebuffer;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
    private static void inject(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
        if (Settings.Companion.getUtilities().getScreenshot_folder_open().get()) {
            takeScreenshot(framebuffer, image -> {
                File file2 = new File(gameDirectory, "screenshots");
                file2.mkdir();
                File file3;
                if (fileName == null) {
                    file3 = getScreenshotFilename(file2);
                } else {
                    file3 = new File(file2, fileName);
                }

                Util.getIoWorkerExecutor().execute(() -> {
                    try {
                        try {
                            assert file3 != null;
                            image.writeTo(file3);
                            Text text = Text.literal(file3.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent.RunCommand("/bewisclient screenshot \""+file3.getName()+"\"")));
                            messageReceiver.accept(Text.translatable("screenshot.success", text));

                            if (ScreenshotElement.Companion.getAddNew()) {
                                ScreenshotElement.Companion.addScreenshot(file3);
                            }
                        } catch (Throwable var7) {
                            if (image != null) {
                                try {
                                    image.close();
                                } catch (Throwable var6) {
                                    var7.addSuppressed(var6);
                                }
                            }

                            throw var7;
                        }

                        image.close();
                    } catch (Exception var8) {
                        LOGGER.warn("Couldn't save screenshot", var8);
                        messageReceiver.accept(Text.translatable("screenshot.failure", var8.getMessage()));
                    }
                });
            });
        } else {
            takeScreenshot(framebuffer, image -> {
                File file2 = new File(gameDirectory, "screenshots");
                file2.mkdir();
                File file3;
                if (fileName == null) {
                    file3 = getScreenshotFilename(file2);
                } else {
                    file3 = new File(file2, fileName);
                }

                Util.getIoWorkerExecutor().execute(() -> {
                    try {
                        try {
                            assert file3 != null;
                            image.writeTo(file3);
                            Text text = Text.literal(file3.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent.OpenFile(file3.getAbsoluteFile())));
                            messageReceiver.accept(Text.translatable("screenshot.success", text));

                            if (ScreenshotElement.Companion.getAddNew()) {
                                ScreenshotElement.Companion.addScreenshot(file3);
                            }
                        } catch (Throwable var7) {
                            if (image != null) {
                                try {
                                    image.close();
                                } catch (Throwable var6) {
                                    var7.addSuppressed(var6);
                                }
                            }

                            throw var7;
                        }

                        image.close();
                    } catch (Exception var8) {
                        LOGGER.warn("Couldn't save screenshot", var8);
                        messageReceiver.accept(Text.translatable("screenshot.failure", var8.getMessage()));
                    }
                });
            });
        }

        ci.cancel();
    }
}
