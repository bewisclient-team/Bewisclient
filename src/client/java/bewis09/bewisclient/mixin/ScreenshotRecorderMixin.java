package bewis09.bewisclient.mixin;

import net.minecraft.client.util.ScreenshotRecorder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotRecorderMixin {
//    @Shadow
//    @Final
//    public static String SCREENSHOTS_DIRECTORY;
//
//    @Shadow
//    private static File getScreenshotFilename(File directory) {
//        return null;
//    }
//
//    @Shadow
//    public static NativeImage takeScreenshot(Framebuffer framebuffer) {
//        return null;
//    }
//
//    @Shadow
//    @Final
//    private static Logger LOGGER;
//
//    @Inject(method = "saveScreenshotInner", at = @At("HEAD"), cancellable = true)
//    private static void inject(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
//        if(Settings.Companion.getUtilities().getScreenshot_folder_open().get()) {
//            NativeImage nativeImage = takeScreenshot(framebuffer);
//            File file = new File(gameDirectory, SCREENSHOTS_DIRECTORY);
//            file.mkdir();
//            File file2 = fileName == null ? getScreenshotFilename(file) : new File(file, fileName);
//            ScreenshotElement.Companion.setId(ScreenshotElement.Companion.getId()+1);
//            assert file2 != null;
//            assert nativeImage != null;
//            if(ScreenshotElement.Companion.getAddNew()) {
//                MinecraftClient.getInstance().getTextureManager().registerTexture(
//                        Identifier.of("bewisclient","screenshot_" + (ScreenshotElement.Companion.getId())),
//                        new NativeImageBackedTexture(nativeImage)
//                );
//
//                ScreenshotElement.Companion.getScreenshots().add(new ScreenshotElement.SizedIdentifier(Identifier.of("bewisclient","screenshot_" + (ScreenshotElement.Companion.getId())),nativeImage.getWidth(),nativeImage.getHeight(),file2.getName()));
//            }
//            Util.getIoWorkerExecutor().execute(() -> {
//                try {
//                    nativeImage.writeTo(file2);
//                    MutableText text = Text.literal(file2.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bewisclient screenshot \""+file2.getName()+"\"")));
//                    messageReceiver.accept(Text.translatable("screenshot.success", text));
//                } catch (Exception exception) {
//                    LOGGER.warn("Couldn't save screenshot", exception);
//                    messageReceiver.accept(Text.translatable("screenshot.failure", exception.getMessage()));
//                } finally {
//                    nativeImage.close();
//                }
//            });
//        } else {
//            NativeImage nativeImage = takeScreenshot(framebuffer);
//            File file = new File(gameDirectory, SCREENSHOTS_DIRECTORY);
//            file.mkdir();
//            File file2 = fileName == null ? getScreenshotFilename(file) : new File(file, fileName);
//            assert file2 != null;
//            assert nativeImage != null;
//            ScreenshotElement.Companion.setId(ScreenshotElement.Companion.getId()+1);
//            MinecraftClient.getInstance().getTextureManager().registerTexture(
//                    Identifier.of("bewisclient","screenshot_" + (ScreenshotElement.Companion.getId())),
//                    new NativeImageBackedTexture(nativeImage)
//            );
//            ScreenshotElement.Companion.getScreenshots().add(new ScreenshotElement.SizedIdentifier(Identifier.of("bewisclient","screenshot_" + (ScreenshotElement.Companion.getId())),nativeImage.getWidth(),nativeImage.getHeight(),file2.getName()));
//            Util.getIoWorkerExecutor().execute(() -> {
//                try {
//                    nativeImage.writeTo(file2);
//                    MutableText text = Text.literal(file2.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath())));
//                    messageReceiver.accept(Text.translatable("screenshot.success", text));
//                } catch (Exception exception) {
//                    LOGGER.warn("Couldn't save screenshot", exception);
//                    messageReceiver.accept(Text.translatable("screenshot.failure", exception.getMessage()));
//                } finally {
//                    nativeImage.close();
//                }
//            });
//        }
//
//        ci.cancel();
//    }
}
