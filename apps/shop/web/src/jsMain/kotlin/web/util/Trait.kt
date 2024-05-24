package web.util

import component.localization.Strings
import component.localization.getString
import data.type.Trait

fun Trait.titleString(): String = getString(Strings.valueOf(name))

fun Trait.descriptionString(): String = getString(Strings.valueOf("${name}TraitDescription"))
