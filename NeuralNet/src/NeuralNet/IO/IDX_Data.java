/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNet.IO;

import java.io.*;

/*
THE IDX FILE FORMAT

the IDX file format is a simple format for vectors and multidimensional matrices of various numerical types.
The basic format is

magic number 
size in dimension 0 
size in dimension 1 
size in dimension 2 
..... 
size in dimension N 
data

The magic number is an integer (MSB first). The first 2 bytes are always 0.

The third byte codes the type of the data: 
0x08: unsigned byte 
0x09: signed byte 
0x0B: short (2 bytes) 
0x0C: int (4 bytes) 
0x0D: float (4 bytes) 
0x0E: double (8 bytes)

The 4-th byte codes the number of dimensions of the vector/matrix: 1 for vectors, 2 for matrices....
The sizes in each dimension are 4-byte integers (MSB first, high endian, like in most non-Intel processors).
The data is stored like in a C array, i.e. the index in the last dimension changes the fastest. 
*/

/**
 *
 * @author AMBurlakov
 */
public class IDX_Data
{
    private IDX_DataType m_DataType = null;
    private int[] m_Dimensions = null;
    private byte[] m_Data = null;
    
    public IDX_DataType DataType()
    {
        return m_DataType;
    }
    
    public int NumberOfDimensions()
    {
        return m_Dimensions != null ? m_Dimensions.length : 0;
    }
    
    public int GetDimension(int dimIndex) throws IllegalArgumentException
    {
        if (m_Dimensions == null || dimIndex < 0 || dimIndex >= m_Dimensions.length)
            throw new IllegalArgumentException("dimIndex is out of range.");
        return m_Dimensions[dimIndex];
    }
    
    public void ReadFromIDXFile(String filename) throws FileNotFoundException, IOException
    {
        try (FileInputStream stream = new FileInputStream(filename)) 
        {
            ReadFromIDXStream(stream);
        }
    }
    
    public void ReadFromIDXStream(InputStream stream)
    {
        assert stream != null : "stream is null";
        
    }
}
