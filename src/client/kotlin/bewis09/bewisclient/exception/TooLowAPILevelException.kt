package bewis09.bewisclient.exception

import bewis09.bewisclient.Bewisclient

class TooLowAPILevelException(min: Int): Exception("The current API Level (${Bewisclient.API_LEVEL}) is too low. At least $min is required")