package plugins;

import java.awt.AWTEvent;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class Gerar_HSV implements  PlugIn, DialogListener{
	public void run(String arg) {
		ImagePlus imagem = IJ.getImage();
		
		if (imagem.getType() == ImagePlus.COLOR_RGB) {
			IG(imagem);	
		}
		else {
			IJ.error("Imagem não é RGB");
		}
		
	}
	
	public void IG(ImagePlus imagem) {

		ImagePlus imagemCopia = imagem.duplicate();
		imagemCopia.show();
		
		
		GenericDialog interfaceGrafica = new GenericDialog("HSV");
		interfaceGrafica.addDialogListener(this);
		
		interfaceGrafica.addMessage("MATIZ");
		interfaceGrafica.addSlider("Max", 0, 360, 360, 1);
		interfaceGrafica.addSlider("Min", 0, 360, 0, 1);
		interfaceGrafica.addMessage("SATURAÇÃO");
		interfaceGrafica.addSlider("Max", 0, 1, 1, 0.1);
		interfaceGrafica.addSlider("Min", 0, 1, 0, 0.1);
		interfaceGrafica.addMessage("BRILHO");
		interfaceGrafica.addSlider("Max", 0, 1, 1, 0.1);		
		interfaceGrafica.addSlider("Min", 0, 1, 0, 0.1);
		
		interfaceGrafica.showDialog();		
		
		if (interfaceGrafica.wasCanceled()) {
			IJ.showMessage("PlugIn cancelado!");
		}
		else {
			if (interfaceGrafica.wasOKed()) {
				
		        IJ.showMessage("Plugin encerrado com sucesso!");
			}
		}
	}
	
	@Override
	public boolean dialogItemChanged(GenericDialog interfaceGrafica, AWTEvent e) {
		if (interfaceGrafica.wasCanceled()) return false;
		
		processarImagem(interfaceGrafica);
        
		return true;
	}
	
	public void processarImagem(GenericDialog interfaceGrafica) {
		int idImagens[] = WindowManager.getIDList();
		ImagePlus imagemOriginnal = WindowManager.getImage(idImagens[0]);
		ImagePlus imagem = WindowManager.getImage(idImagens[1]);
		ImageProcessor processadorOriginal = imagemOriginnal.getProcessor();
		ImageProcessor processador = imagem.getProcessor();
		
		int larguraImagem = imagem.getWidth(), alturaImagem = imagem.getHeight(), valorPixel[] = {0,0,0};
		
		double[]  HSV_max = {360,1,1}, HSV_min= {0,0,0};
			
		HSV_max[0] = interfaceGrafica.getNextNumber();
		HSV_min[0] = interfaceGrafica.getNextNumber();
		HSV_max[1] = interfaceGrafica.getNextNumber();
		HSV_min[1] = interfaceGrafica.getNextNumber();
		HSV_max[2] = interfaceGrafica.getNextNumber();
		HSV_min[2]= interfaceGrafica.getNextNumber();
		
		
		for (int x = 0; x < larguraImagem; x++) {	
			for (int y = 0; y < alturaImagem; y++) {
				processadorOriginal.getPixel(x, y, valorPixel);
				calcular(HSV_max, HSV_min, valorPixel);
				processador.putPixel(x, y, valorPixel);
			}
		}
		
		imagem.setProcessor(processador);
		imagem.show();
			
	}
	
	
	
	
	public void calcular(double[] HSV_max, double[] HSV_min, int[] valorPixel){

		double[] pixelHSV = {0,0,0};
		
		double MAX=0, MIN=0;
		
		
		//Normalizando 
		pixelHSV[0] = valorPixel[0]*1.0/255.0; 
		pixelHSV[1] = valorPixel[1]*1.0/255.0; 
		pixelHSV[2] = valorPixel[2]*1.0/255.0; 
		
		
		//Convertendo RGB para HSV
		
		
		if(pixelHSV[0]>=pixelHSV[1] && pixelHSV[0]>=pixelHSV[2]) {
			MAX = pixelHSV[0];
		}else {
			if(pixelHSV[1]>=pixelHSV[0] && pixelHSV[1]>=pixelHSV[2]) {
				MAX = pixelHSV[1];
			}else{
				MAX = pixelHSV[2];
			}		
		}
		

		if(pixelHSV[0]<=pixelHSV[1 ]&& pixelHSV[0]<=pixelHSV[2]) {
			MIN = pixelHSV[0];
		}else {
			if(pixelHSV[1]<=pixelHSV[0] && pixelHSV[1]<=pixelHSV[2]) {
				MIN = pixelHSV[1];
			}else{
				MIN = pixelHSV[2];
			}		
		}
		
		//H
		
		if(pixelHSV[0]==MAX && pixelHSV[1]>=pixelHSV[2]) {
			pixelHSV[0] = 60*(pixelHSV[1]-pixelHSV[2])/(MAX-MIN);
		}else {
			if(pixelHSV[0]==MAX && pixelHSV[1]<pixelHSV[2]) {
				pixelHSV[0] = (60*(pixelHSV[1]-pixelHSV[2])/(MAX-MIN))+360;
			}else {
				if(pixelHSV[1]==MAX) {
					pixelHSV[0] = (60*(pixelHSV[2]-pixelHSV[0])/(MAX-MIN))+120;
				}else {
					pixelHSV[0] = (60*(pixelHSV[0]-pixelHSV[1])/(MAX-MIN))+240;
				}
			}
		}
		
		//S
		
		if(MAX>0) {
			pixelHSV[1]=(MAX-MIN)/MAX;
		}else {
			pixelHSV[1]=0;
		}
		
		//V
		pixelHSV[2] = MAX;
		
				
		//Checagem
		
		if(pixelHSV[0] > HSV_max[0]) {
			valorPixel[0] = 255;
			valorPixel[1] = 255;
			valorPixel[2] = 255;
			
		}
		
		if(pixelHSV[1]> HSV_max[1] ) {
		
			valorPixel[0] = 255;
			valorPixel[1] = 255;
			valorPixel[2] = 255;
			
		}
		
		if(pixelHSV[2]> HSV_max[2]) {
		
			valorPixel[0] = 255;
			valorPixel[1] = 255;
			valorPixel[2] = 255;
		}
		
		
		if(pixelHSV[0]< HSV_min[0] ) {
			valorPixel[0] = 255;
			valorPixel[1] = 255;
			valorPixel[2] = 255;
		}
		
		if(pixelHSV[1]< HSV_min[1]) {
			valorPixel[0] = 255;
			valorPixel[1] = 255;
			valorPixel[2] = 255;
		}
		
		if(pixelHSV[2]< HSV_min[2]) {
			valorPixel[0] = 255;
			valorPixel[1] = 255;
			valorPixel[2] = 255;
		}
	}
}