package bewis09.bewisclient

import bewis09.bewisclient.screen.SnakeScreen
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

class BewisclientClient: ClientModInitializer {

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, registryAccess: CommandRegistryAccess? ->
            dispatcher.register(ClientCommandManager.literal("snake")
                    .executes { context: CommandContext<FabricClientCommandSource> ->
                        context.source.client.send {
                            context.source.client.setScreen(SnakeScreen())
                        }
                        1
                    })
        })
    }
}