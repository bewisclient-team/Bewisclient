package bewis09.bewisclient.mixin;

import bewis09.bewisclient.Bewisclient;
import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.SettingsLoader;
import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(SimpleOption.class)
public abstract class SimpleOptionMixin<T> {

    @Shadow @Final private Consumer<T> changeCallback;

    @Shadow
    T value;

    @Shadow @Final private Codec<T> codec;

    /**
     *@author Mojang
     *@reason Why not?
     **/

    @Overwrite
    public void setValue(T value) {
        if (!MinecraftClient.getInstance().isRunning()) {
            this.value = value;
            return;
        }
        if (!Objects.equals(this.value, value)) {
            this.value = value;
            this.changeCallback.accept(this.value);
        }
    }

    @Inject(method="createWidget*",at=@At("HEAD"),cancellable = true)
    public void createButton(GameOptions options, int x, int y, int width, CallbackInfoReturnable<ClickableWidget> cir) {
        SettingsLoader.Settings settings = JavaSettingsSender.Companion.getDesignSettings().getValue("fullbright");
        if(((boolean)settings.getValue("enabled")) &&(MinecraftClient.getInstance().options.getGamma().getValue()!=(double)(float)settings.getValue("value"))) MinecraftClient.getInstance().options.getGamma().setValue((double)(float)settings.getValue("value"));
        if(this.codec==MinecraftClient.getInstance().options.getGamma().getCodec()&&(boolean)settings.getValue("enabled")) {
            ButtonWidget b = ButtonWidget.builder(Bewisclient.INSTANCE.getTranslationText("fullbright"),null).dimensions(x, y, width, 20).build();
            b.active = false;
            cir.setReturnValue(b);
        }
    }
}
