package bewis09.bewisclient.cape;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A {@link PlayerEntity} used for displaying the player in the {@link bewis09.bewisclient.screen.CosmeticsScreen}
 */
public class CapePlayer extends PlayerEntity {

    /**
     * @param world The world the player is in
     */
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
