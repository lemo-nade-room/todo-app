package validation

import scala.util.matching.Regex

object ValidationRegex {

  val alphabetOrNumberOrJapaneseCharacterOneLine: Regex = "^[A-Za-zぁ-んァ-ヶｱ-ﾝﾞ　一-龠0-9]*$".r

  val alphabetOrNumber: Regex = "^[A-Za-z0-9]*$".r

}
