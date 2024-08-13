package bewis09.bewisclient.doc

import bewis09.bewisclient.settingsLoader.Settings
import bewis09.bewisclient.settingsLoader.SettingsLoader
import bewis09.bewisclient.util.ColorSaver
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object Samples: Settings() {
    val SAMPLE_ID_STRING = SettingsLoader.TypedSettingID<String>("id")
    val SAMPLE_ID_NUMBER = SettingsLoader.TypedSettingID<Number>("id")
    val SAMPLE_ID_BOOLEAN = SettingsLoader.TypedSettingID<Boolean>("id")
    val SAMPLE_ID_COLOR_SAVER = SettingsLoader.TypedSettingID<ColorSaver>("id")
    val SAMPLE_ID_FLOAT = SettingsLoader.TypedSettingID<Float>("id")
    val SAMPLE_ID_INT = SettingsLoader.TypedSettingID<Int>("id")
    val SAMPLE_ID_JSON_ARRAY = SettingsLoader.TypedSettingID<JsonArray>("id")
    val SAMPLE_ID_JSON_OBJECT = SettingsLoader.TypedSettingID<JsonObject>("id")

    fun setString() = SettingsLoader.set(Settings.DESIGN, "value", SAMPLE_ID_STRING, "path1", "path2")
    fun setNumber() = SettingsLoader.set(Settings.DESIGN, 1, SAMPLE_ID_NUMBER, "path1", "path2")
    fun setBoolean() = SettingsLoader.set(Settings.DESIGN, true, SAMPLE_ID_BOOLEAN, "path1", "path2")
    fun setColorSaver() = SettingsLoader.set(Settings.DESIGN, ColorSaver.of("0xFFFFFF"), SAMPLE_ID_COLOR_SAVER, "path1", "path2")

    fun setStringArray() = SettingsLoader.set(Settings.DESIGN, "value", arrayOf("path1", "path2"), SAMPLE_ID_STRING)
    fun setNumberArray() = SettingsLoader.set(Settings.DESIGN, 1, arrayOf("path1", "path2"), SAMPLE_ID_NUMBER)
    fun setBooleanArray() = SettingsLoader.set(Settings.DESIGN, true, arrayOf("path1", "path2"), SAMPLE_ID_BOOLEAN)
    fun setColorSaverArray() = SettingsLoader.set(Settings.DESIGN, ColorSaver.of("0xFFFFFF"), arrayOf("path1", "path2"), SAMPLE_ID_COLOR_SAVER)

    fun getFloat() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_FLOAT, "path1", "path2")
    fun getInt() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_INT, "path1", "path2")
    fun getString() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_STRING, "path1", "path2")
    fun getBoolean() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_BOOLEAN, "path1", "path2")
    fun getColorSaver() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_COLOR_SAVER, "path1", "path2")
    fun getJsonArray() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_JSON_ARRAY, "path1", "path2")
    fun getJsonObject() = SettingsLoader.get(Settings.DESIGN, SAMPLE_ID_JSON_OBJECT, "path1", "path2")

    fun getFloatArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_FLOAT)
    fun getIntArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_INT)
    fun getStringArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_STRING)
    fun getBooleanArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_BOOLEAN)
    fun getColorSaverArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_COLOR_SAVER)
    fun getJsonArrayArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_JSON_ARRAY)
    fun getJsonObjectArray() = SettingsLoader.get(Settings.DESIGN, arrayOf("path1", "path2"), SAMPLE_ID_JSON_OBJECT)
}