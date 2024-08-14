package bewis09.bewisclient.exception

// TODO Document
class WidgetToElementLoadingException(key: String, value: Any): Exception("Could not construct OptionsElement for key $key since its value is of type ${value::class.java.name}")