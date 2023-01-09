package plugins;

import java.awt.AWTEvent;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class Funcoes_Binarias_ implements  PlugIn, DialogListener {
	public void run(String arg) 
	{
		ImagePlus imagem = IJ.getImage();
		
		if (imagem.getType() == ImagePlus.GRAY8) 
		{
			IG(imagem);	
		}
		else 
		{
			IJ.error("Imagem não é 8_bits");
		}
		
	}
	
	public void IG(ImagePlus imagem) {

		ImagePlus imagemCopia = imagem.duplicate();
		imagemCopia.show();
		
		String[] estrategia = {"DILATACAO","EROSAO","FECHAMENTO","ABERTURA", "OUTLINE"};
		GenericDialog interfaceGrafica = new GenericDialog("HSV");
		interfaceGrafica.addDialogListener(this);
		
	
		interfaceGrafica.addRadioButtonGroup("Escolha uma das opções:", estrategia, 1, 5, estrategia[0]);
		interfaceGrafica.showDialog();		
		
		if (interfaceGrafica.wasCanceled()) {
			IJ.showMessage("PlugIn cancelado!");
		}
		else {
			if (interfaceGrafica.wasOKed()) {
				processarImagem(interfaceGrafica, estrategia);
		        IJ.showMessage("Plugin encerrado com sucesso!");
			}
		}
	}
	
	@Override
	public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
		if (interfaceGrafica.wasCanceled()) return false;
		return true;
	}
	
	void processarImagem(GenericDialog interfaceGrafica, String[] estrategia) {
		String opcao = interfaceGrafica.getNextRadioButton();
		
		int idImagens[] = WindowManager.getIDList();
		ImagePlus imagemOriginal = WindowManager.getImage(idImagens[0]);
		ImagePlus imagem = WindowManager.getImage(idImagens[1]);
		ImageProcessor processadorOriginal = imagemOriginal.getProcessor();
		ImageProcessor processador = imagem.getProcessor();
		
		int larguraImagem = imagemOriginal.getWidth(), alturaImagem = imagemOriginal.getHeight(); 
		
		if(opcao == estrategia[0]) {
			dilatacao(processadorOriginal, processador, larguraImagem, alturaImagem);
		}
		else {
			if(opcao == estrategia[1]) {
				erosao(processadorOriginal, processador, larguraImagem, alturaImagem);
			}
			else {
				if(opcao == estrategia[2]) {
					ImageProcessor processadorAuxiliar = processador.duplicate();
					fechamento(processadorOriginal, processador, processadorAuxiliar, larguraImagem, alturaImagem);
				}
				else {
					if(opcao == estrategia[3]) {
						ImageProcessor processadorAuxiliar = processador.duplicate();
						abertura(processadorOriginal, processador, processadorAuxiliar, larguraImagem, alturaImagem);
					}
					else {
						outline(processadorOriginal, processador, larguraImagem, alturaImagem);
					}
				}
	 		}
		}
		
		System.out.print(opcao);
		imagem.setProcessor(processador);
		imagem.show();
		
	}
	
	
	void dilatacao(ImageProcessor processadorOriginal,ImageProcessor processador, int larguraImagem, int alturaImagem) {
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorOriginal.getPixel(x, y) == 255) {
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							processador.putPixel(x+a, y+b, 255);
						
						}
					}
				}
				
			}
		}	
	}
	
	void erosao(ImageProcessor processadorOriginal, ImageProcessor processador, int larguraImagem, int alturaImagem) {
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorOriginal.getPixel(x, y) == 255) {
					
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							if(processadorOriginal.getPixel(x+a, y+b) == 0) {
								
								processador.putPixel(x, y, 0);
							}
						
						}
					}
				}
				
			}
		}	
	}
	
	void fechamento(ImageProcessor processadorOriginal, ImageProcessor processador,ImageProcessor processadorAuxiliar , int larguraImagem, int alturaImagem) {
		
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorOriginal.getPixel(x, y) == 255) {
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							processador.putPixel(x+a, y+b, 255);
						
						}
					}
				}
				
			}
		}
		
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorAuxiliar.getPixel(x, y) == 0) {
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							processador.putPixel(x+a, y+b, 0);
						
						}
					}
				}
				
			}
		}
	}
	
	void abertura(ImageProcessor processadorOriginal, ImageProcessor processador,ImageProcessor processadorAuxiliar , int larguraImagem, int alturaImagem) {
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorOriginal.getPixel(x, y) == 0) {
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							processadorAuxiliar.putPixel(x+a, y+b, 0);
							processador.putPixel(x+a, y+b, 0);
						
						}
					}
				}
				
			}
		}
		
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorAuxiliar.getPixel(x, y) == 255) {
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							processador.putPixel(x+a, y+b, 255);
						
						}
					}
				}
				
			}
		}
	}

	void outline(ImageProcessor processadorOriginal,ImageProcessor processador, int larguraImagem, int alturaImagem) {
		int valorPixel=0;
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				
				if(processadorOriginal.getPixel(x, y) == 255) {
					
					for(int a=-1; a<=1; a++) {
						for(int b=-1; b<=1; b++) {
							
							if(processadorOriginal.getPixel(x+a, y+b) == 0) {
								
								processador.putPixel(x, y, 0);
							}
						
						}
					}
				}
				
			}
		}
		
		for (int x = 1; x < larguraImagem-1; x++) {	
			for (int y = 1; y < alturaImagem-1; y++) {
				valorPixel =  processadorOriginal.getPixel(x, y) - processador.getPixel(x, y) ;
				processador.putPixel(x, y, valorPixel);
				
			}
		}	
	}
}