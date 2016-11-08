package com.richrelevance.internal.net.oauth.signpost;

public class Base64 {
    static final int CHUNK_SIZE = 76;
    static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes();
    static final int BASELENGTH = 255;
    static final int LOOKUPLENGTH = 64;
    static final int EIGHTBIT = 8;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    static final int FOURBYTE = 4;
    static final int SIGN = -128;
    static final byte PAD = 61;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    public Base64() {
    }

    private static boolean isBase64(byte octect) {
        return octect == 61?true:base64Alphabet[octect] != -1;
    }

    public static boolean isArrayByteBase64(byte[] arrayOctect) {
        arrayOctect = discardWhitespace(arrayOctect);
        int length = arrayOctect.length;
        if(length == 0) {
            return true;
        } else {
            for(int i = 0; i < length; ++i) {
                if(!isBase64(arrayOctect[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public byte[] decode(byte[] pArray) {
        return decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        Object encodedData = null;
        boolean encodedDataLength = false;
        int nbrChunks = 0;
        int var22;
        if(fewerThan24bits != 0) {
            var22 = (numberTriplets + 1) * 4;
        } else {
            var22 = numberTriplets * 4;
        }

        if(isChunked) {
            nbrChunks = CHUNK_SEPARATOR.length == 0?0:(int)Math.ceil((double)((float)var22 / 76.0F));
            var22 += nbrChunks * CHUNK_SEPARATOR.length;
        }

        byte[] var21 = new byte[var22];
        boolean k = false;
        boolean l = false;
        boolean b1 = false;
        boolean b2 = false;
        boolean b3 = false;
        int encodedIndex = 0;
        boolean dataIndex = false;
        boolean i = false;
        int nextSeparatorIndex = 76;
        int chunksSoFar = 0;

        byte val2;
        byte val1;
        byte var23;
        byte var25;
        byte var24;
        byte var26;
        int var29;
        int var28;
        for(var28 = 0; var28 < numberTriplets; ++var28) {
            var29 = var28 * 3;
            var26 = binaryData[var29];
            var25 = binaryData[var29 + 1];
            byte var27 = binaryData[var29 + 2];
            var24 = (byte)(var25 & 15);
            var23 = (byte)(var26 & 3);
            val1 = (var26 & -128) == 0?(byte)(var26 >> 2):(byte)(var26 >> 2 ^ 192);
            val2 = (var25 & -128) == 0?(byte)(var25 >> 4):(byte)(var25 >> 4 ^ 240);
            byte val3 = (var27 & -128) == 0?(byte)(var27 >> 6):(byte)(var27 >> 6 ^ 252);
            var21[encodedIndex] = lookUpBase64Alphabet[val1];
            var21[encodedIndex + 1] = lookUpBase64Alphabet[val2 | var23 << 4];
            var21[encodedIndex + 2] = lookUpBase64Alphabet[var24 << 2 | val3];
            var21[encodedIndex + 3] = lookUpBase64Alphabet[var27 & 63];
            encodedIndex += 4;
            if(isChunked && encodedIndex == nextSeparatorIndex) {
                System.arraycopy(CHUNK_SEPARATOR, 0, var21, encodedIndex, CHUNK_SEPARATOR.length);
                ++chunksSoFar;
                nextSeparatorIndex = 76 * (chunksSoFar + 1) + chunksSoFar * CHUNK_SEPARATOR.length;
                encodedIndex += CHUNK_SEPARATOR.length;
            }
        }

        var29 = var28 * 3;
        if(fewerThan24bits == 8) {
            var26 = binaryData[var29];
            var23 = (byte)(var26 & 3);
            val1 = (var26 & -128) == 0?(byte)(var26 >> 2):(byte)(var26 >> 2 ^ 192);
            var21[encodedIndex] = lookUpBase64Alphabet[val1];
            var21[encodedIndex + 1] = lookUpBase64Alphabet[var23 << 4];
            var21[encodedIndex + 2] = 61;
            var21[encodedIndex + 3] = 61;
        } else if(fewerThan24bits == 16) {
            var26 = binaryData[var29];
            var25 = binaryData[var29 + 1];
            var24 = (byte)(var25 & 15);
            var23 = (byte)(var26 & 3);
            val1 = (var26 & -128) == 0?(byte)(var26 >> 2):(byte)(var26 >> 2 ^ 192);
            val2 = (var25 & -128) == 0?(byte)(var25 >> 4):(byte)(var25 >> 4 ^ 240);
            var21[encodedIndex] = lookUpBase64Alphabet[val1];
            var21[encodedIndex + 1] = lookUpBase64Alphabet[val2 | var23 << 4];
            var21[encodedIndex + 2] = lookUpBase64Alphabet[var24 << 2];
            var21[encodedIndex + 3] = 61;
        }

        if(isChunked && chunksSoFar < nbrChunks) {
            System.arraycopy(CHUNK_SEPARATOR, 0, var21, var22 - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        }

        return var21;
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        base64Data = discardNonBase64(base64Data);
        if(base64Data.length == 0) {
            return new byte[0];
        } else {
            int numberQuadruple = base64Data.length / 4;
            Object decodedData = null;
            boolean b1 = false;
            boolean b2 = false;
            boolean b3 = false;
            boolean b4 = false;
            boolean marker0 = false;
            boolean marker1 = false;
            int encodedIndex = 0;
            boolean dataIndex = false;
            int i = base64Data.length;

            while(base64Data[i - 1] == 61) {
                --i;
                if(i == 0) {
                    return new byte[0];
                }
            }

            byte[] var12 = new byte[i - numberQuadruple];

            for(i = 0; i < numberQuadruple; ++i) {
                int var19 = i * 4;
                byte var17 = base64Data[var19 + 2];
                byte var18 = base64Data[var19 + 3];
                byte var13 = base64Alphabet[base64Data[var19]];
                byte var14 = base64Alphabet[base64Data[var19 + 1]];
                byte var15;
                if(var17 != 61 && var18 != 61) {
                    var15 = base64Alphabet[var17];
                    byte var16 = base64Alphabet[var18];
                    var12[encodedIndex] = (byte)(var13 << 2 | var14 >> 4);
                    var12[encodedIndex + 1] = (byte)((var14 & 15) << 4 | var15 >> 2 & 15);
                    var12[encodedIndex + 2] = (byte)(var15 << 6 | var16);
                } else if(var17 == 61) {
                    var12[encodedIndex] = (byte)(var13 << 2 | var14 >> 4);
                } else if(var18 == 61) {
                    var15 = base64Alphabet[var17];
                    var12[encodedIndex] = (byte)(var13 << 2 | var14 >> 4);
                    var12[encodedIndex + 1] = (byte)((var14 & 15) << 4 | var15 >> 2 & 15);
                }

                encodedIndex += 3;
            }

            return var12;
        }
    }

    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        int packedData = 0;

        while(packedData < data.length) {
            switch(data[packedData]) {
                default:
                    groomedData[bytesCopied++] = data[packedData];
                case 9:
                case 10:
                case 13:
                case 32:
                    ++packedData;
            }
        }

        byte[] var4 = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, var4, 0, bytesCopied);
        return var4;
    }

    static byte[] discardNonBase64(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;

        for(int packedData = 0; packedData < data.length; ++packedData) {
            if(isBase64(data[packedData])) {
                groomedData[bytesCopied++] = data[packedData];
            }
        }

        byte[] var4 = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, var4, 0, bytesCopied);
        return var4;
    }

    public byte[] encode(byte[] pArray) {
        return encodeBase64(pArray, false);
    }

    static {
        int i;
        for(i = 0; i < 255; ++i) {
            base64Alphabet[i] = -1;
        }

        for(i = 90; i >= 65; --i) {
            base64Alphabet[i] = (byte)(i - 65);
        }

        for(i = 122; i >= 97; --i) {
            base64Alphabet[i] = (byte)(i - 97 + 26);
        }

        for(i = 57; i >= 48; --i) {
            base64Alphabet[i] = (byte)(i - 48 + 52);
        }

        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;

        for(i = 0; i <= 25; ++i) {
            lookUpBase64Alphabet[i] = (byte)(65 + i);
        }

        i = 26;

        int j;
        for(j = 0; i <= 51; ++j) {
            lookUpBase64Alphabet[i] = (byte)(97 + j);
            ++i;
        }

        i = 52;

        for(j = 0; i <= 61; ++j) {
            lookUpBase64Alphabet[i] = (byte)(48 + j);
            ++i;
        }

        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }
}
