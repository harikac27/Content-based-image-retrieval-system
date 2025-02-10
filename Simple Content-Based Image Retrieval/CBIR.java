/* Project 1 : Content-Based Image Retrieval System (CBIR)
*/

// Import necessary libraries
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.net.URL;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.*;

public class CBIR extends JFrame {

    // JLabel to display the selected image
    private JLabel photographLabel = new JLabel();  //container to hold a large 

    // Array of JPanel for displaying images
    private JPanel[] imagePanelArray = new JPanel[101]; //creates an array of JPanel

    // Array to keep track of the order of displayed images
    private int[] buttonOrder = new int[101]; //creates an array to keep up with the image order

    // Array to store image sizes
    private double[] imageSize = new double[101]; //keeps up with the image sizes
    private GridLayout gridLayout1;
    private GridLayout gridLayout2;
    private GridLayout gridLayout3;
    private GridLayout gridLayout4;
    private JPanel panelBottom1;
    private JPanel panelBottom2;
    private JPanel panelTop;
    private JPanel buttonPanel;

    // Matrices to store image features
    private Double[][] intensityMatrix = new Double[101][26];
    private Double[][] colorCodeMatrix = new Double[100][64];
    private static ImageProcessor imageProcessor;

    // Variables to keep track of image and page numbers
    int picNo = 0;
    int imageCount = 1; //keeps up with the number of images displayed since the first page.
    int pageNo = 1;


