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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.Arrays
import okio.Buffer
import okio.ByteString
import okio.ForwardingSource
import okio.Source
import okio.buffer
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail

class TestAllTypes {

  private val builder: AllTypes.Builder
    get() = getBuilder(2)

  private val allTypes = createAllTypes()
  private val adapter = AllTypes.ADAPTER

  // Return a two-element list with a given repeated value
  private fun <T> list(x: T): List<T> {
    return list(x, 2)
  }

  private fun <T> list(x: T, numRepeated: Int): List<T> {
    val data = ArrayList<T>(numRepeated)
    for (i in 0 until numRepeated) {
      data.add(x)
    }
    return data
  }

  private fun getBuilder(numRepeated: Int): AllTypes.Builder {
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
        .rep_int32(list(111, numRepeated))
        .rep_uint32(list(112, numRepeated))
        .rep_sint32(list(113, numRepeated))
        .rep_fixed32(list(114, numRepeated))
        .rep_sfixed32(list(115, numRepeated))
        .rep_int64(list(116L, numRepeated))
        .rep_uint64(list(117L, numRepeated))
        .rep_sint64(list(118L, numRepeated))
        .rep_fixed64(list(119L, numRepeated))
        .rep_sfixed64(list(120L, numRepeated))
        .rep_bool(list(true, numRepeated))
        .rep_float(list(122.0f, numRepeated))
        .rep_double(list(123.0, numRepeated))
        .rep_string(list("124", numRepeated))
        .rep_bytes(list(bytes, numRepeated))
        .rep_nested_enum(list(A, numRepeated))
        .rep_nested_message(list(nestedMessage, numRepeated))
        .pack_int32(list(111, numRepeated))
        .pack_uint32(list(112, numRepeated))
        .pack_sint32(list(113, numRepeated))
        .pack_fixed32(list(114, numRepeated))
        .pack_sfixed32(list(115, numRepeated))
        .pack_int64(list(116L, numRepeated))
        .pack_uint64(list(117L, numRepeated))
        .pack_sint64(list(118L, numRepeated))
        .pack_fixed64(list(119L, numRepeated))
        .pack_sfixed64(list(120L, numRepeated))
        .pack_bool(list(true, numRepeated))
        .pack_float(list(122.0f, numRepeated))
        .pack_double(list(123.0, numRepeated))
        .pack_nested_enum(list(A, numRepeated))
        .ext_opt_bool(true)
        .ext_rep_bool(list(true, numRepeated))
        .ext_pack_bool(list(true, numRepeated))
  }

  private fun createAllTypes(numRepeated: Int): AllTypes {
    return getBuilder(numRepeated).build()
  }

  private fun createAllTypes(): AllTypes {
    return builder.build()
  }

  @Test
  fun testHashCodes() {
    val builder = builder
    val message = builder.build()
    val messageHashCode = message.hashCode()
    assertThat(messageHashCode).isEqualTo(allTypes.hashCode())
  }

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

