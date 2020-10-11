package ru.ncedu.frolov;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipFile;

public class App {
    public static void main(String[] args) {
        try {
            ZipFile fstZipFile = new ZipFile(args[0]);
            ZipFile sndZipFile = new ZipFile(args[1]);

            ArchiveComparator comparator = new ArchiveComparator(fstZipFile, sndZipFile);
            String result = comparator.compare();
            try (FileWriter writer = new FileWriter(new File("result.txt"))) {
                writer.write(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
