package validation

import scala.util.matching.Regex

object ValidationRegex {

  val alphabetOrNumberOrJapaneseCharacter: Regex = "^[A-Za-zぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9]*$".r

  val alphabetOrNumber: Regex = "^[A-Za-z0-9]*$".r

}
