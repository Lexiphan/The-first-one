/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralNet.Math;

import java.util.Random;

/**
 *
 * @author lexiphan
 */
public class Matrix {
    private int m_Rows, m_Columns;
    private boolean m_Transposed;
    private double[] m_Items;

    public Matrix(int rows, int columns) throws IllegalArgumentException {
        if (rows <= 0) throw new IllegalArgumentException("Number of rows cannot be zero or negative.");
        if (columns <= 0) throw new IllegalArgumentException("Number of columns cannot be zero or negative.");
        m_Rows = rows;
        m_Columns = columns;
        m_Items = new double[rows * columns];
    }
    
    public int GetRows() {
        return m_Transposed ? m_Columns : m_Rows;
    }
    
    public int GetColumns() {
        return m_Transposed ? m_Rows : m_Columns;
    }
    
    public void Transpose() {
        m_Transposed = !m_Transposed;
    }
    
    protected double GetItemInternal(int row, int col)
    {
        return m_Items[m_Transposed ? col * m_Rows + row : row * m_Columns + col];
    }
    
    protected void SetItemInternal(int row, int col, double value)
    {
        m_Items[m_Transposed ? col * m_Rows + row : row * m_Columns + col] = value;
    }
    
    public double GetItem(int row, int col) {
        //if (row < 0 || row >= m_Rows) throw new IllegalArgumentException("Row index is out of range.");
        //if (col < 0 || col >= m_Columns) throw new IllegalArgumentException("Column index is out of range.");
        assert row >= 0 || row < m_Rows : "Row index is out of range.";
        assert col >= 0 || col < m_Columns : "Column index is out of range.";
        return GetItemInternal(row, col);
    }
    
    public void SetItem(int row, int col, double value) {
        //if (row < 0 || row >= m_Rows) throw new IllegalArgumentException("Row index is out of range.");
        //if (col < 0 || col >= m_Columns) throw new IllegalArgumentException("Column index is out of range.");
        assert row >= 0 || row < m_Rows : "Row index is out of range.";
        assert col >= 0 || col < m_Columns : "Column index is out of range.";
        SetItemInternal(row, col, value);
    }
    
    public void Add(double value) {
        for (int i = 0; i < m_Items.length; i++) {
            m_Items[i] += value;
        }
    }
    
    public void Multiply(double value) {
        for (int i = 0; i < m_Items.length; i++) {
            m_Items[i] += value;
        }
    }
    
    public Matrix Product(Matrix other) throws IllegalArgumentException {
        assert other != null : "other matrix cannot be null.";
        int depth = other.GetRows();
        if (depth != this.GetColumns()) throw new IllegalArgumentException("other matrix must have same number of rows as this matrix columns.");
        int rows = this.GetRows(), columns = other.GetColumns();
        Matrix result = new Matrix(rows, columns);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                double item = 0.0;
                for (int k = 0; k < depth; k++) {
                    item += this.GetItemInternal(r, k) * other.GetItemInternal(k, c);
                }
                result.SetItemInternal(r, c, item);
            }
        }
        return result;
    }
    
    public void SetAll(double value) {
        for (int i = 0; i < m_Items.length; i++) {
            m_Items[i] = value;
        }
    }
    
    public void Randomize() {
        java.util.Random rand = new Random();
        for (int i = 0; i < m_Items.length; i++) {
            m_Items[i] = rand.nextDouble();
        }
    }
    
    public void Randomize(long seed) {
        java.util.Random rand = new Random(seed);
        for (int i = 0; i < m_Items.length; i++) {
            m_Items[i] = rand.nextDouble();
        }
    }
}
