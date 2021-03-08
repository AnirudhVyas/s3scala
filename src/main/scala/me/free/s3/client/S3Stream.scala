package me.free.s3.client

import cats.effect.ContextShift
import monix.reactive.Observable
import software.amazon.awssdk.services.s3.model.{
  CreateBucketResponse,
  DeleteBucketResponse,
  DeleteObjectResponse,
  ListBucketsResponse,
  PutObjectResponse
}

final case class BucketObjectEntry(name: String, key: String, filePath: String)
trait S3StreamReader[F[_]] {
  def listObjects(bucketNames: Observable[String])
  def listBucketsStream: F[ListBucketsResponse]
}
trait S3StreamWriter[F[_]] {
  def putObjects(stream: F[BucketObjectEntry])(implicit cs: ContextShift[F]): F[PutObjectResponse]
  def deleteObjects(stream: F[Bucket])(implicit cs: ContextShift[F]): F[DeleteObjectResponse]
}
