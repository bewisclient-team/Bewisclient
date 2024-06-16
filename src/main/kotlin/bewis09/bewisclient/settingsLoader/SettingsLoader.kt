package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.exception.SettingNotFoundException
import bewis09.bewisclient.screen.MainOptionsScreen
import bewis09.bewisclient.screen.elements.ElementList
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import java.io.File
import java.io.PrintWriter
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

object SettingsLoader {
    val gson = Gson()

    var WidgetSettings: JsonObject = JsonObject()
    var GeneralSettings: JsonObject = JsonObject()
    var DesignSettings: JsonObject = JsonObject()

    private var autoSave: Boolean = true

    fun loadSettings() {
        WidgetSettings = loadSetting("widgets")
        GeneralSettings = loadSetting("general")
        DesignSettings = loadSetting("design")

        if(MinecraftClient.getInstance().currentScreen is MainOptionsScreen)
            (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).startAllAnimation(MainOptionsScreen(arrayListOf(
                ElementList.main())))
    }

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

    fun disableAutoSave() { autoSave = false }

    fun saveSettings(id:String,settings: JsonObject) {
        val file = File((FabricLoader.getInstance().gameDir).toString()+"/bewisclient/"+id+".json")
        val printWriter = PrintWriter(file)
        printWriter.print(gson.toJson(settings))
        printWriter.close()
    }

    fun saveAllSettings() {
        saveSettings("widgets", WidgetSettings)
        saveSettings("general", GeneralSettings)
        saveSettings("design", DesignSettings)
    }

    class TypedSettingID<K>(val id: String) {
        override fun equals(other: Any?): Boolean {
            if (other is TypedSettingID<*>)
                return id == other.id
            return false
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }

    fun set(settings: String, id: String, value: String) = set(settings,id,JsonPrimitive(value))
    fun set(settings: String, id: String, value: Number) = set(settings,id,JsonPrimitive(value))
    fun set(settings: String, id: String, value: Boolean) = set(settings,id,JsonPrimitive(value))
    fun set(settings: String, id: String, value: ColorSaver) = set(settings,id,JsonPrimitive(value.toString()))

    fun set(settings: String, id: String, value: JsonElement) {
        settingMap = HashMap()

        var set = getSettings(settings).asJsonObject

        for ((index, i) in id.split(".").withIndex()) {
            val j = set.get(i)
            if(index==(id.split(".").size-1)) {
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

    var settingMap = HashMap<String,JsonPrimitive>()

    fun getFloat(settings: String, id: String): Float = get(settings, id).asFloat
    fun getInt(settings: String, id: String): Int = get(settings, id).asInt
    fun getString(settings: String, id: String): String = get(settings, id).asString
    fun getArray(settings: String, id: String): JsonArray = get(settings, id).asJsonArray
    fun getBoolean(settings: String, id: String): Boolean = get(settings, id).asBoolean
    fun getColorSaver(settings: String, id: String): ColorSaver = ColorSaver.of(get(settings, id).asString)
    fun getJsonObject(settings: String, id: String): JsonObject = get(settings, id).asJsonObject

    fun get(settings: String, id: String): JsonPrimitive {
        return get(settings,getSettings(settings).asJsonObject,settings,id,0)
    }

    fun get(sID: String, settings: JsonObject ,settingsID: String, id: String, iteration: Int): JsonPrimitive {
        if(settingMap.containsKey("$sID.$id")) return settingMap["$sID.$id"]!!

        if(iteration>2) throw SettingNotFoundException(id)

        var set = settings

        for ((index, i) in id.split(".").withIndex()) {
            val j = set.get(i)
            if(j!=null) {
                if(index==(id.split(".").size-1)) {
                    settingMap["$sID.$id"] = j as JsonPrimitive
                    return j
                } else if(j.isJsonObject) {
                    set = j.asJsonObject
                } else {
                    return get(sID,DefaultSettings.getDefault(settingsID).asJsonObject,settingsID,id,iteration+1)
                }
            } else {
                return get(sID,DefaultSettings.getDefault(settingsID).asJsonObject,settingsID,id,iteration+1)
            }
        }
        return get(sID,DefaultSettings.getDefault(settingsID).asJsonObject,settingsID,id,iteration+1)
    }

    fun getSettings(string: String): JsonElement {
        when (string) {
            "widgets" -> return WidgetSettings
            "general" -> return GeneralSettings
            "design" -> return DesignSettings
        }
        return DefaultSettings.gson.fromJson("{}",JsonElement::class.java)
    }
}