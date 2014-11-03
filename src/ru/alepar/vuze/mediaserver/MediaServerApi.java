package ru.alepar.vuze.mediaserver;

import org.gudy.azureus2.plugins.disk.DiskManagerFileInfo;

public interface MediaServerApi {

    FileInfo getFileInfos(DiskManagerFileInfo fileInfo);

}