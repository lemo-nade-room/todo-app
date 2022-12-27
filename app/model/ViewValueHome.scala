/**
 *
 * to do sample project
 *
 */

package model

import model.content.CategoryContent
import play.api.data.FormError

// Topページのviewvalue
case class ViewValueHome(
  title:  String,
  cssSrc: Seq[String],
  jsSrc:  Seq[String],
  categories: Seq[CategoryContent.View],
  errors: Seq[FormError],
) extends ViewValueCommon

