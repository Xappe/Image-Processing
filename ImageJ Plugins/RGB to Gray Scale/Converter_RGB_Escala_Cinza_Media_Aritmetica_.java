package plugins;

import ij.plugin.PlugIn;
import ij.ImagePlus;

import ij.IJ;
import ij.process.ImageProcessor;


public class Converter_RGB_Escala_Cinza_Media_Aritmetica_ implements PlugIn {
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
		
		
		ImagePlus image= IJ.createImage("Escala_Cinza", "8-Bit", largura_imagem, altura_imagem, 1);
		
		ImageProcessor novaImagem = image.getProcessor();
		
		
		//Criando variavel para representar pixel 8-Bits
		int valorPixel[] = {0,0,0}, novoValorPixel;	
		
		for (int x = 0; x < largura_imagem; x++) {	
			for (int y = 0; y < altura_imagem; y++) {
				
				valorPixel = processador.getPixel(x, y, valorPixel);
				
				//Media
				novoValorPixel = ((valorPixel[0]+valorPixel[1]+valorPixel[2])/3);
				novaImagem.putPixel(x, y, novoValorPixel);			
				
			}
			
			//Set imagemPlus
			image.setProcessor(novaImagem);
				
			image.show();		
			
		}	
	}
}