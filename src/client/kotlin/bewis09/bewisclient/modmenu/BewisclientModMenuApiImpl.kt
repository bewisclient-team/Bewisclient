package bewis09.bewisclient.modmenu

import bewis09.bewisclient.Bewisclient
import bewis09.bewisclient.screen.MainOptionsScreen
import com.terraformersmc.modmenu.api.*

class BewisclientModMenuApiImpl: ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { MainOptionsScreen(it) }
    }

    override fun getUpdateChecker(): UpdateChecker {
        return UpdateChecker { BewisclientUpdateInfo() }
    }

    class BewisclientUpdateInfo: UpdateInfo {
        override fun isUpdateAvailable(): Boolean = Bewisclient.update != null

        override fun getDownloadLink(): String {
            if(Bewisclient.update==null) return ""

            return Bewisclient.update!!.get("files").asJsonArray[0].asJsonObject.get("url").asString
        }

        override fun getUpdateChannel(): UpdateChannel {
            if (Bewisclient.update == null)
                return UpdateChannel.getUserPreference()

            return when (Bewisclient.update!!.get("version_type").asString) {
                "alpha" -> UpdateChannel.ALPHA
                "beta" -> UpdateChannel.BETA
                "release" -> UpdateChannel.RELEASE
                else -> UpdateChannel.getUserPreference()
            }
        }
    }
}