    public static void main(String args[]) {

        // Create an ImageProcessor and process images
        imageProcessor = new ImageProcessor(100, 25,64);
        try {
            imageProcessor.processImages();
        } catch (Exception ex) {
            return;
        }

        // Create and display the CBIR application
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CBIR app = new CBIR();
                app.setVisible(true);
            }
        });
    }


    public CBIR() {
        //The following lines set up the interface including the layout of the buttons and JPanels.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Icon Demo: Please Select an Image");
        panelBottom1 = new JPanel();
        panelBottom2 = new JPanel();
        panelTop = new JPanel();
        buttonPanel = new JPanel();
        gridLayout1 = new GridLayout(4, 5, 5, 5);
        gridLayout2 = new GridLayout(2, 1, 5, 5);
        gridLayout3 = new GridLayout(1, 2, 5, 5);
        gridLayout4 = new GridLayout(2, 3, 5, 5);
        setLayout(gridLayout2);
        panelBottom1.setLayout(gridLayout1);
        panelBottom2.setLayout(gridLayout1);
        panelTop.setLayout(gridLayout3);
        add(panelTop);
        add(panelBottom1);
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(gridLayout4);
        panelTop.add(photographLabel);

        panelTop.add(buttonPanel);
        JButton previousPage = new JButton("Previous Page");
        JButton nextPage = new JButton("Next Page");
        JButton intensity = new JButton("Intensity");
        JButton colorCode = new JButton("Color Code");
        buttonPanel.add(previousPage);
        buttonPanel.add(nextPage);
        buttonPanel.add(intensity);
        buttonPanel.add(colorCode);

        nextPage.addActionListener(new nextPageHandler());
        previousPage.addActionListener(new previousPageHandler());
        intensity.addActionListener(new intensityHandler());
        colorCode.addActionListener(new colorCodeHandler());
        setSize(1100, 750);
        // this centers the frame on the screen
        setLocationRelativeTo(null);

        /*This for loop goes through the images in the database and stores them as icons and adds
         * the images to JButtons and then to the JButton array
         */

        for (int i = 1; i < 101; i++) {
            ImageIcon icon;
            URL url = getClass().getResource("images/" + i + ".jpg");
            icon = new ImageIcon(url);

            if (icon != null) {
                JButton imageButton = new JButton(icon);

                // Create a JPanel to hold the button and label
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(imageButton, BorderLayout.CENTER);

                // Create a JLabel to display the image number
                JLabel label = new JLabel(Integer.toString(i)+".jpg");
                label.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the label

                // Add the label below the button
                panel.add(label, BorderLayout.SOUTH);

                // Add an ActionListener to the button (this is the existing line)
                imageButton.addActionListener(new IconButtonHandler(i, icon));

                buttonOrder[i] = i;
                imagePanelArray[i] = panel;

                // Add the panel to the main panelBottom1
                panelBottom1.add(panel);


            }
        }

        displayFirstPage();
    }

    /*This method displays the first twenty images in the panelBottom.  The for loop starts at number one and gets the image
     * number stored in the buttonOrder array and assigns the value to imageButNo.  The button associated with the image is
     * then added to panelBottom1.  The for loop continues this process until twenty images are displayed in the panelBottom1
     */
    private void displayFirstPage() {
        int imageButNo = 0;
        panelBottom1.removeAll();
        for (int i = 1; i < 21; i++) {
            //System.out.println(button[i]);
            imageButNo = buttonOrder[i];
            panelBottom1.add(imagePanelArray[imageButNo]);
            imageCount++;
        }
        panelBottom1.revalidate();
        panelBottom1.repaint();

    }

    /*This class implements an ActionListener for each iconButton.  When an icon button is clicked, the image on the
     *button is added to the photographLabel and the picNo is set to the image number selected and being displayed.
     */
    private class IconButtonHandler implements ActionListener {
        int pNo = 0;
        ImageIcon iconUsed;

        IconButtonHandler(int i, ImageIcon j) {
            pNo = i;
            iconUsed = j;  //sets the icon to the one used in the button
        }

        public void actionPerformed(ActionEvent e) {
            photographLabel.setIcon(iconUsed);
            picNo = pNo;
        }

    }

    /*This class implements an ActionListener for the nextPageButton.  The last image number to be displayed is set to the
     * current image count plus 20.  If the endImage number equals 101, then the next page button does not display any new
     * images because there are only 100 images to be displayed.  The first picture on the next page is the image located in
     * the buttonOrder array at the imageCount
     */
    private class nextPageHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int imageButNo = 0;
            int endImage = imageCount + 20;
            if (endImage <= 101) {
                panelBottom1.removeAll();
                for (int i = imageCount; i < endImage; i++) {
                    imageButNo = buttonOrder[i];
                    panelBottom1.add(imagePanelArray[imageButNo]);
                    imageCount++;

                }

                panelBottom1.revalidate();
                panelBottom1.repaint();
            }
        }

    }

    /*This class implements an ActionListener for the previousPageButton.  The last image number to be displayed is set to the
     * current image count minus 40.  If the endImage number is less than 1, then the previous page button does not display any new
     * images because the starting image is 1.  The first picture on the next page is the image located in
     * the buttonOrder array at the imageCount
     */
    private class previousPageHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int imageButNo = 0;
            int startImage = imageCount - 40;
            int endImage = imageCount - 20;
            if (startImage >= 1) {
                panelBottom1.removeAll();
                /*The for loop goes through the buttonOrder array starting with the startImage value
                 * and retrieves the image at that place and then adds the button to the panelBottom1.
                 */
                for (int i = startImage; i < endImage; i++) {
                    imageButNo = buttonOrder[i];
                    panelBottom1.add(imagePanelArray[imageButNo]);
                    imageCount--;

                }

                panelBottom1.revalidate();
                panelBottom1.repaint();
            }
        }

    }
    //Abstract class to use Manhattan Distance for Intensity algorithm
    //and Color-code algorithm simultaneously
    private abstract class CommonSelectionHandler implements ActionListener {
        // This method is abstract and will be implemented by subclasses
        protected abstract Set<ImageInfo> calculateManhattanDistance(ImageInfo refImageInfo);

        // ActionListener method
        public void actionPerformed(ActionEvent e) {
            // Get the index of the selected image
            int pic = (picNo - 1);

            // Retrieve the image information for the selected image
            ImageInfo refImageInfo = imageProcessor.getImageInfo(pic);

            // Calculate the Manhattan Distance and get a sorted set of similar images
            Set<ImageInfo> orderOfButtons = calculateManhattanDistance(refImageInfo);

            int i = 1;
            // Update the button order based on similarity
            Iterator<ImageInfo> iterator = orderOfButtons.iterator();
            while (iterator.hasNext()) {
                buttonOrder[i] = iterator.next().picNum;
                i++;
            }

            int imageButNo = 0;
            imageCount = 1; // reset image count to 1
            panelBottom1.removeAll();  // Remove previous images from the display panel

            // Display the first 20 images in the new order
            for (int j = 1; j < 21; j++) {
                imageButNo = buttonOrder[j];
                panelBottom1.add(imagePanelArray[imageButNo]);
                imageCount++;
            }
            // Refresh the display panel
            panelBottom1.revalidate();
            panelBottom1.repaint();
        }

    }

    /*This class implements an ActionListener when the user selects the intensityHandler button.  The image number that the
     * user would like to find similar images for is stored in the variable pic.  pic takes the image number associated with
     * the image selected and subtracts one to account for the fact that the intensityMatrix starts with zero and not one.
     * The size of the image is retrieved from the imageSize array.  The selected image's intensity bin values are
     * compared to all the other image's intensity bin values and a score is determined for how well the images compare.
     * The images are then arranged from most similar to the least.
     */
    private class intensityHandler extends CommonSelectionHandler {

        // Calculate Manhattan Distance based on intensity features
        protected Set<ImageInfo> calculateManhattanDistance(ImageInfo refImageInfo) {

            // Create a sorted set to store images based on Manhattan Distance
            Set<ImageInfo> sortedSet = new TreeSet<ImageInfo>(
                    new Comparator<ImageInfo>() {
                        @Override
                        public int compare(ImageInfo image1, ImageInfo image2) {
                            return Double.compare(image1.distance, image2.distance);
                        }
                    });

            for (int i = 0; i < 100; i++) {
                // Create a copy of the image information
                ImageInfo imageInfo = new ImageInfo(imageProcessor.getImageInfo(i));

                // Calculate the Manhattan Distance for intensity bins
                for (int bin = 0; bin < refImageInfo.intensityBins.length; bin++) {
                    imageInfo.distance = imageInfo.distance +
                            Math.abs((refImageInfo.intensityBins[bin])/(refImageInfo.width * refImageInfo.height)
                                    - (imageInfo.intensityBins[bin]/(imageInfo.width * imageInfo.height)));
                }

                // Add the image to the sorted set
                sortedSet.add(imageInfo);
            }

            return sortedSet;
        }
    }

    /*This class implements an ActionListener when the user selects the colorCode button.  The image number that the
     * user would like to find similar images for is stored in the variable pic.  pic takes the image number associated with
     * the image selected and subtracts one to account for the fact that the intensityMatrix starts with zero and not one.
     * The size of the image is retrieved from the imageSize array.  The selected image's intensity bin values are
     * compared to all the other image's intensity bin values and a score is determined for how well the images compare.
     * The images are then arranged from most similar to the least.
     */
    private class colorCodeHandler extends CommonSelectionHandler {

        // Calculate Manhattan Distance based on color code features
        protected Set<ImageInfo> calculateManhattanDistance(ImageInfo refImageInfo) {
            // Create a sorted set to store images based on Manhattan Distance
            Set<ImageInfo> sortedSet = new TreeSet<ImageInfo>(
                    new Comparator<ImageInfo>() {
                        @Override
                        public int compare(ImageInfo image1, ImageInfo image2) {
                            return Double.compare(image1.distance, image2.distance);
                        }
                    });

            for (int i = 0; i < 100; i++) {
                // Create a copy of the image information
                ImageInfo imageInfo = new ImageInfo(imageProcessor.getImageInfo(i));

                // Calculate the Manhattan Distance for color code bins
                for (int bin = 0; bin < refImageInfo.colorCodeBins.length; bin++) {
                    imageInfo.distance = imageInfo.distance +
                            Math.abs((refImageInfo.colorCodeBins[bin])/(refImageInfo.width * refImageInfo.height)
                                    - (imageInfo.colorCodeBins[bin]/(imageInfo.width * imageInfo.height)));
                }

                // Add the image to the sorted set
                sortedSet.add(imageInfo);
            }

            return sortedSet;
        }
    }
}