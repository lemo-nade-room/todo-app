package validation.api

import play.api.libs.json.{JsonValidationError, Reads}
import play.api.libs.json.Reads.pattern
import validation.ValidationRegex.alphabetOrNumberOrJapaneseCharacterOneLine

object TodoValidation {
  def title(implicit reads: Reads[String]): Reads[String] =
    pattern(
      alphabetOrNumberOrJapaneseCharacterOneLine,
      "error.todoTitle"
    )

  def body(implicit reads: Reads[String]): Reads[String] =
    Reads.filterNot[String](JsonValidationError("error.todoBody"))(_.split("\n").exists {
      case alphabetOrNumberOrJapaneseCharacterOneLine() => false
      case _ => true
    })
}
