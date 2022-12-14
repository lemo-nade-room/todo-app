package validation

import play.api.data.validation.{Constraint, Invalid, Valid}

object CategoryValidation {

  val nameConstraint: Constraint[String] = Constraint("constraints.categoryName")({
    case ValidationRegex.alphabetOrNumberOrJapaneseCharacter() => Valid
    case _ => Invalid("category name must be a line, and include only alphabet or number, or Japanese character")
  })

  val slugConstraint: Constraint[String] = Constraint("constraints.slug")({
    case ValidationRegex.alphabetOrNumber() => Valid
    case _ => Invalid("slug must be a line, and include only alphabet or number")
  })
}