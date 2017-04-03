import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.BufferOverflowException;

/**
 * Created by EduardoKarakawa on 20/03/2017.
 */
public class Exercicios {

    public int[] histogram(BufferedImage img){
        int cont[] = new int[256];
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                cont[new Color(img.getRGB(x, y)).getRed()] += 1;
            }
        }

    return cont;
    }


    public int[] acumhistogram(int[] histogram){
        int acum[] = histogram;
        for(int i = 1; i < acum.length; i++){
            acum[i] += acum[i - 1];
        }
        return acum;
    }


    public BufferedImage drawhistogram(int[] histogram){
        BufferedImage histogramimg = new BufferedImage(512, 600, BufferedImage.TYPE_INT_RGB);
        float tmp[] = new float[histogram.length];
        for(int i = 0; i < histogram.length; i++){
            tmp[i] = (float)histogram[i];
        }

        for(int x = 0; x < histogramimg.getWidth(); x++){
            if((x > 128) && (x < 384))
                tmp[x - 128] = tmp[x - 128] / 500.f;

            for(int y = histogramimg.getHeight()-1; y > 0; y--){
                histogramimg.setRGB(x, y, new Color(255,255,255).getRGB());

                if((x > 128) && (x < 384)){
                    if(tmp[x - 128] > 0){
                        tmp[x - 128]-= 1;
                        histogramimg.setRGB(x, y, new Color(255,0,0).getRGB());
                    }
                }

            }

        }

        return histogramimg;
    }


    public BufferedImage equalize(BufferedImage img){
        int acumhistogram[] = acumhistogram(histogram(img));
        int newacum[] = new int[256];
        int hmin = 0;
        for(int i = 0; i < 256; i++){
            if((acumhistogram[i] != 0) && (hmin == 0)){
                hmin = acumhistogram[i];
                break;
            }
        }
        float tam = img.getHeight() * img.getWidth() - 1;
        for(int i = 0; i < 255; i++){
            newacum[i] = Math.round(((acumhistogram[i] - hmin) / tam)*255.f);


        }

        BufferedImage newimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                int tmp = newacum[new Color(img.getRGB(x, y)).getRed()];
                newimg.setRGB(x, y, new Color(tmp, tmp , tmp).getRGB());

            }
        }
        return newimg;
    }


    public BufferedImage equalizeSaturation(BufferedImage img){
        float h=1, s=1, b=1;
        Color test = new Color(img.getRGB(0, 0));
        float t[] = new float[3];
        Color.RGBtoHSB(test.getRed(),test.getGreen(),test.getBlue(),t);
        System.out.println(t[0] + "  " + t[1] + "  " + t[2]);
        return img;
    }

    public void run() throws IOException {
        BufferedImage img = ImageIO.read(new File("C:\\Users\\B155 FIRE V3\\Downloads\\Image\\img\\cor\\lara.png"));
        int histog[] = histogram(img);

        equalizeSaturation(img);
        BufferedImage histogramimg = drawhistogram(histog);
        ImageIO.write(histogramimg,"png", new File("crowdhistogram.png"));

        BufferedImage equalizeimg = equalize(img);
        ImageIO.write(equalizeimg, "png", new File("crowequalize.png"));
    }

    public static void main(String[] args) throws IOException{
        new Exercicios().run();
    }

}
