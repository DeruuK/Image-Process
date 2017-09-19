package ImgProcessing;

//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
//import com.google.common.base.Optional;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImgProcess {

	private JFrame frame;
	BufferedImage ProcessedImg;
	int width = 0;
	int height = 0;
	int imgwidth = 0;
	int imgheight = 0;
	Color[][] pcolor;
	//DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

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
		JMenuBar menuBar;
		JLabel lblorigimg;
		JLabel lblprocimg;
		JFileChooser OpenFile;
		JFileChooser SaveFile;
		JMenu JMFile;
		JMenuItem JMOpenFile;
		JMenuItem JMSaveFile;
		JMenu JMProcess;
		JMenuItem JMGrayScale;
		JMenuItem JMAddBlue;
		JMenuItem JMColorEdge;
		//JMenuItem JMOilPaint;
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblorigimg = new JLabel("Orignal-Img");
		lblorigimg.setBounds(16, 50, 415, 382);
		frame.getContentPane().add(lblorigimg);
		width = lblorigimg.getWidth();
		height = lblorigimg.getHeight();
		
		lblprocimg = new JLabel("Processed-Img");
		lblprocimg.setBounds(468, 50, 415, 382);
		frame.getContentPane().add(lblprocimg);
		
		OpenFile = new JFileChooser();
		OpenFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		SaveFile = new JFileChooser();
		SaveFile.setCurrentDirectory(new File(System.getProperty("user.dir")));

		
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 928, 22);
		frame.getContentPane().add(menuBar);
		JMFile = new JMenu("File");
		menuBar.add(JMFile);
		// Open an image file
		JMOpenFile = new JMenuItem("Open File");
		JMOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter ImgFilefilter = new FileNameExtensionFilter("*.images","jpg","jpeg","gif","png");
				OpenFile.addChoosableFileFilter(ImgFilefilter);
				int OpenFileResult = OpenFile.showOpenDialog(null);
				if(OpenFileResult == JFileChooser.APPROVE_OPTION){
					File SelectedFile = OpenFile.getSelectedFile();
					String path = SelectedFile.getAbsolutePath();
					
					//read image file pixel information
					BufferedImage bimg = null;
					try{
						bimg = ImageIO.read(SelectedFile);
						//BufferedImage grayImg = new BufferedImage(bimg.getWidth(), bimg.getHeight(),BufferedImage.TYPE_INT_ARGB);
						imgwidth = bimg.getWidth();
						imgheight = bimg.getHeight();
						pcolor = getColors(bimg);
						ImageIcon inputimg = new ImageIcon(path);
						lblprocimg.setIcon(null);
						lblorigimg.setIcon(ResizeImg(inputimg));
						lblorigimg.setText(null);
						lblprocimg.setText(null);
						
					}catch(IOException ee){
						ee.printStackTrace();
					}
					
				}else if(OpenFileResult == JFileChooser.CANCEL_OPTION){
					System.out.println("No file selected...");
				}
			}
		});
		JMFile.add(JMOpenFile);
		
		// save a file
		JMSaveFile = new JMenuItem("Save");
		JMSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter ImgFilefilter = new FileNameExtensionFilter(".png", ".png");
				SaveFile.setFileFilter(ImgFilefilter);
				int SaveFileResult = SaveFile.showSaveDialog(null);
				if(SaveFileResult != JFileChooser.APPROVE_OPTION) return;
				
				Graphics2D g = ProcessedImg.createGraphics();
				//g.setBackground(Color.WHITE);
				g.drawImage(ProcessedImg, null, 0, 0);
				//g.fillRect(0, 0, ProcessedImg.getWidth(), ProcessedImg.getHeight());
				
				try{

					File fileToSave = new File(SaveFile.getSelectedFile()+".png");
					
					ImageIO.write(ProcessedImg, "png", fileToSave);
				}catch(IOException ee){
					ee.printStackTrace();
				}
			}
		});
		JMFile.add(JMSaveFile);
		
		// Image processing
		JMProcess = new JMenu("Processing");
		menuBar.add(JMProcess);
		// Sepia Picture
		JMGrayScale = new JMenuItem("Sepia Image");
		JMGrayScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(pcolor!=null){
					int pheight = pcolor.length;
					int pwidth = pcolor[0].length;
					ProcessedImg = new BufferedImage(pwidth, pheight, BufferedImage.TYPE_INT_ARGB);
					for (int pi=0; pi<pheight; pi++){
						for (int pj=0; pj<pwidth; pj++){
							Color c = pcolor[pi][pj];
							int r = c.getRed();
							int g = c.getGreen();
							int b = c.getBlue();
							int a = c.getAlpha();
							
							int gr = (int) ((0.393*r+0.769*g+0.189*b>255) ? 255:(0.393*r+0.769*g+0.189*b));
							int gg = (int) ((0.349*r+0.686*g+0.168*b>255) ? 255:(0.349*r+0.686*g+0.168*b));
							int gb = (int) ((0.272*r+0.534*g+0.131*b>255) ? 255:(0.272*r+0.534*g+0.131*b));
							Color gray = new Color(gr,gg,gb,a);
							ProcessedImg.setRGB(pj, pi, gray.getRGB());
						}
					}
					//show image in Process frame
					ImageIcon showProcessedImg = new ImageIcon(ProcessedImg);
					lblprocimg.setIcon(ResizeImg(showProcessedImg));
					lblprocimg.setText(null);
				}
			}
		});
		JMProcess.add(JMGrayScale);
		
		// Cold color renderer
		JMAddBlue = new JMenuItem("Cold Render");
		JMAddBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage blueef = null;
				try {
					blueef = ImageIO.read(new File("Image_Process/src/images/blue.jpg"));
					blueef = fitsize(blueef, imgwidth, imgheight);
					//blueef.getScaledInstance(imgwidth, imgheight, BufferedImage.SCALE_SMOOTH);
					Color[][] bcolor = getColors(blueef);
					ProcessedImg = new BufferedImage(imgwidth, imgheight, BufferedImage.TYPE_INT_ARGB);
					for(int h=0; h<imgheight; h++){
						for(int w=0; w<imgwidth; w++){
							Color c=pcolor[h][w];
							Color bc=bcolor[h][w];
							int bluer = 255-((255-c.getRed())*(255-bc.getRed())>>8);
							int blueg = 255-((255-c.getGreen())*(255-bc.getGreen())>>8);
							int blueb = 255-((255-c.getBlue())*(255-bc.getBlue())>>8);
							Color blueit = new Color(bluer, blueg, blueb, c.getAlpha());
							ProcessedImg.setRGB(w, h, blueit.getRGB());
						}
					}
					ImageIcon showProcessedImg = new ImageIcon(ProcessedImg);
					lblprocimg.setIcon(ResizeImg(showProcessedImg));
					lblprocimg.setText(null);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		JMProcess.add(JMAddBlue);
		
		// Colorful Edge
		JMColorEdge = new JMenuItem("Color Edge");
		JMColorEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//BufferedImage edgeimg = null;
				
				ProcessedImg = new BufferedImage(imgwidth, imgheight, BufferedImage.TYPE_INT_ARGB);
				for(int h=1; h<imgheight-1; h++){
					for(int w=1; w<imgwidth-1; w++){
						Color c1 = pcolor[h+1][w-1];
						Color c2 = pcolor[h+1][w];
						Color c3 = pcolor[h+1][w+1];
						Color c4 = pcolor[h-1][w-1];
						Color c5 = pcolor[h-1][w];
						Color c6 = pcolor[h-1][w+1];
						Color c7 = pcolor[h][w+1];
						Color c8 = pcolor[h][w-1];
						
						int r1 = c1.getRed() + 2*c2.getRed() + c3.getRed() - c4.getRed() - 2*c5.getRed() - c6.getRed();
						int r2 = c6.getRGB() + 2*c7.getRed() + c3.getRed() - c4.getRed() - 2*c8.getRed() - c1.getRed();
						int g1 = c1.getGreen() + 2*c2.getGreen() + c3.getGreen() - c4.getGreen() - 2*c5.getGreen() - c6.getGreen();
						int g2 = c6.getGreen() + 2*c7.getGreen() + c3.getGreen() - c4.getGreen() - 2*c8.getGreen() - c1.getGreen();
						int b1 = c1.getBlue() + 2*c2.getBlue() + c3.getBlue() - c4.getBlue() - 2*c5.getBlue() - c6.getBlue();
						int b2 = c6.getBlue() + 2*c7.getBlue() + c3.getBlue() - c4.getBlue() - 2*c8.getBlue() - c1.getBlue();
						
						int nr = colorange(Math.abs(r1)+Math.abs(r2));
						int ng = colorange(Math.abs(g1)+Math.abs(g2));
						int nb = colorange(Math.abs(b1)+Math.abs(b2));
						
//						int nr = Math.abs(r1)+Math.abs(r2);
//						int ng = Math.abs(g1)+Math.abs(g2);
//						int nb = Math.abs(b1)+Math.abs(b2);
						
//						Color nc = new Color(nr, ng, nb, c1.getAlpha());
						Color nc = new Color(colorange(nr<<1), ng, nb, c1.getAlpha());
						ProcessedImg.setRGB(w, h, nc.getRGB());
					}
				}
				ImageIcon showProcessedImg = new ImageIcon(ProcessedImg);
				lblprocimg.setIcon(ResizeImg(showProcessedImg));
				lblprocimg.setText(null);
			}
		});
		JMProcess.add(JMColorEdge);
	}
	
	
	public ImageIcon ResizeImg(ImageIcon myImg){
		Image img = myImg.getImage();
		int nwidth;
		int nheight;
		Image newImg;
		
		if(imgwidth/imgheight>=width/height){  
			if(imgwidth>width){  
				nwidth=width;  
				nheight=imgheight*nwidth/imgwidth;  
			}else{  
				nwidth=imgwidth;  
				nheight=imgheight;  
			}  
		}else{  
			if(imgwidth>width){  
				nheight=height;  
				nwidth=imgwidth*nheight/imgheight;  
			}else{  
				nwidth=imgwidth;  
				nheight=imgheight;  
			}  
		}  
		newImg = img.getScaledInstance(nwidth, nheight, Image.SCALE_SMOOTH);
		//newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon reImg = new ImageIcon(newImg);
		return reImg;
	}
	
	public Color[][] getColors(BufferedImage img){
		int imgw = img.getWidth();
		int imgh = img.getHeight();
		Color[][] out = new Color[imgh][imgw];
		for(int i=0; i<imgh; i++){
			for (int j=0; j<imgw; j++){
				//System.out.println(i+";"+j);
				Color c = new Color(img.getRGB(j, i));
				if(c.toString().isEmpty()){
					//System.out.println("This is Empty: "+c);
					out[i][j]=new Color(0,0,0,255);
				}else{
					out[i][j]=c;
				}
			}
		}
		return out;
	}
	
	public BufferedImage fitsize(BufferedImage img, int width, int height){
		BufferedImage out = new BufferedImage(width, height, img.getType());
		Graphics2D g = out.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, width, height, null); 
		g.dispose();
		return out;
	}
	
	public int colorange(int co){
		if(co>255) return 255;
		if(co<0) return 0;
		
		return co;
	}
}
