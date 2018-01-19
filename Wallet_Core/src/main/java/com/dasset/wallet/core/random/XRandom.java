/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasset.wallet.core.random;

import com.dasset.wallet.components.utils.LogUtil;

import java.security.SecureRandom;

public class XRandom extends SecureRandom {

    private IUEntropy iuEntropy;

    public XRandom(IUEntropy iuEntropy) {
        this.iuEntropy = iuEntropy;
    }

    private byte[] getRandomBytes(int length) {
        LogUtil.getInstance().print(String.format("Request %S bytes from XRandom", length));
        byte[] uRandomBytes = URandom.nextBytes(length);
        if (this.iuEntropy == null) {
            return uRandomBytes;
        } else {
            byte[] bytes = new byte[length];
            byte[] entropyBytes = iuEntropy.nextBytes(length);
            for (int i = 0; i < uRandomBytes.length; i++) {
                bytes[i] = (byte) (uRandomBytes[i] ^ entropyBytes[i]);
            }
            return bytes;
        }
    }

    @Override
    public void setSeed(long seed) { }

    @Override
    public synchronized void nextBytes(byte[] bytes) {
        byte[] nextBytes = getRandomBytes(bytes.length);
        if (nextBytes.length != bytes.length) {
            throw new RuntimeException("Xrandom bytes length not match");
        }
        for (int i = 0; i < bytes.length && i < nextBytes.length; i++) {
            bytes[i] = nextBytes[i];
        }
    }

    @Override
    public byte[] generateSeed(int numBytes) {
        return getRandomBytes(numBytes);
    }
}
