package plugins;

import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.process.LUT;
import java.awt.Color;
import ij.IJ;
import ij.process.ImageProcessor;


public class Converter_RGB_Escala_Cinza_LUTs_ implements PlugIn {
	public void run(String arg) {
		
		ImagePlus imagem = IJ.getImage();
		
		//Checando se a imagem é RGB
		if (imagem.getType() == ImagePlus.COLOR_RGB) {
			gerar_imagens(imagem);
		}
		else {
			IJ.error("Imagem não é RGB");
		}
	}	
	
	
	public void gerar_imagens(ImagePlus imagem)
	{
		
		ImageProcessor processador = imagem.getProcessor();
		int largura_imagem = imagem.getWidth(), altura_imagem = processador.getHeight();
		
		
		ImagePlus redImage = IJ.createImage("Red_Cinza", "8-Bit", largura_imagem, altura_imagem, 1);
		ImagePlus greenImage = IJ.createImage("Green_Cinza", "8-Bit", largura_imagem, altura_imagem, 1);
		ImagePlus blueImage = IJ.createImage("Blue_Cinza", "8-Bit", largura_imagem, altura_imagem, 1);	
	
		
		ImageProcessor novaImagemRed = redImage.getProcessor();
		ImageProcessor novaImagemGreen = greenImage.getProcessor();;
		ImageProcessor novaImagemBlue = blueImage.getProcessor();;
		
		
		//Criando variavel para representar pixel 8-Bits
		int valorPixel[] = {0,0,0}, novoValorPixel;	
		
		for (int x = 0; x < largura_imagem; x++) {	
			for (int y = 0; y < altura_imagem; y++) {
				
				valorPixel = processador.getPixel(x, y, valorPixel);
				
				
				//Red
				novoValorPixel = valorPixel[0];
				novaImagemRed.putPixel(x, y, novoValorPixel);			
				
				//Green
				novoValorPixel = valorPixel[1];
				novaImagemGreen.putPixel(x, y, novoValorPixel);
				
				//Blue
				novoValorPixel = valorPixel[2];
				novaImagemBlue.putPixel(x, y, novoValorPixel);

			}
			
			//Set imagemPlus
			redImage.setProcessor(novaImagemRed);
			greenImage.setProcessor(novaImagemGreen);
			blueImage.setProcessor(novaImagemBlue);
			
			
			redImage.show();
			greenImage.show();
			blueImage.show();
			
			//Set Luts
			redImage.setLut(LUT.createLutFromColor(Color.RED));
			greenImage.setLut(LUT.createLutFromColor(Color.GREEN));
			blueImage.setLut(LUT.createLutFromColor(Color.BLUE));
			
		}	
	}
}