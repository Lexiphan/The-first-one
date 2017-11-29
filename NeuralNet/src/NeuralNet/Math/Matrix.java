/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralNet.Math;

import java.util.Random;
import java.util.function.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    
    public Matrix(int columns, double ... values) throws IllegalArgumentException {
        assert values != null : "values array cannot be null.";
        if (columns <= 0) throw new IllegalArgumentException("Number of columns cannot be zero or negative.");
        if (values.length == 0) throw new IllegalArgumentException("Values array cannot be empty.");
        m_Rows = values.length / columns + (values.length % columns > 0 ? 1 : 0);
        m_Columns = columns;
        m_Items = java.util.Arrays.copyOf(values, m_Rows * columns);
    }

    public Matrix(Matrix source) {
        assert source != null : "source cannot be null.";
        m_Rows = source.m_Rows;
        m_Columns = source.m_Columns;
        m_Transposed = source.m_Transposed;
        m_Items = java.util.Arrays.copyOf(source.m_Items, source.m_Items.length);
    }
    
    
    
    public double[] ToArray() {
        if (m_Transposed)
        {
            double[] arr = new double[m_Items.length];
            for (int r = 0; r < m_Rows; r++) {
                for (int c = 0; c < m_Columns; c++) {
                    arr[r * m_Columns + c] = m_Items[c * m_Rows + r];
                }
            }
            return arr;
        }
        else
        {
            return java.util.Arrays.copyOf(m_Items, m_Items.length);
        }
    }
    
    public static Matrix SingleColumn(double ... values) {
        assert values != null : "values are null.";
        return new Matrix(values.length, values);
    }
    
    public static Matrix SingleRow(double ... values) {
        assert values != null : "values are null.";
        return new Matrix(1, values);
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
    
    public void ApplyFunction(DoubleFunction<Double> f) {
        for (int i = 0; i < m_Items.length; i++) {
            m_Items[i] = f.apply(m_Items[i]);
        }
    }
    
    public void SetAll(double value) {
        java.util.Arrays.fill(m_Items, value);
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
    
    public Matrix Inverse() {
        throw new NotImplementedException();
    }
    
    /**
     * Calculates matrix product of this * other. Number of this.Columns must be equal to number of other.Rows. 
     * @param other Matrix to product with.
     * @throws IllegalArgumentException 
     */
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
    
    /**
     * Calculates matrix product of this matrix and specified single column vector.
     * Number of elements in the columnVector must equal to this.Columns.
     * @param columnVector Column vector to product with.
     * @return
     * @throws IllegalArgumentException 
     */
    public double[] Product(double[] columnVector) throws IllegalArgumentException {
        assert columnVector != null : "columnVector cannot be null.";
        int depth = columnVector.length;
        if (depth != this.GetColumns()) throw new IllegalArgumentException("columnVector must have same number of elements as this matrix columns.");
        int rows = this.GetRows();
        double[] result = new double[rows];
        for (int r = 0; r < rows; r++) {
            double item = 0.0;
            for (int k = 0; k < depth; k++) {
                item += this.GetItemInternal(r, k) * columnVector[k];
            }
            result[r] = item;
        }
        return result;
    }

    /**
     * Calculates matrix product of specified single row vector and this matrix.
     * Number of elements in the columnVector must equal to this.Columns.
     * @param rowVector Row vector to product with.
     * @throws IllegalArgumentException 
     */
    public double[] ProductInv(double[] rowVector) throws IllegalArgumentException {
        assert rowVector != null : "rowVector cannot be null.";
        int depth = rowVector.length;
        if (depth != this.GetRows()) throw new IllegalArgumentException("rowVector must have same number of elements as this matrix rows.");
        int columns = this.GetColumns();
        double[] result = new double[columns];
        for (int c = 0; c < columns; c++) {
            double item = 0.0;
            for (int k = 0; k < depth; k++) {
                item += this.GetItemInternal(k, c) * rowVector[k];
            }
            result[c] = item;
        }
        return result;
    }
    
    /**
     * Calculates matrix product of column and row vectors.
     * @param columnVector
     * @param rowVector
     * @return 
     */
    public static Matrix Product(double[] columnVector, double[] rowVector) throws IllegalArgumentException {
        assert columnVector != null : "columnVector matrix cannot be null.";
        assert rowVector != null : "rowVector matrix cannot be null.";
        Matrix result = new Matrix(columnVector.length, rowVector.length);
        for (int r = 0; r < columnVector.length; r++) {
            for (int c = 0; c < rowVector.length; c++) {
                result.SetItemInternal(r, c, columnVector[r] * rowVector[c]);
            }
        }
        return result;
    }
    
    public static Matrix Product(Matrix a, Matrix b) throws IllegalArgumentException {
        assert a != null : "Matrix a is null.";
        return a.Product(b);
    }
    
    public static double[] Product(double[] rowVector, Matrix m) throws IllegalArgumentException {
        assert m != null : "Matrix m is null.";
        return m.ProductInv(rowVector);
    }
    
    public static double[] Product(Matrix m, double[] columnVector) throws IllegalArgumentException {
        assert m != null : "Matrix m is null.";
        return m.Product(columnVector);
    }
    
    /**
     * Calculates matrix product of row and column vectors, which is the single double number.
     * Number of elements in both vectors must be the same.
     * @param rowVector
     * @param columnVector
     * @return
     * @throws IllegalArgumentException 
     */
    public static double ProductInv(double[] rowVector, double[] columnVector) throws IllegalArgumentException {
        assert rowVector != null : "rowVector matrix cannot be null.";
        assert columnVector != null : "columnVector matrix cannot be null.";
        int depth = rowVector.length;
        if (depth != columnVector.length) throw new IllegalArgumentException("Number of elements in rowVector and columnVector must be the same.");
        double result = 0.0;
        for (int k = 0; k < depth; k++) result += rowVector[k] * columnVector[k];
        return result;
    }
}
