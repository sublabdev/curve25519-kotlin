/**
 *
 * Copyright 2023 SUBSTRATE LABORATORY LLC <info@sublab.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package dev.sublab.curve25519.support

import dev.sublab.curve25519.edwardsPoint.CompressedEdwardsY
import dev.sublab.curve25519.ristrettoElement.CompressedRistretto
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.hex.hex

/**
 * x =
 * 2238329342913194256032495932344128051776374960164957527413114840482143558222
 */
val X = Scalar(
    "4e5ab4345d4708845913b4641bc27d5252a585101bcc4244d449f4a879d9f204".hex.decode()
)

/**
 * 1/x =
 * 6859937278830797291664592131120606308688036382723378951768035303146619657244
 */
val XINV = Scalar(
    "1cdc17fce0e9a5bbd9247e56bb016347bbba31edd5a9bb96d50bcd7a3f962a0f".hex.decode()
)

/**
 * y =
 * 2592331292931086675770238855846338635550719849568364935475441891787804997264
 */
val Y = Scalar(
    "907633fe1c4b66a4a28d2dd7678386c353d0de5455d4fc9de8ef7ac31f35bb05".hex.decode()
)

/**
 * x*y =
 * 5690045403673944803228348699031245560686958845067437804563560795922180092780
 */
val X_TIMES_Y = Scalar(
    "6c3374a1894f62210aaa2fe186a6f92ce0aa75c2779581c295fc08179a73940c".hex.decode()
)

/**
 * sage: l = 2^252 + 27742317777372353535851937790883648493 sage: big = 2^256 -
 * 1 sage: repr((big % l).digits(256))
 */
val CANONICAL_2_256_MINUS_1 = Scalar(
    "1c95988d7431ecd670cf7d73f45befc6feffffffffffffffffffffffffffff0f".hex.decode()
)

val A_NAF = byteArrayOf(
    0, 13, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, -9, 0, 0, 0, 0, -11, 0, 0,
    0, 0, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 9, 0, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 11, 0, 0, 0, 0, 11,
    0, 0, 0, 0, 0, -9, 0, 0, 0, 0, 0, -3, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0,
    9, 0, 0, 0, 0, -15, 0, 0, 0, 0, -7, 0, 0, 0, 0, -9, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, -3, 0,
    0, 0, 0, -11, 0, 0, 0, 0, -7, 0, 0, 0, 0, -13, 0, 0, 0, 0, 11, 0, 0, 0, 0, -9, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
    0, -15, 0, 0, 0, 0, 1, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 11, 0,
    0, 0, 0, 0, 15, 0, 0, 0, 0, 0, -9, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, -15, 0,
    0, 0, 0, 0, 15, 0, 0, 0, 0, 15, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0
)

/**
 * Example from RFC 8032 test case 1
 */
val TV1_R_INPUT get() = "b6b19cd8e0426f5983fa112d89a143aa97dab8bc5deb8d5b6253c928b65272f4044098c2a990039cde5b6a4818df0bfb6e40dc5dee54248032962323e701352d".hex.decode()
val TV1_R get() = "f38907308c893deaf244787db4af53682249107418afc2edc58f75ac58a07404".hex.decode()
val TV1_H get() = "86eabc8e4c96193d290504e7c600df6cf8d8256131ec2c138a3e7e162e525404".hex.decode()
val TV1_A get() = "307c83864f2833cb427a2ef1c00a013cfdff2768d980c0a3a520f006904de94f".hex.decode()
val TV1_S get() = "5fb8821590a33bacc61e39701cf9b46bd25bf5f0595bbe24655141438e7a100b".hex.decode()


val FORTYTWO = Scalar(
    "2A00000000000000000000000000000000000000000000000000000000000000".hex.decode()
)
val S1234567890 = Scalar(
    "D202964900000000000000000000000000000000000000000000000000000000".hex.decode()
)

