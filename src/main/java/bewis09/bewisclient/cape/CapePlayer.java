package bewis09.bewisclient.cape;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CapePlayer extends PlayerEntity {
    public CapePlayer(World world) {
        super(world, new BlockPos(0,0,0), 0, MinecraftClient.getInstance().getGameProfile());
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
