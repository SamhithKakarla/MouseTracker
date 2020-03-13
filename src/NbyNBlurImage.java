import processing.core.PApplet;

import javax.swing.*;

public class NbyNBlurImage implements PixelFilter {
    int size;
    private short[][] BOX_BLUR;
    int kernelWeigth;

    public NbyNBlurImage(int size){
        this.size = size;
        BOX_BLUR = makeBigGrid(size, size);
        kernelWeigth = countKernalWeight(BOX_BLUR);
    }



    @Override
    public DImage processImage(DImage img) {
        short[][] pixels = img.getBWPixelGrid();
        short[][] newGrid = new short[pixels.length][pixels[0].length];

        for (int row = ((size - 1)/2); row < pixels.length - ((size - 1)/2); row++) {
            for (int col = ((size - 1)/2); col < pixels[0].length - ((size - 1)/2); col++) {
                int output = 0;
                for (int r = -((size - 1)/2); r < ((size - 1)/2) + 1; r++) {
                    for (int c = -((size - 1)/2); c < ((size - 1)/2) + 1; c++) {

                        int KernelVal = BOX_BLUR[r + ((size - 1)/2)][c + ((size - 1)/2)];
                        int pixelval = pixels[row + r][col + c];

                        output += KernelVal*pixelval;
                    }
                }
                if(kernelWeigth == 0) kernelWeigth = 1;
                output = output / kernelWeigth;
                if( output < 0) output = 0;
                if( output > 255) output = 255;
                newGrid[row][col] = (short)output;
            }
        }

        img.setPixels(newGrid);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {

    }

    private short[][] makeBigGrid(int a, int b){
        short[][] output = new short[a][b];
        for (int i = 0; i < a - 1; i++) {
            for (int j = 0; j < b - 1; j++) {
                output[i][j] = 1;
            }
        }

        return output;
    }

    private int countKernalWeight(short[][] grid){
        int total = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                total += grid[i][j];
            }
        }
        return total;

    }
}