val RADIX16_ZERO = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".hex.decode()
val RADIX16_ONE = "01000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".hex.decode()
val RADIX16_42 = "FA030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".hex.decode()

/**
 * Compressed Edwards Y form of the Ed25519 basepoint.
 */
val ED25519_BASEPOINT_COMPRESSED = CompressedEdwardsY(
    "5866666666666666666666666666666666666666666666666666666666666666".hex.decode()
)

/**
 * Compressed Edwards Y form of 2*basepoint.
 */
val BASE2_CMPRSSD = CompressedEdwardsY(
    "c9a3f86aae465f0e56513864510f3997561fa2c9e85ea21dc2292309f3cd6022".hex.decode()
)

/**
 * Compressed Edwards Y form of 16*basepoint.
 */
val BASE16_CMPRSSD = CompressedEdwardsY(
    "eb2767c137ab7ad8279c078eff116ab0786ead3a2e0f989f72c37f82f2969670".hex.decode()
)

/**
 * 4493907448824000747700850167940867464579944529806937181821189941592931634714
 */
val A_SCALAR = Scalar(
    "1a0e978a90f6622d3747023f8ad8264da758aa1b88e040d1589e7b7f2376ef09".hex.decode()
)

/**
 * 2506056684125797857694181776241676200180934651973138769173342316833279714961
 */
val B_SCALAR = Scalar(
    "91267acf25c2091ba217747b66f0b32e9df2a56741cfdac456a7d4aab8608a05".hex.decode()
)

/**
 * A_SCALAR * basepoint, computed with ed25519.py
 */
val A_TIMES_BASEPOINT = CompressedEdwardsY(
    "ea27e26053df1b5956f14d5dec3c34c384a269b74cc3803ea8e2e7c9425e40a5".hex.decode()
)

/**
 * A_SCALAR * (A_TIMES_BASEPOINT) + B_SCALAR * BASEPOINT computed with
 * ed25519.py
 */
val DOUBLE_SCALAR_MULT_RESULT = CompressedEdwardsY(
    "7dfd6c45af6d6e0eba20371a236459c4c0468343de704b85096ffe354f132b42".hex.decode()
)

/**
 * The 8-torsion subgroup $\mathcal E [8]$.
 *
 *
 * In the case of Curve25519, it is cyclic; the $i$-th element of the array is
 * $[i]P$, where $P$ is a point of order $8$ generating $\mathcal E[8]$.
 *
 *
 * Thus $\mathcal E[8]$ is the points indexed by 0,2,4,6, and $\mathcal E[2]$ is
 * the points indexed by 0,4.
 */
val EIGHT_TORSION_COMPRESSED = arrayOf(
    CompressedEdwardsY(
        "0100000000000000000000000000000000000000000000000000000000000000".hex.decode()
    ),
    CompressedEdwardsY(
        "c7176a703d4dd84fba3c0b760d10670f2a2053fa2c39ccc64ec7fd7792ac037a".hex.decode()
    ),
    CompressedEdwardsY(
        "0000000000000000000000000000000000000000000000000000000000000080".hex.decode()
    ),
    CompressedEdwardsY(
        "26e8958fc2b227b045c3f489f2ef98f0d5dfac05d3c63339b13802886d53fc05".hex.decode()
    ),
    CompressedEdwardsY(
        "ecffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f".hex.decode()
    ),
    CompressedEdwardsY(
        "26e8958fc2b227b045c3f489f2ef98f0d5dfac05d3c63339b13802886d53fc85".hex.decode()
    ),
    CompressedEdwardsY(
        "0000000000000000000000000000000000000000000000000000000000000000".hex.decode()
    ),
    CompressedEdwardsY(
        "c7176a703d4dd84fba3c0b760d10670f2a2053fa2c39ccc64ec7fd7792ac03fa".hex.decode()
    )
)

// Test vectors below, and the tests they are used in, are from
// curve25519-dalek.
// https://github.com/dalek-cryptography/curve25519-dalek/blob/4bdccd7b7c394d9f8ffc4b29d5acc23c972f3d7a/src/field.rs#L280-L301

