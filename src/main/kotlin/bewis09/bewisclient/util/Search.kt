package bewis09.bewisclient.util

/**
 * Used to search through a [java.util.ArrayList] with type [SearchableElement]
 *
 * Collects a [java.util.HashMap] with the keywords as the keys and a [java.util.ArrayList] as the value with the results
 */
object Search {

    /**
     * Appends the elements to the corresponding keywords used by the search engine
     * @param [map] The [java.util.HashMap] where the elements corresponding to the keywords are stored
     * @param [elements] The [java.util.ArrayList] with all the elements that should be searched
     * @return [map] with elements appended
     */
    fun <K: SearchableElement<K>> appendAllElementsToKeyword(
        map: HashMap<String, ArrayList<K>>,
        elements: Collection<K>
    ): HashMap<String, ArrayList<K>> {
        elements.forEach {
            it.appendElementToKeywords(map)
            it.appendChildElementsToKeywords(map)
        }
        return map
    }

    /**
     * Collects all lambas that return additional elements that should be shown on specific keywords
     * @param [elements] The [java.util.ArrayList] with all the elements that should be searched
     * @return [java.util.ArrayList] with the lambas that return a [java.util.ArrayList] according to the keyword
     */
    fun <K: SearchableElement<K>> collectAdditions(elements: ArrayList<K>): ArrayList<(String) -> ArrayList<K>?> {
        val list = arrayListOf<(String)->ArrayList<K>?>()

        elements.forEach {
            list.add(it.getAdditionalElementsWithKeyword())
            list.addAll(it.collectChildAdditions())
        }

        return list
    }

    /**
     * Collects all keywords and results in a [java.util.HashMap]
     * @param [elements] The [java.util.ArrayList] with all the elements that should be searched
     * @return [java.util.HashMap] with the keywords as the keys and a [java.util.ArrayList] as the value with the results
     */
    fun <K: SearchableElement<K>> collect(elements: Collection<K>): HashMap<String, ArrayList<K>> {
        return appendAllElementsToKeyword(HashMap(), elements)
    }

    /**
     * Searches through a [java.util.HashMap] for a given keyword while ignoring the casing
     * @param [map] The [java.util.HashMap] with the keywords and the corresponding elements
     * @param [key] The keyword that is searched for
     * @return A [java.util.ArrayList] containing the elements corresponding to all keywords of which [key] is a substring
     */
    fun <K: SearchableElement<K>> search(map: HashMap<String, ArrayList<K>>, key: String): ArrayList<K> {
        val resultList = arrayListOf<K>()

        map.keys.forEach{ okey ->
            if(okey.lowercase().contains(key.lowercase())) {
                map[okey]!!.forEach{
                    if(!resultList.contains(it)) {
                        resultList.add(it)
                    }
                }
            }
        }

        return resultList
    }

    /**
     * Searches through a [java.util.HashMap] for a given keyword while ignoring the casing and adds the elements given by the additions [java.util.ArrayList] of lambas
     * @param [map] The [java.util.HashMap] with the keywords and the corresponding elements
     * @param [key] The keyword that is searched for
     * @param [additions] A list of lambas that have a [String] as input, which is the keyword that is searched for and return an [java.util.ArrayList] of elements that will be shown
     * @return A [java.util.ArrayList] containing the elements corresponding to all keywords of which [key] is a substring and the additions
     */
    fun <K: SearchableElement<K>> searchWithAdditions(map: HashMap<String, ArrayList<K>>, key: String, additions: ArrayList<(String) -> ArrayList<K>?>): ArrayList<K> {
        val resultList = arrayListOf<K>()

        resultList.addAll(search(map,key))

        additions.forEach {
            resultList.addAll(it(key)?: arrayListOf())
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

        /**
         * Appends the element to the corresponding keywords used by the search engine
         * @param [map] The [java.util.HashMap] where the elements corresponding to the keywords are stored
         * @return [map] with elements appended
         * @throws [java.lang.ClassCastException] if [K] is not in the hierarchy of the class that implements this interface
         */
        @Suppress("unchecked_cast")
        fun appendElementToKeywords(map: HashMap<String, ArrayList<K>>): HashMap<String, ArrayList<K>> {
            getSearchKeywords()?.forEach {
                map.putIfAbsent(it, arrayListOf())
                map[it]!!.add(this as K)
            }
            return map
        }

        @Suppress("unchecked_cast")
        fun <K: SearchableElement<K>> collectChildAdditions(): ArrayList<(String) -> ArrayList<K>?> {
            val list = arrayListOf<(String)->ArrayList<K>?>()

            getChildElementsForSearch()?.forEach {
                list.add(it.getAdditionalElementsWithKeyword() as ((String) -> ArrayList<K>))
                list.addAll(it.collectChildAdditions())
            }

            return list
        }

        /**
         * Appends the child elements to the corresponding keywords used by the search engine
         * @param [map] The [java.util.HashMap] where the elements corresponding to the keywords are stored
         * @return [map] with child elements appended
         */
        fun appendChildElementsToKeywords(map: HashMap<String, ArrayList<K>>): HashMap<String, ArrayList<K>> {
            getChildElementsForSearch()?.forEach{
                it.appendElementToKeywords(map)
                it.appendChildElementsToKeywords(map)
            }
            return map
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
         * Allows additional elements and keywords to be added to the collection. Can be used when the element has an internal structure
         * @return A Lamba that has a [String] as input, which is the keyword that is searched for and returns an [java.util.ArrayList] of elements that will be shown
         */
        fun getAdditionalElementsWithKeyword(): (String)->ArrayList<K>? {
            return { null }
        }
    }
}