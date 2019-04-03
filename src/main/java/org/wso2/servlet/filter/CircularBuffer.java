package org.wso2.servlet.filter;

public class CircularBuffer {
    private volatile long[] data;
    private final int size;
    private volatile int current;

    public CircularBuffer(int size){
        this.size = size;
        data = new long[size];

        for(int i = 0; i<size; i++)
            data[i] = -1;

        current = 0;
    }

    public synchronized void add(long value){
        data[current] = value;
        current = (++current)%size;
    }

    public float getIAR(){
        // Check if there's only one or zero
        if (data[1] == -1)
            return 0;

        // check if partially filled
        if (data[current] == -1){
            float sum = 0;
            for(int i = 1; i < current; i ++){
                sum += data[i] - data[i-1];
            }

            return sum / (current - 1);
        }

        // if the array is filled
        float sum = 0;
        for(int i = 0; i < size; i ++){
            sum += data[(i+current+1)%size] - data[(i+current)%size];
        }
        return sum / size;
    }

}
