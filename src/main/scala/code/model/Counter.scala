package code.model
import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import com.foursquare.rogue.LiftRogue._
import java.util.Date

class Counter extends MongoRecord[Counter] with ObjectIdPk[Counter] {
  def meta = Counter
  object c extends IntField(this)
}

object Counter extends Counter with MongoMetaRecord[Counter]