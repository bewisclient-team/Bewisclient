package bewis09.bewisclient.util

/**
 * Used to search through a [java.util.ArrayList] with type [SearchableElement]
 *
 * Collects a [java.util.ArrayList] of Lambas which have the keyword that is searched for as the input and returns the element that should be shown
 */
object Search {

    fun <K: SearchableElement<K>> collect(elements: Collection<K>): ArrayList<(String) -> K?> {
        val list = arrayListOf<(String)->K?>()

        elements.forEach {
            list.add(it.getElementByKeywordLamba())
        }

        elements.forEach {
            list.addAll(it.collectChildLambas())
        }

        return list
    }

    /**
     * Searches through a [java.util.ArrayList] of lambas for a given keyword and collects the returned elements
     * @param [key] The keyword that is searched for
     * @param [lambas] A [java.util.ArrayList] with the lambas that can return an element according to the keyword
     * @return A [java.util.ArrayList] containing the elements which were returned by the [lambas]
     */
    fun <K: SearchableElement<K>> search(key: String, lambas: ArrayList<(String) -> K?>): ArrayList<K> {
        val resultList = arrayListOf<K>()

        lambas.forEach {
            val a = it(key)
            if(a!=null)
                resultList.add(a)
        }

        return resultList
    }

    /**
     * An element that can be found by the search engine and can have child elements that can be found as well
     * @param K The type of the result - Should always be the type of the class that implements this interface
     * @throws [java.lang.ClassCastException] if [K] is not in the hierarchy of the class that implements this interface
     */
    interface SearchableElement<K: SearchableElement<K>> {

        /**
         * @return Returns the keywords that the search engine uses to list the corresponding elements as an [java.util.ArrayList]
         */
        fun getSearchKeywords(): ArrayList<String>? {
            return null
        }

        fun collectChildLambas(): ArrayList<(String) -> K?> {
            val list = arrayListOf<(String)->K?>()

            getChildElementsForSearch()?.forEach {
                list.add(it.getElementByKeywordLamba())
                list.addAll(it.collectChildLambas())
            }

            return list
        }

        /**
         * @return The [java.util.ArrayList] of the child elements that should be searched when using the search engine
         *
         * Should return null or an empty [java.util.ArrayList] if there are no children
         */
        fun getChildElementsForSearch(): ArrayList<K>? {
            return null
        }

        /**
         * Can be overwritten when the element has an internal structure
         * @return A Lamba that has a [String] as input, which is the keyword that is searched for and returns an element that will be shown or null if non should be shown
         */
        @Suppress("unchecked_cast")
        fun getElementByKeywordLamba(): (String)->K? {
            return fun(it: String): K? {
                getSearchKeywords()?.forEach { keyword: String ->
                    if (keyword.lowercase().contains(it.lowercase())) return this as K
                }
                return null
            }
        }
    }
}