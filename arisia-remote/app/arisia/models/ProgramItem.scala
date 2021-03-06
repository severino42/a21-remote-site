package arisia.models

import java.util.Date

import arisia.util._

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class ProgramItemId(v: String) extends StdString
object ProgramItemId extends StdStringUtils(new ProgramItemId(_))

case class ProgramItemTitle(v: String) extends StdString
object ProgramItemTitle extends StdStringUtils(new ProgramItemTitle(_))

case class ProgramItemTag(v: String) extends StdString
object ProgramItemTag extends StdStringUtils(new ProgramItemTag(_))

case class ProgramItemLoc(v: String) extends StdString
object ProgramItemLoc extends StdStringUtils(new ProgramItemLoc(_))

case class ProgramItemPersonName(v: String) extends StdString
object ProgramItemPersonName extends StdStringUtils(new ProgramItemPersonName(_))

case class ProgramItemPerson(
  id: ProgramPersonId,
  name: ProgramItemPersonName
)
object ProgramItemPerson {
  implicit val fmt: Format[ProgramItemPerson] = Json.format
}

case class ProgramItemDesc(v: String) extends StdString
object ProgramItemDesc extends StdStringUtils(new ProgramItemDesc(_))

/**
 * Represents a Program Item, in a way that exactly matches KonOpas.
 *
 * See https://konopas.org/data-fmt for the format we are ingesting to get this.
 *
 * Note that we are playing it cautious, and assuming that basically all fields are optional. The id is the only
 * absolutely-required field, since that's the primary key.
 */
case class ProgramItem(
  id: ProgramItemId,
  title: Option[ProgramItemTitle],
  tags: List[ProgramItemTag],
  date: Option[Date],
  // TODO: how do we represent "time" in the KonOpas sense?
  // TODO: convert this String to Int on the way in and out:
  mins: Option[String],
  loc: List[ProgramItemLoc],
  people: List[ProgramItemPerson],
  desc: Option[ProgramItemDesc]
)
object ProgramItem {
  implicit val fmt: Format[ProgramItem] = (
    (JsPath \ "id").format[ProgramItemId] and
      (JsPath \ "title").formatNullable[ProgramItemTitle] and
      (JsPath \ "tags").formatWithDefault[List[ProgramItemTag]](List.empty) and
      (JsPath \ "date").formatNullable[Date] and
      (JsPath \ "mins").formatNullable[String] and
      (JsPath \ "loc").formatWithDefault[List[ProgramItemLoc]](List.empty) and
      (JsPath \ "people").formatWithDefault[List[ProgramItemPerson]](List.empty) and
      (JsPath \ "desc").formatNullable[ProgramItemDesc]
    )(ProgramItem.apply, unlift(ProgramItem.unapply))
}
