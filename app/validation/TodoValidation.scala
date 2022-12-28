package validation

import play.api.data.validation.{Constraint, Invalid, Valid}

object TodoValidation {

  val titleConstraint: Constraint[String] = Constraint("constraints.title")({
    case ValidationRegex.alphabetOrNumberOrJapaneseCharacterOneLine() => Valid
    case _ => Invalid("title must be a line, and include only alphabet or number, or Japanese character")
  })

  val bodyConstraint: Constraint[String] = Constraint("constraints.title")({
    case text if text.split("\n").forall {
      case ValidationRegex.alphabetOrNumberOrJapaneseCharacterOneLine() => true
      case _ => false
    } => Valid
    case _ => Invalid("body must be included only alphabet or number, or Japanese character")
  })
}