package sample.poi;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.model.CHPX;
import org.apache.poi.hwpf.model.PAPX;
import org.apache.poi.hwpf.model.SEPX;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.hwpf.usermodel.Range;
import org.w3c.dom.Document;

import wfc.service.util.StringHelper;

public class DocParseTest {
	public void parseDoc1(HWPFDocument doc) {
		System.out.println("文档长度:" + doc.characterLength());
		Range range = doc.getRange();
		String text = range.text();
		System.out.println(text);
	}
	
	
	public void parseDoc2(HWPFDocument doc) {
		int length = doc.characterLength();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length - 1; i++) {
			Range range = new Range(i, i + 1, doc);
			// 之所以用这个构造方法，是因为整篇文章的字符判断不准确。只好一个字符一个字符的来判断。
			// 而且API的说明文字相当的不全。
			for (int j = 0; j < range.numCharacterRuns(); j++) {
				CharacterRun cr = range.getCharacterRun(j);
				if ( cr.getSubSuperScriptIndex() == 0 )// getSubSuperScriptIndex()这个方法来判断是否是上下角标
					sb.append(range.text());
			}
		}
		System.out.println(sb.toString());
	}
	
	
	public void parseDocParagraph(HWPFDocument doc) {
		List<PAPX> papxList = doc.getParagraphTable().getParagraphs();
		System.out.println("papxList.size=" + papxList.size()); // TODO System.out.println();
		for ( int i = 0; i < papxList.size(); i++ ) {
			PAPX papx = papxList.get(i);
			
			int pStart = papx.getStart();
			int pEnd = papx.getEnd();
			Range range = new Range(pStart, pEnd, doc);
			String text = range.text();
			text = StringHelper.replaceAll(text, "\r", "");
			System.out.println((i+1) + ":text[" + pStart + "-" + pEnd + "]=\"" + text + "\""); // TODO System.out.println();
		}
	}
	
	
	public void parseDocCharacter(HWPFDocument doc) {
		List<CHPX> chpxList = doc.getCharacterTable().getTextRuns();
		System.out.println("chpxList.size=" + chpxList.size()); // TODO System.out.println();
		for ( int i = 0; i < chpxList.size(); i++ ) {
			CHPX chpx = chpxList.get(i);
			
			int cStart = chpx.getStart();
			int cEnd = chpx.getEnd();
			Range range = new Range(cStart, cEnd, doc);
			String text = range.text();
			text = StringHelper.replaceAll(text, "\r", "");
			System.out.println((i+1) + ":text[" + cStart + "-" + cEnd + "]=\"" + text + "\""); // TODO System.out.println();
		}
	}
	
	
	public void parseDocPics(HWPFDocument doc) {
		List<Picture> pictureList = doc.getPicturesTable().getAllPictures();
		System.out.println("pictureList.size=" + pictureList.size()); // TODO System.out.println();
		for (int i = 0; i < pictureList.size(); i++) {
			Picture pic = pictureList.get(i);
			int pStartOffset = pic.getStartOffset();
			int x = pic.getHorizontalScalingFactor();
			int y = pic.getVerticalScalingFactor();
			int size = pic.getSize();
			String fe = pic.suggestFileExtension();
			String fn = pic.suggestFullFileName();
			String ft = pic.suggestPictureType().getExtension();
			
			String des = pic.getDescription();
			
			System.out.println("pStartOffset=" + pStartOffset + ",x=" + x + "y=" + y + ",size=" + size + ",fe=" + fe + ",fn=" + fn + ",ft=" + ft + ",des=" + des); // TODO System.out.println();
		}
	}
	
	
	public void parseDocSection(HWPFDocument doc) {
		List<SEPX> sepxList = doc.getSectionTable().getSections();
		System.out.println("sepxList.size=" + sepxList.size()); // TODO System.out.println();
		for ( int i = 0; i < sepxList.size(); i++ ) {
			SEPX sepx = sepxList.get(i);
			int sStart = sepx.getStart();
			int sEnd = sepx.getEnd();
			Range range = new Range(sStart, sEnd, doc);
			String text = range.text();
			text = StringHelper.replaceAll(text, "\r", "");
			System.out.println((i+1) + ":text[" + sStart + "-" + sEnd + "]=\"" + text + "\""); // TODO System.out.println();
		}
	}
	
	
	public static void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "GBK"));
			bw.write(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if ( bw != null )
					bw.close();
				if ( fos != null )
					fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	public static void convert2Html(HWPFDocument doc, String filePathName) throws TransformerException, IOException, ParserConfigurationException {
		String filePath = filePathName.substring(0, filePathName.lastIndexOf("\\") + 1);
		final String subPath = "images";
		
		File fileSubPath = new File(filePath + subPath);
		if ( !fileSubPath.exists() ) {
			fileSubPath.mkdirs();
		}
		
		System.out.println("filePath=" + filePath); // TODO System.out.println();
		
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
				return subPath + "/" + suggestedName;
			}
		});
		wordToHtmlConverter.processDocument(doc);
		// save pictures
		List<Picture> pics = doc.getPicturesTable().getAllPictures();
		System.out.println("pics.size=" + pics.size()); // TODO System.out.println();
		if ( pics != null ) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = pics.get(i);
				try {
					pic.writeImageContent(new FileOutputStream(filePath + subPath + "\\" + pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "GBK");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		writeFile(new String(out.toByteArray()), filePathName);
	}
}
