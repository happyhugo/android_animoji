//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static final int BUFF_SIZE = 8192;

    public ZipUtils() {
    }

    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        Iterator var3 = resFileList.iterator();

        while(var3.hasNext()) {
            File resFile = (File)var3.next();
            zipFile(resFile, zipout, "");
        }

        zipout.close();
    }

    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        Iterator var4 = resFileList.iterator();

        while(var4.hasNext()) {
            File resFile = (File)var4.next();
            zipFile(resFile, zipout, "");
        }

        zipout.setComment(comment);
        zipout.close();
    }

    public static void upZipFile(File zipFile, String folderPath) throws IOException {
        File desDir = new File(folderPath);
        if(!desDir.exists()) {
            desDir.mkdirs();
        }

        ZipFile zf = new ZipFile(zipFile);
        Enumeration entries = zf.entries();

        while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if(!desFile.exists()) {
                File out = desFile.getParentFile();
                if(!out.exists()) {
                    out.mkdirs();
                }

                desFile.createNewFile();
            }

            FileOutputStream out1 = new FileOutputStream(desFile);
            byte[] buffer = new byte[8192];

            int realLength;
            while((realLength = in.read(buffer)) > 0) {
                out1.write(buffer, 0, realLength);
            }

            in.close();
            out1.close();
        }

    }

    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath, String nameContains) throws IOException {
        ArrayList fileList = new ArrayList();
        File desDir = new File(folderPath);
        if(!desDir.exists()) {
            desDir.mkdir();
        }

        ZipFile zf = new ZipFile(zipFile);
        Enumeration entries = zf.entries();

        while(true) {
            ZipEntry entry;
            do {
                if(!entries.hasMoreElements()) {
                    return fileList;
                }

                entry = (ZipEntry)entries.nextElement();
            } while(!entry.getName().contains(nameContains));

            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if(!desFile.exists()) {
                File out = desFile.getParentFile();
                if(!out.exists()) {
                    out.mkdirs();
                }

                desFile.createNewFile();
            }

            FileOutputStream out1 = new FileOutputStream(desFile);
            byte[] buffer = new byte[8192];

            int realLength;
            while((realLength = in.read(buffer)) > 0) {
                out1.write(buffer, 0, realLength);
            }

            in.close();
            out1.close();
            fileList.add(desFile);
        }
    }

    public static ArrayList<String> getEntriesNames(File zipFile) throws IOException {
        ArrayList entryNames = new ArrayList();
        Enumeration entries = getEntriesEnumeration(zipFile);

        while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            entryNames.add(new String(getEntryName(entry).getBytes("GB2312"), "8859_1"));
        }

        return entryNames;
    }

    public static Enumeration<?> getEntriesEnumeration(File zipFile) throws IOException {
        ZipFile zf = new ZipFile(zipFile);
        return zf.entries();
    }

    public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getComment().getBytes("GB2312"), "8859_1");
    }

    public static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getName().getBytes("GB2312"), "8859_1");
    }

    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath) throws IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0?"":File.separator) + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        int realLength;
        if(resFile.isDirectory()) {
            File[] buffer = resFile.listFiles();
            File[] in = buffer;
            realLength = buffer.length;

            for(int var6 = 0; var6 < realLength; ++var6) {
                File file = in[var6];
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte[] var8 = new byte[8192];
            BufferedInputStream var9 = new BufferedInputStream(new FileInputStream(resFile));
            zipout.putNextEntry(new ZipEntry(rootpath));

            while((realLength = var9.read(var8)) != -1) {
                zipout.write(var8, 0, realLength);
            }

            var9.close();
            zipout.flush();
            zipout.closeEntry();
        }

    }

    public static void unzip(File zip, File targetDir) throws IOException {
        FileInputStream in = new FileInputStream(zip);
        unzip((InputStream)in, targetDir);
    }

    public static void unzip(InputStream in, File targetDir) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(in);

        ZipEntry zipEntry;
        for(byte[] b = new byte[8192]; (zipEntry = zipIn.getNextEntry()) != null; zipIn.closeEntry()) {
            File file = new File(targetDir, zipEntry.getName());
            if(zipEntry.isDirectory()) {
                file.mkdirs();
            } else {
                File parent = file.getParentFile();
                if(!parent.exists()) {
                    parent.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(file);

                int r;
                while((r = zipIn.read(b)) != -1) {
                    fos.write(b, 0, r);
                }

                fos.close();
            }
        }

    }

    public static boolean isZipFile(File file) throws IOException {
        if(file.isDirectory()) {
            return false;
        } else {
            byte[] bytes = new byte[4];
            FileInputStream fIn = new FileInputStream(file);
            if(fIn.read(bytes) != bytes.length) {
                return false;
            } else {
                int header = bytes[0] + (bytes[1] << 8) + (bytes[2] << 16) + (bytes[3] << 24);
                return 67324752 == header;
            }
        }
    }
}
