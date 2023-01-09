package plugins;

import ij.plugin.PlugIn;
import ij.ImagePlus;
import ij.WindowManager;
import ij.IJ;
import ij.process.ImageProcessor;


public class Converter_8bits_RGB_ implements PlugIn {
	public void run(String arg) {
			
		//Checando se há 3 imagens abertas
		if(WindowManager.getWindowCount() == 3) {
			int idImagens[] = WindowManager.getIDList();
			ImagePlus imagemRed = WindowManager.getImage(idImagens[0]);
			ImagePlus imagemGreen = WindowManager.getImage(idImagens[1]);
			ImagePlus imagemBlue = WindowManager.getImage(idImagens[2]);
			
			//Checando se as imagens são 8-bits
			if(imagemRed.getType() == ImagePlus.GRAY8 && imagemRed.getType() == ImagePlus.GRAY8 && imagemRed.getType() == ImagePlus.GRAY8) {
				gerar_imagem(imagemRed, imagemGreen, imagemBlue);
			}else {
				IJ.error("Imagens não são 8-Bits");
			}
		}else {
			IJ.error("Deve haver 3 imagens abertas!");
		}
	}	
	
	public void gerar_imagem(ImagePlus imagemRed, ImagePlus imagemGreen, ImagePlus imagemBlue)
	{
	
		ImageProcessor processadorRed = imagemRed.getProcessor();
		ImageProcessor processadorGreen = imagemGreen.getProcessor();
		ImageProcessor processadorBlue = imagemBlue.getProcessor();
		
		int largura_imagem = imagemRed.getWidth(), altura_imagem = imagemRed.getHeight();
		
		ImagePlus novaImagem = IJ.createImage("RGB", "RGB", largura_imagem, altura_imagem, 1);
		ImageProcessor novoProcessador = novaImagem.getProcessor();
		
		//Criando array de um pixel RGB
		int valorPixel[]= {0}, novoValorPixel[] = {0,0,0};
		
		//Escaneando cada pixel das imagens
		for (int x = 0; x < largura_imagem; x++) {	
			for (int y = 0; y < altura_imagem; y++) {
				
				//Lendo o valor de cada pixel e redirecionand para cada canal RGB
				
				valorPixel = processadorRed.getPixel(x, y, valorPixel);
				novoValorPixel[0] = valorPixel[0];
				
				valorPixel = processadorGreen.getPixel(x, y, valorPixel);
				novoValorPixel[1] = valorPixel[0];
				
				valorPixel = processadorBlue.getPixel(x, y, valorPixel);
				novoValorPixel[2] = valorPixel[0];
				
				//Colocando o novo array RGB
				novoProcessador.putPixel(x, y, novoValorPixel);
				
			}
		}
		novaImagem.setProcessor(novoProcessador);
		novaImagem.show();
				
	}
}




	