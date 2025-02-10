/*
 * Project 1: Image Processor for Content-Based Image Retrieval System (CBIR)
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Date;

public class ImageProcessor
{
  private final String imageLocationFormat = "images/%d.jpg";
  private final int totalImageCount;
  private final int intensityBins;
  private final int colorCodeBins;

  // Array to store ImageInfo objects for each image
  private ImageInfo imageInfoArray[];

  /*Each image is retrieved from the file.  The height and width are found for the image and the getIntensity and
   * getColorCode methods are called.
  */

  // Constructor to initialize ImageProcessor
  public ImageProcessor(int totalImageCount, int intensityBins, int colorCodeBins)
  {
    this.totalImageCount = totalImageCount;
    this.intensityBins = intensityBins;
    this.colorCodeBins = colorCodeBins;
    this.imageInfoArray = new ImageInfo[totalImageCount];
  }

  // Get ImageInfo for a specific image
  public ImageInfo getImageInfo(int picNum) {
    return this.imageInfoArray[picNum];
  }

  // Process images and populate imageInfoArray
  public void processImages() throws Exception {
    int imageNum = 0;
    while (imageNum < this.totalImageCount) {
      try {
        System.out.println("Processing image: " + imageNum + " time: " + new Date());

        // Build the path to the image file
        String imagePath = String.format(imageLocationFormat, imageNum + 1);

        // Read the image from the resource stream
        BufferedImage image = ImageIO.read(getClass().getResourceAsStream(imagePath));

        // Process the image and store its information
        this.imageInfoArray[imageNum] = processImage(imageNum + 1, image);

        imageNum++;
      } catch (Exception ex) {
        System.out.println("Encountered exception while processing images " + ex);
        throw ex;
      }
    }
  }
  
  ///////////////////////////////////////////////
  //add other functions you think are necessary//
  ///////////////////////////////////////////////

  // Process a single image and return its ImageInfo
  private ImageInfo processImage(int picNum, BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double[] intensityBins = new double[this.intensityBins];
    double[] colorCodeBins = new double[this.colorCodeBins];

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int intensityBin = getIntensity(image, i, j);
        intensityBins[intensityBin]++;

        int colorCodeBin = getColorCode(image, i, j);
        colorCodeBins[colorCodeBin]++;
      }
    }
    // Create and return an ImageInfo object with processed information
    return new ImageInfo(picNum,
            height,
            width,
            intensityBins,
            colorCodeBins);
  }

  // Calculate the intensity bin for a pixel in an image
  private int getIntensity(BufferedImage image, int i, int j) {
    int pixel = image.getRGB(i, j);
    int red = ((pixel >> 16) & 0xFF);
    int green = ((pixel >> 8) & 0xFF);
    int blue = (pixel & 0xFF);

    // Calculate intensity using a weighted formula
    double intensity = (0.299 * red) + (0.587 * green) + (0.114 * blue);

    // Map the intensity to a bin (0-24)
    if (intensity > 240) {
      return 24;
    } else {
      return (int) intensity / 10;
    }
  }


  // Calculate the color code bin for a pixel in an image
  private int getColorCode(BufferedImage image, int i, int j) {
    int rgb = image.getRGB(i, j);

    // Extract the red, green, and blue components
    int red = (rgb >> 16) & 0xC0;  // Mask and shift for the red component
    int green = (rgb >> 8) & 0xC0; // Mask and shift for the green component
    int blue = rgb & 0xC0;         // Mask for the blue component

    // Calculate the color code bin using bit manipulation
    return ((red >> 6) << 4) | ((green >> 6) << 2) | (blue >> 6);
  }
}
