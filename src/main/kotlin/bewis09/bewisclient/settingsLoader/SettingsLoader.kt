@file:Suppress("UNCHECKED_CAST")

package bewis09.bewisclient.settingsLoader

import bewis09.bewisclient.Bewisclient
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.PrintWriter
import java.util.*

object SettingsLoader {

    var WidgetSettings = Settings()
    var GeneralSettings = Settings()
    var DesignSettings = Settings()

    fun loadSettings() {
        WidgetSettings = Settings()
        GeneralSettings = Settings()
        DesignSettings = Settings()

        loadSetting("widgets",WidgetSettings)
        loadSetting("general",GeneralSettings)
        loadSetting("design",DesignSettings)
    }

    private fun loadSetting(id: String, settings: Settings) {
        val file = File((FabricLoader.getInstance().gameDir).toString()+"/bewisclient/"+id+".json")
        if(file.exists()) {
            val scanner = Scanner(file)

            var str = ""

            while (scanner.hasNextLine()) {
                str += scanner.nextLine()
            }

            val sets: ArrayList<Settings> = arrayListOf()

            var keyString = ""
            var openString = ""
            var stringOpen = false
            var first = true

            for (c in str.chars()) {
                if(c?.toChar() =='{') {
                    first=true
                    if(sets.size==0) {
                        sets.add(settings)
                    } else {
                        val o = Settings()
                        sets[sets.size-1].setValueWithoutSave(TypedSettingID(keyString),o,)
                        sets.add(o)
                    }
                } else if(c?.toChar() =='"') {
                    stringOpen=!stringOpen
                    if(!stringOpen) {
                        if(first) {
                            first = false
                            keyString = openString
                        } else {
                            sets[sets.size-1].setValueWithoutSave(TypedSettingID(keyString),openString)
                            keyString = ""
                            first=true
                        }
                        openString = ""
                    } else {
                        openString = ""
                    }
                } else if(stringOpen) {
                    openString+=c?.toChar()
                } else if(c?.toChar() ==',') {
                    if(openString!="") {
                        try {
                            if (openString.contains("true") || openString.contains("false")) {
                                sets[sets.size - 1].setValueWithoutSave(TypedSettingID(keyString), (openString).contains("true"))
                            } else
                                sets[sets.size - 1].setValueWithoutSave(TypedSettingID(keyString), (openString).toFloat())
                        } catch (_: Exception) {}
                    }
                    first=true
                    keyString = ""
                    openString = ""
                } else if(c?.toChar() =='}') {
                    if(openString!="") {
                        try {
                            if (openString.contains("true") || openString.contains("false")) {
                                sets[sets.size - 1].setValueWithoutSave(TypedSettingID(keyString), (openString).contains("true"))
                            } else
                                sets[sets.size - 1].setValueWithoutSave(TypedSettingID(keyString), (openString).toFloat())
                        } catch (_: Exception) {}
                    }
                    first=true
                    keyString = ""
                    openString = ""
                    sets.remove(sets[sets.size-1])
                } else if(c?.toChar() ==':') {
                    first=false
                } else {
                    openString += c?.toChar()
                }
            }
        } else {
            file.createNewFile()
        }

        for (pair in DefaultSettings.getDefault(id)) {
            var set = settings

            val o = pair.first.split(".")
            for ((index,string) in o.iterator().withIndex()) {
                if(index==o.size-1) {
                    if(set.getValueAsAny(string)==null)
                        set.setValueWithoutSave(string,pair.second)
                } else {
                    val s = set.getValueAsAny(string)
                    if(s is Settings) {
                        set = s
                    } else {
                        break
                    }
                }
            }
        }

        saveSettings(id,settings)
    }

    fun saveSettings(id:String,settings: Settings) {
        val file = File((FabricLoader.getInstance().gameDir).toString()+"/bewisclient/"+id+".json")
        val printWriter = PrintWriter(file)
        printWriter.print(settings.toString())
        printWriter.close()
    }

    fun saveAllSettings() {
        saveSettings("widgets", WidgetSettings)
        saveSettings("general", GeneralSettings)
        saveSettings("design", DesignSettings)
    }

    class TypedSettingID<K>(val id: String) {
        override fun equals(other: Any?): Boolean {
            if(other is TypedSettingID<*>)
                return id == other.id
            return false
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }

    class Settings {

        private val settings: HashMap<String,Any> = HashMap()

        fun getValueAsAny(settingID: String): Any? {
            if(!settings.contains(settingID)) return null
            return settings.getValue(settingID)
        }

        fun <K> getValue(settingID: TypedSettingID<K>): K? {
            if(!settings.contains(settingID.id)) return null
            return settings.getValue(settingID.id) as K
        }

        fun <K> getValue(settingID: String): K? {
            if(!settings.contains(settingID)) return null
            return settings.getValue(settingID) as K
        }

        fun <K> setValueWithoutSave(settingID: TypedSettingID<K>, value: K): K? {
            return setValueWithoutSave(settingID.id,value)
        }

        fun <K> setValueWithoutSave(settingID: String, value: K): K? {
            val t = settings.remove(settingID) as K?
            settings[settingID] = value as Any
            return t
        }

        fun <K> setValue(settingID: String, value: K): K? {
            val t = settings.remove(settingID) as K?
            settings[settingID] = value as Any
            saveAllSettings()
            return t
        }

        fun <K> setValue(settingID: TypedSettingID<K>, value: K): K? {
            return setValue(settingID.id,value)
        }

        override fun toString(): String {
            var str = ""
            for ((i,s) in settings.entries.iterator().withIndex()) {
                var sl = s.value.toString()
                if(s.value is String) {
                    sl="\"$sl\""
                }
                str+=("\""+s.key+"\""+":"+sl)
                if(i!=settings.entries.size-1) {
                    str+=",\n"
                }
            }
            return "{\n$str\n}"
        }
    }
}