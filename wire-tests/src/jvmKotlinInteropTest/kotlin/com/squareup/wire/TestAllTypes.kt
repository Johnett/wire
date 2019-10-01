/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.wire

import com.squareup.wire.protos.kotlin.alltypes.AllTypes
import com.squareup.wire.protos.kotlin.alltypes.AllTypes.NestedEnum.A
import okio.Buffer
import okio.ByteString
import okio.ByteString.Companion.toByteString
import okio.ForwardingSource
import okio.Source
import okio.buffer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class TestAllTypes {

  private val builder: AllTypes.Builder
    get() = builder()

  private val allTypes = createAllTypes()
  private val adapter = AllTypes.ADAPTER

  // Return a two-element list with a given repeated value
  private fun <T> list(x: T): List<T> = listOf(x, x)

  private fun builder(): AllTypes.Builder {
    val bytes = ByteString.of(125.toByte(), 225.toByte())
    val nestedMessage = AllTypes.NestedMessage.Builder().a(999).build()
    return AllTypes.Builder()
        .opt_int32(111)
        .opt_uint32(112)
        .opt_sint32(113)
        .opt_fixed32(114)
        .opt_sfixed32(115)
        .opt_int64(116L)
        .opt_uint64(117L)
        .opt_sint64(118L)
        .opt_fixed64(119L)
        .opt_sfixed64(120L)
        .opt_bool(true)
        .opt_float(122.0f)
        .opt_double(123.0)
        .opt_string("124")
        .opt_bytes(bytes)
        .opt_nested_enum(A)
        .opt_nested_message(nestedMessage)
        .req_int32(111)
        .req_uint32(112)
        .req_sint32(113)
        .req_fixed32(114)
        .req_sfixed32(115)
        .req_int64(116L)
        .req_uint64(117L)
        .req_sint64(118L)
        .req_fixed64(119L)
        .req_sfixed64(120L)
        .req_bool(true)
        .req_float(122.0f)
        .req_double(123.0)
        .req_string("124")
        .req_bytes(bytes)
        .req_nested_enum(A)
        .req_nested_message(nestedMessage)
        .rep_int32(list(111))
        .rep_uint32(list(112))
        .rep_sint32(list(113))
        .rep_fixed32(list(114))
        .rep_sfixed32(list(115))
        .rep_int64(list(116L))
        .rep_uint64(list(117L))
        .rep_sint64(list(118L))
        .rep_fixed64(list(119L))
        .rep_sfixed64(list(120L))
        .rep_bool(list(true))
        .rep_float(list(122.0f))
        .rep_double(list(123.0))
        .rep_string(list("124"))
        .rep_bytes(list(bytes))
        .rep_nested_enum(list(A))
        .rep_nested_message(list(nestedMessage))
        .pack_int32(list(111))
        .pack_uint32(list(112))
        .pack_sint32(list(113))
        .pack_fixed32(list(114))
        .pack_sfixed32(list(115))
        .pack_int64(list(116L))
        .pack_uint64(list(117L))
        .pack_sint64(list(118L))
        .pack_fixed64(list(119L))
        .pack_sfixed64(list(120L))
        .pack_bool(list(true))
        .pack_float(list(122.0f))
        .pack_double(list(123.0))
        .pack_nested_enum(list(A))
        .ext_opt_bool(true)
        .ext_rep_bool(list(true))
        .ext_pack_bool(list(true))
  }

  private fun createAllTypes(): AllTypes = builder.build()

  @Test
  fun testBuilder() {
    val builder = builder
    val nestedMessage = AllTypes.NestedMessage.Builder().a(999).build()

    assertThat(builder.opt_int32).isEqualTo(111)
    assertThat(builder.opt_uint32).isEqualTo(112)
    assertThat(builder.opt_sint32).isEqualTo(113)
    assertThat(builder.opt_fixed32).isEqualTo(114)
    assertThat(builder.opt_sfixed32).isEqualTo(115)
    assertThat(builder.opt_int64).isEqualTo(116L)
    assertThat(builder.opt_uint64).isEqualTo(117L)
    assertThat(builder.opt_sint64).isEqualTo(118L)
    assertThat(builder.opt_fixed64).isEqualTo(119L)
    assertThat(builder.opt_sfixed64).isEqualTo(120L)
    assertThat(builder.opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat(builder.opt_float).isEqualTo(122.0f)
    assertThat(builder.opt_double).isEqualTo(123.0)
    assertThat(builder.opt_string).isEqualTo("124")
    assertThat(builder.opt_bytes).isEqualTo(ByteString.of(125.toByte(), 225.toByte()))
    assertThat(builder.opt_nested_enum).isEqualTo(A)
    assertThat(builder.opt_nested_message).isEqualTo(nestedMessage)

    assertThat(builder.req_int32).isEqualTo(111)
    assertThat(builder.req_uint32).isEqualTo(112)
    assertThat(builder.req_sint32).isEqualTo(113)
    assertThat(builder.req_fixed32).isEqualTo(114)
    assertThat(builder.req_sfixed32).isEqualTo(115)
    assertThat(builder.req_int64).isEqualTo(116L)
    assertThat(builder.req_uint64).isEqualTo(117L)
    assertThat(builder.req_sint64).isEqualTo(118L)
    assertThat(builder.req_fixed64).isEqualTo(119L)
    assertThat(builder.req_sfixed64).isEqualTo(120L)
    assertThat(builder.req_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat(builder.req_float).isEqualTo(122.0f)
    assertThat(builder.req_double).isEqualTo(123.0)
    assertThat(builder.req_string).isEqualTo("124")
    assertThat(builder.req_bytes).isEqualTo(ByteString.of(125.toByte(), 225.toByte()))
    assertThat(builder.req_nested_enum).isEqualTo(A)
    assertThat(builder.req_nested_message).isEqualTo(nestedMessage)

    assertThat(builder.rep_int32).hasSize(2)
    assertThat(builder.rep_int32[0]).isEqualTo(111)
    assertThat(builder.rep_int32[1]).isEqualTo(111)
    assertThat(builder.rep_uint32).hasSize(2)
    assertThat(builder.rep_uint32[0]).isEqualTo(112)
    assertThat(builder.rep_uint32[1]).isEqualTo(112)
    assertThat(builder.rep_sint32).hasSize(2)
    assertThat(builder.rep_sint32[0]).isEqualTo(113)
    assertThat(builder.rep_sint32[1]).isEqualTo(113)
    assertThat(builder.rep_fixed32).hasSize(2)
    assertThat(builder.rep_fixed32[0]).isEqualTo(114)
    assertThat(builder.rep_fixed32[1]).isEqualTo(114)
    assertThat(builder.rep_sfixed32).hasSize(2)
    assertThat(builder.rep_sfixed32[0]).isEqualTo(115)
    assertThat(builder.rep_sfixed32[1]).isEqualTo(115)
    assertThat(builder.rep_int64).hasSize(2)
    assertThat(builder.rep_int64[0]).isEqualTo(116L)
    assertThat(builder.rep_int64[1]).isEqualTo(116L)
    assertThat(builder.rep_uint64).hasSize(2)
    assertThat(builder.rep_uint64[0]).isEqualTo(117L)
    assertThat(builder.rep_uint64[1]).isEqualTo(117L)
    assertThat(builder.rep_sint64).hasSize(2)
    assertThat(builder.rep_sint64[0]).isEqualTo(118L)
    assertThat(builder.rep_sint64[1]).isEqualTo(118L)
    assertThat(builder.rep_fixed64).hasSize(2)
    assertThat(builder.rep_fixed64[0]).isEqualTo(119L)
    assertThat(builder.rep_fixed64[1]).isEqualTo(119L)
    assertThat(builder.rep_sfixed64).hasSize(2)
    assertThat(builder.rep_sfixed64[0]).isEqualTo(120L)
    assertThat(builder.rep_sfixed64[1]).isEqualTo(120L)
    assertThat(builder.rep_bool).hasSize(2)
    assertThat(builder.rep_bool[0]).isTrue()
    assertThat(builder.rep_bool[1]).isTrue()
    assertThat(builder.rep_float).hasSize(2)
    assertThat(builder.rep_float[0]).isEqualTo(122.0f)
    assertThat(builder.rep_float[1]).isEqualTo(122.0f)
    assertThat(builder.rep_double).hasSize(2)
    assertThat(builder.rep_double[0]).isEqualTo(123.0)
    assertThat(builder.rep_double[1]).isEqualTo(123.0)
    assertThat(builder.rep_string).hasSize(2)
    assertThat(builder.rep_string[0]).isEqualTo("124")
    assertThat(builder.rep_string[1]).isEqualTo("124")
    assertThat(builder.rep_bytes).hasSize(2)
    assertThat(builder.rep_bytes[0]).isEqualTo(ByteString.of(125.toByte(), 225.toByte()))
    assertThat(builder.rep_bytes[1]).isEqualTo(ByteString.of(125.toByte(), 225.toByte()))
    assertThat(builder.rep_nested_enum).hasSize(2)
    assertThat(builder.rep_nested_enum[0]).isEqualTo(A)
    assertThat(builder.rep_nested_enum[1]).isEqualTo(A)
    assertThat(builder.rep_nested_message).hasSize(2)
    assertThat(builder.rep_nested_message[0]).isEqualTo(nestedMessage)
    assertThat(builder.rep_nested_message[1]).isEqualTo(nestedMessage)

    assertThat(builder.pack_int32).hasSize(2)
    assertThat(builder.pack_int32[0]).isEqualTo(111)
    assertThat(builder.pack_int32[1]).isEqualTo(111)
    assertThat(builder.pack_uint32).hasSize(2)
    assertThat(builder.pack_uint32[0]).isEqualTo(112)
    assertThat(builder.pack_uint32[1]).isEqualTo(112)
    assertThat(builder.pack_sint32).hasSize(2)
    assertThat(builder.pack_sint32[0]).isEqualTo(113)
    assertThat(builder.pack_sint32[1]).isEqualTo(113)
    assertThat(builder.pack_fixed32).hasSize(2)
    assertThat(builder.pack_fixed32[0]).isEqualTo(114)
    assertThat(builder.pack_fixed32[1]).isEqualTo(114)
    assertThat(builder.pack_sfixed32).hasSize(2)
    assertThat(builder.pack_sfixed32[0]).isEqualTo(115)
    assertThat(builder.pack_sfixed32[1]).isEqualTo(115)
    assertThat(builder.pack_int64).hasSize(2)
    assertThat(builder.pack_int64[0]).isEqualTo(116L)
    assertThat(builder.pack_int64[1]).isEqualTo(116L)
    assertThat(builder.pack_uint64).hasSize(2)
    assertThat(builder.pack_uint64[0]).isEqualTo(117L)
    assertThat(builder.pack_uint64[1]).isEqualTo(117L)
    assertThat(builder.pack_sint64).hasSize(2)
    assertThat(builder.pack_sint64[0]).isEqualTo(118L)
    assertThat(builder.pack_sint64[1]).isEqualTo(118L)
    assertThat(builder.pack_fixed64).hasSize(2)
    assertThat(builder.pack_fixed64[0]).isEqualTo(119L)
    assertThat(builder.pack_fixed64[1]).isEqualTo(119L)
    assertThat(builder.pack_sfixed64).hasSize(2)
    assertThat(builder.pack_sfixed64[0]).isEqualTo(120L)
    assertThat(builder.pack_sfixed64[1]).isEqualTo(120L)
    assertThat(builder.pack_bool).hasSize(2)
    assertThat(builder.pack_bool[0]).isTrue()
    assertThat(builder.pack_bool[1]).isTrue()
    assertThat(builder.pack_float).hasSize(2)
    assertThat(builder.pack_float[0]).isEqualTo(122.0f)
    assertThat(builder.pack_float[1]).isEqualTo(122.0f)
    assertThat(builder.pack_double).hasSize(2)
    assertThat(builder.pack_double[0]).isEqualTo(123.0)
    assertThat(builder.pack_double[1]).isEqualTo(123.0)
    assertThat(builder.pack_nested_enum).hasSize(2)
    assertThat(builder.pack_nested_enum[0]).isEqualTo(A)
    assertThat(builder.pack_nested_enum[1]).isEqualTo(A)

    assertThat(builder.ext_opt_bool).isTrue()
    assertThat(builder.ext_rep_bool).isEqualTo(list(true))
    assertThat(builder.ext_pack_bool).isEqualTo(list(true))

    builder.ext_opt_bool(false)
    builder.ext_rep_bool(list(false))
    builder.ext_pack_bool(list(false))

    assertThat(builder.ext_opt_bool).isFalse()
    assertThat(builder.ext_rep_bool).isEqualTo(list(false))
    assertThat(builder.ext_pack_bool).isEqualTo(list(false))
  }

  @Test
  fun testInitBuilder() {
    val builder = allTypes.newBuilder()
    assertThat(builder.build()).isEqualTo(allTypes)
    builder.opt_bool = false
    assertThat(builder.build()).isNotSameAs(allTypes)
  }

  @Test
  fun testWriteStream() {
    val stream = ByteArrayOutputStream()
    adapter.encode(stream, allTypes)
    val output = stream.toByteArray()
    assertThat(output.size).isEqualTo(expectedOutput.size)
    assertThat(output.toByteString()).isEqualTo(expectedOutput)
  }

  @Test
  fun testReadStream() {
    val data = adapter.encode(allTypes)
    val stream = ByteArrayInputStream(data)

    val parsed = adapter.decode(stream)
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isTrue()
    assertThat(allTypes.ext_rep_bool).isEqualTo(list(true))
    assertThat(allTypes.ext_pack_bool).isEqualTo(list(true))
  }

  /** A source that returns 1, 2, 3, or 4 bytes at a time. */
  private class SlowSource(delegate: Source) : ForwardingSource(delegate) {
    private var pos: Long = 0

    override fun read(sink: Buffer, byteCount: Long): Long {
      val bytesToReturn = byteCount.coerceAtMost(pos % 4 + 1)
      pos += bytesToReturn
      return super.read(sink, byteCount)
    }
  }

  @Test
  fun testReadFromSlowSource() {
    val data = adapter.encode(allTypes)

    val input = SlowSource(Buffer().write(data))
    val parsed = adapter.decode(input.buffer())
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isTrue()
    assertThat(allTypes.ext_rep_bool).isEqualTo(list(true))
    assertThat(allTypes.ext_pack_bool).isEqualTo(list(true))
  }

  @Test
  fun testDefaults() {
    assertThat(String(AllTypes.DEFAULT_DEFAULT_BYTES.toByteArray(), Charsets.ISO_8859_1)).isEqualTo(
        "çok\u0007\b\\f\n\r\t\u000b\u0001\u0001\u0001\u000f\u000f~\u0001\u0001\u0011\u0001\u0001\u0011güzel\u0011güzel")
  }

  @Test
  fun testUnknownFields() {
    val builder = builder
    builder.addUnknownField(10000, FieldEncoding.VARINT, 1L)
    val withUnknownField = builder.build()
    val data = adapter.encode(withUnknownField)
    val count = expectedOutput.size
    assertThat(data.size).isEqualTo(count + 4)
    assertThat(data[count]).isEqualTo(0x80.toByte())
    assertThat(data[count + 1]).isEqualTo(0xf1.toByte())
    assertThat(data[count + 2]).isEqualTo(0x04.toByte())
    assertThat(data[count + 3]).isEqualTo(0x01.toByte())
  }
}
