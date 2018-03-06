package io.grpc.testing.integration.test

import java.io.InputStream

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.google.protobuf.ByteString
import com.google.protobuf.empty.Empty
import io.grpc.testing.integration.messages._
import io.grpc.testing.integration2.{ ChannelBuilder, ClientTester, Settings }
import io.grpc.{ ManagedChannel, Status, StatusRuntimeException }
import org.junit.Assert._

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContext }
import scala.util.Failure
import scala.util.control.NonFatal

class AkkaGrpcClientTester(val settings: Settings)(implicit mat: Materializer, ex: ExecutionContext) extends ClientTester {

  private var channel: ManagedChannel = null
  private var client: TestServiceServiceClient = null

  private val awaitTimeout = 3.seconds

  def createChannel(): ManagedChannel = ChannelBuilder.buildChannel(settings)

  def setUp(): Unit = {
    channel = createChannel()
    client = TestServiceServiceClient(channel)
  }

  def tearDown(): Unit = {
    if (channel != null) channel.shutdown()
  }

  def emptyUnary(): Unit = {
    assertEquals(Empty(), Await.result(client.emptyCall(Empty()), awaitTimeout))
  }

  def cacheableUnary(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def largeUnary(): Unit = {
    val request =
      SimpleRequest(
        PayloadType.COMPRESSABLE,
        responseSize = 314159,
        payload = Option(Payload(body = ByteString.copyFrom(new Array[Byte](271828)))))

    val expectedResponse = SimpleResponse(payload = Option(Payload(body = ByteString.copyFrom(new Array[Byte](314159)))))

    val response = Await.result(client.unaryCall(request), awaitTimeout)
    assertEquals(expectedResponse, response)
  }

  def clientCompressedUnary(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def serverCompressedUnary(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def clientStreaming(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def clientCompressedStreaming(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def serverStreaming(): Unit = {

    val request =
      StreamingOutputCallRequest(
        responseType = PayloadType.COMPRESSABLE,
        responseParameters = Seq(
          ResponseParameters(31415),
          ResponseParameters(9),
          ResponseParameters(2653),
          ResponseParameters(58979)))

    val expected: Seq[StreamingOutputCallResponse] = Seq(
      StreamingOutputCallResponse(
        Option(Payload(body = ByteString.copyFrom(new Array[Byte](31415))))),
      StreamingOutputCallResponse(
        Option(Payload(body = ByteString.copyFrom(new Array[Byte](9))))),
      StreamingOutputCallResponse(
        Option(Payload(body = ByteString.copyFrom(new Array[Byte](2653))))),
      StreamingOutputCallResponse(
        Option(Payload(body = ByteString.copyFrom(new Array[Byte](58979))))))

    val actual = Await.result(client.streamingOutputCall(request).runWith(Sink.seq), awaitTimeout)

    assertEquals(expected.size, actual.size)
    expected.zip(actual).foreach {
      case (exp, act) => assertEquals(exp, act)
    }
  }

  def serverCompressedStreaming(): Unit = {
    val request =
      StreamingOutputCallRequest(
        responseType = PayloadType.COMPRESSABLE,
        responseParameters = Seq(
          ResponseParameters(size = 31415, compressed = Some(true)),
          ResponseParameters(size = 92653, compressed = Some(true))))

    val expected: Seq[StreamingOutputCallResponse] = Seq(
      StreamingOutputCallResponse(
        Option(Payload(body = ByteString.copyFrom(new Array[Byte](31415))))),
      StreamingOutputCallResponse(
        Option(Payload(body = ByteString.copyFrom(new Array[Byte](92653))))))

    val actual = Await.result(client.streamingOutputCall(request).runWith(Sink.seq), awaitTimeout)

    assertEquals(expected.size, actual.size)
    expected.zip(actual).foreach {
      case (exp, act) => assertEquals(exp, act)
    }
  }

  def pingPong(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def emptyStream(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def computeEngineCreds(serviceAccount: String, oauthScope: String): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def serviceAccountCreds(jsonKey: String, credentialsStream: InputStream, authScope: String): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def jwtTokenCreds(serviceAccountJson: InputStream): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def oauth2AuthToken(jsonKey: String, credentialsStream: InputStream, authScope: String): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def perRpcCreds(jsonKey: String, credentialsStream: InputStream, oauthScope: String): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def customMetadata(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def statusCodeAndMessage(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def unimplementedMethod(): Unit = {
    Await.ready(client.unimplementedCall(Empty()), awaitTimeout)
      .onComplete {
        case Failure(e: StatusRuntimeException) =>
          assertEquals(Status.UNIMPLEMENTED.getCode, e.getStatus.getCode)
        case _ => fail(s"Expected to fail with UNIMPLEMENTED")
      }
  }

  def unimplementedService(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def cancelAfterBegin(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def cancelAfterFirstResponse(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

  def timeoutOnSleepingServer(): Unit = {
    throw new RuntimeException("Not implemented!")
  }

}
