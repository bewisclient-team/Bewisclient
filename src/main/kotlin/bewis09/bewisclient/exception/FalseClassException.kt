package bewis09.bewisclient.exception

class FalseClassException(k:Any, clazz: Class<*>): RuntimeException("Wrong class present: $k is not of class ${clazz.name}")