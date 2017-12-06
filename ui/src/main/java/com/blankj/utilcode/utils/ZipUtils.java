//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private ZipUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean zipFiles(Collection<File> resFiles, String zipFilePath) throws IOException {
        return zipFiles(resFiles, (String)zipFilePath, (String)null);
    }

    public static boolean zipFiles(Collection<File> resFiles, String zipFilePath, String comment) throws IOException {
        return zipFiles(resFiles, FileUtils.getFileByPath(zipFilePath), comment);
    }

    public static boolean zipFiles(Collection<File> resFiles, File zipFile) throws IOException {
        return zipFiles(resFiles, (File)zipFile, (String)null);
    }

    public static boolean zipFiles(Collection<File> resFiles, File zipFile, String comment) throws IOException {
        if(resFiles != null && zipFile != null) {
            ZipOutputStream zos = null;

            boolean var10;
            try {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                Iterator var4 = resFiles.iterator();

                while(var4.hasNext()) {
                    File resFile = (File)var4.next();
                    if(!zipFile(resFile, "", zos, comment)) {
                        boolean var6 = false;
                        return var6;
                    }
                }

                var10 = true;
            } finally {
                if(zos != null) {
                    zos.finish();
                    CloseUtils.closeIO(new Closeable[]{zos});
                }

            }

            return var10;
        } else {
            return false;
        }
    }

    public static boolean zipFile(String resFilePath, String zipFilePath) throws IOException {
        return zipFile((String)resFilePath, (String)zipFilePath, (String)null);
    }

    public static boolean zipFile(String resFilePath, String zipFilePath, String comment) throws IOException {
        return zipFile(FileUtils.getFileByPath(resFilePath), FileUtils.getFileByPath(zipFilePath), comment);
    }

    public static boolean zipFile(File resFile, File zipFile) throws IOException {
        return zipFile((File)resFile, (File)zipFile, (String)null);
    }

    public static boolean zipFile(File resFile, File zipFile, String comment) throws IOException {
        if(resFile != null && zipFile != null) {
            ZipOutputStream zos = null;

            boolean var4;
            try {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                var4 = zipFile(resFile, "", zos, comment);
            } finally {
                if(zos != null) {
                    CloseUtils.closeIO(new Closeable[]{zos});
                }

            }

            return var4;
        } else {
            return false;
        }
    }

    private static boolean zipFile(File resFile, String rootPath, ZipOutputStream zos, String comment) throws IOException {
        rootPath = rootPath + (StringUtils.isSpace(rootPath)?"":File.separator) + resFile.getName();
        ZipEntry entry;
        int len;
        if(resFile.isDirectory()) {
            File[] is = resFile.listFiles();
            if(is != null && is.length > 0) {
                File[] var13 = is;
                int buffer = is.length;

                for(len = 0; len < buffer; ++len) {
                    File file = var13[len];
                    if(!zipFile(file, rootPath, zos, comment)) {
                        return false;
                    }
                }
            } else {
                entry = new ZipEntry(rootPath + '/');
                if(!StringUtils.isEmpty(comment)) {
                    entry.setComment(comment);
                }

                zos.putNextEntry(entry);
                zos.closeEntry();
            }
        } else {
            BufferedInputStream var12 = null;

            try {
                var12 = new BufferedInputStream(new FileInputStream(resFile));
                entry = new ZipEntry(rootPath);
                if(!StringUtils.isEmpty(comment)) {
                    entry.setComment(comment);
                }

                zos.putNextEntry(entry);
                byte[] var14 = new byte[1024];

                while((len = var12.read(var14, 0, 1024)) != -1) {
                    zos.write(var14, 0, len);
                }

                zos.closeEntry();
            } finally {
                CloseUtils.closeIO(new Closeable[]{var12});
            }
        }

        return true;
    }

    public static boolean unzipFiles(Collection<File> zipFiles, String destDirPath) throws IOException {
        return unzipFiles(zipFiles, FileUtils.getFileByPath(destDirPath));
    }

    public static boolean unzipFiles(Collection<File> zipFiles, File destDir) throws IOException {
        if(zipFiles != null && destDir != null) {
            Iterator var2 = zipFiles.iterator();

            File zipFile;
            do {
                if(!var2.hasNext()) {
                    return true;
                }

                zipFile = (File)var2.next();
            } while(unzipFile(zipFile, destDir));

            return false;
        } else {
            return false;
        }
    }

    public static boolean unzipFile(String zipFilePath, String destDirPath) throws IOException {
        return unzipFile(FileUtils.getFileByPath(zipFilePath), FileUtils.getFileByPath(destDirPath));
    }

    public static boolean unzipFile(File zipFile, File destDir) throws IOException {
        return unzipFileByKeyword((File)zipFile, (File)destDir, (String)null) != null;
    }

    public static List<File> unzipFileByKeyword(String zipFilePath, String destDirPath, String keyword) throws IOException {
        return unzipFileByKeyword(FileUtils.getFileByPath(zipFilePath), FileUtils.getFileByPath(destDirPath), keyword);
    }

    public static List<File> unzipFileByKeyword(File zipFile, File destDir, String keyword) throws IOException {
        if(zipFile != null && destDir != null) {
            ArrayList files = new ArrayList();
            ZipFile zf = new ZipFile(zipFile);
            Enumeration entries = zf.entries();

            while(true) {
                while(true) {
                    ZipEntry entry;
                    String entryName;
                    do {
                        if(!entries.hasMoreElements()) {
                            return files;
                        }

                        entry = (ZipEntry)entries.nextElement();
                        entryName = entry.getName();
                    } while(!StringUtils.isEmpty(keyword) && !FileUtils.getFileName(entryName).toLowerCase().contains(keyword.toLowerCase()));

                    String filePath = destDir + File.separator + entryName;
                    File file = new File(filePath);
                    files.add(file);
                    if(!entry.isDirectory()) {
                        if(!FileUtils.createOrExistsFile(file)) {
                            return null;
                        }

                        BufferedInputStream in = null;
                        BufferedOutputStream out = null;

                        try {
                            in = new BufferedInputStream(zf.getInputStream(entry));
                            out = new BufferedOutputStream(new FileOutputStream(file));
                            byte[] buffer = new byte[1024];

                            int len;
                            while((len = in.read(buffer)) != -1) {
                                out.write(buffer, 0, len);
                            }
                        } finally {
                            CloseUtils.closeIO(new Closeable[]{in, out});
                        }
                    } else if(!FileUtils.createOrExistsDir(file)) {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    public static List<String> getFilesPath(String zipFilePath) throws IOException {
        return getFilesPath(FileUtils.getFileByPath(zipFilePath));
    }

    public static List<String> getFilesPath(File zipFile) throws IOException {
        if(zipFile == null) {
            return null;
        } else {
            ArrayList paths = new ArrayList();
            Enumeration entries = getEntries(zipFile);

            while(entries.hasMoreElements()) {
                paths.add(((ZipEntry)entries.nextElement()).getName());
            }

            return paths;
        }
    }

    public static List<String> getComments(String zipFilePath) throws IOException {
        return getComments(FileUtils.getFileByPath(zipFilePath));
    }

    public static List<String> getComments(File zipFile) throws IOException {
        if(zipFile == null) {
            return null;
        } else {
            ArrayList comments = new ArrayList();
            Enumeration entries = getEntries(zipFile);

            while(entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
                comments.add(entry.getComment());
            }

            return comments;
        }
    }

    public static Enumeration<?> getEntries(String zipFilePath) throws IOException {
        return getEntries(FileUtils.getFileByPath(zipFilePath));
    }

    public static Enumeration<?> getEntries(File zipFile) throws IOException {
        return zipFile == null?null:(new ZipFile(zipFile)).entries();
    }
}
