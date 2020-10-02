package ru.ncedu.frolov;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class App {
    public static void main(String[] args) {
        try {
            ZipFile fstZipFile = new ZipFile(args[0]);
            ZipFile sndZipFile = new ZipFile(args[1]);

            Enumeration<? extends ZipEntry> fstZipEntries = fstZipFile.entries();
            System.out.println("ARCHIVE 1. " + fstZipFile.getName());
            while (fstZipEntries.hasMoreElements()) {
                ZipEntry origEntry = fstZipEntries.nextElement();
                ZipEntry entryToCompare = sndZipFile.getEntry(origEntry.getName());
                if (entryToCompare != null) {
                    if (origEntry.getSize() != entryToCompare.getSize()
                            || origEntry.getCrc() != entryToCompare.getCrc()
                    ) {
                        System.out.println(origEntry + " :: UPDATED");
                    } else {
                        System.out.println(origEntry + " :: NOT MODIFIED");
                    }
                } else {
                    System.out.println(origEntry.getName() + " :: DELETED");
                }
            }
            System.out.println();
            System.out.println("ARCHIVE 2. " + sndZipFile.getName());
            Enumeration<? extends ZipEntry> sndZipEntries = sndZipFile.entries();
            while (sndZipEntries.hasMoreElements()) {
                ZipEntry origEntry = sndZipEntries.nextElement();
                ZipEntry entryToCompare = fstZipFile.getEntry(origEntry.getName());
                if (entryToCompare != null) {
                    if (origEntry.getSize() != entryToCompare.getSize()
                            || origEntry.getCrc() != entryToCompare.getCrc()
                    ) {
                        System.out.println(origEntry + " :: UPDATED");
                    } else {
                        System.out.println(origEntry + " :: NOT MODIFIED");
                    }
                } else {
                    System.out.println(origEntry.getName() + " :: ADDED");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
