package com.rtkcamera.nativebridge

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test for NativeLoader.
 * Verifies that the loader correctly delegates to the NativeBridge.
 */
class NativeLoaderTest {

    private val nativeBridge = mockk<NativeBridge>()
    private val nativeLoader = NativeLoader(nativeBridge)

    @Test
    fun `load should return true when bridge succeeds`() {
        val algo = AlgorithmInfo("test", "Test Plugin", "/path/to/lib.so")
        every { nativeBridge.loadPlugin("/path/to/lib.so") } returns true

        val result = nativeLoader.load(algo)

        assertTrue(result)
        verify { nativeBridge.loadPlugin("/path/to/lib.so") }
    }

    @Test
    fun `load should return false when bridge fails`() {
        val algo = AlgorithmInfo("test", "Test Plugin", "/path/to/lib.so")
        every { nativeBridge.loadPlugin("/path/to/lib.so") } returns false

        val result = nativeLoader.load(algo)

        assertFalse(result)
        verify { nativeBridge.loadPlugin("/path/to/lib.so") }
    }
}
