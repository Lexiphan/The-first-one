/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralNet;

import NeuralNet.Math.*;
import java.util.function.*;

/**
 *
 * @author lexiphan
 */
public class Net {
    private int[] m_LayerNeurons;
    private Matrix[] m_LayerWeights = null;
    private boolean m_NeedCreateWeights = true;

    public Net(int additionalLayers) throws IllegalArgumentException {
        this(1, 1, additionalLayers);
    }
    
    public Net(int inputs, int outputs, int additionalLayers) throws IllegalArgumentException {
        if (inputs <= 0) throw new IllegalArgumentException("Number of inputs cannot be zero or negative.");
        if (outputs <= 0) throw new IllegalArgumentException("Number of outputs cannot be zero or negative.");
        if (additionalLayers < 0) throw new IllegalArgumentException("Number of additional layers cannot be negative.");
        if (additionalLayers > 100) throw new IllegalArgumentException("Number of additional layers is too big (> 100).");
        
        m_LayerNeurons = new int[additionalLayers + 2];
        m_LayerNeurons[0] = inputs;
        m_LayerNeurons[++additionalLayers] = outputs;
        for (int i = 1; i < additionalLayers; i++) {
            m_LayerNeurons[i] = inputs + (outputs - inputs) * i / additionalLayers;
        }
    }

    public Net(int[] layerNeurons) throws IllegalArgumentException {
        if (layerNeurons == null) throw new IllegalArgumentException("Layer neurons array cannot be null.");
        if (layerNeurons.length < 2) throw new IllegalArgumentException("Number of layers cannot be less than 2.");
        for (int i = 0; i < layerNeurons.length; i++) {
            if (layerNeurons[i] <= 0) throw new IllegalArgumentException("Number of neurons cannot be zero or negative.");
        }
        m_LayerNeurons = (int[])layerNeurons.clone();
    }
    
    
    
    public int GetTotalNumberOfLayers() {
        return m_LayerNeurons.length;
    }
    
    public int GetNumberOfAdditionalLayers() {
        return m_LayerNeurons.length - 2;
    }
    
    public int GetNumberOfNeurons(int layer) throws IllegalArgumentException {
        if (layer < 0 && layer >= m_LayerNeurons.length) throw new IllegalArgumentException("Layer must be in range from 0 to NumberOfLayers-1.");
        return m_LayerNeurons[layer];
    }
    
    public int GetInputs() {
        return m_LayerNeurons[0];
    }
    
    public int GetOutputs() {
        return m_LayerNeurons[m_LayerNeurons.length - 1];
    }
    
    public void SetInputs(int inputs) throws IllegalArgumentException {
        SetNumberOfNeurons(0, inputs);
    }
    
    public void SetOutputs(int outputs) throws IllegalArgumentException {
        SetNumberOfNeurons(m_LayerNeurons.length - 1, outputs);
    }
    
    public void SetNumberOfNeurons(int layer, int neurons) {
        if (layer < 0 && layer >= m_LayerNeurons.length) throw new IllegalArgumentException("Layer must be in range from 0 to NumberOfLayers-1.");
        if (neurons <= 0) throw new IllegalArgumentException("Number of neurons cannot be zero or negative.");
        m_LayerNeurons[layer] = neurons;
        if (m_LayerWeights != null)
        {
            if (layer > 0) m_LayerWeights[layer - 1] = null;
            if (layer < m_LayerNeurons.length) m_LayerWeights[layer] = null;
        }
        m_NeedCreateWeights = true;
    }
    
    private void ValidateWeights()
    {
        if (m_NeedCreateWeights)
        {
            if (m_LayerWeights == null) m_LayerWeights = new Matrix[m_LayerNeurons.length - 1];
            for (int i = 0; i < m_LayerWeights.length; i++)
                if (m_LayerWeights[i] == null)
                    m_LayerWeights[i] = new Matrix(m_LayerNeurons[i], m_LayerNeurons[i + 1]);
            m_NeedCreateWeights = false;
        }
    }
    
    public void InitializeWeights() {
        ValidateWeights();
        for (int i = 0; i < m_LayerWeights.length; i++)
        {
            Matrix m = m_LayerWeights[i];
            m.Randomize();
            m.Multiply(0.99 / m.GetColumns());
            m.Add(0.01 / m.GetColumns());
        }
    }
    
    private double NeuronFunction(double input) {
        return 1.0 / (1.0 + Math.exp(input));
    }
    
    private double NeuronFunctionInv(double input) {
        return Math.log(1.0 / input - 1.0);
    }
    
    public void Train(double[] inputValues, double[] outputValues) {
        
    }
    
    public double[] Calculate(double[] inputValues) {
        assert inputValues != null;
        assert inputValues.length == this.GetInputs();
        if (m_LayerWeights == null || m_NeedCreateWeights) throw new IllegalStateException("Weights are not assigned. You need to train the network before calculate.");
        Matrix result = Matrix.SingleRow(inputValues);
        for (int i = 0; i < m_LayerWeights.length; i++) {
            result = result.Product(m_LayerWeights[i]);
            result.ApplyFunction(this::NeuronFunction);
        }
        return result.ToArray();
    }
    
    public double[] ReversedCalculate(double[] outputValues) {
        assert outputValues != null;
        assert outputValues.length == this.GetOutputs();
        if (m_LayerWeights == null || m_NeedCreateWeights) throw new IllegalStateException("Weights are not assigned. You need to train the network before calculate.");
        Matrix result = Matrix.SingleRow(outputValues);
        for (int i = 0; i < m_LayerWeights.length; i++) {
            result.ApplyFunction(this::NeuronFunctionInv);
            result = result.Product(m_LayerWeights[i].Inverse());
        }
        return result.ToArray();
    }
}
