package code
package snippet

import java.util.Date
import code.lib._
import net.liftweb.common._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import com.foursquare.rogue.LiftRogue._
import code.model.Counter

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  val c = Counter.where(_.c > 0).orderDesc(_.c).get()
  var k = 1
  c match {
    case Some(in) =>
      Counter.createRecord.c(in.c.get + 1).save(true); k = in.c.get + 1
    case None => Counter.createRecord.c(1).save(true)
  }

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> (date.map(_.toString) + "  counter:" + k.toString() )

  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}