    assertThat<Int>(builder.rep_int32).hasSize(2)
    assertThat(builder.rep_int32.get(0)).isEqualTo(111)
    assertThat(builder.rep_int32.get(1)).isEqualTo(111)
    assertThat<Int>(builder.rep_uint32).hasSize(2)
    assertThat(builder.rep_uint32.get(0)).isEqualTo(112)
    assertThat(builder.rep_uint32.get(1)).isEqualTo(112)
    assertThat<Int>(builder.rep_sint32).hasSize(2)
    assertThat(builder.rep_sint32.get(0)).isEqualTo(113)
    assertThat(builder.rep_sint32.get(1)).isEqualTo(113)
    assertThat<Int>(builder.rep_fixed32).hasSize(2)
    assertThat(builder.rep_fixed32.get(0)).isEqualTo(114)
    assertThat(builder.rep_fixed32.get(1)).isEqualTo(114)
    assertThat<Int>(builder.rep_sfixed32).hasSize(2)
    assertThat(builder.rep_sfixed32.get(0)).isEqualTo(115)
    assertThat(builder.rep_sfixed32.get(1)).isEqualTo(115)
    assertThat<Long>(builder.rep_int64).hasSize(2)
    assertThat(builder.rep_int64.get(0)).isEqualTo(116L)
    assertThat(builder.rep_int64.get(1)).isEqualTo(116L)
    assertThat<Long>(builder.rep_uint64).hasSize(2)
    assertThat(builder.rep_uint64.get(0)).isEqualTo(117L)
    assertThat(builder.rep_uint64.get(1)).isEqualTo(117L)
    assertThat<Long>(builder.rep_sint64).hasSize(2)
    assertThat(builder.rep_sint64.get(0)).isEqualTo(118L)
    assertThat(builder.rep_sint64.get(1)).isEqualTo(118L)
    assertThat<Long>(builder.rep_fixed64).hasSize(2)
    assertThat(builder.rep_fixed64.get(0)).isEqualTo(119L)
    assertThat(builder.rep_fixed64.get(1)).isEqualTo(119L)
    assertThat<Long>(builder.rep_sfixed64).hasSize(2)
    assertThat(builder.rep_sfixed64.get(0)).isEqualTo(120L)
    assertThat(builder.rep_sfixed64.get(1)).isEqualTo(120L)
    assertThat<Boolean>(builder.rep_bool).hasSize(2)
    assertThat(builder.rep_bool.get(0)).isEqualTo(java.lang.Boolean.TRUE)
    assertThat(builder.rep_bool.get(1)).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Float>(builder.rep_float).hasSize(2)
    assertThat(builder.rep_float.get(0)).isEqualTo(122.0f)
    assertThat(builder.rep_float.get(1)).isEqualTo(122.0f)
    assertThat<Double>(builder.rep_double).hasSize(2)
    assertThat(builder.rep_double.get(0)).isEqualTo(123.0)
    assertThat(builder.rep_double.get(1)).isEqualTo(123.0)
    assertThat<String>(builder.rep_string).hasSize(2)
    assertThat(builder.rep_string.get(0)).isEqualTo("124")
    assertThat(builder.rep_string.get(1)).isEqualTo("124")
    assertThat(builder.rep_bytes).hasSize(2)
    assertThat(builder.rep_bytes.get(0)).isEqualTo(
        ByteString.of(125.toByte(), 225.toByte()))
    assertThat(builder.rep_bytes.get(1)).isEqualTo(
        ByteString.of(125.toByte(), 225.toByte()))
    assertThat(builder.rep_nested_enum).hasSize(2)
    assertThat(builder.rep_nested_enum.get(0)).isEqualTo(A)
    assertThat(builder.rep_nested_enum.get(1)).isEqualTo(A)
    assertThat(builder.rep_nested_message).hasSize(2)
    assertThat(builder.rep_nested_message.get(0)).isEqualTo(nestedMessage)
    assertThat(builder.rep_nested_message.get(1)).isEqualTo(nestedMessage)

    assertThat<Int>(builder.pack_int32).hasSize(2)
    assertThat(builder.pack_int32.get(0)).isEqualTo(111)
    assertThat(builder.pack_int32.get(1)).isEqualTo(111)
    assertThat<Int>(builder.pack_uint32).hasSize(2)
    assertThat(builder.pack_uint32.get(0)).isEqualTo(112)
    assertThat(builder.pack_uint32.get(1)).isEqualTo(112)
    assertThat<Int>(builder.pack_sint32).hasSize(2)
    assertThat(builder.pack_sint32.get(0)).isEqualTo(113)
    assertThat(builder.pack_sint32.get(1)).isEqualTo(113)
    assertThat<Int>(builder.pack_fixed32).hasSize(2)
    assertThat(builder.pack_fixed32.get(0)).isEqualTo(114)
    assertThat(builder.pack_fixed32.get(1)).isEqualTo(114)
    assertThat<Int>(builder.pack_sfixed32).hasSize(2)
    assertThat(builder.pack_sfixed32.get(0)).isEqualTo(115)
    assertThat(builder.pack_sfixed32.get(1)).isEqualTo(115)
    assertThat<Long>(builder.pack_int64).hasSize(2)
    assertThat(builder.pack_int64.get(0)).isEqualTo(116L)
    assertThat(builder.pack_int64.get(1)).isEqualTo(116L)
    assertThat<Long>(builder.pack_uint64).hasSize(2)
    assertThat(builder.pack_uint64.get(0)).isEqualTo(117L)
    assertThat(builder.pack_uint64.get(1)).isEqualTo(117L)
    assertThat<Long>(builder.pack_sint64).hasSize(2)
    assertThat(builder.pack_sint64.get(0)).isEqualTo(118L)
    assertThat(builder.pack_sint64.get(1)).isEqualTo(118L)
    assertThat<Long>(builder.pack_fixed64).hasSize(2)
    assertThat(builder.pack_fixed64.get(0)).isEqualTo(119L)
    assertThat(builder.pack_fixed64.get(1)).isEqualTo(119L)
    assertThat<Long>(builder.pack_sfixed64).hasSize(2)
    assertThat(builder.pack_sfixed64.get(0)).isEqualTo(120L)
    assertThat(builder.pack_sfixed64.get(1)).isEqualTo(120L)
    assertThat<Boolean>(builder.pack_bool).hasSize(2)
    assertThat(builder.pack_bool.get(0)).isEqualTo(java.lang.Boolean.TRUE)
    assertThat(builder.pack_bool.get(1)).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Float>(builder.pack_float).hasSize(2)
    assertThat(builder.pack_float.get(0)).isEqualTo(122.0f)
    assertThat(builder.pack_float.get(1)).isEqualTo(122.0f)
    assertThat<Double>(builder.pack_double).hasSize(2)
    assertThat(builder.pack_double.get(0)).isEqualTo(123.0)
    assertThat(builder.pack_double.get(1)).isEqualTo(123.0)
    assertThat(builder.pack_nested_enum).hasSize(2)
    assertThat(builder.pack_nested_enum.get(0)).isEqualTo(A)
    assertThat(builder.pack_nested_enum.get(1)).isEqualTo(A)