// Test vectors below, and the tests they are used in, are from
// curve25519-dalek.
// https://github.com/dalek-cryptography/curve25519-dalek/blob/4bdccd7b7c394d9f8ffc4b29d5acc23c972f3d7a/src/field.rs#L280-L301
// Random element a of GF(2^255-19), from Sage
// a = 1070314506888354081329385823235218444233221\
// 2228051251926706380353716438957572
val A_BYTES = byteArrayOf(
    0x04,
    0xfe.toByte(),
    0xdf.toByte(),
    0x98.toByte(),
    0xa7.toByte(),
    0xfa.toByte(),
    0x0a,
    0x68,
    0x84.toByte(),
    0x92.toByte(),
    0xbd.toByte(),
    0x59,
    0x08,
    0x07,
    0xa7.toByte(),
    0x03,
    0x9e.toByte(),
    0xd1.toByte(),
    0xf6.toByte(),
    0xf2.toByte(),
    0xe1.toByte(),
    0xd9.toByte(),
    0xe2.toByte(),
    0xa4.toByte(),
    0xa4.toByte(),
    0x51,
    0x47,
    0x36,
    0xf3.toByte(),
    0xc3.toByte(),
    0xa9.toByte(),
    0x17
)

// Byte representation of a**2
val ASQ_BYTES = byteArrayOf(
    0x75,
    0x97.toByte(),
    0x24,
    0x9e.toByte(),
    0xe6.toByte(),
    0x06,
    0xfe.toByte(),
    0xab.toByte(),
    0x24,
    0x04,
    0x56,
    0x68,
    0x07,
    0x91.toByte(),
    0x2d,
    0x5d,
    0x0b,
    0x0f,
    0x3f,
    0x1c,
    0xb2.toByte(),
    0x6e,
    0xf2.toByte(),
    0xe2.toByte(),
    0x63,
    0x9c.toByte(),
    0x12,
    0xba.toByte(),
    0x73,
    0x0b,
    0xe3.toByte(),
    0x62
)

// Byte representation of 1/a
val AINV_BYTES = byteArrayOf(
    0x96.toByte(),
    0x1b,
    0xcd.toByte(),
    0x8d.toByte(),
    0x4d,
    0x5e,
    0xa2.toByte(),
    0x3a,
    0xe9.toByte(),
    0x36,
    0x37,
    0x93.toByte(),
    0xdb.toByte(),
    0x7b,
    0x4d,
    0x70,
    0xb8.toByte(),
    0x0d,
    0xc0.toByte(),
    0x55,
    0xd0.toByte(),
    0x4c,
    0x1d,
    0x7b,
    0x90.toByte(),
    0x71,
    0xd8.toByte(),
    0xe9.toByte(),
    0xb6.toByte(),
    0x18,
    0xe6.toByte(),
    0x30
)

// Byte representation of a^((p-5)/8)
val AP58_BYTES = byteArrayOf(
    0x6a,
    0x4f,
    0x24,
    0x89.toByte(),
    0x1f,
    0x57,
    0x60,
    0x36,
    0xd0.toByte(),
    0xbe.toByte(),
    0x12,
    0x3c,
    0x8f.toByte(),
    0xf5.toByte(),
    0xb1.toByte(),
    0x59,
    0xe0.toByte(),
    0xf0.toByte(),
    0xb8.toByte(),
    0x1b,
    0x20,
    0xd2.toByte(),
    0xb5.toByte(),
    0x1f,
    0x15,
    0x21,
    0xf9.toByte(),
    0xe3.toByte(),
    0xe1.toByte(),
    0x61,
    0x21,
    0x55
)

