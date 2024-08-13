package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.exception.SettingNotFoundException
import bewis09.bewisclient.screen.MainOptionsScreen
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
            (MinecraftClient.getInstance().currentScreen as MainOptionsScreen).startAllAnimation(MainOptionsScreen())
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
    
    fun set(settings: String, value: String, id: TypedSettingID<String>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))
    fun set(settings: String, value: Number, id: TypedSettingID<out Number>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))
    fun set(settings: String, value: Boolean, id: TypedSettingID<Boolean>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))
    fun set(settings: String, value: ColorSaver, id: TypedSettingID<ColorSaver>, vararg path: String) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value.toString()))

    fun set(settings: String, value: String, path: Array<String>, id: TypedSettingID<String>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))
    fun set(settings: String, value: Number, path: Array<String>, id: TypedSettingID<out Number>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))
    fun set(settings: String, value: Boolean, path: Array<String>, id: TypedSettingID<Boolean>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value))
    fun set(settings: String, value: ColorSaver, path: Array<String>, id: TypedSettingID<ColorSaver>) = set(settings,(path).toMutableList()+id.id,JsonPrimitive(value.toString()))

    fun set(settings: String, id: String, value: JsonElement) {
        set(settings,id.split("."),value)
    }

    fun set(settings: String, value: JsonElement, id: TypedSettingID<*>, vararg path: String) {
        set(settings,path.toMutableList()+id.id,value)
    }

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

    var settingMap = HashMap<String,JsonPrimitive>()

    fun get(settings: String, id: TypedSettingID<Float>, vararg path: String): Float = getUntyped(settings, id, path).asFloat
    fun get(settings: String, id: TypedSettingID<Int>, vararg path: String): Int = getUntyped(settings, id, path).asInt
    fun get(settings: String, id: TypedSettingID<String>, vararg path: String): String = getUntyped(settings, id, path).asString
    fun get(settings: String, id: TypedSettingID<JsonArray>, vararg path: String): JsonArray = getUntyped(settings, id, path).asJsonArray
    fun get(settings: String, id: TypedSettingID<Boolean>, vararg path: String): Boolean = getUntyped(settings, id, path).asBoolean
    fun get(settings: String, id: TypedSettingID<ColorSaver>, vararg path: String): ColorSaver = ColorSaver.of(getUntyped(settings, id, path).asString)
    fun get(settings: String, id: TypedSettingID<JsonObject>, vararg path: String): JsonObject = getUntyped(settings, id, path).asJsonObject

    fun get(settings: String, path: Array<String>, id: TypedSettingID<Float>): Float = getUntyped(settings, id, path).asFloat
    fun get(settings: String, path: Array<String>, id: TypedSettingID<Int>): Int = getUntyped(settings, id, path).asInt
    fun get(settings: String, path: Array<String>, id: TypedSettingID<String>): String = getUntyped(settings, id, path).asString
    fun get(settings: String, path: Array<String>, id: TypedSettingID<JsonArray>): JsonArray = getUntyped(settings, id, path).asJsonArray
    fun get(settings: String, path: Array<String>, id: TypedSettingID<Boolean>): Boolean = getUntyped(settings, id, path).asBoolean
    fun get(settings: String, path: Array<String>, id: TypedSettingID<ColorSaver>): ColorSaver = ColorSaver.of(getUntyped(settings, id, path).asString)
    fun get(settings: String, path: Array<String>, id: TypedSettingID<JsonObject>): JsonObject = getUntyped(settings, id, path).asJsonObject

    fun getUntyped(settings: String, id: TypedSettingID<*>, path: Array<out String>): JsonPrimitive {
        return getUntyped(settings,getSettings(settings).asJsonObject,settings,id,path,0)
    }

    fun getUntyped(sID: String, settings: JsonObject,settingsID: String, id: TypedSettingID<*>, path: Array<out String>, iteration: Int): JsonPrimitive {
        val rId = path.toMutableList()+id.id
        val rsId = (path.toMutableList()+id.id).toTypedArray().joinToString(".")
        
        if(settingMap.containsKey("$sID.$rsId")) return settingMap["$sID.$rsId"]!!

        if(iteration>2) throw SettingNotFoundException(rsId)
        
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
                    return getUntyped(sID,DefaultSettings.getDefault(settingsID).asJsonObject,settingsID,id,path,iteration+1)
                }
            } else {
                return getUntyped(sID,DefaultSettings.getDefault(settingsID).asJsonObject,settingsID,id,path,iteration+1)
            }
        }
        return getUntyped(sID,DefaultSettings.getDefault(settingsID).asJsonObject,settingsID,id,path,iteration+1)
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