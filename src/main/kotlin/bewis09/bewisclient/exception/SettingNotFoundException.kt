package bewis09.bewisclient.exception

class SettingNotFoundException(id: String): Exception("Setting with ID $id not found")