// Notice that the last element has the high bit set, which
// should be ignored.
val B_BYTES = byteArrayOf(
    113,
    191.toByte(),
    169.toByte(),
    143.toByte(),
    91,
    234.toByte(),
    121,
    15,
    241.toByte(),
    131.toByte(),
    217.toByte(),
    36,
    230.toByte(),
    101,
    92,
    234.toByte(),
    8,
    208.toByte(),
    170.toByte(),
    251.toByte(),
    97,
    127,
    70,
    210.toByte(),
    58,
    23,
    166.toByte(),
    87,
    240.toByte(),
    169.toByte(),
    184.toByte(),
    178.toByte()
)

val RISTRETTO_GENERATOR_COMPRESSED = CompressedRistretto(
    "e2f2ae0a6abc4e71a884a961c500515f58e30b6aa582dd8db6a65945e08d2d76".hex.decode()
)

val GENERATOR_MULTIPLES = arrayOf(
    "0000000000000000000000000000000000000000000000000000000000000000",
    "e2f2ae0a6abc4e71a884a961c500515f58e30b6aa582dd8db6a65945e08d2d76",
    "6a493210f7499cd17fecb510ae0cea23a110e8d5b901f8acadd3095c73a3b919",
    "94741f5d5d52755ece4f23f044ee27d5d1ea1e2bd196b462166b16152a9d0259",
    "da80862773358b466ffadfe0b3293ab3d9fd53c5ea6c955358f568322daf6a57",
    "e882b131016b52c1d3337080187cf768423efccbb517bb495ab812c4160ff44e",
    "f64746d3c92b13050ed8d80236a7f0007c3b3f962f5ba793d19a601ebb1df403",
    "44f53520926ec81fbd5a387845beb7df85a96a24ece18738bdcfa6a7822a176d",
    "903293d8f2287ebe10e2374dc1a53e0bc887e592699f02d077d5263cdd55601c",
    "02622ace8f7303a31cafc63f8fc48fdc16e1c8c8d234b2f0d6685282a9076031",
    "20706fd788b2720a1ed2a5dad4952b01f413bcf0e7564de8cdc816689e2db95f",
    "bce83f8ba5dd2fa572864c24ba1810f9522bc6004afe95877ac73241cafdab42",
    "e4549ee16b9aa03099ca208c67adafcafa4c3f3e4e5303de6026e3ca8ff84460",
    "aa52e000df2e16f55fb1032fc33bc42742dad6bd5a8fc0be0167436c5948501f",
    "46376b80f409b29dc2b5f6f0c52591990896e5716f41477cd30085ab7f10301e",
    "e0c418f7c8d9c4cdd7395b93ea124f3ad99021bb681dfc3302a9d99a2e53e64e"
)

val INVALID_ENCODINGS = arrayOf(//Non-canonicalfieldencodings.
    "00ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
    "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
    "f3ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
    "edffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f", //Negativefieldelements.
    "0100000000000000000000000000000000000000000000000000000000000000",
    "01ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f",
    "ed57ffd8c914fb201471d1c3d245ce3c746fcbe63a3679d51b6a516ebebe0e20",
    "c34c4e1826e5d403b78e246e88aa051c36ccf0aafebffe137d148a2bf9104562",
    "c940e5a4404157cfb1628b108db051a8d439e1a421394ec4ebccb9ec92a8ac78",
    "47cfc5497c53dc8e61c91d17fd626ffb1c49e2bca94eed052281b510b1117a24",
    "f1c6165d33367351b0da8f6e4511010c68174a03b6581212c71c0e1d026c3c72",
    "87260f7a2f12495118360f02c26a470f450dadf34a413d21042b43b9d93e1309", //Non-squarex^2.
    "26948d35ca62e643e26a83177332e6b6afeb9d08e4268b650f1f5bbd8d81d371",
    "4eac077a713c57b4f4397629a4145982c661f48044dd3f96427d40b147d9742f",
    "de6a7b00deadc788eb6b6c8d20c0ae96c2f2019078fa604fee5b87d6e989ad7b",
    "bcab477be20861e01e4a0e295284146a510150d9817763caf1a6f4b422d67042",
    "2a292df7e32cababbd9de088d1d1abec9fc0440f637ed2fba145094dc14bea08",
    "f4a9e534fc0d216c44b218fa0c42d99635a0127ee2e53c712f70609649fdff22",
    "8268436f8c4126196cf64b3c7ddbda90746a378625f9813dd9b8457077256731",
    "2810e5cbc2cc4d4eece54f61c6f69758e289aa7ab440b3cbeaa21995c2f4232b", //Negativexyvalue.
    "3eb858e78f5a7254d8c9731174a94f76755fd3941c0ac93735c07ba14579630e",
    "a45fdc55c76448c049a1ab33f17023edfb2be3581e9c7aade8a6125215e04220",
    "d483fe813c6ba647ebbfd3ec41adca1c6130c2beeee9d9bf065c8d151c5f396e",
    "8a2e1d30050198c65a54483123960ccc38aef6848e1ec8f5f780e8523769ba32",
    "32888462f8b486c68ad7dd9610be5192bbeaf3b443951ac1a8118419d9fa097b",
    "227142501b9d4355ccba290404bde41575b037693cef1f438c47f8fbf35d1165",
    "5c37cc491da847cfeb9281d407efc41e15144c876e0170b499a96a22ed31e01e",
    "445425117cb8c90edcbc7c1cc0e74f747f2c1efa5630a967c64f287792a48a4b", //s=-1,whichcausesy=0.
    "ecffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7f"
)

