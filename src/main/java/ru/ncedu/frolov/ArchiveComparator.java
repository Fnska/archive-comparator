package ru.ncedu.frolov;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchiveComparator {
    private ZipFile fstZipFile;
    private ZipFile sndZipFile;

    public String compare() {
        Map<ZipEntry, FileStatus> sourceArchiveEntriesWithStatus = collectZipEntryFileStatus(fstZipFile, sndZipFile, ArchiveStatus.SOURCE);
        Map<ZipEntry, FileStatus> comparedEntriesWithStatus = collectZipEntryFileStatus(sndZipFile, fstZipFile, ArchiveStatus.NON_SOURCE);
        return getFormattedOutput(sourceArchiveEntriesWithStatus, comparedEntriesWithStatus);
    }

    private Map<ZipEntry, FileStatus> collectZipEntryFileStatus(ZipFile fst, ZipFile snd, ArchiveStatus archStatus) {
        Enumeration<? extends ZipEntry> entries = fst.entries();
        Map<ZipEntry, FileStatus> map = new HashMap<>();
        while (entries.hasMoreElements()) {
            ZipEntry originalEntry = entries.nextElement();
            ZipEntry entryToCompare = snd.getEntry(originalEntry.getName());
            FileStatus fileStatus = checkZipEntryFileStatus(originalEntry, entryToCompare, archStatus);
            map.put(originalEntry, fileStatus);
        }
        return map;
    }

    private FileStatus checkZipEntryFileStatus(ZipEntry fst, ZipEntry snd, ArchiveStatus archStatus) {
        FileStatus status;
        if (snd != null) {
            if (fst.getSize() != snd.getSize()
                    || fst.getCrc() != snd.getCrc()
            ) {
                status = FileStatus.UPDATED;
            } else {
                status = FileStatus.NOT_MODIFIED;
            }
        } else {
            if (archStatus == ArchiveStatus.SOURCE) {
                status = FileStatus.DELETED;
            } else {
                status = FileStatus.ADDED;
            }
        }
        return status;
    }

    private String getFormattedOutput(Map<ZipEntry, FileStatus> fstMap, Map<ZipEntry, FileStatus> sndMap) {
        StringBuilder sb = new StringBuilder();
        String divider = "--------------------------------------------------------\n";

        sb.append(fstZipFile.getName()).append("\n");
        sb.append(divider);
        appendFormattedMap(sb, fstMap);
        sb.append(divider);

        sb.append("\n");

        sb.append(sndZipFile.getName()).append("\n");
        sb.append(divider);
        appendFormattedMap(sb, sndMap);
        sb.append(divider);

        return sb.toString();
    }

    private void appendFormattedMap(StringBuilder sb, Map<ZipEntry, FileStatus> map) {
        for (Map.Entry<ZipEntry, FileStatus> entry : map.entrySet()) {
            sb.append("\t* ")
                    .append(entry.getKey())
                    .append("\t")
                    .append(entry.getValue())
                    .append("\n");
        }
    }

    public ArchiveComparator(ZipFile fstZipFile, ZipFile sndZipFile) {
        this.fstZipFile = fstZipFile;
        this.sndZipFile = sndZipFile;
    }

    public ZipFile getFstZipFile() {
        return fstZipFile;
    }

    public void setFstZipFile(ZipFile fstZipFile) {
        this.fstZipFile = fstZipFile;
    }

    public ZipFile getSndZipFile() {
        return sndZipFile;
    }

    public void setSndZipFile(ZipFile sndZipFile) {
        this.sndZipFile = sndZipFile;
    }
}
