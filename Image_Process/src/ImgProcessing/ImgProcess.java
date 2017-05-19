package ImgProcessing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFileChooser;

//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.IOException;
//import com.google.common.base.Optional;


public class ImgProcess {

	private JFrame frame;
	int width = 0;
	Color[][] pcolor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImgProcess window = new ImgProcess();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create the application.
	 */
	public ImgProcess() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblorigimg = new JLabel("Orignal-Img");
		lblorigimg.setBounds(16, 50, 373, 476);
		frame.getContentPane().add(lblorigimg);
		width = lblorigimg.getWidth();
		
		JLabel lblprocimg = new JLabel("Processed-Img");
		lblprocimg.setBounds(479, 50, 373, 476);
		frame.getContentPane().add(lblprocimg);
		
		JButton btnBrowser = new JButton("Browser");
		btnBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.dir")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images","jpg","jpeg","gif","png");
				file.addChoosableFileFilter(filter);
				int result = file.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File selectedFile = file.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					lblorigimg.setIcon(ResizeImg(path));
					
					//read image file pixel information
					BufferedImage bimg = null;
					try{
						bimg = ImageIO.read(selectedFile);
						//BufferedImage grayImg = new BufferedImage(bimg.getWidth(), bimg.getHeight(),BufferedImage.TYPE_INT_ARGB);
						pcolor = new Color[bimg.getHeight()][bimg.getWidth()];
						for(int i=0; i<bimg.getHeight(); i++){
							for (int j=0; j<bimg.getWidth(); j++){
								Color c = new Color(bimg.getRGB(i, j));
								if(c.toString().isEmpty()){
									System.out.println("This is Empty: "+c);
									pcolor[i][j]=new Color(0,0,0,255);
								}else{
									pcolor[i][j]=c;
								}
								
							}
						}
						
					}catch(IOException ee){
						ee.printStackTrace();
					}
					
				}else if(result == JFileChooser.CANCEL_OPTION){
					System.out.println("No file selected...");
				}
			}
		});
		btnBrowser.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnBrowser.setBounds(6, 6, 93, 29);
		frame.getContentPane().add(btnBrowser);	
		
		JButton btnGray = new JButton("GrayScale");
		btnGray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(pcolor!=null){
					int pheight = pcolor.length;
					int pwidth = pcolor[0].length;
					BufferedImage grayImg = new BufferedImage(pwidth, pheight, BufferedImage.TYPE_INT_ARGB);
					for (int pi=0; pi<pheight; pi++){
						for (int pj=0; pj<pwidth; pj++){
							Color c = pcolor[pi][pj];
							int r = c.getRed();
							int g = c.getGreen();
							int b = c.getBlue();
							int a = c.getAlpha();
							
							int gr = (r+g+b)/3;
							Color gray = new Color(gr,gr,gr,a);
							grayImg.setRGB(pi, pj, gray.getRGB());
						}
					}	
					Image newImg = grayImg.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
					ImageIcon reImg = new ImageIcon(newImg);
					lblprocimg.setIcon(reImg);
				}
			}
		});
		btnGray.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnGray.setBounds(105, 6, 93, 29);
		frame.getContentPane().add(btnGray);
		
		
	}
	
	public ImageIcon ResizeImg(String ImgPath){
		ImageIcon myImg = new ImageIcon(ImgPath);
		Image img = myImg.getImage();
		Image newImg = img.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
		ImageIcon reImg = new ImageIcon(newImg);
		return reImg;
	}
}