val FROM_UNIFORM_BYTES_INPUTS = arrayOf(
    "5d1be09e3d0c82fc538112490e35701979d99e06ca3e2b5b54bffe8b4dc772c14d98b696a1bbfb5ca32c436cc61c16563790306c79eaca7705668b47dffe5bb6",
    "f116b34b8f17ceb56e8732a60d913dd10cce47a6d53bee9204be8b44f6678b270102a56902e2488c46120e9276cfe54638286b9e4b3cdb470b542d46c2068d38",
    "8422e1bbdaab52938b81fd602effb6f89110e1e57208ad12d9ad767e2e25510c27140775f9337088b982d83d7fcf0b2fa1edffe51952cbe7365e95c86eaf325c",
    "ac22415129b61427bf464e17baee8db65940c233b98afce8d17c57beeb7876c2150d15af1cb1fb824bbd14955f2b57d08d388aab431a391cfc33d5bafb5dbbaf",
    "165d697a1ef3d5cf3c38565beefcf88c0f282b8e7dbd28544c483432f1cec7675debea8ebb4e5fe7d6f6e5db15f15587ac4d4d4a1de7191e0c1ca6664abcc413",
    "a836e6c9a9ca9f1e8d486273ad56a78c70cf18f0ce10abb1c7172ddd605d7fd2979854f47ae1ccf204a33102095b4200e5befc0465accc263175485f0e17ea5c",
    "2cdc11eaeb95daf01189417cdddbf95952993aa9cb9c640eb5058d09702c74622c9965a697a3b345ec24ee56335b556e677b30e6f90ac77d781064f866a3c982"
)

val FROM_UNIFORM_BYTES_OUTPUTS = arrayOf(
    "3066f82a1a747d45120d1740f14358531a8f04bbffe6a819f86dfe50f44a0a46",
    "f26e5b6f7d362d2d2a94c5d0e7602cb4773c95a2e5c31a64f133189fa76ed61b",
    "006ccd2a9e6867e6a2c5cea83d3302cc9de128dd2a9a57dd8ee7b9d7ffe02826",
    "f8f0c87cf237953c5890aec3998169005dae3eca1fbb04548c635953c817f92a",
    "ae81e7dedf20a497e10c304a765c1767a42d6e06029758d2d7e8ef7cc4c41179",
    "e2705652ff9f5e44d3e841bf1c251cf7dddb77d140870d1ab2ed64f1a9ce8628",
    "80bd07262511cdde4863f8a7434cef696750681cb9510eea557088f76d9e5065"
)
