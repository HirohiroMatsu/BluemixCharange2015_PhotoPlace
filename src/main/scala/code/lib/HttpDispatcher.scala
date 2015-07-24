package code.lib

//import dispatch._, Defaults._
//import org.apache.commons.codec.binary.Base64._
//import net.liftweb.json._
//import code.model._
//import com.foursquare.rogue.LiftRogue._
//import java.io.File

class HttpDispatcher {

}
/*
object HttpDispatcher {
  val user = "8474f83e-86b4-458f-a736-37b0ca2f8a93"
  val pass = "X5THHNz1htpY"
  val auth = "Basic " + encodeBase64String((user + ":" + pass).getBytes("UTF-8"))
  implicit val formats = net.liftweb.json.DefaultFormats
  def main(args: Array[String]): Unit = {
    //    getGeo("19679727445")
  }

  def getLabels() {
    println("start get labels")
    val tagAPI = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/labels").addHeader("Authorization", auth)
    val result = Http(tagAPI OK as.String)
    for (r <- result) {
      val data = parse(r).extract[LabelInfo]
      data.labels.foreach { x => Label.createRecord.label(x).save(true) }
      data.label_groups.foreach { x => LabelGroupe.createRecord.groupe(x).save(true) }
    }
  }

  def getParam(id: String) = {
    val f = new File("/Users/lift/work/Docker/Docker_sample/19687215381.jpg")
    val tagAPI = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/labels").addHeader("Authorization", auth)
    println(tagAPI.props)
    val recogAPI = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").POST.addHeader("Authorization", auth) <<< f
    println(recogAPI.props)
    println("start recog id:" + id)
    for (r <- Http(recogAPI OK as.String))
      println("id:" + id + " " + r)
  }

  def getPhotoParam(id: String) = {
    val f = new File(id + ".jpg")
    FlickrPhoto.where(_.photoID eqs id).get() match {
      case Some(p) => {
        println("start connect")
        for (res <- Http(url(p.url.get) > as.File(f))) {
          val recogAPI = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").addHeader("Authorization", auth).POST <<< f
          println("start recog id:" + id)
          for (r <- Http(recogAPI OK as.String))
            println("id:" + id + " " + r)
        }
      }
      case _ => println("not exist data id:" + id)
    }

    //    val user = "8474f83e-86b4-458f-a736-37b0ca2f8a93"
    //    val pass = "X5THHNz1htpY"
    //    val auth = "Basic " + encodeBase64String((user + ":" + pass).getBytes("UTF-8"))
    //    val tagAPI = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").addHeader("Authorization", auth)
    //    val a = Http(tagAPI OK as.File("aa.vvv"))
  }

  def getPhotos() {
    println("start get photos")
    val u = url("https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=c669c93c32fed467c39929c71a793849&sort=nterestingness-desc&has_geo=1&content_type=1&bbox=122,20,154,46&format=json&nojsoncallback=1")
    val result = Http(u OK as.String)
    for (r <- result) {
      println("finish photo future")
      println(r)
      parse(r).extract[PhotosPack].photos.photo.foreach { getGeo(_) }
    }
    println("finish")
  }

  private def getGeo(p: Photo) = {
    println("start get geo")
    val u = url("https://api.flickr.com/services/rest?method=flickr.photos.geo.getLocation&api_key=c669c93c32fed467c39929c71a793849&photo_id=" + p.id + "&format=json&nojsoncallback=1")
    for (r <- Http(u OK as.String)) {
      println("finish photo future")
      try { FlickrPhoto.make(p, parse(r).extract[GeoPack].photo.location) }
      catch {
        case t: Throwable => println("error id:" + p.id)
      }
    }
    //    .completeOption.getOrElse(Location("", "", "", "", Place("", "", ""), Place("", "", ""), Place("", "", ""), "", ""))
  }
}

case class LabelInfo(label_groups: List[String], labels: List[String])
case class PhotosPack(photos: Photos)
case class Photos(page: Int, pages: Int, perpage: Int, total: String, photo: List[Photo])
case class Photo(id: String, owner: String, secret: String, server: String, farm: Int, title: String, ispublic: Int, isfriend: Int, isfamily: Int)
case class GeoPack(photo: GeoPhoto, stat: String)
case class GeoPhoto(id: String, location: Location)
case class Location(latitude: String, longitude: String, accuracy: String, context: String, locality: Place, region: Place, country: Place, place_id: String, woeid: String)
case class Place(_content: String, place_id: String, woeid: String)
*/



