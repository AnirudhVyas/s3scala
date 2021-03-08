package me.free.s3.client
import cats.effect.{ContextShift, IO}
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model._

import java.nio.file.Paths
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final case class ClientConfig(awsCredentialsProvider: AwsCredentialsProvider, region: Region)
final case class Bucket(name: String, region: String)

trait S3[F[_]] {
  def createBucket(name: String)(implicit cs: ContextShift[F]): F[CreateBucketResponse]
  def deleteBucket(name: String)(implicit cs: ContextShift[F]): F[DeleteBucketResponse]
  def putObject(name: String, key: String, filePath: String)(implicit cs: ContextShift[F]): F[PutObjectResponse]
  def deleteObject(bucketName: String, key: String)(implicit cs: ContextShift[F]): F[DeleteObjectResponse]
  def listObjects(bucketName: String)(implicit cs: ContextShift[F]): F[ListObjectsResponse]
  def listBuckets(implicit cs: ContextShift[F]): F[ListBucketsResponse]
}

object S3 {

  implicit class S3Impl(cfg: ClientConfig) extends S3[IO] {
    lazy val s3AsyncClient: IO[S3AsyncClient] = IO(
      S3AsyncClient
        .builder()
        .region(cfg.region)
        .credentialsProvider(cfg.awsCredentialsProvider)
        .build()
    )
    override def createBucket(name: String)(implicit cs: ContextShift[IO]): IO[CreateBucketResponse] = IO.fromFuture {
      s3AsyncClient.flatMap { client =>
        IO {
          Future[CreateBucketResponse] {
            val cancellableF = client.createBucket(CreateBucketRequest.builder().bucket(name).build())
            cancellableF.get()
          }
        }
      }
    }

    override def deleteBucket(name: String)(implicit cs: ContextShift[IO]): IO[DeleteBucketResponse] = IO.fromFuture {
      s3AsyncClient.flatMap { client =>
        IO {
          Future[DeleteBucketResponse] {
            val cancellableF = client.deleteBucket(DeleteBucketRequest.builder().bucket(name).build())
            cancellableF.get()
          }
        }
      }
    }

    override def putObject(name: String, key: String, filePath: String)(implicit
        cs: ContextShift[IO]
    ): IO[PutObjectResponse] = IO.fromFuture {
      s3AsyncClient.flatMap { client =>
        IO {
          Future[PutObjectResponse] {
            val cancellableF =
              client.putObject(PutObjectRequest.builder().bucket(name).key(key).build(), Paths.get(filePath))
            cancellableF.get()
          }
        }
      }
    }

    override def deleteObject(bucketName: String, key: String)(implicit
        cs: ContextShift[IO]
    ): IO[DeleteObjectResponse] = IO.fromFuture {
      s3AsyncClient.flatMap { client =>
        IO {
          Future[DeleteObjectResponse] {
            val cancellableF =
              client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build())
            cancellableF.get()
          }
        }
      }
    }

    override def listObjects(bucketName: String)(implicit cs: ContextShift[IO]): IO[ListObjectsResponse] =
      IO.fromFuture {
        s3AsyncClient.flatMap { client =>
          IO {
            Future[ListObjectsResponse] {
              val cancellableF =
                client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build())
              cancellableF.get()
            }
          }
        }
      }

    override def listBuckets(implicit cs: ContextShift[IO]): IO[ListBucketsResponse] =
      IO.fromFuture {
        s3AsyncClient.flatMap { client =>
          IO {
            Future[ListBucketsResponse] {
              val cancellableF =
                client.listBuckets(ListBucketsRequest.builder().build())
              cancellableF.get()
            }
          }
        }
      }
  }
}
