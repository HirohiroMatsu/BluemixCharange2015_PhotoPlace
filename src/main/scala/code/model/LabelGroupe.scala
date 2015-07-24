package code.model

import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import com.foursquare.rogue.LiftRogue._
import java.util.Date

class LabelGroupe extends MongoRecord[LabelGroupe] with ObjectIdPk[LabelGroupe] {
  def meta = LabelGroupe
  object groupe extends StringField(this, 30)
}

object LabelGroupe extends LabelGroupe with MongoMetaRecord[LabelGroupe]