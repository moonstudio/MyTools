package sample;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

/**
 * Title: ���ɶ�ά�루������Logo��
 * Project: QRCode
 * @author maliang
 * Description: 
 * Copyright: Copyright (c) 2013
 * Company: �����Ϣ�ɷ����޹�˾
 */
public class QRCodeHelper {
	private static String code = "UTF-8";
	
	/** 
     * ���ɶ�ά��(QRCode)ͼƬ 
     * @param content ��ά��ͼƬ������
     * @param iconPath  ��ά��ͼƬ�м��ͼ���ļ�·��
     * @param exportPath ���ɶ�ά��ͼƬ������·��
     */  
    public int createQRCode(String content, String iconPath, String exportPath) {  
    	System.out.println("��ʼ���ɶ�ά��..."); // TODO System.out.println();
        try {  
            Qrcode qrcodeHandler = new Qrcode();  
            //���ö�ά���Ŵ��ʣ���ѡL(7%)��M(15%)��Q(25%)��H(30%)���Ŵ���Խ�߿ɴ洢����ϢԽ�٣����Զ�ά�������ȵ�Ҫ��ԽС  
            qrcodeHandler.setQrcodeErrorCorrect('M');  
            //N��������,A�����ַ�a-Z,B���������ַ�
            qrcodeHandler.setQrcodeEncodeMode('B'); 
            // �������ö�ά��汾��ȡֵ��Χ1-40��ֵԽ��ߴ�Խ�󣬿ɴ洢����ϢԽ��  
            qrcodeHandler.setQrcodeVersion(6);  
  
            byte[] contentBytes = content.getBytes(code);  
            boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
            
            // �����ά��ͼƬ��Ⱥ͸߶�
            int imgWidth = codeOut.length * 3 + 4;
            int imgHeight = codeOut.length * 3 + 4;
            
            BufferedImage imgOut = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);  
            Graphics2D gs = imgOut.createGraphics();  
  
            gs.setBackground(Color.WHITE);	// ��ɫ
            gs.setColor(Color.BLACK);		// ��ɫ
            gs.clearRect(0, 0, imgWidth, imgHeight);  
  
            // ����ƫ���� �����ÿ��ܵ��½�������  
            int pixoff = 2;  
            
            // ������� > ��ά��  
            if (contentBytes.length > 0 && contentBytes.length < 1000) {  
                for (int i = 0; i < codeOut.length; i++) {  
                    for (int j = 0; j < codeOut.length; j++) {  
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);  
                        }  
                    }  
                }  
            } else {  
                System.err.println("content������");  
                return -1;
            }  
            
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			// ��Ӷ�ά������ͼ��
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            BufferedImage imgIcon = ImageIO.read(new File(iconPath));
            int iconWidth = imgIcon.getWidth();
            int iconHeight = imgIcon.getHeight();
            int iconLeft = (imgWidth - iconWidth) / 2;
            int iconTop = (imgHeight - iconHeight) / 2;
            gs.drawImage(imgIcon, iconLeft, iconTop, null);
            gs.dispose();  
            imgOut.flush(); 
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
            // ���ɶ�ά��QRCodeͼƬ  
            File imgFile = new File(exportPath);  
            ImageIO.write(imgOut, "png", imgFile);  
  
            System.out.println("��ά��������ϣ�"); // TODO System.out.println();
        } catch (Exception e) {  
            e.printStackTrace();  
            return -100;
        }  
        
        return 0;
    }  

    public static void main(String[] args) {
    	 String iconPath = "E:/_test/icon.png"; 
    	 String exportPath = "E:/_test/out.png"; 
    	 String encoderContent = "����ǻ۽���ƽ̨ - edu.wondersgroup.com";
    	 QRCodeHelper qrCodeHelper = new QRCodeHelper();
    	 qrCodeHelper.createQRCode(encoderContent, iconPath, exportPath);
	}
}
