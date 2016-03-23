package io.mediachain.translation

import io.mediachain.Types._
import io.mediachain.XorMatchers
import org.json4s.JObject
import org.specs2.Specification
import tate.{TateLoader, TateTranslator}
import org.json4s.jackson.JsonMethods._

import scala.io.Source

object TateTranslatorSpec extends Specification with XorMatchers {

  def is = skipAllUnless(SpecResources.Tate.sampleDataExists) ^
  s2"""
       $loadsArtwork - Translates Tate artwork json into PhotoBlob
    """

  def loadsArtwork = {
    val expected = SpecResources.Tate.SampleArtworkA00001

    if (!expected.jsonFile.exists) {
      ok(s"Skipping artwork test for ${expected.jsonFile.getPath}. File does not exist")
    } else {
      val source: String = Source.fromFile(expected.jsonFile).mkString
      val json: JObject = parse(source).asInstanceOf[JObject] // have faith

      val translated = TateTranslator.translate(json)

      def matchBundleElement(expected: (BundleKey, MetadataBlob)) = expected.zip(===, ===)

      translated must beRightXor { blobBundle: BlobBundle =>
        blobBundle must contain(
          matchBundleElement((BundleKey.Author, Person(None, expected.artistName)))
        ).exactly(1)
        blobBundle must contain(
          matchBundleElement((BundleKey.Self, PhotoBlob(None, expected.title, expected.medium, expected.dateText)))
        ).exactly(1)
      }
    }
  }
}

