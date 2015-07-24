package code.model

import net.liftweb.mongodb.record._
import net.liftweb.mongodb.record.field._
import net.liftweb.record.field._
import com.foursquare.rogue.LiftRogue._
import java.util.Date
import code.lib._
import scala.collection.mutable.Map

class FlickrPhoto extends MongoRecord[FlickrPhoto] with ObjectIdPk[FlickrPhoto] {
  def meta = FlickrPhoto
  object photoID extends StringField(this, 30)
  object owner extends StringField(this, 30)
  object secret extends StringField(this, 30)
  object server extends StringField(this, 30)
  object farm extends IntField(this)
  object title extends StringField(this, 30)
  object ispublic extends IntField(this)
  object isfriend extends IntField(this)
  object isfamily extends IntField(this)
  object latitude extends StringField(this, 30)
  object longitude extends StringField(this, 30)
  object accuracy extends StringField(this, 30)
  object locality_content extends StringField(this, 30)
  object region_content extends StringField(this, 30)
  object country_content extends StringField(this, 30)
  object url extends StringField(this, 100)
  object scoreList extends BsonRecordListField(this, Score)
  object scoreSize extends DoubleField(this)

  def calcScore(m: Map[String, Double]) = {
    val s = scoreList.get.map(x => m.getOrElse(x.label.get, 0.0) * x.point.get).reduceLeft(_ + _) / scoreSize.get
    if (s == 0) {
      None
    } else {
      Some((this, s))
    }
  }

  def getScoreSize() = {
    val sl = scoreList.get
    val ll =  sl.map { x => x.point.get * x.point.get }.reduceLeft(_ + _)
    Math.sqrt(ll)
  }
}

object FlickrPhoto extends FlickrPhoto with MongoMetaRecord[FlickrPhoto] {
  def make(p: Photo, g: Location, ls: List[LabelScore]) = {
    createRecord.photoID(p.id)
      .owner(p.owner).secret(p.secret).server(p.server).farm(p.farm).title(p.title).ispublic(p.ispublic).isfriend(p.isfriend).isfamily(p.isfamily)
      .latitude(g.latitude).longitude(g.longitude).accuracy(g.accuracy).locality_content(g.locality._content).region_content(g.region._content).country_content(g.country._content)
      .url("https://farm" + p.farm + ".staticflickr.com/" + p.server + "/" + p.id + "_" + p.secret + ".jpg")
      .scoreList(ls.map { x => Score.createRecord.label(x.label_name).point(x.label_score.toDouble) })
      .save(true)
  }
}

class Score extends MongoRecord[Score] with ObjectIdPk[Score] {
  def meta = Score
  object label extends StringField(this, 30)
  object point extends DoubleField(this)
}

object Score extends Score with MongoMetaRecord[Score]
