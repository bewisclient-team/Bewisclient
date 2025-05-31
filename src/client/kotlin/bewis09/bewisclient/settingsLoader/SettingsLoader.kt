package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.exception.SettingNotFoundException
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader.get
import com.google.gson.*
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import java.io.File
import java.io.PrintWriter
import java.util.*

object SettingsLoader: Settings() {
    /**
     * Gson for the Settings Loader
     */
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * The options in the category widgets stored as a [JsonObject]
     */
    var WidgetSettings: JsonObject = JsonObject()

    /**
     * The options in the category general stored as a [JsonObject]
     */
    var GeneralSettings: JsonObject = JsonObject()

    /**
     * The options in the category design stored as a [JsonObject]
     */
    var DesignSettings: JsonObject = JsonObject()

    val overwrittenSettings: HashMap<String,List<String>> = hashMapOf(
        Pair("blockhit.blockhit",listOf("blockhit.enabled")),
        Pair("fullbright.fullbright",listOf("fullbright.enabled")),
        Pair("hit_overlay.hit_overlay",listOf("hit_overlay.enabled")),
        Pair("fullbright.fullbright_value",listOf("fullbright.value")),
        Pair("zoom",listOf("zoom_enabled")),
    )

    /**
     * Determines if the settings should be automatically saved after a setting change
     *
     * Can be set to false by the [disableAutoSave] function. This affects only the next setting change. After that [autoSave] will be set to true again
     */
    private var autoSave: Boolean = true

    /**
     * Loads the settings in all three categories. Reloads the options screen if it is the screen that is currently shown
     */
    fun loadSettings() {
        WidgetSettings = loadSetting(WIDGETS)
        GeneralSettings = loadSetting(GENERAL)
        DesignSettings = loadSetting(DESIGN)

        if(MinecraftClient.getInstance().currentScreen is MainOptionsScreen)
            (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).startAllAnimation(MainOptionsScreen())
    }

    /**
     * Loads a specific category of settings and saves it to the file
     *
     * @param [id] the ID of the category that should be loaded
     * @return The [JsonObject] the settings got loaded into
     */
    private fun loadSetting(id: String): JsonObject {
        val file = File((FabricLoader.getInstance().gameDir).toString()+"/bewisclient/"+id+".json")

        var gsonLoaded: JsonObject

        try {
            val scanner = Scanner(file)
            var str = ""

            while (scanner.hasNextLine()) {
                str += scanner.nextLine()
            }

            gsonLoaded = gson.fromJson(str,JsonElement::class.java) as JsonObject
        } catch (e: Exception) {
            System.err.println("ERROR LOADING SETTINGS FILE $id")
            file.parentFile.mkdirs()
            file.createNewFile()
            gsonLoaded = JsonObject()
        }

        saveSettings(id,gsonLoaded)

        return gsonLoaded
    }

    /**
     * Disables autosave when changing a setting. Only affects the next setting change, after that [autoSave] will be turned back to true
     */
    fun disableAutoSave() { autoSave = false }

    /**
     * Saves the settings to a file
     *
     * @param id The id of the setting category and the name of the file
     * @param settings The settings that should be saved as a [JsonObject]
     */
    fun saveSettings(id:String,settings: JsonObject) {
        val file = File((FabricLoader.getInstance().gameDir).toString()+"/bewisclient/"+id+".json")
        val printWriter = PrintWriter(file)
        printWriter.print(gson.toJson(settings))
        printWriter.close()
    }

    /**
     * Saves all three categories of settings to the corresponding file
     */
    fun saveAllSettings() {
        saveSettings(WIDGETS, WidgetSettings)
        saveSettings(GENERAL, GeneralSettings)
        saveSettings(DESIGN, DesignSettings)
    }

    /**
     * Changes a setting with the type [String]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @sample [bewis09.bewisclient.doc.Samples.setString]
     *
     * @see [Settings]
     */
    fun set(settings: String, value: JsonElement, path: Array<String>, id: String) = set(settings,(path).toMutableList()+id,value)

    /**
     * Changes a setting
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value as a [JsonElement]
     * @param id The ID of the Setting containing the path
     *
     * @sample [set]
     *
     * @see [Settings]
     */
    fun set(settings: String, id: List<String>, value: JsonElement) {
        settingMap = HashMap()

        var set = getSettings(settings).asJsonObject

        for ((index, i) in id.withIndex()) {
            val j = set.get(i)
            if(index==(id.size-1)) {
                set.add(i,value)
            } else if(j!=null && j.isJsonObject) {
                set = j.asJsonObject
            } else {
                val t = JsonObject()

                set.add(i,t)
                set = t
            }
        }

        if(autoSave) saveAllSettings()

        autoSave=true
    }

    /**
     * A [HashMap] used to cache the results of the [get] functions
     */
    var settingMap = HashMap<String,JsonPrimitive>()

    /**
     * Gets the value of a setting
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [JsonElement]
     *
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [get]
     *
     * @see [Settings]
     */
    fun get(settings: String, id: String, path: Array<out String>, default: JsonPrimitive): JsonPrimitive {
        return getUntyped(settings, getSettings(settings).asJsonObject, id, path, default)
    }

    /**
     * Gets the value of a setting
     *
     * This function is a recursive function because it will be invoked a second time if the setting hasn't been found, but then it is look through the default settings
     *
     * @param sID The ID of the category the setting is in
     * @param settings The [JsonObject] that the setting should be in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [JsonElement]
     *
     * @sample [getUntyped]
     *
     * @see [Settings]
     */
    fun getUntyped(sID: String, settings: JsonObject, id: String, path: Array<out String>, default: JsonPrimitive): JsonPrimitive {
        val rId = path.toMutableList()+id
        val rsId = (path.toMutableList()+id).toTypedArray().joinToString(".")
        
        if(settingMap.containsKey("$sID.$rsId")) return settingMap["$sID.$rsId"]!!
        
        var set = settings

        for ((index, i) in rId.withIndex()) {
            val j = set.get(i)
            if(j!=null) {
                if(index==(rId.size-1)) {
                    settingMap["$sID.$id"] = j as JsonPrimitive
                    return j
                } else if(j.isJsonObject) {
                    set = j.asJsonObject
                } else {
                    return defaultOrOld(rsId,sID,default)
                }
            } else {
                return defaultOrOld(rsId,sID,default)
            }
        }
        return defaultOrOld(rsId,sID,default)
    }

    fun defaultOrOld(rsID: String, settings: String, default: JsonPrimitive): JsonPrimitive {
        if(overwrittenSettings.containsKey(rsID)) {
            val list = overwrittenSettings[rsID]!!
            for (k in list) {
                val i = k.split(".")
                val j = getUntyped(settings, getSettings(settings),i.last(),i.toTypedArray().sliceArray(0 until i.size - 1),default)
                if(j!=default) return j
            }
        }

        return default
    }

    /**
     * Returns the [JsonObject] of the category where all changed settings are stored in
     *
     * @param string The ID of the category
     * @return The corresponding [JsonObject] or an empty [JsonObject] if the settings category isn't one the three default categories
     */
    fun getSettings(string: String): JsonObject {
        when (string) {
            WIDGETS -> return WidgetSettings
            GENERAL -> return GeneralSettings
            DESIGN -> return DesignSettings
        }
        return gson.fromJson("{}",JsonObject::class.java)
    }
}