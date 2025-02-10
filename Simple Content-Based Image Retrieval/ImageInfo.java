/*
 * Project 1:  Image Information for Content-Based Image Retrieval System
 */

// This class represents the information associated with an image
public class ImageInfo {

    // Image number
    final public int picNum;

    // Height and width of the image
    final public int height;
    final public int width;

    // Arrays to store intensity and color code bins
    final public double intensityBins[];
    final public double colorCodeBins[];

    // Distance value for similarity comparison
    public double distance;


    // Constructor to initialize ImageInfo with specified values
    public ImageInfo(int picNum, int h, int w, double[] intensityBins, double[] colorCodeBins) {
        this.picNum = picNum;
        this.height = h;
        this.width = w;
        this.intensityBins = intensityBins;
        this.colorCodeBins = colorCodeBins;
        this.distance = 0;
    }


    // Constructor to create a copy of an existing ImageInfo object
    public ImageInfo(ImageInfo copy) {
        this.picNum = copy.picNum;
        this.height = copy.height;
        this.width = copy.width;
        this.intensityBins = copy.intensityBins;
        this.colorCodeBins = copy.colorCodeBins;
        this.distance = copy.distance;
    }
}
