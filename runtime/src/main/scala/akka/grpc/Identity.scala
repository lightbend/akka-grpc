/*
 * Copyright (C) 2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc

import akka.util.ByteString

object Identity extends Codec {
  override val name = "identity"

  override def compress(bytes: ByteString): ByteString = bytes

  override def uncompress(bytes: ByteString): ByteString = bytes
}
