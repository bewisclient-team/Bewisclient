package bewis09.bewisclient.server

import com.google.gson.Gson
import net.minecraft.client.MinecraftClient
import java.net.URI
import java.net.http.HttpClient
import java.net.http.WebSocket
import java.net.http.WebSocket.Builder
import java.net.http.WebSocket.Listener
import java.util.concurrent.CompletionStage

// work in progress
@Suppress("unused")
class ServerConnection {
    val gson: Gson = Gson()

    init {
        val uri = "wss://bewisclient.deno.dev/socket"

        val client: HttpClient = HttpClient.newHttpClient()
        val webSocketBuilder: Builder = client.newWebSocketBuilder()

        val webSocket: WebSocket = webSocketBuilder.buildAsync(URI.create(uri), WebSocketListener()).join()

        println(webSocket)

        webSocket.sendText(gson.toJson(SendMessage("server::get",ServerData("https://hypixel.net",MinecraftClient.getInstance().gameProfile.id.toString(),null))), true)
    }

    class WebSocketListener : Listener {
        val gson: Gson = Gson()

        override fun onText(webSocket: WebSocket, data: CharSequence, last: Boolean): CompletionStage<*> {
            val out = gson.fromJson("$data", SendMessage::class.java)
            when (out.type){
                "server::players" -> {
                    val serverGetData = gson.fromJson(gson.toJson(out.data),ServerGetData::class.java)
                }
            }
            return super.onText(webSocket, data, last)
        }
    }

    data class SendMessage (val type: String, val data: Any)

    data class ServerData (val url: String, val uuid: String, val cosmeticsData: Cosmetics?)

    data class ServerGetData (val url: String, val players: ServerPlayer)

    data class Cosmetics (val icon: String, val cape: String, val hat: String, val wing: String)

    data class ServerPlayer (val uuid: String, val cosmeticsData: Cosmetics)
}