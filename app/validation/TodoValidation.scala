package validation

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

object TodoValidation {

  private val titlePattern= "^[A-Za-zぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠0-9]*$".r

  val titleConstraint: Constraint[String] = Constraint("constraints.title")({
    case titlePattern() => Valid
    case _ => Invalid("title must be a line, and include only alphabet or number, or Japanese character")
  })
}