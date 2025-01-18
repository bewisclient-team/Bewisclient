package bewis09.bewisclient.mixin;

import bewis09.bewisclient.drawable.UsableTexturedButtonWidget;
import bewis09.bewisclient.drawable.option_elements.screenshot.ScreenshotElement;
import bewis09.bewisclient.screen.MainOptionsScreen;
import bewis09.bewisclient.screen.WelcomingScreen;
import bewis09.bewisclient.server.ServerConnection;
import bewis09.bewisclient.settingsLoader.Settings;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static bewis09.bewisclient.settingsLoader.Settings.*;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init",at=@At("HEAD"))
    private void init(CallbackInfo ci) {
        if(SettingsLoader.INSTANCE.get(DESIGN, Settings.Companion.getFULLBRIGHT(),Settings.Companion.getENABLED()))
            MinecraftClient.getInstance().options.getGamma().setValue((double) SettingsLoader.INSTANCE.get(DESIGN,Settings.Companion.getFULLBRIGHT(),Settings.Companion.getFULLBRIGHT_VALUE()));
        if(SettingsLoader.INSTANCE.get(DESIGN, Settings.Companion.getSHOW_TITLE_MENU(),Settings.Companion.getOPTIONS_MENU()))
            addDrawableChild(new UsableTexturedButtonWidget(width/2+104,this.height / 4 + 72,20,20,new ButtonTextures(Identifier.of("bewisclient","textures/sprites/bewisclient_button.png"),Identifier.of("bewisclient","textures/sprites/bewisclient_button_highlighted.png")),(b)->{
                assert client != null;
                if(!SettingsLoader.INSTANCE.get(DESIGN, Companion.getOPTIONS_MENU(), Companion.getSHOWN_START_MENU())) {
                    client.setScreen(new WelcomingScreen());
                } else {
                    client.setScreen(new MainOptionsScreen());
                }
            }));

        Util.getIoWorkerExecutor().execute(ScreenshotElement.Companion::getScreenshots);

        Util.getIoWorkerExecutor().execute(ServerConnection.INSTANCE::registerCosmetics);
    }
}
