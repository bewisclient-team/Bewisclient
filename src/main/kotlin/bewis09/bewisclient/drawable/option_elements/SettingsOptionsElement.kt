package bewis09.bewisclient.drawable.option_elements

import net.minecraft.util.Identifier

open class SettingsOptionsElement(originalTitle: String, val path: String, elements: ArrayList<MainOptionsElement>): MainOptionsElement(if (originalTitle.toCharArray()[0] =='%') originalTitle.drop(1) else "widgets.$originalTitle", if (originalTitle.toCharArray()[0] =='%') "description."+originalTitle.drop(1) else "widgets.description.$originalTitle", elements, Identifier.of(""))