package validation.api


import play.api.libs.json.Reads
import play.api.libs.json.Reads.pattern
import validation.ValidationRegex.{alphabetOrNumber, alphabetOrNumberOrJapaneseCharacterOneLine}

object CategoryValidation {
  def name(implicit reads: Reads[String]): Reads[String] =
    pattern(
      alphabetOrNumberOrJapaneseCharacterOneLine,
      "error.todoTitle"
    )

  def slug(implicit reads: Reads[String]): Reads[String] =
    pattern(
      alphabetOrNumber,
      "error.todoTitle"
    )
}