    assertThat(builder.ext_opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Boolean>(builder.ext_rep_bool).isEqualTo(list(true))
    assertThat<Boolean>(builder.ext_pack_bool).isEqualTo(list(true))

    builder.ext_opt_bool(false)
    builder.ext_rep_bool(list(false))
    builder.ext_pack_bool(list(false))

    assertThat(builder.ext_opt_bool).isEqualTo(java.lang.Boolean.FALSE)
    assertThat<Boolean>(builder.ext_rep_bool).isEqualTo(list(false))
    assertThat<Boolean>(builder.ext_pack_bool).isEqualTo(list(false))
  }

  @Test
  fun testInitBuilder() {
    val builder = allTypes.newBuilder()
    assertThat(builder.build()).isEqualTo(allTypes)
    builder.opt_bool = false
    assertThat(builder.build()).isNotSameAs(allTypes)
  }

  @Test
  fun testWrite() {
    val output = adapter.encode(allTypes)
    assertThat(output.size).isEqualTo(TestAllTypesData.expectedOutput.size)
    assertThat(ByteString.of(*output)).isEqualTo(TestAllTypesData.expectedOutput)
  }

  @Test
  @Throws(IOException::class)
  fun testWriteSource() {
    val sink = Buffer()
    adapter.encode(sink, allTypes)
    assertThat(sink.readByteString()).isEqualTo(TestAllTypesData.expectedOutput)
  }

  @Test
  @Throws(IOException::class)
  fun testWriteBytes() {
    val output = adapter.encode(allTypes)
    assertThat(output.size).isEqualTo(TestAllTypesData.expectedOutput.size)
    assertThat(ByteString.of(*output)).isEqualTo(TestAllTypesData.expectedOutput)
  }

  @Test
  @Throws(IOException::class)
  fun testWriteStream() {
    val stream = ByteArrayOutputStream()
    adapter.encode(stream, allTypes)
    val output = stream.toByteArray()
    assertThat(output.size).isEqualTo(TestAllTypesData.expectedOutput.size)
    assertThat(ByteString.of(*output)).isEqualTo(TestAllTypesData.expectedOutput)
  }

  @Test
  @Throws(IOException::class)
  fun testReadSource() {
    val data = adapter.encode(allTypes)
    val input = Buffer().write(data)

    val parsed = adapter.decode(input)
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Boolean>(allTypes.ext_rep_bool).isEqualTo(list(true))
    assertThat<Boolean>(allTypes.ext_pack_bool).isEqualTo(list(true))
  }

  @Test
  @Throws(IOException::class)
  fun testReadBytes() {
    val data = adapter.encode(allTypes)

    val parsed = adapter.decode(data)
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Boolean>(allTypes.ext_rep_bool).isEqualTo(list(true))
    assertThat<Boolean>(allTypes.ext_pack_bool).isEqualTo(list(true))
  }

  @Test
  @Throws(IOException::class)
  fun testReadStream() {
    val data = adapter.encode(allTypes)
    val stream = ByteArrayInputStream(data)

    val parsed = adapter.decode(stream)
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Boolean>(allTypes.ext_rep_bool).isEqualTo(list(true))
    assertThat<Boolean>(allTypes.ext_pack_bool).isEqualTo(list(true))
  }

  @Test
  @Throws(IOException::class)
  fun testReadLongMessages() {
    val allTypes = createAllTypes(50)
    val data = adapter.encode(allTypes)

    val parsed = adapter.decode(data)
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Boolean>(allTypes.ext_rep_bool).isEqualTo(list(true, 50))
    assertThat<Boolean>(allTypes.ext_pack_bool).isEqualTo(list(true, 50))
  }

  /** A source that returns 1, 2, 3, or 4 bytes at a time.  */
  private class SlowSource internal constructor(delegate: Source) : ForwardingSource(delegate) {
    private var pos: Long = 0

    @Throws(IOException::class)
    override fun read(sink: Buffer, byteCount: Long): Long {
      val bytesToReturn = Math.min(byteCount, pos % 4 + 1)
      pos += bytesToReturn
      return super.read(sink, byteCount)
    }
  }

