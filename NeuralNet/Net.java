/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralNet;

/**
 *
 * @author lexiphan
 */
public class Net {
    private int[] m_LayerNeurons;

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
        if (inputs <= 0) throw new IllegalArgumentException("Number of inputs cannot be zero or negative.");
        m_LayerNeurons[0] = inputs;
    }
    
    public void SetOutputs(int outputs) throws IllegalArgumentException {
        if (outputs <= 0) throw new IllegalArgumentException("Number of outputs cannot be zero or negative.");
        m_LayerNeurons[m_LayerNeurons.length - 1] = outputs;
    }
    
    public void SetNumberOfNeurons(int layer, int neurons) {
        if (layer < 0 && layer >= m_LayerNeurons.length) throw new IllegalArgumentException("Layer must be in range from 0 to NumberOfLayers-1.");
        if (neurons <= 0) throw new IllegalArgumentException("Number of neurons cannot be zero or negative.");
        m_LayerNeurons[layer] = neurons;
    }
    
    public void InitializeWeights() {
        
    }
    
    public void Train(double[] inputValues, double[] outputValues) {
        
    }
    
    public double[] Calculate(double[] inputValues){
        return null;
    }
    
    public double[] ReversedCalculate(double[] outputValues) {
        return null;
    }
}
