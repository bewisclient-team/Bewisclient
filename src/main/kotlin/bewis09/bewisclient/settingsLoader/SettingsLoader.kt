package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.exception.SettingNotFoundException
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.settingsLoader.SettingsLoader.get
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import java.io.File
import java.io.PrintWriter
import java.util.*
import kotlin.collections.HashMap

object SettingsLoader: Settings() {
    /**
     * Gson for the Settings Loader
     */
    val gson = Gson()

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
        WidgetSettings = loadSetting("widgets")
        GeneralSettings = loadSetting("general")
        DesignSettings = loadSetting("design")

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
        saveSettings("widgets", WidgetSettings)
        saveSettings("general", GeneralSettings)
        saveSettings("design", DesignSettings)
    }

    /**
     * Used for getting and setting Settings
     *
     * @param K The Type of the values being stored
     * @param id The id of the setting. Should not contain the path and should not contain periods
     */
    open class TypedSettingID<K>(val id: String) {
        override fun equals(other: Any?): Boolean {
            if (other is TypedSettingID<*>)
                return id == other.id
            return false
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun toString(): String {
            return id
        }
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
     */
    fun set(settings: String, value: String, id: TypedSettingID<String>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))

    /**
     * Changes a setting with the type [Number]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @sample [bewis09.bewisclient.doc.Samples.setNumber]
     */
    fun set(settings: String, value: Number, id: TypedSettingID<out Number>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))

    /**
     * Changes a setting with the type [Boolean]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @sample [bewis09.bewisclient.doc.Samples.setBoolean]
     */
    fun set(settings: String, value: Boolean, id: TypedSettingID<Boolean>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))

    /**
     * Changes a setting with the type [ColorSaver]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @sample [bewis09.bewisclient.doc.Samples.setColorSaver]
     */
    fun set(settings: String, value: ColorSaver, id: TypedSettingID<ColorSaver>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value.toString()))

    /**
     * Changes a setting with the type [String]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param path A collection of the path steps
     * @param id The ID of the Setting. Should not contain the path
     *
     * @sample [bewis09.bewisclient.doc.Samples.setStringArray]
     */
    fun set(settings: String, value: String, path: Array<String>, id: TypedSettingID<String>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))

    /**
     * Changes a setting with the type [Number]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param path A collection of the path steps
     * @param id The ID of the Setting. Should not contain the path
     *
     * @sample [bewis09.bewisclient.doc.Samples.setNumberArray]
     */
    fun set(settings: String, value: Number, path: Array<String>, id: TypedSettingID<out Number>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))

    /**
     * Changes a setting with the type [Boolean]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param path A collection of the path steps
     * @param id The ID of the Setting. Should not contain the path
     *
     * @sample [bewis09.bewisclient.doc.Samples.setBooleanArray]
     */
    fun set(settings: String, value: Boolean, path: Array<String>, id: TypedSettingID<Boolean>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))

    /**
     * Changes a setting with the type [ColorSaver]
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value
     * @param path A collection of the path steps
     * @param id The ID of the Setting. Should not contain the path
     *
     * @sample [bewis09.bewisclient.doc.Samples.setColorSaverArray]
     */
    fun set(settings: String, value: ColorSaver, path: Array<String>, id: TypedSettingID<ColorSaver>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value.toString()))

    /**
     * Changes a setting
     *
     * @param settings The ID of the category the setting is in
     * @param value The new value as a [JsonElement]
     * @param id The ID of the Setting containing the path
     *
     * @sample [set]
     */
    private fun set(settings: String, id: List<String>, value: JsonElement) {
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
     * Gets the value of a setting with the type [Float]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [Float]
     *
     * @throws NumberFormatException if the real value is not of type [Float] or of a type that can be converted into one
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getFloat]
     */
    fun get(settings: String, id: TypedSettingID<Float>, vararg path: String): Float = getUntyped(settings, id, path).asFloat

    /**
     * Gets the value of a setting with the type [Int]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [Int]
     *
     * @throws NumberFormatException if the real value is not of type [Int] or of a type that can be converted into one
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getInt]
     */
    fun get(settings: String, id: TypedSettingID<Int>, vararg path: String): Int = getUntyped(settings, id, path).asInt

    /**
     * Gets the value of a setting with the type [String]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [String]
     *
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getString]
     */
    fun get(settings: String, id: TypedSettingID<String>, vararg path: String): String = getUntyped(settings, id, path).asString

    /**
     * Gets the value of a setting with the type [JsonArray]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [JsonArray]
     *
     * @throws IllegalStateException if the real value is not of type [JsonArray]
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getJsonArray]
     */
    fun get(settings: String, id: TypedSettingID<JsonArray>, vararg path: String): JsonArray = getUntyped(settings, id, path).asJsonArray

    /**
     * Gets the value of a setting with the type [Boolean]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [Boolean]
     *
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getBoolean]
     */
    fun get(settings: String, id: TypedSettingID<Boolean>, vararg path: String): Boolean = getUntyped(settings, id, path).asBoolean

    /**
     * Gets the value of a setting with the type [ColorSaver]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [ColorSaver]
     *
     * @throws NumberFormatException if the real value is not of type [Int] or of a type that can be converted into one
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getColorSaver]
     */
    fun get(settings: String, id: TypedSettingID<ColorSaver>, vararg path: String): ColorSaver = ColorSaver.of(getUntyped(settings, id, path).asString)

    /**
     * Gets the value of a setting with the type [JsonObject]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [JsonObject]
     *
     * @throws IllegalStateException if the real value is not of type [JsonObject]
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getJsonObject]
     */
    fun get(settings: String, id: TypedSettingID<JsonObject>, vararg path: String): JsonObject = getUntyped(settings, id, path).asJsonObject

    /**
     * Gets the value of a setting with the type [Float]
     *
     * @param settings The ID of the category the setting is in
     * @param path A collection of the path steps
     * @param id The ID of the Setting. Should not contain the path
     *
     * @return The value of the setting as a [Float]
     *
     * @throws NumberFormatException if the real value is not of type [Float] or of a type that can be converted into one
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getFloatArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<Float>): Float = getUntyped(settings, id, path).asFloat

    /**
     * Gets the value of a setting with the type [Int]
     *
     * @param settings The ID of the category the setting is in
     * @param path A collection of the path steps
     * @param id The ID of the Setting. Should not contain the path
     *
     * @return The value of the setting as a [Int]
     *
     * @throws NumberFormatException if the real value is not of type [Int] or of a type that can be converted into one
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getIntArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<Int>): Int = getUntyped(settings, id, path).asInt

    /**
     * Gets the value of a setting with the type [String]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [String]
     *
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getStringArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<String>): String = getUntyped(settings, id, path).asString

    /**
     * Gets the value of a setting with the type [JsonArray]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [JsonArray]
     *
     * @throws IllegalStateException if the real value is not of type [JsonArray]
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getJsonArrayArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<JsonArray>): JsonArray = getUntyped(settings, id, path).asJsonArray

    /**
     * Gets the value of a setting with the type [Boolean]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [Boolean]
     *
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getBooleanArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<Boolean>): Boolean = getUntyped(settings, id, path).asBoolean

    /**
     * Gets the value of a setting with the type [ColorSaver]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [ColorSaver]
     *
     * @throws NumberFormatException if the real value is not of type [Int] or of a type that can be converted into one
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getColorSaverArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<ColorSaver>): ColorSaver = ColorSaver.of(getUntyped(settings, id, path).asString)

    /**
     * Gets the value of a setting with the type [JsonObject]
     *
     * @param settings The ID of the category the setting is in
     * @param id The ID of the Setting. Should not contain the path
     * @param path A collection of the path steps
     *
     * @return The value of the setting as a [JsonObject]
     *
     * @throws NumberFormatException if the real value is not of type [JsonObject]
     * @throws SettingNotFoundException if the setting hasn't been found
     *
     * @sample [bewis09.bewisclient.doc.Samples.getJsonObjectArray]
     */
    fun get(settings: String, path: Array<String>, id: TypedSettingID<JsonObject>): JsonObject = getUntyped(settings, id, path).asJsonObject

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
     */
    private fun getUntyped(settings: String, id: TypedSettingID<*>, path: Array<out String>): JsonElement {
        return getUntyped(settings, getSettings(settings).asJsonObject, id, path, 0)
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
     * @param iteration The iteration of the setting getting process.
     *
     * @return The value of the setting as a [JsonElement]
     *
     * @throws SettingNotFoundException if [iteration] is bigger than 1 (Which indicates that the setting hasn't been found in the given [JsonObject] and the default [JsonObject])
     *
     * @sample [getUntyped]
     */
    fun getUntyped(sID: String, settings: JsonObject, id: TypedSettingID<*>, path: Array<out String>, iteration: Int): JsonPrimitive {
        val rId = path.toMutableList()+id.id
        val rsId = (path.toMutableList()+id.id).toTypedArray().joinToString(".")
        
        if(settingMap.containsKey("$sID.$rsId")) return settingMap["$sID.$rsId"]!!

        if(iteration>1) throw SettingNotFoundException(rsId)
        
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
                    return getUntyped(sID, DefaultSettings.getDefault(sID).asJsonObject, id, path, iteration+1)
                }
            } else {
                return getUntyped(sID, DefaultSettings.getDefault(sID).asJsonObject, id, path, iteration+1)
            }
        }
        return getUntyped(sID, DefaultSettings.getDefault(sID).asJsonObject, id, path, iteration+1)
    }

    /**
     * Returns the [JsonObject] of the category where all changed settings are stored in
     *
     * @param string The ID of the category
     * @return The corresponding [JsonObject] or an empty [JsonObject] if the settings category isn't one the three default categories
     */
    fun getSettings(string: String): JsonObject {
        when (string) {
            "widgets" -> return WidgetSettings
            "general" -> return GeneralSettings
            "design" -> return DesignSettings
        }
        return DefaultSettings.gson.fromJson("{}",JsonObject::class.java)
    }
}