package bewis09.bewisclient.donate

import bewis09.bewisclient.Bewisclient
import com.google.gson.Gson
import net.minecraft.util.Util
import java.net.URI
import java.nio.charset.StandardCharsets

object DonationAcquirer {
    lateinit var donation_data: Result
    var data_loading_status = State.UNSTARTED
    var error = ""

    fun loadDonationLink() {
        data_loading_status = State.LOADING
        Util.getIoWorkerExecutor().execute {
            try {
                val gson = Gson()

                val url = URI("https://bewisclient.deno.dev/api/donations").toURL()

                val connection = url.openConnection()

                val result = String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8)

                val test_json = gson.fromJson(result, Response::class.java)

                if (Bewisclient.API_LEVEL >= test_json.minimum_api_level) {
                    val json = gson.fromJson(result, CorrectResponse::class.java).data

                    donation_data = json

                    data_loading_status = State.FINISHED
                } else {
                    data_loading_status = State.FALSE_API_LEVEL

                    error = test_json.minimum_api_level.toString()
                }
            } catch (e: Exception) {
                data_loading_status = State.ERROR
                error = e.localizedMessage
            }
        }
    }

    enum class State {
        UNSTARTED,
        LOADING,
        FINISHED,
        ERROR,
        FALSE_API_LEVEL
    }

    data class Response (
        val minimum_api_level: Int,
        val data: Any
    )

    data class CorrectResponse (
        val minimum_api_level: Int,
        val data: Result
    )

    data class Money (
        val value: Double,
        val currency: String
    )

    data class Avatar (
        val width: String,
        val height: String,
        val src: String,
        val alt: String
    )

    data class Cause (
        val name: String,
        val description: String,
        val short_description: String,
        val avatar: Avatar,
        val email: String,
        val website: String
    )

    data class Result (
        val goal: Money,
        val published_at: String,
        val id: String,
        val name: String,
        val description: String,
        val url: String,
        val cause: Cause,
        val slug: String,
        val avatar: Avatar,
        val amount_raised: Money,
        val donate_url: String
    )
}