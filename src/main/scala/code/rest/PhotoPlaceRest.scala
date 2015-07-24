package code.rest

import net.liftweb.http.rest.RestHelper
import net.liftweb.http._
import net.liftweb.util.Helpers._
import code.model._
import net.liftweb.json.JsonDSL._
import com.foursquare.rogue.LiftRogue._
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import java.util.Date
import java.io.FileOutputStream
import java.io.File
import net.liftweb.util.Props
import code.lib._

object PhotoPlaceRest extends RestHelper {
  serve({
    case "api" :: "byfile" :: _ Post req => uploadFile(req)
  })

  def uploadFile(req: Req): LiftResponse = {
    req.uploadedFiles match {
      case FileParamHolder(_, mime, fileName, data) :: Nil => {
        val f = new File(fileName)
        val fos = new FileOutputStream(f)
        fos.write(data)
        fos.close()
        JsonResponse(decompose(HttpDispatcherClassic.makeRecomends(f)))
      }
      case _ => BadResponse()
    }
  }
}

