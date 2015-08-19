package sample.poi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * word 2 html
 * 
 * @author Michael
 * 
 */
public class WordToHtml {

	/**
	 * 回车符ASCII码
	 */
	private static final short ENTER_ASCII = 13;

	/**
	 * 空格符ASCII码
	 */
	private static final short SPACE_ASCII = 32;

	/**
	 * 水平制表符ASCII码
	 */
	private static final short TABULATION_ASCII = 9;

	private String htmlText = "";

	/**
	 * 读取每个文字样式
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void getWordAndStyle(String fileName) throws Exception {
		FileInputStream in = new FileInputStream(new File(fileName));
		HWPFDocument doc = new HWPFDocument(in);

		// 取得文档中字符的总数
		int length = doc.characterLength();

		// 创建图片容器
		PicturesTable pTable = doc.getPicturesTable();

		htmlText = "<html><head><title>"
				+ doc.getSummaryInformation().getTitle()
				+ "</title></head><body>";

		// 创建临时字符串,好加以判断一串字符是否存在相同格式

		String tempString = "";

		for (int i = 0; i < length - 1; i++) {
			// 整篇文章的字符通过一个个字符的来判断,range为得到文档的范围
			// System.out.println("###############################");
			Range range = new Range(i, i + 1, doc);
			CharacterRun cr = range.getCharacterRun(0);

			if (pTable.hasPicture(cr)) {
				// 读写图片
				this.readPicture(pTable, cr);
			} else {
				Range range2 = new Range(i + 1, i + 2, doc);

				// 第二个字符
				CharacterRun cr2 = range2.getCharacterRun(0);

				// 当前字符
				char currentChar = cr.text().charAt(0);
				// System.out.println("当前字符"+currentChar);
				// 判断是否为回车符
				if (currentChar == ENTER_ASCII) {
					tempString += "<br/>";
				}

				// 判断是否为空格符
				else if (currentChar == SPACE_ASCII) {
					tempString += "&nbsp;";
				}
				// 判断是否为水平制表符
				else if (currentChar == TABULATION_ASCII) {
					tempString += " &nbsp;&nbsp;&nbsp;";
				}
				// 比较前后2个字符是否具有相同的格式
				boolean flag = compareCharStyle(cr, cr2);

				String fontStyle = "<span style='font-family:"
						+ cr.getFontName() + ";font-size:" + cr.getFontSize()
						/ 2 + "pt;";

				if (cr.isBold()) {
					fontStyle += "font-weight:bold;";
				}
				if (cr.isItalic()) {
					fontStyle += "font-style:italic;";
				}

				if (flag && i != length - 2)
					tempString += currentChar;
				else if (!flag) {
					htmlText += fontStyle + "'>" + tempString + currentChar
							+ "</span>";
					tempString = "";
				} else
					htmlText += fontStyle + "'>" + tempString + currentChar
							+ "</span>";
			}
			// this.writeFile(htmlText);
		}
		htmlText += "</body></html>";
		 //System.out.println(htmlText);
		 //预先截取htmlhtmlText
		this.fuck(htmlText);
	}

	/**
	 * 解析html
	 * 
	 * @param htmlText2
	 */
	private void fuck(String htmlText2) {
		String htmlText = String.valueOf(htmlText2);
		//去掉所有不需要的元素
		htmlText = htmlText.replace("<body>", "");
		htmlText = htmlText.replace("</body>", "");
		htmlText = htmlText.replace("<html>", "");
		htmlText = htmlText.replace("</html>", "");
		htmlText = htmlText.replace("<br/>", "");
		htmlText = htmlText.substring(0, htmlText.lastIndexOf("#")+1);
		System.out.println("处理后的字符串--》"+htmlText+"《");
		
		int s = 1;
		int tempIndex =0;
		int index = 0;
		List<Question> qList = new ArrayList<Question>();
		while (s != -1) {
			//////////////////////////////////////
			Question ques = new Question();
			ques.setQuestionId("这里是题型");
			ques.setGategory("这里是内容");
			Answers ans = new Answers();
			ans.setAnswerValue("答案1");
			List<Answers> ansList = new ArrayList<Answers>();
			ansList.add(ans);
			ques.setAns(ansList);
			//////////////////////////////////////
			String tx = htmlText.substring(htmlText.indexOf("【题型】"),
					htmlText.indexOf("【题干】"));
			
			String tg = htmlText.substring(htmlText.indexOf("【题干】"),
					htmlText.indexOf("【答案】"));
			String da = htmlText.substring(htmlText.indexOf("【答案】"),
					htmlText.indexOf("【选项】"));
			String xx = htmlText.substring(htmlText.indexOf("【选项】"),
					htmlText.indexOf("【提示】"));
			String ts = htmlText.substring(htmlText.indexOf("【提示】"),
					htmlText.indexOf("【解析】"));
			String jx = htmlText.substring(htmlText.indexOf("【解析】"),
					htmlText.indexOf("#"));
			qList.add(ques);
			this.saveWord(qList);
			tempIndex =  htmlText.indexOf("#")+1;
			//首次出现的位置
			System.out.println("首次出现的位置####+1->"+tempIndex);
			//长度
			System.out.println("长度####length->"+htmlText.length());
			
			if (tempIndex>= htmlText.length()) {
				index = htmlText.length();
				s = -1;
				//System.out.println("###################s="+s);
			} else {
				index = htmlText.indexOf("#")+1;
				htmlText = htmlText.substring(index, htmlText.length());
			}
			System.out.println(index);
			
			// System.out.println("截取后的html"+htmlText);
			if (htmlText.length() < 0) {
				s = -1;
			}
		}

	}
	/**
	 * 输出方法
	 * @param tx
	 * @param tg
	 * @param da
	 * @param xx
	 * @param ts
	 * @param jx
	 */
	private void saveWord(List<Question> questionList) {
		// TODO Auto-generated method stub
		//解析List
		System.out.println("#############################################################");
	}

	/**
	 * 读写文档中的图片
	 * 
	 * @param pTable
	 * @param cr
	 * @throws Exception
	 */
	private void readPicture(PicturesTable pTable, CharacterRun cr)
			throws Exception {
		// 提取图片
		Picture pic = pTable.extractPicture(cr, false);
		// 返回POI建议的图片文件名
		String afileName = pic.suggestFullFileName();
		OutputStream out = new FileOutputStream(new File("d:\\tmp"
				+ File.separator + afileName));
		pic.writeImageContent(out);
		//存储到本地并返回本地的外连接
		htmlText += "<img src='D:\\tmp\\" + afileName + "'/>";
	}

	private boolean compareCharStyle(CharacterRun cr1, CharacterRun cr2) {
		boolean flag = false;
		if (cr1.isBold() == cr2.isBold() && cr1.isItalic() == cr2.isItalic()
				&& cr1.getFontName().equals(cr2.getFontName())
				&& cr1.getFontSize() == cr2.getFontSize()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 写文件
	 * 
	 * @param s
	 */
	private void writeFile(String s) {
		// System.out.println("HTML内容"+s);
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File("D:\\abc.html");
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
			bw.write(s);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	public static void main(String[] args) {
		WordToHtml a = new WordToHtml();
		String s = "H:/Wonders/SMILE/H后台管理系统/试卷导入格式/新试题格式1223 - 副本2.doc";
		try {
			a.getWordAndStyle(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
