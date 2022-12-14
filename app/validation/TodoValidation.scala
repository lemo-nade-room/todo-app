package validation

import play.api.data.validation.{Constraint, Invalid, Valid}

object TodoValidation {

  val titleConstraint: Constraint[String] = Constraint("constraints.title")({
    case ValidationRegex.alphabetOrNumberOrJapaneseCharacter() => Valid
    case _ => Invalid("title must be a line, and include only alphabet or number, or Japanese character")
  })

  val bodyConstraint: Constraint[String] = Constraint("constraints.body")({
    plainText =>
      val invalids = plainText.split("\n").find {
        case ValidationRegex.alphabetOrNumberOrJapaneseCharacter() => false
        case _ => true
      }
      if (invalids.isEmpty) {
        Valid
      } else {
        Invalid("body must be include only alphabet or number, or Japanese character")
      }
  })

}