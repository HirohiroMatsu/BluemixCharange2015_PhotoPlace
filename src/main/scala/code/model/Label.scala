package code.model

import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import com.foursquare.rogue.LiftRogue._
import java.util.Date

class Label extends MongoRecord[Label] with ObjectIdPk[Label] {
  def meta = Label
  object label extends StringField(this, 30)
}

object Label extends Label with MongoMetaRecord[Label]