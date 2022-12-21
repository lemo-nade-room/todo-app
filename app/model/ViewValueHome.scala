/**
 *
 * to do sample project
 *
 */

package model

import model.content.CategoryContent

// Topページのviewvalue
case class ViewValueHome(
  title:  String,
  cssSrc: Seq[String],
  jsSrc:  Seq[String],
  categories: Seq[CategoryContent.View],
) extends ViewValueCommon

