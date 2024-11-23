package bewis09.bewisclient.exception

/**
 * Gets thrown when trying to get or set a setting that doesn't exist
 */
class SettingNotFoundException(id: String): Exception("Setting with ID $id not found")