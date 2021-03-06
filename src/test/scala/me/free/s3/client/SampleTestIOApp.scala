package me.free.s3.client

import cats.effect.{ContextShift, IO}
import org.scalatest.featurespec.AnyFeatureSpec
import cats.effect._
import cats.syntax.all._
import cats.effect._
import cats.effect.internals.{IOAppPlatform, IOContextShift, PoolUtils}
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AsyncFreeSpec
import software.amazon.awssdk.auth.credentials.{
  AwsBasicCredentials,
  AwsCredentials,
  AwsCredentialsProvider,
  StaticCredentialsProvider
}
import software.amazon.awssdk.regions.Region

import java.nio.file.Paths
object SampleTestIOApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = IO {

    val s3: S3[IO] = implicitly[S3[IO]](
      ClientConfig(
        StaticCredentialsProvider.create(
          AwsBasicCredentials.create("api-key", "secret-key")
        ),
        region = Region.US_WEST_1
      )
    )
    for {
      // Create the bucket
      createBucketResponse <- s3.createBucket("s3-bucket-name")(contextShift)
      _                    <- IO(println(createBucketResponse))
      // Upload the file
      putObjectResponse <- s3.putObject("s3-bucket-name", "key-file", "file.txt")
      _                 <- IO(println(putObjectResponse))
      // Delete the file
      deleteObjectResponse <- s3.deleteObject("s3-bucket-name", "file.txt")
      _                    <- IO(println(deleteObjectResponse))
      // Delete the bucket
      deleteBucketResponse <- s3.deleteBucket("s3-bucket-name")
      _                    <- IO(println(deleteBucketResponse))
      // List all buckets
      resp <- s3.listBuckets
    } yield resp
  }.flatMap(_ => IO.pure(ExitCode.Success))
}