  @Test
  @Throws(IOException::class)
  fun testReadFromSlowSource() {
    val data = adapter.encode(allTypes)

    val input = SlowSource(Buffer().write(data))
    val parsed = adapter.decode(input.buffer())
    assertThat(parsed).isEqualTo(allTypes)

    assertThat(allTypes.ext_opt_bool).isEqualTo(java.lang.Boolean.TRUE)
    assertThat<Boolean>(allTypes.ext_rep_bool).isEqualTo(list(true))
    assertThat<Boolean>(allTypes.ext_pack_bool).isEqualTo(list(true))
  }

  @Test
  @Throws(IOException::class)
  fun testReadNoExtension() {
    val data = adapter.encode(allTypes)
    val parsed = AllTypes.ADAPTER.decode(data)
    assertThat(allTypes).isEqualTo(parsed)
  }

  @Test
  @Throws(IOException::class)
  fun testReadNonPacked() {
    val parsed = adapter.decode(
        Buffer().write(TestAllTypesData.nonPacked))
    assertThat(parsed).isEqualTo(allTypes)
  }

  @Test
  @Throws(IOException::class)
  fun testToString() {
    val data = adapter.encode(allTypes)
    val parsed = adapter.decode(data)
    assertThat(parsed.toString()).isEqualTo(TestAllTypesData.expectedToString)
  }

  @Test
  fun testEnums() {
    assertThat(AllTypes.NestedEnum.fromValue(1)).isEqualTo(A)
    assertThat(AllTypes.NestedEnum.fromValue(10)).isNull()
    assertThat(A.value).isEqualTo(1)
  }

  @Test
  @Throws(IOException::class)
  fun testSkipGroup() {
    val data = ByteArray(TestAllTypesData.expectedOutput.size + 27)
    System.arraycopy(TestAllTypesData.expectedOutput.toByteArray(), 0, data, 0, 17)
    var index = 17
    data[index++] = 0xa3.toByte() // start group, tag = 20, type = 3
    data[index++] = 0x01.toByte()
    data[index++] = 0x08.toByte() // tag = 1, type = 0 (varint)
    data[index++] = 0x81.toByte()
    data[index++] = 0x82.toByte()
    data[index++] = 0x6f.toByte()
    data[index++] = 0x21.toByte() // tag = 2, type = 1 (fixed64)
    data[index++] = 0x01.toByte()
    data[index++] = 0x02.toByte()
    data[index++] = 0x03.toByte()
    data[index++] = 0x04.toByte()
    data[index++] = 0x05.toByte()
    data[index++] = 0x06.toByte()
    data[index++] = 0x07.toByte()
    data[index++] = 0x08.toByte()
    data[index++] = 0x1a.toByte() // tag = 3, type = 2 (length-delimited)
    data[index++] = 0x03.toByte() // length = 3
    data[index++] = 0x01.toByte()
    data[index++] = 0x02.toByte()
    data[index++] = 0x03.toByte()
    data[index++] = 0x25.toByte() // tag = 4, type = 5 (fixed32)
    data[index++] = 0x01.toByte()
    data[index++] = 0x02.toByte()
    data[index++] = 0x03.toByte()
    data[index++] = 0x04.toByte()
    data[index++] = 0xa4.toByte() // end group, tag = 20, type = 4
    data[index++] = 0x01.toByte()

    System.arraycopy(TestAllTypesData.expectedOutput.toByteArray(), 17, data, index,
        TestAllTypesData.expectedOutput.size - 17)

    val parsed = adapter.decode(data)
    assertThat(parsed).isEqualTo(allTypes)
  }

  @Test
  fun testUnknownFields() {
    val builder = builder
    builder.addUnknownField(10000, FieldEncoding.VARINT, 1L)
    val withUnknownField = builder.build()
    val data = adapter.encode(withUnknownField)
    val count = TestAllTypesData.expectedOutput.size
    assertThat(data.size).isEqualTo(count + 4)
    assertThat(data[count]).isEqualTo(0x80.toByte())
    assertThat(data[count + 1]).isEqualTo(0xf1.toByte())
    assertThat(data[count + 2]).isEqualTo(0x04.toByte())
    assertThat(data[count + 3]).isEqualTo(0x01.toByte())
  }

  @Test
  @Ignore("we no longer enforce this constraint")
  fun testUnknownFieldsTypeMismatch() {
    val builder = builder
    builder.addUnknownField(10000, FieldEncoding.VARINT, 1)
    try {
      // Don't allow heterogeneous types for the same tag
      builder.addUnknownField(10000, FieldEncoding.FIXED32, 2)
      fail()
    } catch (expected: IllegalArgumentException) {
      assertThat(expected).hasMessage(
          "Wire type FIXED32 differs from previous type VARINT for tag 10000")
    }

  }

  @Test
  fun sanity() {
    val builder = builder
    assertThat(ByteString.of(*adapter.encode(builder.build())).hex())
        .isEqualTo(TestAllTypesData.expectedOutput.hex())
  }
}
