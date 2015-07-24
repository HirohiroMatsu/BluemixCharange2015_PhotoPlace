package code.lib

import dispatch.classic._
import dispatch.classic.mime.Mime._
import java.io.File
import java.io.FileOutputStream
import net.liftweb.json._
import code.model._
import com.foursquare.rogue.LiftRogue._
import scala.annotation.tailrec
import scala.collection.mutable.Map

object HttpDispatcherClassic {
  val user = "8474f83e-86b4-458f-a736-37b0ca2f8a93"
  val pass = "X5THHNz1htpY"
  implicit val formats = net.liftweb.json.DefaultFormats

  def main(args: Array[String]): Unit = {
    val f = new File("19687215381.jpg")
    val s = Http((url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").POST.as(user, pass) <<* ("imgFile", f) as_str))
    println(s)
  }
  
  def setScoreSize() = {
    FlickrPhoto.findAll.foreach{x =>  x.scoreSize( x.getScoreSize()).save(true)}
    println("finish calc")
  }
  
  def refreshData() = {
    FlickrPhoto.where { _.country_content eqs "Russia" }.fetch().map(_.delete_!)
  }

  def makeRecomends(f: File): RecomendList = {
    val pm: Map[String, Double] = Map()
    getPhotoParam(f).foreach(x => pm.put(x.label_name, x.label_score.toDouble))
    RecomendList(FlickrPhoto.findAll.map(_.calcScore(pm)).collect { case Some(t) => t }.sortWith { (a, b) => a._2 > b._2 }.take(120).map(x => Recomend(x._1.url.get, x._1.latitude.get.toDouble, x._1.longitude.get.toDouble)))
  }

  def sample() {
    val s = FlickrPhoto.findAll.sortWith((x, y) => x.photoID.get.toLong < y.photoID.get.toLong)
    println(s.head.photoID)
    println(s.tail.head.photoID)
    println(s.tail.tail.head.photoID)
    println(s.tail.tail.tail.head.photoID)
  }

  def getLabels() {
    println("start get labels")
    val tag = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/labels").as(user, pass)
    val r = Http(tag as_str)
    val data = parse(r).extract[LabelInfo]
    data.labels.foreach { x => Label.createRecord.label(x).save(true) }
    data.label_groups.foreach { x => LabelGroupe.createRecord.groupe(x).save(true) }
  }

  def getPhotos() = {
    println("start")
    for (i <- 1 to 20) {
      getPhoto(i)
    }
    println("end")
  }

  def getPhoto(no: Int) = {
    println("start get photo page " + no)
    try {
      val u = url("https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=c669c93c32fed467c39929c71a793849&sort=interestingness-desc&has_geo=1&content_type=1&bbox=122,20,154,46&page=" + no + "&format=json&nojsoncallback=1")
      val r = Http(u as_str)
      println(r)
      getParams(getGeos(parse(r).extract[PhotosPack].photos.photo))
      //      parse(r).extract[PhotosPack].photos.photo.foreach { x => if (!FlickrPhoto.where(_.photoID eqs x.id).exists) { getGeo(x) } }
      //      (parse(r).extract[PhotosPack].photos.photo.head :: Nil).foreach { x => if (!FlickrPhoto.where(_.photoID eqs x.id).exists) { getGeo(x) } }
      println("finish page " + no)
    } catch {
      case t: Throwable => println("photo error page:" + no); t.printStackTrace()
    }
  }

  private def getGeo(p: Photo) = {
    println("start get geo id " + p.id)
    try {
      val u = url("https://api.flickr.com/services/rest?method=flickr.photos.geo.getLocation&api_key=c669c93c32fed467c39929c71a793849&photo_id=" + p.id + "&format=json&nojsoncallback=1")
      val r = Http(u as_str)
      FlickrPhoto.make(p, parse(r).extract[GeoPack].photo.location, getPhotoParam(p).get)
      println("finish get geo id " + p.id)
    } catch {
      case t: Throwable => println("geo error id:" + p.id)
    }
  }

  private def getGeos(ps: List[Photo]) = {
    ps.map(getLoc(_)).collect { case Some(t) => t }
  }

  private def getLoc(p: Photo) = {
    if (FlickrPhoto.where(_.photoID eqs p.id).exists) {
      None
    } else {
      val u = url("https://api.flickr.com/services/rest?method=flickr.photos.geo.getLocation&api_key=c669c93c32fed467c39929c71a793849&photo_id=" + p.id + "&format=json&nojsoncallback=1")
      try {
        Some(p, (parse(Http(u as_str)).extract[GeoPack].photo.location))
      } catch {
        case t: Throwable => None
      }
    }
  }

  def getPhotoParam(p: Photo) = {
    val f = new File(p.id + ".jpg")
    val o = new FileOutputStream(f)
    try {
      Http(url(makeURL(p)) >>> o)
      val s = Http((url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").POST.as(user, pass) <<* ("imgFile", f) as_str))
      Some(parse(s).extract[Images].images.head.labels)
    } catch {
      case t: Throwable => println("param error id:" + p.id); None
    } finally {
      o.close
      if (f.exists) { f.delete }
    }
  }

  def getPhotoParam(f: File) = {
    val s = Http((url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").POST.as(user, pass) <<* ("imgFile", f) as_str))
    if (f.exists) { f.delete }
    parse(s).extract[Images].images.head.labels
  }

  private def getParams(ts: List[(Photo, Location)]) = {
    var u = url("https://gateway.watsonplatform.net/visual-recognition-beta/api/v1/tag/recognize").POST.as(user, pass)
    ts.map(getImage(_)).foreach { x => u = u <<* (x.id, new File(x.id + ".jpg")) }
    merge(ts.sortWith((x, y) => x._1.id.toLong < y._1.id.toLong), parse(Http(u as_str)).extract[Images].images.sortWith((x, y) => x.image_name.replaceAll(".jpg", "").toLong < y.image_name.replaceAll(".jpg", "").toLong)).foreach { x => FlickrPhoto.make(x._1, x._2, x._3); (new File(x._1.id + ".jpg")).delete }
    //    val is = parse(Http(u as_str)).extract[Images].images
    //    println("")
  }

  private def getImage(t: (Photo, Location)) = {
    val o = new FileOutputStream(t._1.id + ".jpg")
    Http(url(makeURL(t._1)) >>> o)
    o.close
    t._1
  }

  private def makeURL(p: Photo) = {
    "https://farm" + p.farm + ".staticflickr.com/" + p.server + "/" + p.id + "_" + p.secret + ".jpg"
  }

  private def merge(in1: List[(Photo, Location)], in2: List[Image]): List[(Photo, Location, List[LabelScore])] = {
    println("asize:" + in1.size + ",bsize:" + in2.size)
    @tailrec
    def rec(a: List[(Photo, Location)], b: List[Image], o: List[(Photo, Location, List[LabelScore])]): List[(Photo, Location, List[LabelScore])] = {
      a match {
        case h :: t =>
          println(h._1.id + "," + b.head.image_name); rec(t, b.tail, (h._1, h._2, b.head.labels) :: o)
        case Nil => o
      }
    }
    rec(in1, in2, Nil)
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
case class Images(images: List[Image])
case class Image(image_id: String, image_name: String, labels: List[LabelScore])
case class LabelScore(label_name: String, label_score: String)

case class RecomendList(recomends: List[Recomend])
case class Recomend(url: String, latitude: Double, longitude: Double)