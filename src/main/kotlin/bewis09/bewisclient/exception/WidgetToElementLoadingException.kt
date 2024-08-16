package bewis09.bewisclient.exception

/**
 * Gets thrown when the type of the value of a setting of a widget doesn't correspond to an [bewis09.bewisclient.drawable.option_elements.OptionsElement]
 */
class WidgetToElementLoadingException(key: String, value: Any): Exception("Could not construct OptionsElement for key $key since its value is of type ${value::class.java.name}")