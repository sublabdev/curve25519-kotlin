package dev.sublab.curve25519

import dev.sublab.curve25519.ristrettoElement.CompressedRistretto
import dev.sublab.curve25519.ristrettoElement.RistrettoElement.Companion.IDENTITY
import dev.sublab.curve25519.ristrettoElement.RistrettoElement.Companion.RISTRETTO_GENERATOR
import dev.sublab.curve25519.ristrettoElement.functions.toRistrettoElement
import dev.sublab.curve25519.scalar.Scalar
import dev.sublab.curve25519.support.*
import dev.sublab.hex.hex
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class TestRistrettoElement {
    @Test
    fun generatorDecompressionCompression() {
        val re = RISTRETTO_GENERATOR_COMPRESSED.decompress()
        assertEquals(RISTRETTO_GENERATOR, re)
        assertEquals(RISTRETTO_GENERATOR_COMPRESSED, re.compress())
    }

    @Test
    fun generatorMultiplesAdd() {
        var re = IDENTITY
        for (generator in GENERATOR_MULTIPLES) {
            val compressed = CompressedRistretto(generator.hex.decode())
            assertEquals(compressed, re.compress())
            assertEquals(compressed.decompress(), re)
            re = re.add(RISTRETTO_GENERATOR)
        }
    }

    @Test
    fun generatorMultiplesSubtract() {
        var re = RISTRETTO_GENERATOR.double().double().double().double()
        for (generator in GENERATOR_MULTIPLES.reversed()) {
            re = re.subtract(RISTRETTO_GENERATOR)
            val compressed = CompressedRistretto(generator.hex.decode())
            assertEquals(compressed, re.compress())
            assertEquals(compressed.decompress(), re)
        }
    }

    @Test
    fun generatorNegateVsIdentityMinusGenerator() {
        assertEquals(
            RISTRETTO_GENERATOR.negate(),
            IDENTITY.subtract(RISTRETTO_GENERATOR)
        )
    }

    @Test
    fun generatorDblVsGenerator2() {
        assertEquals(
            CompressedRistretto(GENERATOR_MULTIPLES[2].hex.decode()).decompress(),
            RISTRETTO_GENERATOR.double()
        )
    }

    @Test
    fun generatorTimesTwelveVsGenerator12() {
        val s = ByteArray(32)
        s[0] = 12
        val twelve = Scalar(s)

        assertEquals(
            CompressedRistretto(GENERATOR_MULTIPLES[12].hex.decode()).decompress(),
            RISTRETTO_GENERATOR.multiply(twelve)
        )
    }

    @Test
    fun invalidEncodingsAreRejected() {
        for (invalid in INVALID_ENCODINGS) {
            assertThrows<InvalidEncodingException> {
                CompressedRistretto(invalid.hex.decode()).decompress()
            }
        }
    }

    @Test
    fun fromUniformBytes() {
        assertEquals(FROM_UNIFORM_BYTES_INPUTS.size, FROM_UNIFORM_BYTES_INPUTS.size)
        for ((i, input) in FROM_UNIFORM_BYTES_INPUTS.withIndex()) {
            val re = input.hex.decode().toRistrettoElement()
            assertEquals(CompressedRistretto(FROM_UNIFORM_BYTES_OUTPUTS[i].hex.decode()), re.compress())
        }
    }
}