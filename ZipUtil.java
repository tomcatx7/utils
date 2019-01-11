package casTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("开始压缩");
		File file = new File("E:\\test");
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("E:\\test2.zip"));
		compress(file, zout,"test");
		try {
			zout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("压缩完成");
	}

	/**
	 * 
	 * @param sourcefile
	 *            压缩的目标文件 E:\test\lib
	 * @param zout
	 *            压缩输出流
	 * @param name
	 *            目标文件在压缩包中的文件名 test\lib 会对应生成目录结构
	 */
	public static void compress(File sourcefile, ZipOutputStream zout, String rootname) {

		if (sourcefile.isFile()) {
			FileInputStream fis = null;
			try {
				zout.putNextEntry(new ZipEntry(rootname));
				fis = new FileInputStream(sourcefile);
				byte[] buff = new byte[1024];
				int len;
				while ((len = fis.read(buff)) != -1) {
					zout.write(buff, 0, len);
				}
				zout.closeEntry();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			File[] listFiles = sourcefile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				try {
					// 文件夹为空目录，生成一个空的压缩实体，注：生成文件夹要带上‘/’
					zout.putNextEntry(new ZipEntry(rootname + "/"));
					zout.closeEntry();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				for (File file : listFiles) {
					compress(file, zout, rootname + "/" + file.getName());
				}
			}
		}
	}

}
