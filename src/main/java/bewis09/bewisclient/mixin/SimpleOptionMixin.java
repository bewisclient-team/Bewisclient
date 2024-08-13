package bewis09.bewisclient.mixin;

import bewis09.bewisclient.Bewisclient;
import bewis09.bewisclient.JavaSettingsSender;
import bewis09.bewisclient.settingsLoader.Settings;
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

    @Inject(method="createWidget(Lnet/minecraft/client/option/GameOptions;IIILjava/util/function/Consumer;)Lnet/minecraft/client/gui/widget/ClickableWidget;",at=@At("HEAD"),cancellable = true)
    public void createButton(GameOptions options, int x, int y, int width, Consumer<T> changeCallback, CallbackInfoReturnable<ClickableWidget> cir) {
        if(JavaSettingsSender.Companion.getSettings().get("design", Settings.Companion.getSettings().getFULLBRIGHT(),Settings.Companion.getSettings().getENABLED()) &&(MinecraftClient.getInstance().options.getGamma().getValue()!=JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getFULLBRIGHT(),Settings.Companion.getSettings().getFULLBRIGHT_VALUE()))) MinecraftClient.getInstance().options.getGamma().setValue((double)(float)JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getFULLBRIGHT(),Settings.Companion.getSettings().getFULLBRIGHT_VALUE()));
        if(this.codec==MinecraftClient.getInstance().options.getGamma().getCodec()&&JavaSettingsSender.Companion.getSettings().get("design",Settings.Companion.getSettings().getFULLBRIGHT(),Settings.Companion.getSettings().getENABLED())) {
            ButtonWidget b = ButtonWidget.builder(Bewisclient.INSTANCE.getTranslationText("fullbright"),null).dimensions(x, y, width, 20).build();
            b.active = false;
            cir.setReturnValue(b);
        }
    }
}
