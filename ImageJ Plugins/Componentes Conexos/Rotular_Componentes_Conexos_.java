package plugins;

import java.util.ArrayList;
import java.util.List;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class Rotular_Componentes_Conexos_ implements PlugIn {

	@Override
	public void run(String arg) {
		ImagePlus imagem = IJ.getImage();

		if (imagem.getType() == ImagePlus.GRAY8) {
			processarImagem(imagem);
		} else {
			IJ.error("Imagem não é 8-Bits");
		}

	}

	public void processarImagem(ImagePlus imagemOriginal) {

		ImagePlus imagemRotulada = imagemOriginal.duplicate();
		ImageProcessor processadorOriginal = imagemOriginal.getProcessor();
		ImageProcessor processadorRotulado = imagemRotulada.getProcessor();
		int cor = 50;

		List<int[]> fila = new ArrayList<int[]>();

		int larguraImagem = imagemOriginal.getWidth(), alturaImagem = imagemOriginal.getHeight();

		zerarImagemRotulada(processadorRotulado, larguraImagem, alturaImagem);

		percorrerPixels(processadorOriginal, processadorRotulado, larguraImagem, alturaImagem, fila, cor);

		imagemRotulada.setProcessor(processadorRotulado);
		imagemRotulada.show();

	}

	void zerarImagemRotulada(ImageProcessor processadorRotulado, int larguraImagem, int alturaImagem) {
		for (int x = 0; x < larguraImagem ; x++) {
			for (int y = 0; y < alturaImagem ; y++) {
				processadorRotulado.putPixel(x, y, 0);
			}
		}
	}

	void percorrerPixels(ImageProcessor processadorOriginal, ImageProcessor processadorRotulado, int larguraImagem,
			int alturaImagem, List<int[]> fila, int cor) {

		for (int x = 0; x <= larguraImagem; x++) {
			for (int y = 0; y <= alturaImagem; y++) {

				if (processadorOriginal.getPixel(x, y) != 0 && processadorRotulado.getPixel(x, y) == 0) {

					addPixelFila(x, y, fila);
					executarFila(processadorOriginal, processadorRotulado, fila, alturaImagem, larguraImagem, cor);

					cor = cor + 50;
					if (cor > 255) {
						cor = 50;
					}

				}
			}
		}
	}

	void executarFila(ImageProcessor processadorOriginal, ImageProcessor processadorRotulado, List<int[]> fila,
			int alturaImagem, int larguraImagem, int cor) {
		
		while (fila.isEmpty() == false) {

			int x = fila.get(0)[0];
			int y = fila.get(0)[1];
			processadorRotulado.putPixel(x, y, cor);
			removerPixelFila(x, y, fila);
			pixelVizinho(processadorOriginal, processadorRotulado, x, y, alturaImagem, larguraImagem, fila);
		}
	}

	void pixelVizinho(ImageProcessor processadorOriginal, ImageProcessor processadorRotulado, int x, int y,
			int alturaImagem, int larguraImagem, List<int[]> fila) {

		if (x - 1 >= 0 && processadorOriginal.getPixel(x - 1, y) != 0
				&& processadorRotulado.getPixel(x - 1, y) == 0) {

			addPixelFila(x - 1, y, fila);
		}
		if (y - 1 >= 0  && processadorOriginal.getPixel(x, y - 1) != 0
				&& processadorRotulado.getPixel(x, y - 1) == 0) {

			addPixelFila(x, y - 1, fila);
		}
		if (x + 1 <= alturaImagem && processadorOriginal.getPixel(x + 1, y) != 0
				&& processadorRotulado.getPixel(x + 1, y) == 0) {

			addPixelFila(x + 1, y, fila);
		}
		if (y + 1 <= larguraImagem && processadorOriginal.getPixel(x, y + 1) != 0
				&& processadorRotulado.getPixel(x, y + 1) == 0) {

			addPixelFila(x, y + 1, fila);
		}
	}

	void addPixelFila(int x, int y, List<int[]> fila) {
		int[] pixel = { 0, 0 };

		pixel[0] = x;
		pixel[1] = y;

		fila.add(pixel);
	}

	void removerPixelFila(int x, int y, List<int[]> fila) {

		int tamanho = fila.size();
		for (int i = 0; i < tamanho; i++) {

			if (fila.get(i)[0] == x) {
				if (fila.get(i)[1] == y) {
					fila.remove(i);
					tamanho--;
				}
			}
		}
	}

}

 