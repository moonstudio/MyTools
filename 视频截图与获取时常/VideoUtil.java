package com.test;

import java.io.File;
import java.util.List;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

public class VideoUtil {
	public static void main(String[] args) {
		File source = new File("H:\\Download\\注子注碗.mp4");
		Encoder encoder = new Encoder();
		try {
			MultimediaInfo m = encoder.getInfo(source);
			long ls = m.getDuration();
			System.out.println("此视频时长为:" + ls / 1000 + "秒！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		processImg("H:\\Download\\注子注碗.mp4", "H:\\Download\\ffmpeg.exe");
	}

	public static boolean processImg(String veido_path, String ffmpeg_path) {
		File file = new File(veido_path);
		if (!file.exists()) {
			System.err.println("路径[" + veido_path + "]对应的视频文件不存在!");
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
		commands.add("28");// 设置截取视频多少秒时的画面
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
			System.out.println("截取成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
