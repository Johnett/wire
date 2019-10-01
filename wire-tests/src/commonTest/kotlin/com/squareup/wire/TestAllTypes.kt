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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class TestAllTypes {
  private val allTypes = createAllTypes()
  private val adapter = AllTypes.ADAPTER

  // Return a numRepeated-element list with a given repeated value
  private fun <T> list(x: T, numRepeated: Int = 2): List<T> = List(numRepeated) { x }

  private fun arraycopy(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
    var i = srcPos
    var j = destPos
    while (i < srcPos + length) {
      dest[j++] = src[i++]
    }
  }

  private fun createAllTypes(numRepeated: Int = 2): AllTypes {
    val bytes = ByteString.of(125.toByte(), 225.toByte())
    val nestedMessage = AllTypes.NestedMessage(a = 999)
    return AllTypes(
        opt_int32 = 111,
        opt_uint32 = 112,
        opt_sint32 = 113,
        opt_fixed32 = 114,
        opt_sfixed32 = 115,
        opt_int64 = 116L,
        opt_uint64 = 117L,
        opt_sint64 = 118L,
        opt_fixed64 = 119L,
        opt_sfixed64 = 120L,
        opt_bool = true,
        opt_float = 122.0f,
        opt_double = 123.0,
        opt_string = "124",
        opt_bytes = bytes,
        opt_nested_enum = A,
        opt_nested_message = nestedMessage,
        req_int32 = 111,
        req_uint32 = 112,
        req_sint32 = 113,
        req_fixed32 = 114,
        req_sfixed32 = 115,
        req_int64 = 116L,
        req_uint64 = 117L,
        req_sint64 = 118L,
        req_fixed64 = 119L,
        req_sfixed64 = 120L,
        req_bool = true,
        req_float = 122.0f,
        req_double = 123.0,
        req_string = "124",
        req_bytes = bytes,
        req_nested_enum = A,
        req_nested_message = nestedMessage,
        rep_int32 = list(111, numRepeated),
        rep_uint32 = list(112, numRepeated),
        rep_sint32 = list(113, numRepeated),
        rep_fixed32 = list(114, numRepeated),
        rep_sfixed32 = list(115, numRepeated),
        rep_int64 = list(116L, numRepeated),
        rep_uint64 = list(117L, numRepeated),
        rep_sint64 = list(118L, numRepeated),
        rep_fixed64 = list(119L, numRepeated),
        rep_sfixed64 = list(120L, numRepeated),
        rep_bool = list(true, numRepeated),
        rep_float = list(122.0f, numRepeated),
        rep_double = list(123.0, numRepeated),
        rep_string = list("124", numRepeated),
        rep_bytes = list(bytes, numRepeated),
        rep_nested_enum = list(A, numRepeated),
        rep_nested_message = list(nestedMessage, numRepeated),
        pack_int32 = list(111, numRepeated),
        pack_uint32 = list(112, numRepeated),
        pack_sint32 = list(113, numRepeated),
        pack_fixed32 = list(114, numRepeated),
        pack_sfixed32 = list(115, numRepeated),
        pack_int64 = list(116L, numRepeated),
        pack_uint64 = list(117L, numRepeated),
        pack_sint64 = list(118L, numRepeated),
        pack_fixed64 = list(119L, numRepeated),
        pack_sfixed64 = list(120L, numRepeated),
        pack_bool = list(true, numRepeated),
        pack_float = list(122.0f, numRepeated),
        pack_double = list(123.0, numRepeated),
        pack_nested_enum = list(A, numRepeated),
        ext_opt_bool = true,
        ext_rep_bool = list(true, numRepeated),
        ext_pack_bool = list(true, numRepeated)
    )
  }

  @Test
  fun testHashCodes() {
    val message = createAllTypes()
    val messageHashCode = message.hashCode()
    assertEquals(allTypes.hashCode(), messageHashCode)
  }

  @Test
  fun testWrite() {
    val output = adapter.encode(allTypes)
    assertEquals(expectedOutput.size, output.size)
    assertEquals(expectedOutput, output.toByteString())
  }

  @Test
  fun testWriteSource() {
    val sink = Buffer()
    adapter.encode(sink, allTypes)
    assertEquals(expectedOutput, sink.readByteString())
  }

  @Test
  fun testWriteBytes() {
    val output = adapter.encode(allTypes)
    assertEquals(expectedOutput.size, output.size)
    assertEquals(expectedOutput, output.toByteString())
  }

  @Test
  fun testReadSource() {
    val data = adapter.encode(allTypes)
    val input = Buffer().write(data)

    val parsed = adapter.decode(input)
    assertEquals(allTypes, parsed)

    assertTrue(allTypes.ext_opt_bool!!)
    assertEquals(list(true), allTypes.ext_rep_bool)
    assertEquals(list(true), allTypes.ext_pack_bool)
  }

  @Test
  fun testReadBytes() {
    val data = adapter.encode(allTypes)

    val parsed = adapter.decode(data)
    assertEquals(allTypes, parsed)

    assertTrue(allTypes.ext_opt_bool!!)
    assertEquals(list(true), allTypes.ext_rep_bool)
    assertEquals(list(true), allTypes.ext_pack_bool)
  }

  @Test
  fun testReadLongMessages() {
    val allTypes = createAllTypes(50)
    val data = adapter.encode(allTypes)

    val parsed = adapter.decode(data)
    assertEquals(allTypes, parsed)

    assertTrue(allTypes.ext_opt_bool!!)
    assertEquals(list(true, 50), allTypes.ext_rep_bool)
    assertEquals(list(true, 50), allTypes.ext_pack_bool)
  }

  @Test
  fun testReadNoExtension() {
    val data = adapter.encode(allTypes)
    val parsed = AllTypes.ADAPTER.decode(data)
    assertEquals(parsed, allTypes)
  }

  @Test
  fun testReadNonPacked() {
    val parsed = adapter.decode(Buffer().write(nonPacked))
    assertEquals(allTypes, parsed)
  }

  @Test
  fun testToString() {
    val data = adapter.encode(allTypes)
    val parsed = adapter.decode(data)
    assertEquals(expectedToString, parsed.toString())
  }

  @Test
  fun testDefaults() {
    assertEquals(true, AllTypes.DEFAULT_DEFAULT_BOOL)
    // original: "<c-cedilla>ok\a\b\f\n\r\t\v\1\01\001\17\017\176\x1\x01\x11\X1\X01\X11g<u umlaut>zel"
    assertEquals("çok\u0007\b\\f\n\r\t\u000b\u0001\u0001" + "\u0001\u000f\u000f~\u0001\u0001\u0011\u0001\u0001\u0011güzel",
        AllTypes.DEFAULT_DEFAULT_STRING)
  }

  @Test
  fun testEnums() {
    assertEquals(A, AllTypes.NestedEnum.fromValue(1))
    assertNull(AllTypes.NestedEnum.fromValue(10))
    assertEquals(1, A.value)
  }

  @Test
  fun testSkipGroup() {
    val data = ByteArray(expectedOutput.size + 27)
    arraycopy(expectedOutput.toByteArray(), 0, data, 0, 17)
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

    arraycopy(expectedOutput.toByteArray(), 17, data, index, expectedOutput.size - 17)

    val parsed = adapter.decode(data)
    assertEquals(allTypes, parsed)
  }
}
