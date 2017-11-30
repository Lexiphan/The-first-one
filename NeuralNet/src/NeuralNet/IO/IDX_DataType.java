/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet.IO;

/**
 *
 * @author AMBurlakov
 */
public enum IDX_DataType {
    UNSIGNED_BYTE((byte)0x8, (byte)1),
    SIGNED_BYTE((byte)0x9, (byte)1),
    SHORT((byte)0xB, (byte)2),
    INT((byte)0xC, (byte)4),
    FLOAT((byte)0xD, (byte)4),
    DOUBLE((byte)0xE, (byte)8);
    
    public final byte FileHeaderValue;
    public final byte ValueSize;

    private IDX_DataType(byte fileHeaderValue, byte valueSize)
    {
        this.FileHeaderValue = fileHeaderValue;
        this.ValueSize = valueSize;
    }
    
    public static IDX_DataType Parse(byte fileHdrValue)
    {
        switch (fileHdrValue) {
            case 0x8: return UNSIGNED_BYTE;
            case 0x9: return SIGNED_BYTE;
            case 0xB: return SHORT;
            case 0xC: return INT;
            case 0xD: return FLOAT;
            case 0xE: return DOUBLE;
            default: return null;
        }
    }
}
