package com.test;

import java.io.File;
import java.util.List;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

public class VideoUtil {
	public static void main(String[] args) {
		File source = new File("H:\\Download\\ע��ע��.mp4");
		Encoder encoder = new Encoder();
		try {
			MultimediaInfo m = encoder.getInfo(source);
			long ls = m.getDuration();
			System.out.println("����Ƶʱ��Ϊ:" + ls / 1000 + "�룡");
		} catch (Exception e) {
			e.printStackTrace();
		}
		processImg("H:\\Download\\ע��ע��.mp4", "H:\\Download\\ffmpeg.exe");
	}

	public static boolean processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			System.err.println("·��[" + veido_path + "]��Ӧ����Ƶ�ļ�������!");
			return false;
		}
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpeg_path);
		commands.add("-i");
		commands.add(veido_path);
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		commands.add("-ss");
		commands.add("28");// ���ý�ȡ��Ƶ������ʱ�Ļ���
		// commands.add("-t");
		// commands.add("0.001");
		commands.add("-s");
		commands.add("640x607");
		commands.add(veido_path.substring(0, veido_path.lastIndexOf("."))
				.replaceFirst("vedio", "file") + ".jpg");
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.start();
			System.out.println("��ȡ�ɹ�");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
